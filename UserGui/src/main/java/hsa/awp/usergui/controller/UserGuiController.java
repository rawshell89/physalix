/*
 * Copyright (c) 2010-2012 Matthias Klass, Johannes Leimer,
 *               Rico Lieback, Sebastian Gabriel, Lothar Gesslein,
 *               Alexander Rampp, Kai Weidner
 *
 * This file is part of the Physalix Enrollment System
 *
 * Foobar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package hsa.awp.usergui.controller;

import hsa.awp.campaign.model.*;
import hsa.awp.campaign.rule.ICampaignRuleChecker;
import hsa.awp.common.exception.DataAccessException;
import hsa.awp.event.model.Category;
import hsa.awp.event.model.Event;
import hsa.awp.gui.controller.GuiController;
import hsa.awp.rule.model.RegistrationRuleSet;
import hsa.awp.scire.controller.IScireController;
import hsa.awp.scire.procedureLogic.DrawProcedureLogic;
import hsa.awp.scire.procedureLogic.IFifoProcedureLogic;
import hsa.awp.scire.procedureLogic.IProcedureLogic;
import hsa.awp.scire.services.EventCheckService;
import hsa.awp.user.facade.IUserFacade;
import hsa.awp.user.model.Mandator;
import hsa.awp.user.model.SingleUser;
import hsa.awp.user.model.Student;
import hsa.awp.user.model.User;

import java.util.*;

/**
 * Controller for UserGui.
 *
 * @author Rico Lieback
 * @author basti
 */
public class UserGuiController extends GuiController implements IUserGuiController {
  private IScireController scireController;

  private ICampaignRuleChecker campaignRuleChecker;

  private EventCheckService eventCheckService;

  /**
   * Default Constructor.
   */
  public UserGuiController() {

  }

  @Override
  public void createPrioList(String participant, String initiator, Set<List<Event>> eventLists, Campaign campaign) {

    if (prioListsRestricted(participant, eventLists, campaign)) {
      throw new IllegalArgumentException("one list is faulty");
    }

    campaign = updateCampaign(campaign);

    IProcedureLogic<? extends Procedure> pl = scireController.findActiveLogicByCampaign(campaign);
    DrawProcedureLogic dpl;

    if (pl instanceof DrawProcedureLogic) {
      dpl = (DrawProcedureLogic) pl;
    } else {
      throw new UnsupportedOperationException();
    }

    DrawProcedure proc = getDrawProcedureById(dpl.getProcedure().getId());

    Set<PriorityList> lists = createPrioList(eventLists, userFacade.getSingleUserByLogin(initiator), userFacade
        .getSingleUserByLogin(participant), proc);

    dpl.register(lists);
  }

  /*
      * (non-Javadoc)
      *
      * @see hsa.awp.gui.controller.IGuiController#getCategories()
      */
  public List<Category> getCategories() {

    List<Category> list = null;
    try {
      list = evtFacade.getAllCategories();
    } catch (DataAccessException dae) {
      return null;
    }
    return list;
  }

  @Override
  public SingleUser getUserById(String login) {

    return userFacade.getSingleUserByLogin(login);
  }

  @Override
  public void registerWithFifoProcedure(FifoProcedure procedure, Event event, String participant, String initiator,
                                        boolean examOnly) {

    procedure = camFacade.getFifoProcedureById(procedure.getId());
    event = evtFacade.getEventById(event.getId());

    IFifoProcedureLogic logic = (IFifoProcedureLogic) (scireController.findActiveLogicByProcedure(procedure.getId()));
    if (logic == null) {
      throw new IllegalArgumentException("no matching procedureLogic is currently active");
    }
    logic.register(event, userFacade.getSingleUserByLogin(participant), userFacade.getSingleUserByLogin(initiator), examOnly);
  }

  public List<Event> filterEventList(List<Event> events, SingleUser singleUser, DrawProcedure drawProcedure, List<Event> blackList) {

    return eventCheckService.filterEventList(events, singleUser, drawProcedure, blackList);
  }

  @Override
  public List<Campaign> getCampaignsWithActiveProcedures(SingleUser user) {

    List<Campaign> campaigns = new ArrayList<Campaign>();
    for (IProcedureLogic<? extends Procedure> logic : scireController.getRunningProcedures()) {
      Campaign campaign = getCampaignById(logic.getProcedure().getCampaign().getId());
      if (isCampaignVisibleForUser(campaign, user))
        campaigns.add(campaign);
    }

    return campaigns;
  }

  private boolean isCampaignVisibleForUser(Campaign campaign, SingleUser user) {

    if (user instanceof Student) {
      return campaign.getStudyCourseIds().contains(((Student) user).getStudyCourse().getId());
    }
    return false;
  }

