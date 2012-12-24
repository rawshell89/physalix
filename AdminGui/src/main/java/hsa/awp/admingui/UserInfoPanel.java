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

package hsa.awp.admingui;

import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.user.model.Mandator;
import hsa.awp.user.model.SingleUser;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class UserInfoPanel extends Panel {

  private SingleUser singleUser;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;


  public UserInfoPanel(String id) {
    super(id);

    try {
      singleUser = getCurrentUser();
    } catch (Exception e) {
      e.printStackTrace();
      singleUser = null;
    }

    if (singleUser == null) {   //Happens in testcases with dummy authentication
      setupDummyUser();
    }

    add(new Label("name", new Model<String>(singleUser.getName())));

    List<Mandator> mandatorList = controller.getMandatorsFromSingleUser(singleUser);
    boolean hasMandators = mandatorList.size() > 0;
    final Model<Mandator> mandatorModel = new Model<Mandator>(hasMandators ? controller.getMandatorById(controller.getActiveMandator(getSession())) : null);
    DropDownChoice<Mandator> dropDownChoice = new DropDownChoice<Mandator>("mandators", mandatorModel, mandatorList, new ChoiceRenderer<Mandator>("name"));
    dropDownChoice.setVisible(hasMandators);

    dropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {

      @Override
      protected void onUpdate(AjaxRequestTarget target) {
        ((AuthenticatedSpringSecuritySession) getSession()).setMandatorId(mandatorModel.getObject().getId());
        setResponsePage(HomePage.class);
      }
    });

    Form<Mandator> form = new Form<Mandator>("form");

    form.add(dropDownChoice);

    add(form);
  }

  private SingleUser getCurrentUser() {
    Long userId = ((AuthenticatedSpringSecuritySession) getSession()).getUserId();
    SingleUser user;
    if (userId == null || userId == 0L) {
      user = controller.getUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
      ((AuthenticatedSpringSecuritySession) getSession()).setUserId(user.getId());
    } else {
      user = controller.getUserById(userId);
    }

    return user;
  }

  private void setupDummyUser() {
    singleUser = SingleUser.getInstance();
  }
}
