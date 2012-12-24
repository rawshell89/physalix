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

package hsa.awp.admingui.report.lists;

import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.admingui.report.util.ExportList;
import hsa.awp.admingui.report.util.HeaderFooter;
import hsa.awp.admingui.report.util.Row;
import hsa.awp.admingui.report.util.formats.csv.CsvPrintable;
import hsa.awp.admingui.report.util.formats.csv.CsvProperties;
import hsa.awp.admingui.report.util.formats.pdf.PdfCellProperties;
import hsa.awp.admingui.report.util.formats.pdf.PdfPrintable;
import hsa.awp.admingui.report.util.formats.pdf.PdfProperties;
import hsa.awp.campaign.model.ConfirmedRegistration;
import hsa.awp.event.model.Event;
import hsa.awp.user.model.SingleUser;
import hsa.awp.user.model.Student;
import hsa.awp.user.model.User;
import hsa.awp.user.util.UserUtil;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static hsa.awp.event.util.EventFormattingUtils.formatIdSubjectNameAndDetailInformation;

public class ParticipantList extends ExportList implements CsvPrintable, PdfPrintable {

  private Event event;

  private Comparator<SingleUser> userComparator = new Comparator<SingleUser>() {
    @Override
    public int compare(SingleUser singleUser, SingleUser singleUser1) {
      String lastName = singleUser.getLastName();
      String lastName1 = singleUser1.getLastName();
      if (lastName.compareTo(lastName1) == 0) {
        return singleUser.getName().compareTo(singleUser1.getName());
      }
      return lastName.compareTo(lastName1);
    }
  };

  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  private boolean matriculationNumber, mail, eventDesc;

  public ParticipantList(Event event, boolean matriculationNumber, boolean mail, boolean eventDesc) {
    InjectorHolder.getInjector().inject(this);

    this.event = event;
    this.matriculationNumber = matriculationNumber;
    this.mail = mail;
    this.eventDesc = eventDesc;
    fillRows();
  }

  public ParticipantList(Event event, boolean matriculationNumber, boolean mail) {
    this(event, matriculationNumber, mail, false);
  }

  private String getFormattedUserName(SingleUser user) {
    return String.format("%s, %s", user.getLastName(), user.getFirstName());
  }

  private void fillRows() {

    List<SingleUser> users = getUsersFromEvent();
    Collections.sort(users, userComparator);

    int index = 1;
    for (SingleUser user : users) {
      List<String> content = new ArrayList<String>();
      content.add(String.valueOf(index));

      content.add(getFormattedUserName(user));

      if (matriculationNumber) {
        content.add(getMatriculationNumberFromUser(user));
      }

      if (mail) {
        content.add(user.getMail());
      }

      content.add(getStudyCourseFromUser(user));
      content.add(getTermFromUser(user));

      if (eventDesc) {
        content.add(getEventDescription(event));
      }

      rows.add(new Row(content));
      index++;
    }

  }

  private String getEventDescription(Event event) {
    return formatIdSubjectNameAndDetailInformation(event);
  }

  private String getMatriculationNumberFromUser(SingleUser user) {
    if (user instanceof Student) {
      return String.valueOf(((Student) user).getMatriculationNumber());
    } else {
      return "-";
    }
  }

  private String getStudyCourseFromUser(SingleUser user) {
    if (user instanceof Student) {
      return ((Student) user).getStudyCourse().getName();
    } else {
      return "-";
    }
  }

  private String getTermFromUser(SingleUser user) {
    if (user instanceof Student) {
      return String.valueOf(((Student) user).getTerm());
    } else {
      return "-";
    }
  }

  private List<SingleUser> getUsersFromEvent() {

    List<SingleUser> users = new ArrayList<SingleUser>();

    for (ConfirmedRegistration reg : controller.getConfirmedRegistrationsByEvent(event)) {
      User usr = controller.getParticipantById(reg.getParticipant());
      users.addAll(UserUtil.generateSingleUserFromUser(usr));
    }

    return users;

  }

  @Override
  public PdfProperties getPdfProperties() {
    List<PdfCellProperties> cellProperties = new ArrayList<PdfCellProperties>();

    cellProperties.add(new PdfCellProperties(2f, "Nr."));
    cellProperties.add(new PdfCellProperties(10f, "Teilnehmer"));
    if (matriculationNumber) {
      cellProperties.add(new PdfCellProperties(4f, "Mat.Nr."));
    }
    if (mail) {
      cellProperties.add(new PdfCellProperties(14f, "E-Mail"));
    }
    cellProperties.add(new PdfCellProperties(5f, "Studium"));
    cellProperties.add(new PdfCellProperties(1.5f, "Sem."));

    event = controller.getEventById(event.getId());

    HeaderFooter headerFooter = new HeaderFooter(formatIdSubjectNameAndDetailInformation(event), "Teilnehmerliste");

    return new PdfProperties(headerFooter, cellProperties);
  }

  @Override
  public CsvProperties getCsvProperties() {
    return new CsvProperties();
  }

  @Override
  public String toString() {
    return event.getSubject().getName() + "-Teilnehmerliste";
  }
}
