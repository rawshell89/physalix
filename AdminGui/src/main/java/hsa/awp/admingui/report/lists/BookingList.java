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
import hsa.awp.admingui.report.util.Alignment;
import hsa.awp.admingui.report.util.ExportList;
import hsa.awp.admingui.report.util.HeaderFooter;
import hsa.awp.admingui.report.util.Row;
import hsa.awp.admingui.report.util.formats.pdf.PdfCellProperties;
import hsa.awp.admingui.report.util.formats.pdf.PdfPrintable;
import hsa.awp.admingui.report.util.formats.pdf.PdfProperties;
import hsa.awp.admingui.report.view.PrintableLists;
import hsa.awp.campaign.model.Campaign;
import hsa.awp.event.model.Event;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static hsa.awp.event.util.EventFormattingUtils.formatIdSubjectNameAndDetailInformation;

/**
 * A list which displays all registrations in a campaign.
 */
public class BookingList extends ExportList implements PdfPrintable {

  /**
   * Campaign whose registrations will be displayed.
   */
  private Campaign campaign;


  /**
   * Controller which feeds the class with data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * List of events which are displayed.
   */
  private List<Event> events;

  /**
   * List for all bookings in a campaign.
   *
   * @param campaign campaign
   */
  public BookingList(Campaign campaign) {

    InjectorHolder.getInjector().inject(this);

    this.campaign = campaign;


    events = new LinkedList<Event>();

    for (Long id : campaign.getEventIds()) {
      events.add(controller.getEventById(id));
    }

    fillRows();
  }

  private void fillRows() {
    int eventIndex = 1;
    for (Event event : events) {
      List<String> content = new ArrayList<String>();

      content.add(String.valueOf(eventIndex));
      content.add(getEventDescription(event));
      content.add(generateTeachersString(event));
      content.add(String.valueOf(controller.countConfirmedRegistrationsByEventId(event.getId())));
      content.add(String.valueOf(event.getMaxParticipants()));

      eventIndex++;

      rows.add(new Row(content));
    }
  }

  private String getEventDescription(Event event) {
    return formatIdSubjectNameAndDetailInformation(event);
  }

  private String generateTeachersString(Event event) {
    String lects = "";
    for (Long id : event.getTeachers()) {
      lects += controller.getUserById(id).getName() + ",";
    }

    if (!lects.equals("")) {
      return lects.substring(0, lects.length() - 1);
    } else {
      return "";
    }
  }

  @Override
  public PdfProperties getPdfProperties() {
    List<PdfCellProperties> cellProperties = new ArrayList<PdfCellProperties>();

    cellProperties.add(new PdfCellProperties(2f, "Nr."));
    cellProperties.add(new PdfCellProperties(8f, "Titel"));
    cellProperties.add(new PdfCellProperties(3f, "Dozent"));
    cellProperties.add(new PdfCellProperties("Anz.", Alignment.CENTER));
    cellProperties.add(new PdfCellProperties("Max.", Alignment.CENTER));


    HeaderFooter headerFooter = new HeaderFooter("Belegungsliste f\u00fcr " + campaign.getName());

    return new PdfProperties(headerFooter, cellProperties);
  }

  @Override
  public String toString() {
    return PrintableLists.BOOKINGLIST.getTitle();  //To change body of implemented methods use File | Settings | File Templates.
  }
}
