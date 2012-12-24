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

package hsa.awp.usergui;

import hsa.awp.event.model.Event;
import hsa.awp.event.model.Occurrence;
import hsa.awp.user.model.SingleUser;
import hsa.awp.user.model.User;
import hsa.awp.usergui.controller.IUserGuiController;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Panel showing detailed information about an {@link Event}.
 *
 * @author klassm
 */
public class EventDetailPanel extends Panel {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = 9180564827437598145L;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "usergui.controller")
  private transient IUserGuiController controller;

  /**
   * Constructor.
   *
   * @param id    wicket:id.
   * @param event event to show.
   */
  public EventDetailPanel(String id, Event event) {

    super(id);

    List<SingleUser> teachers = new LinkedList<SingleUser>();
    event = controller.getEventById(event.getId());
    for (Long teacherId : event.getTeachers()) {
      User user = controller.getUserById(teacherId);
      if (user != null && user instanceof SingleUser) {
        teachers.add((SingleUser) user);
      }
    }
    Collections.sort(teachers, new Comparator<SingleUser>() {
      @Override
      public int compare(SingleUser o1, SingleUser o2) {

        return o1.getName().compareTo(o2.getName());
      }
    });

    StringBuffer teachersList = new StringBuffer();
    if (teachers.size() == 0) {
      teachersList.append("keine");
    } else {
      boolean first = true;
      for (SingleUser teacher : teachers) {
        if (first) {
          first = false;
        } else {
          teachersList.append(", ");
        }

        teachersList.append(teacher.getName());
      }
    }

    WebMarkupContainer eventGeneral = new WebMarkupContainer("event.general");
    add(eventGeneral);

    eventGeneral.add(new Label("event.general.caption", "Allgemeines"));

    eventGeneral.add(new Label("event.general.eventId", new Model<Integer>(event.getEventId())));
    eventGeneral.add(new Label("event.general.subjectName", new Model<String>(event.getSubject().getName())));
    eventGeneral.add(new Label("event.general.maxParticipants", new Model<Integer>(event.getMaxParticipants())));

    Label teacherLabel = new Label("event.general.teachers", new Model<String>(teachersList.toString()));
    eventGeneral.add(teacherLabel);

    eventGeneral.add(new Label("event.general.eventDescription", new Model<String>(event.getDetailInformation())));

    ExternalLink detailLink = new ExternalLink("event.general.link", event.getSubject().getLink());
    eventGeneral.add(detailLink);
    detailLink.add(new Label("event.general.linkDesc", event.getSubject().getLink()));

    String description = event.getSubject().getDescription();
    if (description == null || ((description = description.trim().replace("\n", "<br>")).equals(""))) {
      description = "keine";
    }
    Label subjectDescription = new Label("event.general.subjectDescription", new Model<String>(description));
    subjectDescription.setEscapeModelStrings(false);
    eventGeneral.add(subjectDescription);

    WebMarkupContainer eventTimetable = new WebMarkupContainer("event.timetable");
    add(eventTimetable);

    eventTimetable.add(new Label("event.timetable.caption", "Stundenplan"));

    List<Occurrence> occurences;
    if (event.getTimetable() == null) {
      occurences = new LinkedList<Occurrence>();
    } else {
      occurences = new LinkedList<Occurrence>(event.getTimetable().getOccurrences());
    }

    eventTimetable.add(new ListView<Occurrence>("event.timetable.list", occurences) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = -1041971433878928045L;

      @Override
      protected void populateItem(ListItem<Occurrence> item) {

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

        item.add(new Label("event.timetable.list.occurrence", s));
      }
    });
    if (occurences.size() == 0) {
      eventTimetable.setVisible(false);
    }
  }
}
