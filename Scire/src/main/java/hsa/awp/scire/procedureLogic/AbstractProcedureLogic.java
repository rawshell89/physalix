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

package hsa.awp.scire.procedureLogic;

import hsa.awp.campaign.facade.ICampaignFacade;
import hsa.awp.campaign.model.Campaign;
import hsa.awp.campaign.model.ConfirmedRegistration;
import hsa.awp.campaign.model.Procedure;
import hsa.awp.campaign.rule.ICampaignRuleChecker;
import hsa.awp.common.exception.NoMatchingElementException;
import hsa.awp.common.exception.NoSpaceAvailableException;
import hsa.awp.common.mail.IMail;
import hsa.awp.common.mail.IMailFactory;
import hsa.awp.common.model.TemplateDetail;
import hsa.awp.common.model.TemplateType;
import hsa.awp.common.services.TemplateService;
import hsa.awp.common.util.ITimerTaskFactory;
import hsa.awp.event.facade.IEventFacade;
import hsa.awp.event.model.Event;
import hsa.awp.user.facade.IUserFacade;
import hsa.awp.user.model.SingleUser;
import hsa.awp.user.model.User;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.Calendar;

import static hsa.awp.event.util.EventFormattingUtils.formatIdSubjectNameAndDetailInformation;

/**
 * Class summing up all necessary methods for a normal registration.
 *
 * @param <T> type of domain object that is the base for the {@link IProcedureLogic}.
 * @author klassm
 */
public abstract class AbstractProcedureLogic<T extends Procedure> implements IProcedureLogic<T> {
  /**
   * Facade for accessing the Campaign component.
   */
  protected ICampaignFacade campaignFacade;

  protected ICampaignRuleChecker campaignRuleChecker;

  /**
   * Facade for accessing the Event component.
   */
  protected IEventFacade eventFacade;

  /**
   * Standard logger.
   */
  protected Logger logger;

  /**
   * Factory for sending mails.
   */
  protected IMailFactory mailFactory;

  /**
   * Subject that is used in mails.
   */
  protected String mailSubject;

  /**
   * Text that is sent in mails.
   */
  protected String mailText;

  /**
   * Applied Procedure.
   */
  protected T procedure;

  /**
   * Facade for accessing the SingleUser component.
   */
  protected IUserFacade userFacade;
  protected ITimerTaskFactory timerTaskFactory;

  /**
   * Type of the Procedure model object (e.g. FifoProcedure.class or DrawProcedure.class).
   */
  private Class<T> procedureType;

  protected TemplateService templateService;

  public AbstractProcedureLogic(Class<T> procedureType) {

    logger = LoggerFactory.getLogger(this.getClass());
    this.procedureType = procedureType;

    mailText = "";
    mailSubject = "";
  }

  /**
   * Setter for the {@link Procedure}.
   *
   * @param procedure procedure to apply to the logic object.
   * @throws IllegalArgumentException if no procedure was given (:= null) or if an illegal procedure type was given.
   */
  public void setProcedure(Procedure procedure) {

    if (procedure == null) {
      throw new IllegalArgumentException("no procedure given");
    }
    try {
      this.procedure = procedureType.cast(procedure);
      logger.debug("set Procedure");
    } catch (ClassCastException e) {
      throw new IllegalArgumentException(procedureType.getSimpleName() + " expected. Got " + procedure.getClass());
    }
  }

  /**
   * Sends a default mail to the participant of the given {@link ConfirmedRegistration}.
   *
   * @param confirmedRegistration {@link ConfirmedRegistration} to use for mail sending.
   * @throws IllegalArgumentException if an invalid confirmedRegistration was given.
   */
  protected void sendMail(ConfirmedRegistration confirmedRegistration) {

    if (confirmedRegistration == null) {
      throw new IllegalArgumentException("no confirmedRegistration was given");
    }

    User participant = getUserFromConfirmedRegistration(confirmedRegistration);
    Event event = getEventFromConfirmedRegistration(confirmedRegistration);

    /** Write a mail to the user **/
    logger.trace("write a mail to the user");

    if (!(participant instanceof SingleUser)) {
      throw new UnsupportedOperationException("groups are not yet supported for mail sending");
    }
    SingleUser singleUser = (SingleUser) participant;

    Template template = getTemplate();

    VelocityContext context = new VelocityContext();

    fillVelocityContext(event, singleUser, context);

    StringWriter writer = new StringWriter();
    template.merge(context, writer);

    String mailSubject = buildMailSubject();
    String mailContent = writer.toString();


    Campaign campaign = procedure.getCampaign();

    logger.trace("sending mail to {}", singleUser.getMail());
    IMail mail = mailFactory.getInstance(singleUser.getMail(), mailSubject, mailContent, campaign.getCorrespondentEMail());
    mail.send();
  }

  private User getUserFromConfirmedRegistration(ConfirmedRegistration confirmedRegistration) {

    User user;
    try {
      user = userFacade.getUserById(confirmedRegistration.getParticipant());
    } catch (NoMatchingElementException e) {
      throw new IllegalArgumentException("participant id is invalid in the given confirmedRegistration");
    }
    return user;
  }

