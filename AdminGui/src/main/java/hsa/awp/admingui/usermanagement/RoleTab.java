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
import hsa.awp.admingui.util.UserSelectPanel;
import hsa.awp.gui.util.ILoadableDetachedModel;
import hsa.awp.gui.util.LoadableDetachedModel;
import hsa.awp.user.model.Mandator;
import hsa.awp.user.model.Role;
import hsa.awp.user.model.RoleMapping;
import hsa.awp.user.model.SingleUser;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoleTab extends Panel {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = 4287120434150615177L;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  private FeedbackPanel feedbackPanel;

  private WebMarkupContainer listUserContainer;

  public RoleTab(String wicketId, final Role role, boolean changeable, final Mandator mandator) {

    super(wicketId);

    if (role == null) {
      throw new IllegalArgumentException("no role given");
    }

    WebMarkupContainer addUserContainer = new WebMarkupContainer("addUser");
    add(addUserContainer);

    addUserContainer.add(new UserSelectPanel("userSelectPanel") {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = -2199695801515215562L;

      @Override
      protected boolean onSendSubmit(SingleUser singleUser) {

        if (hasUserRoleForMandator(singleUser, role, mandator)) {
          getFeedbackPanel().error("User " + singleUser.getName() + " is already in role " + role.name());
          return false;
        }

        RoleMapping roleMapping = RoleMapping.getInstance(role, mandator, singleUser);

        getController().addRoleMappingToSingleUser(singleUser, roleMapping);

        return true;
      }

      @Override
      protected boolean vetoFindSubmit(SingleUser user) {

        if (hasUserRoleForMandator(user, role, mandator)) {
          getFeedbackPanel().error("User " + user.getName() + " is already in role " + role.name());
          return true;
        }
        return false;
      }
    });

    listUserContainer = new WebMarkupContainer("userListContainer");
    listUserContainer.setOutputMarkupId(true);
    add(listUserContainer);

    final ILoadableDetachedModel<List<SingleUser>> usersModel = new LoadableDetachedModel<List<SingleUser>>() {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = -2548188711048825707L;

      @Override
      protected List<SingleUser> load() {

        List<SingleUser> singleUsers = getController().findSingleUsersByRole(role);

        Set<SingleUser> filtered = new HashSet<SingleUser>();

        for (SingleUser singleUser : singleUsers) {
          if (hasUserRoleForMandator(singleUser, role, mandator)) {
            filtered.add(singleUser);
          }
        }

        return new ArrayList<SingleUser>(filtered);
      }
    };

    ListView<SingleUser> list = new ListView<SingleUser>("userList", usersModel) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 4307951155564631399L;

      @Override
      protected void populateItem(final ListItem<SingleUser> item) {

        item.add(new Label("username", item.getModelObject().getUsername()));
        item.add(new Label("name", item.getModelObject().getName()));

        item.add(new AjaxFallbackLink<RoleTab>("delete") {
          /**
           * unique serialization id.
           */
          private static final long serialVersionUID = 2004009811773410560L;

          @Override
          public void onClick(AjaxRequestTarget target) {

            usersModel.getObject();

            SingleUser singleUser = item.getModelObject();
            getController().removeRoleMappingForMandator(singleUser, singleUser.roleMappingForRole(role), mandator);

            target.addComponent(listUserContainer);
            usersModel.detach();
          }
        });
      }
    };
    list.setOutputMarkupId(true);
    listUserContainer.add(list);

    feedbackPanel = new FeedbackPanel("feedbackPanel");
    feedbackPanel.setFilter(new ContainerFeedbackMessageFilter(listUserContainer));
    add(feedbackPanel);

    if (!changeable) {
      addUserContainer.setVisible(false);
    }
  }

  private boolean hasUserRoleForMandator(SingleUser user, Role role, Mandator mandator) {

    RoleMapping roleMapping = null;
    if (user.hasRole(role)) {
      roleMapping = user.roleMappingForRole(role);
    }

    if (roleMapping == null) {
      return false;
    }

    return roleMapping.getMandators().contains(mandator);
  }

  public IAdminGuiController getController() {
    return controller;
  }
}
