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
import hsa.awp.admingui.report.util.Row;
import hsa.awp.admingui.report.util.formats.txt.TxtCellProperties;
import hsa.awp.admingui.report.util.formats.txt.TxtPrintable;
import hsa.awp.admingui.report.util.formats.txt.TxtProperties;
import hsa.awp.admingui.report.view.PrintableLists;
import hsa.awp.campaign.model.Campaign;
import hsa.awp.campaign.model.ConfirmedRegistration;
import hsa.awp.event.model.Event;
import hsa.awp.user.model.Student;
import hsa.awp.user.model.User;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

/**
 * List for the examoffice.
 *
 * @author basti
 */
public class ExamOfficeList extends ExportList implements TxtPrintable {

  /**
   * Controller which feeds the class with data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * List of all Registrations.
   */
  private List<ConfirmedRegistration> registrations;

  /**
   * Constructor.
   *
   * @param campaign campaign to look for
   */
  public ExamOfficeList(Campaign campaign) {

    InjectorHolder.getInjector().inject(this);

    registrations = controller.findConfirmedRegistrationsByCampaign(campaign);

    fillRows();
  }

  private void fillRows() {
    for (ConfirmedRegistration reg : registrations) {
      List<String> content = new ArrayList<String>();

      Student stud;
      User participant = controller.getParticipantById(reg.getParticipant());
      Event event = controller.getEventById(reg.getEventId());

      if (!(participant instanceof Student)) {
        continue; // User is not a Student
      }
      stud = (Student) participant;

      content.add(String.valueOf(stud.getMatriculationNumber()));
      content.add(";");
      content.add(generateEventID(event));

      rows.add(new Row(content));
    }
  }

  private String generateEventID(Event event) {
    StringBuffer sb = new StringBuffer();
    sb.append("99");
    if (event.getEventId() < 1000) {
      sb.append("0");
    }
    sb.append(event.getEventId());
    sb.append("0");

    return sb.toString();
  }

  @Override
  public TxtProperties getTxtProperties() {
    List<TxtCellProperties> cellPropertieses = new ArrayList<TxtCellProperties>();

    cellPropertieses.add(new TxtCellProperties(6));
    cellPropertieses.add(new TxtCellProperties(1));
    cellPropertieses.add(new TxtCellProperties(7));

    return new TxtProperties(cellPropertieses, "");
  }


  @Override
  public String toString() {
    return PrintableLists.EXAMOFFICELIST.getTitle();
  }
}
