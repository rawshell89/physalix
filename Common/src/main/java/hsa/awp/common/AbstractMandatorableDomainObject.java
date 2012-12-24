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

package hsa.awp.common;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractMandatorableDomainObject<T> implements IGenericDomainModel<T> {

  private Long mandatorId;

  protected AbstractMandatorableDomainObject(Long mandatorId) {
    this.mandatorId = mandatorId;
  }

  protected AbstractMandatorableDomainObject() {

  }

  public Long getMandatorId() {
    return mandatorId;
  }

  public void setMandatorId(Long mandatorId) {
    this.mandatorId = mandatorId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AbstractMandatorableDomainObject that = (AbstractMandatorableDomainObject) o;

    if (mandatorId != null ? !mandatorId.equals(that.mandatorId) : that.mandatorId != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return mandatorId != null ? mandatorId.hashCode() : 0;
  }
}
