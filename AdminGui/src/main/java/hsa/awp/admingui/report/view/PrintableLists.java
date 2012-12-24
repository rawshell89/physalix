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

package hsa.awp.admingui.report.view;


import hsa.awp.admingui.report.lists.*;
import hsa.awp.admingui.report.util.ExportList;
import hsa.awp.admingui.report.util.ListType;

/**
 * Data about various types of lists.
 *
 * @author basti
 */
public enum PrintableLists {

  COURSEPARTICIPATIONLIST("Belegungsliste für Prüfungsplan",
      "Liste enthält den Veranstaltungstitel und die Anzahl der Teilnehmer", ListType.CAMPAIGNLIST, CourseParticipationList.class),


  BOOKINGLIST(
      "Belegungsliste",
      "Liste enthält den Veranstaltungstitel, den Name des Dozenten, die Anzahl der Teilnehmer und die "
          + "maximale Teilnehmeranzahl", ListType.CAMPAIGNLIST,
      BookingList.class),


  PARTICIPANTLIST(
      "Teilnehmerliste",
      "Liste enthält den Namen, das Semester und den Studiengang der Studenten, die sich für die ausgewählte "
          + "Veranstaltung angemeldet haben.", ListType.EVENTLIST,
      ParticipantList.class),


  EXAMOFFICELIST("Prüfungsamtliste", "Liste mit Matrikelnummer und Verantaltungsnummer für das Prüfungsamt formattiert", ListType.CAMPAIGNLIST,
      ExamOfficeList.class),

  COURSECONFLICTLIST("Konfliktliste", "Konfliktliste für den Prüfungsplan", ListType.EVENTLIST, ConflictList.class);


  private String title;

  private String description;

  private ListType listType;

  /**
   * Class of the list.
   */
  private Class<? extends ExportList> listClass;


  PrintableLists(String title, String description, ListType listType, Class<? extends ExportList> listClass) {
    this.title = title;
    this.description = description;
    this.listType = listType;
    this.listClass = listClass;
  }

  /**
   * Getter for description.
   *
   * @return the description
   */
  public String getDescription() {

    return description;
  }

  /**
   * Getter for the lists class.
   *
   * @return the listClass
   */
  public Class<? extends ExportList> getListClass() {

    return listClass;
  }

  /**
   * Getter for title.
   *
   * @return the title
   */
  public String getTitle() {

    return title;
  }
}
