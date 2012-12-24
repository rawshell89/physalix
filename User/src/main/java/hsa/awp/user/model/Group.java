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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Persistent object for {@link Group}.
 *
 * @author johannes
 */
@Entity
@Table(name = "`group`")
public class Group extends User {
  /**
   * Version UID which is used for serialization.
   */
  private static final long serialVersionUID = 3981118935653958992L;

  /**
   * List of members in this {@link Group}.
   */
  @OneToMany(cascade = {CascadeType.MERGE})
  private Set<User> members;

  /**
   * Creates a new {@link Group} and initializes it with appropriate values (e.g. inserts a HashSet at appropriate places, so that
   * the Sets can be used without risking a {@link NullPointerException}.
   *
   * @return new domain object.
   */
  public static Group getInstance() {

    Group g = new Group();
    g.setMembers(new HashSet<User>());

    return g;
  }

  /**
   * Default constructor.
   */
  Group() {

  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof Group)) {
      return false;
    }
//        Group other = (Group) obj;
//        
//      // equality is already checked with super.equals()
//      if (getId() != null && getId() != 0L) {
//          return true;
//      }
    return true;
  }

  @Override
  public int hashCode() {
    // if item is persisted use only the id for calculating hash code
    if (getId() != null && getId() != 0L) {
      return super.hashCode();
    }

//        final int prime = 31;
    int result = super.hashCode();
    return result;
  }

  /**
   * Returns members.
   *
   * @return the members
   */
  public Set<User> getMembers() {

    return members;
  }

  /**
   * Setter for members.
   *
   * @param members the members to set
   */
  public void setMembers(Set<User> members) {

    this.members = members;
  }
}