  @Override
  public boolean isAlreadyDrawn(DrawProcedure procedure) {

    DrawProcedureLogic drawProcedureLogic;
    try {
      drawProcedureLogic = (DrawProcedureLogic) scireController.findActiveLogicByProcedure(procedure);
      if (drawProcedureLogic != null) {
        return drawProcedureLogic.isDrawn();
      }
    } catch (ClassCastException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public DrawProcedure updateDrawProcedure(DrawProcedure drawProcedure) {

    return camFacade.updateDrawProcedure(drawProcedure);
  }

  @Override
  public Mandator getMandatorById(Long mandatorId) {
    return userFacade.getMandatorById(mandatorId);
  }

  @Override
  public Campaign updateCampaign(Campaign campaign) {

    return camFacade.updateCampaign(campaign);
  }

  @Override
  public Campaign getCampaignById(Long id) {

    return camFacade.getCampaignById(id);
  }

  @Override
  public List<Event> getEventsWhereRegistrationIsAllowed(Campaign campaign, SingleUser singleUser) {

    /**
     * 1. getEventsByCampaign()
     * 2. findRulesByCampaign()
     * 3. if rule exists for eventid -> check it
     * 4. if false -> remove it
     */

    List<Event> events = getEventsByCampaign(campaign);
    List<RegistrationRuleSet> rulesOfCampaign = ruleFacade.findByCampaign(campaign.getId());

    Set<Long> eventIdsWithAttachedRules = new HashSet<Long>();
    for (RegistrationRuleSet ruleSet : rulesOfCampaign) {
      eventIdsWithAttachedRules.add(ruleSet.getEvent());
    }

    Iterator<Event> iterator = events.iterator();
    while (iterator.hasNext()) {
      Event event = iterator.next();
      if (eventIdsWithAttachedRules.contains(event.getId())) {
        if (!isRegistrationAllowed(singleUser, campaign, event)) {
          iterator.remove();
        }
      }
    }

    return events;
  }

  @Override
  public boolean isRegistrationAllowed(SingleUser user, Campaign campaign, Event event) {

    return campaignRuleChecker.isRegistrationAllowed(user, campaign, event);
  }

  @Override
  public boolean hasCampaignAllowedRegistrations(SingleUser user, Campaign campaign) {

    boolean found = false;

    campaign = camFacade.getCampaignById(campaign.getId());

    for (Long id : campaign.getEventIds()) {
      if (isRegistrationAllowed(user, campaign, evtFacade.getEventById(id))) {
        found = true;
        break;
      }
    }

    return found;
  }

  @Override
  public boolean isCampaignOpen(ConfirmedRegistration reg) {

    reg = camFacade.getConfirmedRegistrationById(reg.getId());

    Date now = new Date();


    Procedure procedure = reg.getProcedure();
    return procedure != null && procedure.getCampaign().getEndShow().getTime().after(now);
  }

  @Override
  public List<Event> convertToEventList(List<Long> ids) {

    return evtFacade.convertToEventList(ids);
  }

  private Set<PriorityList> createPrioList(Set<List<Event>> events, SingleUser initiator, User participant, Procedure procedure) {

    Set<PriorityList> prioLists = new HashSet<PriorityList>();

    for (List<Event> list : events) {
      PriorityList prio = PriorityList.getInstance(procedure.getMandatorId());
      prio.setInitiator(initiator.getId());
      prio.setParticipant(participant.getId());
      // prio.setProcedure(procedure);

      for (int i = 0; i < list.size(); i++) {
        prio.addItem(list.get(i).getId(), i + 1);
      }
      prioLists.add(prio);
    }

    return prioLists;
  }

  /**
   * Checks whether the the list could be created or not according to some rules.
   *
   * @param participant user for whom the lists are
   * @param eventLists  lists to be created
   * @param campaign    campaign
   * @return true if one of the lists is faulty.
   */
  private boolean prioListsRestricted(String participant, Set<List<Event>> eventLists, Campaign campaign) {
    // isListLimitExceeded
    SingleUser user = userFacade.getSingleUserByLogin(participant);

    IProcedureLogic<? extends Procedure> pl = scireController.findActiveLogicByCampaign(campaign);
    DrawProcedureLogic dpl;

    if (pl instanceof DrawProcedureLogic) {
      dpl = (DrawProcedureLogic) pl;
    } else {
      throw new UnsupportedOperationException();
    }

    DrawProcedure proc = getDrawProcedureById(dpl.getProcedure().getId());

    List<PriorityList> persistedPrioLists = findPriorityListsByUserAndProcedure(user.getId(), proc);

    if ((persistedPrioLists.size() + eventLists.size()) > proc.getMaximumPriorityLists()) {
      return true;
    }

    // isItemLimitExceeded

    for (PriorityList list : persistedPrioLists) {
      if (list.getItems().size() > proc.getMaximumPriorityListItems()) {
        return true;
      }
    }

    for (List<Event> list : eventLists) {
      if (list.size() > proc.getMaximumPriorityListItems()) {
        return true;
      }
    }

    // isEventUsedTwice;

    List<Long> persistedPrioListItemEventIds = new ArrayList<Long>();

    for (PriorityList list : persistedPrioLists) {
      for (PriorityListItem item : list.getItems()) {
        persistedPrioListItemEventIds.add(item.getEvent());
      }
    }

    List<Long> newPrioListItemEventIds = new ArrayList<Long>();

    for (List<Event> list : eventLists) {
      for (Event event : list) {
        newPrioListItemEventIds.add(event.getId());
      }
    }

    for (Long id : newPrioListItemEventIds) {
      if (persistedPrioListItemEventIds.contains(id)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public DrawProcedure getDrawProcedureById(Long id) {

    return camFacade.getDrawProcedureById(id);
  }

  @Override
  public List<PriorityList> findPriorityListsByUserAndProcedure(Long userId, Procedure procedure) {

    return camFacade.findPriorityListsByUserAndProcedure(userId, procedure);
  }

  /**
   * @param campaignRuleChecker the campaignRuleChecker to set
   */
  public void setCampaignRuleChecker(ICampaignRuleChecker campaignRuleChecker) {

    this.campaignRuleChecker = campaignRuleChecker;
  }

  public void setEventCheckService(EventCheckService eventCheckService) {

    this.eventCheckService = eventCheckService;
  }

  /**
   * Setter for scireController.
   *
   * @param scireController the scireController to set
   */
  public void setScireController(IScireController scireController) {

    this.scireController = scireController;
    scireController.startTimer();
  }

  /**
   * Setter for userController.
   *
   * @param userFacade the userFacade to set
   */
  public void setUserFacade(IUserFacade userFacade) {

    this.userFacade = userFacade;
  }
}
