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

import hsa.awp.common.IGenericDomainModel;

import javax.persistence.*;

/**
 * Persistent object for {@link User}.
 *
 * @author johannes
 */
@Entity
@Table(name = "`user`")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User implements IGenericDomainModel<Long> {
  /**
   * Version UID which is used for serialization.
   */
  private static final long serialVersionUID = -3167529578178602757L;

  /**
   * Unique id of every User.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id = 0L;

  /**
   * Default constructor.
   */
  User() {

  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof User)) {
      return false;
    }
    User other = (User) obj;

    // if item is persisted use only the id for checking
    if (id != null && id != 0L) {
      return id.equals(other.id);
    }

    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    // if item is persisted use only the id for calculating hash code
    if (id != null && id != 0L) {
      return id.hashCode();
    }

    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  /**
   * Returns id.
   *
   * @return the id
   */
  public Long getId() {

    return this.id;
  }

  /**
   * Initializes the Sets for lectures and confirmedRegistrations.
   */
  protected void initialize() {

  }

  /**
   * Setter of the uuid number of a user.
   *
   * @param id - ID number of the user.
   */
  void setId(Long id) {

    this.id = id;
  }

}
