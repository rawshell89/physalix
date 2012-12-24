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

package hsa.awp.admingui.usermanagement;

import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.user.model.Mandator;
import hsa.awp.user.model.Role;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.LinkedList;
import java.util.List;

public class MandatorDetailPanel extends Panel {

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  private Mandator mandator;

  public MandatorDetailPanel(String id, Mandator mandator) {
    super(id);

    this.mandator = mandator;

    add(new Label("mandatorName", new Model<String>("Benuterverwaltung von " + mandator.getName())));

    addRoleManagementForMandator();
  }

  private void addRoleManagementForMandator() {

    List<ITab> tabs = new LinkedList<ITab>();
    tabs.add(generateTabRole(Role.APPADMIN, true));
    tabs.add(generateTabRole(Role.SECRETARY, true));
    tabs.add(generateTabTemplate());

    AjaxTabbedPanel tabbedPanel = new AjaxTabbedPanel("mandators.tabNav", tabs);
    add(tabbedPanel);
  }

  private AbstractTab generateTabTemplate() {
    return new AbstractTab(new Model<String>("E-Mail Templates")) {
      @Override
      public Panel getPanel(String panelId) {
        return new TemplateTab(panelId, mandator);
      }
    };
  }

  private AbstractTab generateTabRole(final Role role, final boolean edit) {

    return new AbstractTab(new Model<String>(role.getDescription())) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 1L;

      @Override
      public Panel getPanel(String arg0) {

        return new RoleTab(arg0, role, edit, mandator);
      }
    };
  }
}
