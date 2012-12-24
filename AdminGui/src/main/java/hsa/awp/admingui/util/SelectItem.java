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

package hsa.awp.admingui.util;

import hsa.awp.common.IGenericDomainModel;

import java.io.Serializable;

/**
 * Class used as abstract SelectItem in Wicket Select Lists.
 *
 * @param <T> type of item id.
 * @param <K> type of item.
 * @author klassm
 */
public abstract class SelectItem<T, K extends IGenericDomainModel<T>> implements Serializable {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = -7074208563201448563L;

  /**
   * Item backing the selectItem.
   */
  private K item;

  /**
   * Constructor creating a new {@link SelectItem}.
   *
   * @param item item to be stored.
   * @throws IllegalArgumentException if item is null.
   */
  public SelectItem(K item) {

    if (item == null) {
      throw new IllegalArgumentException("no item given");
    }
    this.item = item;
  }

  @Override
  public boolean equals(Object o) {

    return item.equals(o);
  }

  @Override
  public int hashCode() {

    return super.hashCode();
  }

  /**
   * ToString method used as display in the Select cell.
   *
   * @return display message.
   */
  public abstract String toString();

  /**
   * Getter for id.
   *
   * @return the id
   */
  public T getId() {

    return item.getId();
  }

  /**
   * Getter for item.
   *
   * @return the item
   */
  public K getItem() {

    return item;
  }

  /**
   * Setter for item.
   *
   * @param item the item to set
   */
  public void setItem(K item) {

    this.item = item;
  }
}
