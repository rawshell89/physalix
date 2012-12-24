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

package hsa.awp.admingui.edit.event.tabs;

import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.admingui.edit.event.OccurrencePanel;
import hsa.awp.event.model.Event;
import hsa.awp.event.model.Exam;
import hsa.awp.event.model.Occurrence;
import hsa.awp.event.model.Timetable;
import hsa.awp.gui.util.LoadableDetachedModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * tab for timetable actions.
 *
 * @author Basti
 */
public class TabTimetable extends Panel {
  /**
   * generated UID.
   */
  private static final long serialVersionUID = -3428052872202333030L;

  /**
   * Container for list which displays all {@link Occurrence} associated with the {@link Event}.
   */
  private WebMarkupContainer timetableListContainer;

  /**
   * Container for the {@link OccurrencePanel}.
   */
  private WebMarkupContainer timetableEditContainer;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * event whose timetable is edited.
   */
  private Event event;

  /**
   * Tab for timetable.
   *
   * @param id  wicket id
   * @param evt event whose timetable
   */
  public TabTimetable(String id, Event evt) {

    super(id);

    this.event = evt;

    timetableEditContainer = new WebMarkupContainer("event.tabTimetable.TimetableEditContainer");
    timetableEditContainer.setOutputMarkupId(true);
    add(timetableEditContainer);

    OccurrencePanel occurrencePanel = new OccurrencePanel("event.tabTimetable.OccurrencePanel", event, timetableListContainer);
    timetableEditContainer.add(occurrencePanel);
    occurrencePanel.setVisible(false);
    // ExamPanel examPanel = new ExamPanel("event.tabExam.examPanel", examListContainer);
    // examEditContainer.add(examPanel);
    // examPanel.setVisible(false);

    timetableListContainer = new WebMarkupContainer("event.tabTimetable.TimetableListContainer");
    timetableListContainer.setOutputMarkupId(true);
    add(timetableListContainer);

    timetableListContainer.add(new AjaxFallbackLink<Exam>("event.tabTimetable.createNewTimetableLink") {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 1L;

      @Override
      public void onClick(AjaxRequestTarget target) {

        target.addComponent(timetableEditContainer);
        timetableEditContainer.removeAll();
        timetableEditContainer
            .add(new OccurrencePanel("event.tabTimetable.OccurrencePanel", event, timetableListContainer));
        // examEditContainer.add(new ExamPanel("event.tabExam.examPanel", examListContainer));
      }
    });

    final LoadableDetachedModel<List<Occurrence>> occurrences = new LoadableDetachedModel<List<Occurrence>>() {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 7140862438222206869L;

      @Override
      protected List<Occurrence> load() {

        event = controller.getEventById(event.getId());

        if (event.getId().equals(0L) || event.getTimetable() == null || event.getTimetable().getOccurrences().size() <= 0) {
          Timetable time = Timetable.getInstance(controller.getActiveMandator(getSession()));
          return new LinkedList<Occurrence>(time.getOccurrences());
        }
        return new LinkedList<Occurrence>(controller.getEventById(event.getId()).getTimetable().getOccurrences());
      }
    };

    ListView<Occurrence> occurrenceList = new ListView<Occurrence>("event.tabTimetable.OccurrenceList", occurrences) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 999046116701991766L;

      @Override
      protected void populateItem(final ListItem<Occurrence> item) {

        final Occurrence occurence = item.getModelObject();
        DateFormat singleFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        DateFormat dayFormat = new SimpleDateFormat("EEEE");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String s;
        switch (item.getModelObject().getType()) {
          case SINGLE:
            s = "Einzeltermin vom " + singleFormat.format(item.getModelObject().getStartDate().getTime());
            s += " bis " + singleFormat.format(item.getModelObject().getEndDate().getTime());
            break;
          case PERIODICAL:
            s = "Wöchentlich am " + dayFormat.format(item.getModelObject().getStartDate().getTime());
            s += " von " + timeFormat.format(item.getModelObject().getStartDate().getTime()) + " bis "
                + timeFormat.format(item.getModelObject().getEndDate().getTime());
            break;
          default:
            s = "";
        }

        item.add(new Label("event.tabTimetable.string", s));
        item.add(new AjaxFallbackLink<Occurrence>("event.tabTimetable.edit") {
          /**
           * unique serialization id.
           */
          private static final long serialVersionUID = 1L;

          @Override
          public void onClick(AjaxRequestTarget target) {

            target.addComponent(timetableEditContainer);
            timetableEditContainer.removeAll();
            timetableEditContainer.add(new OccurrencePanel("event.tabTimetable.OccurrencePanel", event,
                timetableListContainer, occurence));
            // examEditContainer.add(new ExamPanel("event.tabExam.examPanel", examListContainer, exam));
          }
        });

        item.add(new AjaxFallbackLink<Occurrence>("event.tabTimetable.delete") {
          /**
           * unique serialization id.
           */
          private static final long serialVersionUID = 1L;

          @Override
          public void onClick(AjaxRequestTarget target) {

            target.addComponent(timetableListContainer);
            controller.deleteOccurrence(item.getModelObject(), event.getTimetable());
          }
        });
      }
    };
    timetableListContainer.add(occurrenceList);
    timetableListContainer.setOutputMarkupId(true);
  }
}
