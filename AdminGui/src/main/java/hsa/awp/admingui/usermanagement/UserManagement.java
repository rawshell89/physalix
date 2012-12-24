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
import hsa.awp.user.model.Role;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.LinkedList;
import java.util.List;

public class UserManagement extends Panel {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = -2232340949354295831L;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  public UserManagement(String id) {

    super(id);

    List<ITab> tabs = new LinkedList<ITab>();
    tabs.add(generateTabRole(Role.SYSADMIN, true));

    AjaxTabbedPanel tabbedPanel = new AjaxTabbedPanel("userManagement.tabNav", tabs);
    add(tabbedPanel);
  }

  private AbstractTab generateTabRole(final Role role, final boolean edit) {

    return new AbstractTab(new Model<String>(role.getDescription())) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 1L;

      @Override
      public Panel getPanel(String arg0) {

        return new RoleTab(arg0, role, edit, controller.getTheAllMandator());
      }
    };
  }

}