  private Event getEventFromConfirmedRegistration(ConfirmedRegistration confirmedRegistration) {

    Event event;

    try {
      event = eventFacade.getEventById(confirmedRegistration.getEventId());
    } catch (NoMatchingElementException e) {
      throw new IllegalArgumentException("event is invalid in the given confirmedRegistration");
    }

    return event;
  }

  protected Template getTemplate() {

    return templateService.loadVelocityTemplate(TemplateDetail.getInstance(procedure.getMandatorId(), TemplateType.FIFO));
  }

  protected void fillVelocityContext(Event event, SingleUser singleUser, VelocityContext context) {

    context.put("name", singleUser.getName());
    context.put("eventlist", formatIdSubjectNameAndDetailInformation(event));
    context.put("campaign", procedure.getCampaign().getName());
    context.put("procedure", procedure.getName());
  }

  protected String buildMailSubject() {

    return "Anmeldung " + procedure.getCampaign().getName() + " Phase: " + procedure.getName();
  }

  /**
   * Setter for campaignFacade.
   *
   * @param campaignFacade the campaignFacade to set
   */
  public void setCampaignFacade(ICampaignFacade campaignFacade) {

    this.campaignFacade = campaignFacade;
  }

  /**
   * Setter for campaignRuleChecker.
   *
   * @param campaignRuleChecker the campaignRuleChecker to set
   */
  public void setCampaignRuleChecker(ICampaignRuleChecker campaignRuleChecker) {

    this.campaignRuleChecker = campaignRuleChecker;
  }

  /**
   * Setter for eventFacade.
   *
   * @param eventFacade the eventFacade to set
   */
  public void setEventFacade(IEventFacade eventFacade) {

    this.eventFacade = eventFacade;
  }

  /**
   * Setter for mailFactory.
   *
   * @param mailFactory the mailFactory to set
   */
  public void setMailFactory(IMailFactory mailFactory) {

    if (mailFactory == null) {
      throw new IllegalArgumentException("Wrong argument given!");
    }
    this.mailFactory = mailFactory;
  }

  /**
   * @param mailSubject the mailSubject to set
   */
  public void setMailSubject(String mailSubject) {

    this.mailSubject = mailSubject;
  }

  public void setMailText(String mailText) {

    this.mailText = mailText;
  }

  public void setTimerTaskFactory(ITimerTaskFactory timerTaskFactory) {

    this.timerTaskFactory = timerTaskFactory;
  }

  /**
   * Setter for userFacade.
   *
   * @param userFacade the userFacade to set
   */
  public void setUserFacade(IUserFacade userFacade) {

    this.userFacade = userFacade;
  }

  /**
   * Registers a given user with a given {@link Event}. The registration will be only valid for the exam.
   *
   * @param initiator   person who started the transaction
   * @param participant person who wants to join an event
   * @param event       {@link Event} the {@link User} shall be registered for.
   * @param examOnly    true if the registration will only apply for the final exam.
   * @throws NoSpaceAvailableException if the event has no more available spaces to register for.
   */
  public synchronized ConfirmedRegistration singleRegistration(Event event, User participant, SingleUser initiator,
                                                               boolean examOnly) {

    logger.debug("register with " + procedureType.getSimpleName());

    if (initiator == null) {
      logger.trace("no initiator given.");
      throw new IllegalArgumentException("no initiator given.");
    } else if (participant == null) {
      logger.trace("no participant given");
      throw new IllegalArgumentException("no participant given");
    } else if (event == null) {
      logger.trace("no event given");
      throw new IllegalArgumentException("no event given");
    }

    // check rules
    this.checkRules(participant, event);

    // check whether there is enough space available to register.
    if (!examOnly && event.getConfirmedRegistrations().size() + 1 >= event.getMaxParticipants()) {
      logger.debug("no space available");
      throw new NoSpaceAvailableException();
    }

    // Create the ConfirmedRegistration
    logger.debug("creating ConfirmedRegistration");
    ConfirmedRegistration confirmedRegistration = ConfirmedRegistration.getInstance(event.getId(), procedure.getMandatorId());
    confirmedRegistration.setDate(Calendar.getInstance());
    confirmedRegistration.setExamOnly(examOnly);
    confirmedRegistration.setParticipant(participant.getId());
    confirmedRegistration.setInitiator(initiator.getId());
    confirmedRegistration.setProcedure(procedure);

    campaignFacade.saveConfirmedRegistration(confirmedRegistration);

    // Register the ConfirmedRegistration with the Event
    logger.debug("adding the confirmedRegistration to the event");
    event.getConfirmedRegistrations().add(confirmedRegistration.getId());

    return confirmedRegistration;
  }

  public void checkRules(User participant, Event event) {

    if (participant instanceof SingleUser) {
      if (!campaignRuleChecker.isRegistrationAllowed((SingleUser) participant, procedure.getCampaign(), event)) {
        throw new IllegalStateException("The given user is not allowed to participate in the specified event");
      }
    } else {
      throw new UnsupportedOperationException("Implementation for groups is missing");
    }
  }

  public void setTemplateService(TemplateService templateService) {
    this.templateService = templateService;
  }
}
