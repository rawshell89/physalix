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

package hsa.awp.user.model;

import java.io.Serializable;

/**
 * This set of {@link Role}s specifies the available Roles for managing rights.
 *
 * @author johannes
 */
public enum Role implements Serializable {
  /**
   * Identifier for the administrator role. Administrators have all rights.
   */
  SYSADMIN("SYSADMIN", "Systemadmin"),
  /**
   * User that has all rights for a single Campaign.
   */
  APPADMIN("APPADMIN", "Fakultätsadmin"),

  /**
   * Identifier for the secretary role. Secretaries have the rights to create all event and campaign objects and have limited
   * access to user objects.
   */
  SECRETARY("SECRETARY", "Sekretariat"),

  /**
   * Identifier of a user who is logged in but does not have any rights.
   */
  REGISTERED("REGISTERED", "Registriert"),

  /**
   * Identifier for the guest role. Guests have the right to view details of events.
   */
  GUEST("GUEST", "Gast"),

  /**
   * A teacher is a user who has attached events.
   */
  TEACHER("TEACHER", "Dozent");

  private final String stringRepresentation;

  public String getDescription() {
    return description;
  }

  private final String description;

  private Role(String stringRepresentation, String description) {
    this.stringRepresentation = stringRepresentation;
    this.description = description;
  }

  public String toString() {
    return "ROLE_" + this.getStringRepresentation();
  }

  private String getStringRepresentation() {
    return stringRepresentation;
  }

  public static boolean isAdministrativeAuthority(String authority) {
    return APPADMIN.toString().equals(authority)
            || SYSADMIN.toString().equals(authority)
            || SECRETARY.toString().equals(authority);
  }
}
