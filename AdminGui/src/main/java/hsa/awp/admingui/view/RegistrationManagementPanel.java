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

import hsa.awp.admingui.OnePanelPage;
import hsa.awp.admingui.util.UserSelectPanel;
import hsa.awp.user.model.SingleUser;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.panel.Panel;

public class RegistrationManagementPanel extends Panel {

  private final UserSelectPanel userSelectPanel = new UserSelectPanel("userSelectPanel") {

    private SingleUser singleUser;

    @Override
    protected boolean onSendSubmit(SingleUser singleUser) {
      this.singleUser = singleUser;
      return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected Page getResponsePage() {
      return new OnePanelPage(new UserRegistrationsPanel(OnePanelPage.getPanelIdOne(), singleUser));
    }

    protected String getSubmitButtonText() {
      return "Weiter";
    }
  };

  public RegistrationManagementPanel(String id) {
    super(id);

    add(userSelectPanel);
  }
}
