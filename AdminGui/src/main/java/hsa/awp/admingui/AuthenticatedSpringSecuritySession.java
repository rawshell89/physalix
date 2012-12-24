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
import hsa.awp.user.model.Role;
import hsa.awp.user.model.RoleMapping;
import hsa.awp.user.model.SingleUser;
import hsa.awp.user.util.RoleMappingUtil;
import org.apache.wicket.Request;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
import java.util.Set;

/**
 * AuthenticatedSpringSecuritySession
 * Main purpose is to map from role to rights.
 */
public class AuthenticatedSpringSecuritySession extends AuthenticatedWebSession {

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;
  private Long mandatorId;
  private Long userId;
  private Roles roles;
  private boolean mandatorSwitched;

  public AuthenticatedSpringSecuritySession(Request request) {
    super(request);
    InjectorHolder.getInjector().inject(this);
  }

  @Override
  public boolean authenticate(String username, String password) {

    return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
  }

  @Override
  public Roles getRoles() {

    if (roles == null || mandatorSwitched) {

      roles = new Roles();
      mandatorSwitched = false;

      RoleMappingUtil roleMappingUtil = new RoleMappingUtil();
      SingleUser user = controller.getUserByName(SecurityContextHolder.getContext().getAuthentication().getName());

      for (Role role : getRolesForMandatorFromUser(user, controller.getActiveMandator(this))) {
        for (String right : roleMappingUtil.getRightsForRole(role.toString())) {
          roles.add(right);
        }
      }

    }

    return roles;
  }

  private Set<Role> getRolesForMandatorFromUser(SingleUser user, Long activeMandator) {
    Set<Role> roles1 = new HashSet<Role>();

    for (RoleMapping roleMapping : user.getRolemappings()) {
      roleMapping = controller.updateRoleMapping(roleMapping);
      if (roleMapping.getMandators().contains(controller.getMandatorById(activeMandator))) {
        roles1.add(roleMapping.getRole());
      }
    }

    return roles1;
  }

  public Long getMandatorId() {
    return mandatorId;
  }

  public void setMandatorId(Long mandatorId) {
    mandatorSwitched = true;
    this.mandatorId = mandatorId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
