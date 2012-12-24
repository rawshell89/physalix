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

package hsa.awp.admingui.view;

import hsa.awp.admingui.OnePanelPage;
import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.admingui.util.AccessUtil;
import hsa.awp.admingui.util.JavascriptEventConfirmation;
import hsa.awp.admingui.util.UserSelectPanel;
import hsa.awp.campaign.model.ConfirmedRegistration;
import hsa.awp.campaign.model.PriorityList;
import hsa.awp.campaign.model.PriorityListItem;
import hsa.awp.campaign.model.Procedure;
import hsa.awp.common.exception.NoMatchingElementException;
import hsa.awp.event.model.Event;
import hsa.awp.user.model.SingleUser;
import hsa.awp.user.model.Student;
import hsa.awp.user.model.User;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import static hsa.awp.event.util.EventFormattingUtils.formatIdSubjectNameAndDetailInformation;

/**
 * Detail page for an {@link Event}.
 *
 * @author klassm
 */
public class EventDetailPanel extends Panel {
  private static final String PRIORITYLISTS = "Wunschlisten";

  private static final String DETAIL = "Details";

  private static final String REGISTRATIONS = "Buchungen";

  private static final String CREATE_REGISTRATION = "Buchung hinzufügen";

  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = 5821714528917192263L;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * Date formatter for converting Calendar values to strings.
   */
  private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

  /**
   * {@link Event} whose details shall be viewed.
   */
  private Event event;

  /**
   * Creates a new {@link EventDetailPanel}.
   *
   * @param id      wicket id.
   * @param eventId id of the event whose details shall be viewed.
   */
  public EventDetailPanel(String id, Long eventId) {

    super(id);
    this.event = controller.getEventById(eventId);

    List<ITab> tabs = new LinkedList<ITab>();
    tabs.add(tabEventDetails());
    if (AccessUtil.hasRole(Session.get(), "editEventDetails")) {
      tabs.add(tabEventConfirmedRegistrations());
      tabs.add(tabEventPriorityLists());
      tabs.add(tabEventCreateConfirmedRegistration());
    }


    AjaxTabbedPanel tabbedPanel = new AjaxTabbedPanel("eventDetail.tabNav", tabs);


    add(tabbedPanel);

    add(new Label("eventString", formatIdSubjectNameAndDetailInformation(event)));
  }

