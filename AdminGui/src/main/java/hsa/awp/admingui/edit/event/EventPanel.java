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

package hsa.awp.admingui.edit.event;

import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.admingui.edit.event.tabs.*;
import hsa.awp.event.model.Event;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.LinkedList;
import java.util.List;

import static hsa.awp.event.util.EventFormattingUtils.formatIdSubjectNameAndDetailInformation;

/**
 * Panel with the functionality to create an event.
 *
 * @author Basti
 * @author Rico
 * @author Matze
 */
public class EventPanel extends Panel {
  /**
   * generated UID.
   */
  private static final long serialVersionUID = 6759298091464910049L;

  /**
   * Event which will be created/edited.
   */
  private Event event;

  private WebMarkupContainer eventDetailContainer;

  private Label headlineLabel;

  /**
   * Tab which is displayed first. It contains the general information about the event.
   */
  private AbstractTab tabGeneral;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * Tab for exam management.
   */
  private AbstractTab tabExam;

  /**
   * Tab to configure the timetable.
   */
  private AbstractTab tabTimeTable;

  /**
   * Tab to configure the timetable.
   */
  private AbstractTab tabTeacher;

  /**
   * Tab for adding rules to an Event.
   */
  private AbstractTab tabRules;

  /**
   * Constructor for CreateEvent. This is the setup for the panel and here are all components registered.
   *
   * @param id wicket:id which connects markup with code.
   */
  public EventPanel(String id) {

    this(id, null);
  }

  /**
   * Constructor for CreateEvent. This is the setup for the panel and here are all components registered.
   *
   * @param id  wicket:id which connects markup with code.
   * @param evt the event which has to be edited.
   */
  public EventPanel(String id, Event evt) {

    super(id);

    if (evt == null) {
      this.event = Event.getInstance(0, controller.getActiveMandator(getSession()));
    } else {
      this.event = evt;
    }


    eventDetailContainer = new WebMarkupContainer("event.detail");
    eventDetailContainer.setOutputMarkupId(true);
    add(eventDetailContainer);

    // TODO Sprache
    String headline;
    if (isNewEvent(event)) {
      headline = "Neue Veranstaltung erstellen";
    } else {
      headline = formatIdSubjectNameAndDetailInformation(event) + " bearbeiten";
    }

    headlineLabel = new Label("event.detail.headline", headline);
    eventDetailContainer.add(headlineLabel);

    List<ITab> tabs = new LinkedList<ITab>();

    tabGeneral = new AbstractTab(new Model<String>("Allgemeines")) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 1L;

      @Override
      public Panel getPanel(String arg0) {

        return new TabGeneral(arg0, event, EventPanel.this);
      }
    };
    tabs.add(tabGeneral);

    // disable edit tabs for new events
    if (event.getEventId() != 0) {
      tabExam = new AbstractTab(new Model<String>("Prüfungen")) {
        /**
         * unique serialization id.
         */
        private static final long serialVersionUID = 1L;

        @Override
        public Panel getPanel(String arg0) {

          return new TabExam(arg0, event);
        }
      };

      tabTimeTable = new AbstractTab(new Model<String>("Stundenplan")) {
        /**
         * unique serialization id.
         */
        private static final long serialVersionUID = -4136336402272228404L;

        @Override
        public Panel getPanel(String panelId) {

          return new TabTimetable(panelId, event);
        }
      };

      tabTeacher = new AbstractTab(new Model<String>("Dozenten")) {
        /**
         * generated UID.
         */
        private static final long serialVersionUID = 1985212376578212191L;

        @Override
        public Panel getPanel(String panelId) {

          return new TabTeacher(panelId, event);
        }
      };

      tabRules = new AbstractTab(new Model<String>("Regeln")) {
        /**
         * unique serialization id.
         */
        private static final long serialVersionUID = 6599172109553136464L;

        @Override
        public Panel getPanel(String panelId) {

          return new TabRule(panelId, event);
        }
      };

      tabs.add(tabExam);
      tabs.add(tabTimeTable);
      tabs.add(tabTeacher);
      tabs.add(tabRules);
    }

    TabbedPanel tabbedPanel = new AjaxTabbedPanel("tabbedPanel", tabs);
    tabbedPanel.setOutputMarkupId(true);

    add(tabbedPanel);
  }

  private boolean isNewEvent(Event event) {
    return event.getSubject() == null;
  }

  public void updateEventDetails(AjaxRequestTarget target) {

    target.addComponent(eventDetailContainer);

    event = controller.getEventById(event.getId());

    headlineLabel.setDefaultModelObject(formatIdSubjectNameAndDetailInformation(event));
  }
}
