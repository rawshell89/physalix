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
import hsa.awp.event.model.Category;
import hsa.awp.event.model.Event;
import hsa.awp.gui.controller.IGuiController;
import hsa.awp.scire.procedureLogic.DrawProcedureLogic;
import hsa.awp.user.model.Mandator;
import hsa.awp.user.model.SingleUser;

import java.util.List;
import java.util.Set;

public interface IUserGuiController extends IGuiController {
  /**
   * Checks if a User is already registered with the event.
   *
   * @param user  User to look for.
   * @param event event to look for.
   * @return true if the user is registered.
   */
  boolean checkSingleUserRegistrationForEvent(SingleUser user, Event event);

  List<Event> convertToEventList(List<Long> ids);

  void createPrioList(String participant, String initiator, Set<List<Event>> eventLists, Campaign campaign);

  List<Event> filterEventList(List<Event> events, SingleUser singleUser, DrawProcedure drawProcedure, List<Event> blackList);

  /**
   * Looks for {@link PriorityList}s associated with a given {@link SingleUser} and a given {@link Procedure}.
   *
   * @param userId    SingleUser to look for.
   * @param procedure {@link Procedure} to look for.
   * @return associated {@link PriorityList}s.
   */
  List<PriorityList> findPriorityListsByUserAndProcedure(Long userId, Procedure procedure);

  /**
   * Looks for a {@link Campaign} using its id.
   *
   * @param id identifier
   * @return found Campaign, {@link DataAccessException} if no matching campaign was found, {@link IllegalArgumentException} if id
   *         is invalid.
   */
  Campaign getCampaignById(Long id);

  /**
   * Returns all existing {@link Campaign}s with active procedures.
   *
   * @return all existing {@link Campaign}
   */
  List<Campaign> getCampaignsWithActiveProcedures(SingleUser user);

  /**
   * Returns all {@link Category}s.
   *
   * @return all exist {@link Category}s
   */
  List<Category> getCategories();

  /**
   * Looks for a {@link DrawProcedure} by its id.
   *
   * @param id identifier.
   * @return found {@link DrawProcedure}
   */
  DrawProcedure getDrawProcedureById(Long id);

  /**
   * Looks for a {@link SingleUser} by his login name.
   *
   * @param login login name.
   * @return SingleUser.
   */
  SingleUser getUserById(String login);

  /**
   * Checks whether a campaign has events where the user can register.
   *
   * @param user     user to check
   * @param campaign campaign
   * @return true if campaign has events to register.
   */
  boolean hasCampaignAllowedRegistrations(SingleUser user, Campaign campaign);


  /**
   * Check whether the drawProcedure is already drawn.
   *
   * @param proc procedure to check.
   * @return true if DrawProc's drawdate is passed.
   * @see DrawProcedureLogic#isDrawn()
   */
  boolean isAlreadyDrawn(DrawProcedure proc);

  boolean isCampaignOpen(ConfirmedRegistration modelObject);

  /**
   * Checks if a user is allowed to register for an event.
   *
   * @param user     user to check
   * @param campaign campaign
   * @param event    event
   * @return true if he is allowed.
   */
  boolean isRegistrationAllowed(SingleUser user, Campaign campaign, Event event);

  void registerWithFifoProcedure(FifoProcedure procedure, Event event, String participant, String initiator, boolean examOnly);

  /**
   * Puts all changes of a given {@link Campaign} into the database.
   *
   * @param campaign campaign to synchronize changes
   * @return merged campaign
   */
  Campaign updateCampaign(Campaign campaign);

  /**
   * Merges a given {@link DrawProcedure} with the database. All values in the database will be overwritten.
   *
   * @param d {@link DrawProcedure} to merge.
   * @return merged {@link FifoProcedure}.
   */
  DrawProcedure updateDrawProcedure(DrawProcedure d);

  Mandator getMandatorById(Long mandatorId);
}
