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
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "`roleMapping`")
public class RoleMapping implements IGenericDomainModel<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id = 0L;


  private Role role;

  @ManyToOne
  private SingleUser singleUser;

  @ManyToMany
  private Set<Mandator> mandators = new HashSet<Mandator>();

  public static RoleMapping getInstance(Role role) {

    if (role == null) {
      throw new IllegalArgumentException("role is null");
    }

    return new RoleMapping(role);
  }

  public static RoleMapping getInstance(Role role, Mandator mandator, SingleUser singleUser) {

    if (role == null || mandator == null || singleUser == null) {
      throw new IllegalArgumentException("role, mandator or singleUser is null");
    }

    return new RoleMapping(role, mandator, singleUser);
  }

  protected RoleMapping(Role role, Mandator mandator, SingleUser singleUser) {

    if (role == null || mandator == null || singleUser == null) {
      throw new IllegalArgumentException("role, mandator or singleUser is null");
    }
    setRole(role);
    mandators.add(mandator);
    setSingleUser(singleUser);
  }

  protected RoleMapping(Role role) {

    setRole(role);
  }

  protected RoleMapping() {
  }

  public Set<Mandator> getMandators() {
    return mandators;
  }

  public void setMandators(Set<Mandator> mandators) {
    this.mandators = mandators;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public SingleUser getSingleUser() {
    return singleUser;
  }

  public void setSingleUser(SingleUser singleUser) {
    this.singleUser = singleUser;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RoleMapping that = (RoleMapping) o;

    if (mandators != null ? !mandators.equals(that.mandators) : that.mandators != null) return false;
    if (singleUser != null ? !singleUser.equals(that.singleUser) : that.singleUser != null) return false;
    if (role != that.role) return false;

    return true;
  }

  @Override
  public int hashCode() {
    if (id != null && id != 0L) {
      return id.hashCode();
    }

    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (role != null ? role.hashCode() : 0);
    result = 31 * result + (mandators != null ? mandators.hashCode() : 0);
    result = 31 * result + (singleUser != null ? singleUser.hashCode() : 0);
    return result;
  }


}