  /**
   * Creates a new tab with more information about the {@link Event}.
   *
   * @return tab for information
   */
  private ITab tabEventDetails() {

    return new AbstractTab(new Model<String>(DETAIL)) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 1L;

      @Override
      public Panel getPanel(String arg0) {

        Panel panel = new TabEventDetailPanel(arg0);
        panel.add(new AjaxSelfUpdatingTimerBehavior(Duration.minutes(5d)));
        AccessUtil.allowRender(panel, "viewEventDetails");
        return panel;
      }
    };
  }

  /**
   * Creates a new tab for {@link ConfirmedRegistration}s.
   *
   * @return tab for {@link ConfirmedRegistration}s.
   */
  private ITab tabEventConfirmedRegistrations() {

    return new AbstractTab(new Model<String>(REGISTRATIONS)) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = -3145454617649443969L;

      @Override
      public Panel getPanel(String arg0) {

        Panel panel = new TabEventConfirmedRegistrationsPanel(arg0);
        panel.add(new AjaxSelfUpdatingTimerBehavior(Duration.minutes(5d)));
        AccessUtil.allowRender(panel, "editEventDetails");
        return panel;
      }
    };
  }

  /**
   * Tab including more information about all associated {@link PriorityList}s.
   *
   * @return tab containing {@link PriorityList}s.
   */
  private ITab tabEventPriorityLists() {

    return new AbstractTab(new Model<String>(PRIORITYLISTS)) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = -5460290314327872731L;

      @Override
      public Panel getPanel(String arg0) {

        Panel panel = new TabEventPriorityListPanel(arg0);
        panel.add(new AjaxSelfUpdatingTimerBehavior(Duration.minutes(5d)));
        AccessUtil.allowRender(panel, "editEventDetails");
        return panel;
      }
    };
  }

  /**
   * Creates a new tab for creating {@link ConfirmedRegistration}s.
   *
   * @return creation tab for {@link ConfirmedRegistration}s.
   */
  private ITab tabEventCreateConfirmedRegistration() {

    return new AbstractTab(new Model<String>(CREATE_REGISTRATION)) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 4873872910943L;

      @Override
      public Panel getPanel(String arg0) {

        Panel panel = new TabEventRegistrationPanel(arg0);
        AccessUtil.allowRender(panel, "editEventDetails");

        return panel;
      }
    };
  }


  /**
   * Abstract Mapping table for storing read objects from the database that would only be long values in the normal model objects.
   *
   * @author klassm
   */
  private abstract class AbstractMapping implements Serializable {
    /**
     * unique serialization id.
     */
    private static final long serialVersionUID = -3654654626862151952L;

    /**
     * When was the mapping model object created.
     */
    private Calendar date;

    /**
     * unique key.
     */
    private Long id;

    /**
     * Who created the model object.
     */
    private SingleUser initiator;

    /**
     * Who will be the participant in the Event.
     */
    private User participant;

    /**
     * What way did the initiator use to make the Participant join the Event.
     */
    private Procedure procedure;

    /**
     * Creates a new {@link AbstractMapping}.
     *
     * @param id          unique id.
     * @param participant Who will be the participant in the Event.
     * @param initiator   Who created the model object.
     * @param date        When was the mapping model object created.
     * @param procedure   What way did the initiator use to make the Participant join the Event.
     */
    public AbstractMapping(Long id, User participant, SingleUser initiator, Calendar date, Procedure procedure) {

      super();
      this.id = id;
      this.participant = participant;
      this.initiator = initiator;
      this.date = date;
      this.procedure = procedure;
    }

    /**
     * Getter for date.
     *
     * @return the date
     */
    public Calendar getDate() {

      return date;
    }

    /**
     * @return the id
     */
    public Long getId() {

      return id;
    }

    /**
     * Getter for initiator.
     *
     * @return the initiator
     */
    public SingleUser getInitiator() {

      return initiator;
    }

    /**
     * Getter for participant.
     *
     * @return the participant
     */
    public User getParticipant() {

      return participant;
    }

    /**
     * Getter for procedure.
     *
     * @return the procedure
     */
    public Procedure getProcedure() {

      return procedure;
    }
  }

  /**
   * Mapping for {@link ConfirmedRegistration}s.
   *
   * @author klassm
   */
  private class ConfirmedRegistrationMapping extends AbstractMapping implements Comparable<ConfirmedRegistrationMapping> {
    /**
     * unique serialization id.
     */
    private static final long serialVersionUID = -3123628794698446262L;

    /**
     * Creates a new {@link ConfirmedRegistrationMapping}.
     *
     * @param id          unique id.
     * @param participant Who will be the participant in the Event.
     * @param initiator   Who created the model object.
     * @param date        When was the mapping model object created.
     * @param procedure   What way did the initiator use to make the Participant join the Event.
     */
    public ConfirmedRegistrationMapping(Long id, User participant, SingleUser initiator, Calendar date, Procedure procedure) {

      super(id, participant, initiator, date, procedure);
    }

    @Override
    public int compareTo(ConfirmedRegistrationMapping arg0) {

      if (getParticipant() instanceof SingleUser && arg0.getParticipant() instanceof SingleUser) {
        return ((SingleUser) getParticipant()).getName().compareTo(((SingleUser) arg0.getParticipant()).getName());
      } else {
        return getParticipant().getId().compareTo(arg0.getParticipant().getId());
      }
    }
  }

  /**
   * Mapping for {@link PriorityList}s.
   *
   * @author klassm
   */
  private class PriorityListItemMapping extends AbstractMapping implements Comparable<PriorityListItemMapping> {
    /**
     * unique serialization id.
     */
    private static final long serialVersionUID = -6536023353808221034L;

    /**
     * Priority of the item.
     */
    private int priority;

    private PriorityList priorityList;


    public PriorityList getPriorityList() {

      return priorityList;
    }

    /**
     * Creates a new {@link ConfirmedRegistrationMapping}.
     *
     * @param id          unique id.
     * @param participant Who will be the participant in the Event.
     * @param initiator   Who created the model object.
     * @param date        When was the mapping model object created.
     * @param procedure   What way did the initiator use to make the Participant join the Event.
     * @param priority    Priority of the item.
     */
    public PriorityListItemMapping(Long id, User participant, SingleUser initiator, int priority, Calendar date,
                                   Procedure procedure, PriorityList priorityList) {

      super(id, participant, initiator, date, procedure);
      this.priority = priority;
      this.priorityList = priorityList;
    }

    @Override
    public int compareTo(PriorityListItemMapping o) {

      if (o.priority != priority) {
        return ((Integer) o.priority).compareTo(priority) * (-1);
      } else if (getParticipant() instanceof SingleUser && o.getParticipant() instanceof SingleUser) {
        return ((SingleUser) getParticipant()).getName().compareTo(((SingleUser) o.getParticipant()).getName());
      } else {
        return getParticipant().getId().compareTo(o.getParticipant().getId());
      }
    }

    /**
     * Getter for priority.
     *
     * @return the priority
     */
    public int getPriority() {

      return priority;
    }
  }

  /**
   * Tab showing all {@link ConfirmedRegistration}s of the {@link Event}.
   *
   * @author klassm
   */
  private class TabEventConfirmedRegistrationsPanel extends Panel {
    /**
     * unique serialization id.
     */
    private static final long serialVersionUID = 6911895599022548304L;


    /**
     * Creates a new {@link TabEventConfirmedRegistrationsPanel}.
     *
     * @param id wicket id.
     */
    public TabEventConfirmedRegistrationsPanel(String id) {

      super(id);

      List<ConfirmedRegistration> confirmedRegistrations = controller.getConfirmedRegistrationsByEvent(event);
      List<ConfirmedRegistrationMapping> confirmedRegistrationsMapped = new LinkedList<ConfirmedRegistrationMapping>();

      for (ConfirmedRegistration cr : confirmedRegistrations) {
        confirmedRegistrationsMapped.add(new ConfirmedRegistrationMapping(cr.getId(), controller.getParticipantById(cr
            .getParticipant()), controller.getUserById(cr.getInitiator()), cr.getDate(), cr.getProcedure()));
      }

      add(new ListView<ConfirmedRegistrationMapping>("eventDetail.confirmedRegistrations.list", confirmedRegistrationsMapped) {
        /**
         * unique serialization id.
         */
        private static final long serialVersionUID = 3859150397498901051L;

        @Override
        protected void populateItem(ListItem<ConfirmedRegistrationMapping> item) {

          final ConfirmedRegistrationMapping cr = item.getModelObject();

          String participant = "group";
          if (cr.getParticipant() instanceof SingleUser) {
            participant = ((SingleUser) cr.getParticipant()).getName();
          }
          item.add(new Label("eventDetail.confirmedRegistrations.list.participant", participant));

          String matriculationNumber = "";
          if (cr.getParticipant() instanceof Student) {
            matriculationNumber = String.valueOf(((Student) cr.getParticipant()).getMatriculationNumber());
          }
          item.add(new Label("eventDetail.confirmedRegistrations.list.matriculationNumber", matriculationNumber));

          item.add(new Label("eventDetail.confirmedRegistrations.list.date", dateFormat.format(cr.getDate().getTime())));

          item.add(new Label("eventDetail.confirmedRegistrations.list.initiator", cr.getInitiator().getName()));

          String procedure = "direkt";
          if (cr.getProcedure() != null) {
            procedure = cr.getProcedure().getName();
          }
          item.add(new Label("eventDetail.confirmedRegistrations.list.procedure", procedure));

          Link<ConfirmedRegistration> delLink = new Link<ConfirmedRegistration>("delete") {
            /**
             * unique serialization id.
             */
            private static final long serialVersionUID = -7731226194758185622L;

            @Override
            public void onClick() {

              try {
                controller.deleteConfirmedRegistration(controller.getConfirmedRegistrationById(cr.getId()));
              } catch (NoMatchingElementException e) {
                // no CR with this id => already deleted
              }
              setResponsePage(new OnePanelPage(new EventDetailPanel(OnePanelPage.getPanelIdOne(), event.getId())));
            }
          };
          delLink.add(new JavascriptEventConfirmation("onclick", "Sind Sie sicher?"));

          item.add(delLink);
        }
      });
    }
  }

  /**
   * Shows detail information about the {@link Event}.
   *
   * @author klassm
   */
  private class TabEventDetailPanel extends Panel {
    /**
     * unique serialization id.
     */
    private static final long serialVersionUID = -2297295995003040409L;

    /**
     * Creates a new {@link TabEventDetailPanel}.
     *
     * @param id wicket id.
     */
    public TabEventDetailPanel(String id) {

      super(id);

      add(new Label("eventDetail.category", event.getSubject().getCategory().getName()));
      add(new Label("eventDetail.subject", event.getSubject().getName()));
      add(new Label("eventDetail.eventId", String.valueOf(event.getEventId())));
      add(new Label("eventDetail.eventInfoText", event.getDetailInformation()));
      add(new Label("eventDetail.maxParticipants", String.valueOf(event.getMaxParticipants())));
      add(new Label("eventDetail.prioLists", String.valueOf(controller.getPriorityListItemsByEvent(event).size())));
      add(new Label("eventDetail.registrations", String.valueOf(controller.getConfirmedRegistrationsByEvent(event).size())));
    }
  }

  /**
   * Tab showing information about all {@link PriorityList}s of an {@link Event}.
   *
   * @author klassm
   */
  private class TabEventPriorityListPanel extends Panel {
    /**
     * unique serialization id.
     */
    private static final long serialVersionUID = -6884599158989674745L;

    /**
     * Creates a new {@link TabEventPriorityListPanel}.
     *
     * @param id wicket id.
     */
    public TabEventPriorityListPanel(String id) {

      super(id);

      List<PriorityListItem> prioItems = controller.getPriorityListItemsByEvent(event);
      List<PriorityListItemMapping> mapped = new LinkedList<PriorityListItemMapping>();

      for (PriorityListItem prioItem : prioItems) {
        mapped.add(new PriorityListItemMapping(prioItem.getId(), controller.getParticipantById(prioItem.getPriorityList()
            .getParticipant()), controller.getUserById(prioItem.getPriorityList().getInitiator()), prioItem
            .getPriority(), prioItem.getPriorityList().getDate(), prioItem.getPriorityList().getProcedure(), prioItem.getPriorityList()));
      }

      add(new ListView<PriorityListItemMapping>("eventDetail.priorityList.list", mapped) {
        /**
         * unique serialization id.
         */
        private static final long serialVersionUID = 1186966986878386320L;

        @Override
        protected void populateItem(ListItem<PriorityListItemMapping> item) {

          final PriorityListItemMapping pr = item.getModelObject();

          item.add(new Label("eventDetail.priorityList.list.priority", String.valueOf(pr.getPriority())));

          String participant = "group";
          if (pr.getParticipant() instanceof SingleUser) {
            participant = ((SingleUser) pr.getParticipant()).getName();
          }
          item.add(new Label("eventDetail.priorityList.list.participant", participant));

          String matriculationNumber = "";
          if (pr.getParticipant() instanceof SingleUser && ((SingleUser) pr.getParticipant()) instanceof Student) {
            matriculationNumber = String.valueOf(((Student) pr.getParticipant()).getMatriculationNumber());
          }
          item.add(new Label("eventDetail.confirmedRegistrations.list.matriculationNumber", matriculationNumber));

          item.add(new Label("eventDetail.confirmedRegistrations.list.date", dateFormat.format(pr.getDate().getTime())));

          item.add(new Label("eventDetail.confirmedRegistrations.list.initiator", pr.getInitiator().getName()));

          String procedure = "direkt";
          if (pr.getProcedure() != null) {
            procedure = pr.getProcedure().getName();
          }

          item.add(new Label("eventDetail.confirmedRegistrations.list.procedure", procedure));

          Link<ConfirmedRegistration> delLink = new Link<ConfirmedRegistration>("delete") {
            /**
             * unique serialization id.
             */
            private static final long serialVersionUID = -7731226194758185622L;

            @Override
            public void onClick() {

              try {
                controller.removePriolist(pr.getPriorityList());
              } catch (NoMatchingElementException e) {
                ;
              }
              setResponsePage(new OnePanelPage(new EventDetailPanel(OnePanelPage.getPanelIdOne(), event.getId())));
            }
          };
          delLink.add(new JavascriptEventConfirmation("onclick", "Sind Sie sicher?"));

          item.add(delLink);
        }
      });
    }
  }

  /**
   * Class representing a direct registration with an event.
   *
   * @author klassm
   */
  private class TabEventRegistrationPanel extends Panel {
    private static final String SUCCESS = "Erfolgreich";

    /**
     * unique serialization id.
     */
    private static final long serialVersionUID = 6202486575953880341L;

    /**
     * Default constructor with only the wicket id.
     *
     * @param id wicket id.
     */
    public TabEventRegistrationPanel(String id) {

      super(id);

      add(new UserSelectPanel("userSelectPanel") {
        /**
         * unique serialization id.
         */
        private static final long serialVersionUID = -3208826949677977852L;

        @Override
        protected boolean onSendSubmit(SingleUser singleUser) {

          if (isUserRegistered(singleUser)) {
            // TODO Sprache:
            getFeedbackPanel().error(singleUser.getName() + " ist bereits registriert");
            return false;
          }
          controller.createConfirmedRegistration(singleUser, event, getSession());

          getFeedbackPanel().info(SUCCESS);
          return true;
        }

        @Override
        protected boolean vetoFindSubmit(SingleUser user) {

          if (isUserRegistered(user)) {
            // TODO Sprache:
            getFeedbackPanel().error(user.getName() + " ist bereits registriert");
            return true;
          }
          return false;
        }

        protected String getSubmitButtonText() {

          return "Benutzer einbuchen";
        }
      });
    }

    /**
     * Checks whether a given singleUser is registered with the {@link EventDetailPanel} event.
     *
     * @param singleUser singleUser to check
     * @return true if singleUser is registered, else false.
     * @throws IllegalArgumentException if no singleUser was given.
     */
    public boolean isUserRegistered(SingleUser singleUser) {

      if (singleUser == null) {
        throw new IllegalArgumentException("no singleUser given");
      }
      for (ConfirmedRegistration registration : controller.getConfirmedRegistrationsByEvent(event)) {
        if (registration.getParticipant().equals(singleUser.getId())) {
          return true;
        }
      }
      return false;
    }
  }
}
