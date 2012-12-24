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

import hsa.awp.user.model.SingleUser;
import hsa.awp.usergui.controller.IUserGuiController;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class NavigationLoginPanel extends Panel {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = 6013432757033558633L;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "usergui.controller")
  private transient IUserGuiController controller;

  public NavigationLoginPanel(String id) {

    super(id);

    SingleUser singleUser = SingleUser.getInstance();
    singleUser.setName("");

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      singleUser = controller.getUserById(auth.getName());
    }

    Form<Object> loginForm = new Form<Object>("loginForm");
    add(loginForm);

    MarkupContainer loggedInContainer = new WebMarkupContainer("loggedIn");
    loggedInContainer.add(new Label("loginName", singleUser.getName()));
    add(loggedInContainer);

    if (auth == null) {
      loggedInContainer.setVisible(false);
      loginForm.setVisible(true);
    } else {
      loggedInContainer.setVisible(true);
      loginForm.setVisible(false);
    }
  }
}
