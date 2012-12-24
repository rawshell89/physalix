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

import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.admingui.util.AbstractDeleteLink;
import hsa.awp.campaign.model.ConfirmedRegistration;
import hsa.awp.campaign.model.Procedure;
import hsa.awp.event.model.Event;
import hsa.awp.gui.util.LoadableDetachedModel;
import hsa.awp.user.model.SingleUser;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static hsa.awp.event.util.EventFormattingUtils.formatIdSubjectNameAndDetailInformation;

public class UserRegistrationsPanel extends Panel {

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  public UserRegistrationsPanel(String panelIdOne, final SingleUser singleUser) {
    super(panelIdOne);

    LoadableDetachedModel<List<ConfirmedRegistration>> registrationsModel = new LoadableDetachedModel<List<ConfirmedRegistration>>() {
      @Override
      protected List<ConfirmedRegistration> load() {
        return controller.findConfirmedRegistrationsByParticipantIdAndMandator(singleUser.getId(), controller.getActiveMandator(getSession()));
      }
    };

    ListView<ConfirmedRegistration> registrations = new ListView<ConfirmedRegistration>("registrationList", registrationsModel) {
      @Override
      protected void populateItem(ListItem<ConfirmedRegistration> confirmedRegistrationListItem) {
        ConfirmedRegistration confirmedRegistration = confirmedRegistrationListItem.getModelObject();
        confirmedRegistrationListItem.add(new Label("eventName", new Model<String>(getEventName(confirmedRegistration))));
        confirmedRegistrationListItem.add(new Label("procedureName", new Model<String>(getProcedureName(confirmedRegistration))));
        confirmedRegistrationListItem.add(new AbstractDeleteLink<ConfirmedRegistration>("deleteLink", confirmedRegistration) {
          @Override
          public void modifyItem(ConfirmedRegistration confirmedRegistration) {
            controller.deleteConfirmedRegistration(confirmedRegistration);
          }
        });
      }
    };

    add(registrations);
  }

  private String getProcedureName(ConfirmedRegistration confirmedRegistration) {
    Procedure procedure = confirmedRegistration.getProcedure();
    if (procedure == null) {
      return "direkt";
    } else {
      return procedure.getName();
    }
  }

  private String getEventName(ConfirmedRegistration confirmedRegistration) {
    Event event = controller.getEventById(confirmedRegistration.getEventId());
    return formatIdSubjectNameAndDetailInformation(event);
  }
}
