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
import hsa.awp.admingui.edit.event.EventPanel;
import hsa.awp.admingui.util.AbstractDeleteLink;
import hsa.awp.admingui.util.AbstractDetailLink;
import hsa.awp.admingui.util.AbstractEditLink;
import hsa.awp.admingui.util.AccessUtil;
import hsa.awp.event.model.Event;
import hsa.awp.event.model.Term;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static hsa.awp.event.util.EventFormattingUtils.formatDetailInformation;
import static hsa.awp.event.util.EventFormattingUtils.formatEventId;

/**
 * EventListPanel.Shows all existing {@link Event}.
 *
 * @author Rico Lieback
 */
public class EventListPanel extends Panel {
  /**
   * generated UID.
   */

  private static final long serialVersionUID = 6197456865823351891L;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private IAdminGuiController controller;


  private FeedbackPanel feedbackPanel = new FeedbackPanel("eventList.feedback");

  private IModel<Term> termModel = new Model<Term>();

  public List<Event> getSelectableEvents() {
    if (AccessUtil.isTeacher()) {
      String userName = SecurityContextHolder.getContext().getAuthentication().getName();
      selectedEvents = controller.getEventsByTeacher(userName);
      if (termModel.getObject() != null) {
        return filterForTerm();
      }
      return selectedEvents;
    } else if (termModel.getObject() != null) {
      return controller.getEventsByTermAndMandator(termModel.getObject().getTermDesc(), getSession());
    }

    return controller.getEventsByMandator(getSession());
  }

  private List<Event> filterForTerm() {
    List<Event> filteredEvents = new ArrayList<Event>();
    for (Event e : selectedEvents) {
      if (e.getTerm().equals(termModel.getObject())) {
        filteredEvents.add(e);
      }
    }
    return filteredEvents;
  }


  private List<Event> selectedEvents;
  private WebMarkupContainer listContainer = new WebMarkupContainer("eventList.box");

  /**
   * default constuctor. create a list of all existing procedures and adds a edit link.
   *
   * @param id id of the ProcedureList
   */
  public EventListPanel(String id) {

    super(id);

    selectedEvents = getSelectableEvents();

    // create a model which holds the events
    IModel<List<Event>> eventModel = new LoadableDetachableModel<List<Event>>() {
      /**
       *
       */
      private static final long serialVersionUID = 4463199687890366067L;

      @Override
      protected List<Event> load() {


        List<Event> list = getSelectableEvents(); // get the existing events
        Collections.sort(list, new Comparator<Event>() {
          @Override
          public int compare(Event o1, Event o2) {

            int val = o1.getSubject().getName().compareTo(o2.getSubject().getName());
            if (val == 0) {
              return ((Integer) o1.getEventId()).compareTo(o2.getEventId());
            }
            return val;
          }
        });
        return list;
      }
    };

    // create a wicket ListView
    ListView<Event> eventListView = new ListView<Event>("evtView", eventModel) {
      /**
       * generated UID
       */
      private static final long serialVersionUID = 1049749247094208402L;

      @Override
      protected void populateItem(final ListItem<Event> item) {

        Event event = item.getModelObject();

        item.add(new Label("eventId", new Model<String>(formatEventId(event))));
        item.add(new Label("subjectName", new Model<String>(event.getSubject().getName())));
        item.add(new Label("eventInfoText", new Model<String>(formatDetailInformation(event))));

        item.add(createEditLink(item));
        item.add(createDeleteLink(item));
        item.add(createDetailLink(item));
      }

      private Component createEditLink(final ListItem<Event> item) {

        AbstractEditLink<Event> editLink = new AbstractEditLink<Event>("evtLink", item.getModelObject()) {
          @Override
          public void modifyItem(Event event) {
            setResponsePage(new OnePanelPage(
                new EventPanel(OnePanelPage.getPanelIdOne(), event)));
          }
        };

        AccessUtil.allowRender(editLink, "editEvent");

        return editLink;
      }

      private Component createDeleteLink(final ListItem<Event> item) {

        AbstractDeleteLink<Event> delLink = new AbstractDeleteLink<Event>("evtDeleteLink", item.getModelObject()) {

          @Override
          public void modifyItem(Event event) {

            controller.deleteEvent(event);

            setResponsePage(new OnePanelPage(new EventListPanel(OnePanelPage.getPanelIdOne())));
            feedbackPanel.info("Veranstaltung gel\u00f6scht."); // TODO: Sprache
          }
        };

        AccessUtil.allowRender(delLink, "deleteEvents");
        return delLink;
      }

      private Component createDetailLink(final ListItem<Event> item) {

        AbstractDetailLink<Event> detailLink = new AbstractDetailLink<Event>("evtDetailLink", item.getModelObject()) {
          @Override
          public void modifyItem(Event event) {
            setResponsePage(new OnePanelPage(new EventDetailPanel(OnePanelPage.getPanelIdOne(), event.getId())));
          }
        };

        AccessUtil.allowRender(detailLink, "viewEventDetails");
        return detailLink;
      }
    };


    // adding the ListView to the panel
    listContainer.setOutputMarkupId(true);
    listContainer.add(eventListView);
    add(configureTermDropDown());
    add(listContainer);
    add(feedbackPanel);
  }

  private Component configureTermDropDown() {

    List<Term> termChoices = controller.getTermsByMandator(getSession());


    DropDownChoice<Term> termDropDown = new DropDownChoice<Term>("eventList.term", termModel, termChoices, new IChoiceRenderer<Term>() {

      @Override
      public Object getDisplayValue(Term object) {
        if (object != null) {
          return object.toString();
        } else {
          return "Alle";
        }
      }

      @Override
      public String getIdValue(Term object, int index) {
        if (object != null) {
          return object.toString();
        } else {
          return String.valueOf(index);
        }
      }
    });

    termDropDown.add(new AjaxFormComponentUpdatingBehavior("onchange") {

      @Override
      protected void onUpdate(AjaxRequestTarget target) {

        getSelectableEvents();
        target.addComponent(listContainer);
      }
    });

    return termDropDown;
  }


}
