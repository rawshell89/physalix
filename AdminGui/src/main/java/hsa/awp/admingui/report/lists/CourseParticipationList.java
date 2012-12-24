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
import hsa.awp.admingui.report.util.Row;
import hsa.awp.admingui.report.util.formats.txt.TxtCellProperties;
import hsa.awp.admingui.report.util.formats.txt.TxtPrintable;
import hsa.awp.admingui.report.util.formats.txt.TxtProperties;
import hsa.awp.admingui.report.view.PrintableLists;
import hsa.awp.campaign.model.Campaign;
import hsa.awp.event.model.Event;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

import static hsa.awp.event.util.EventFormattingUtils.formatIdSubjectNameAndDetailInformation;

/**
 * List which shows the Course participation.
 *
 * @author basti
 */
public class CourseParticipationList extends ExportList implements TxtPrintable {
  /**
   * Controller which feeds the class with data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * Campaign whose registrations will be displayed.
   */
  private Campaign campaign;

  /**
   * List of events which are displayed.
   */
  private List<Event> events;


  /**
   * Constructor.
   *
   * @param campaign campaign to look for.
   */
  public CourseParticipationList(Campaign campaign) {

    InjectorHolder.getInjector().inject(this);

    this.campaign = campaign;

    events = new ArrayList<Event>();

    for (Long id : this.campaign.getEventIds()) {
      events.add(controller.getEventById(id));
    }

    fillRows();
  }

  private void fillRows() {
    for (Event event : events) {
      List<String> content = new ArrayList<String>();

      content.add(formatIdSubjectNameAndDetailInformation(event));
      content.add(String.valueOf(controller.countConfirmedRegistrationsByEventId(event.getId())));

      rows.add(new Row(content));
    }
  }

  @Override
  public TxtProperties getTxtProperties() {
    List<TxtCellProperties> cellPropertieses = new ArrayList<TxtCellProperties>();

    cellPropertieses.add(new TxtCellProperties(4, Alignment.RIGHT));
    cellPropertieses.add(new TxtCellProperties(40));
    cellPropertieses.add(new TxtCellProperties(2));


    return new TxtProperties(cellPropertieses);
  }

  @Override
  public String toString() {
    return PrintableLists.COURSEPARTICIPATIONLIST.getTitle();
  }
}
