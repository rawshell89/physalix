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

package hsa.awp.common.dao;

import hsa.awp.common.IGenericDomainModel;
import hsa.awp.common.exception.DataAccessException;
import hsa.awp.common.exception.ItemNotSavedException;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Interface for accesing DataAccessObjects.
 *
 * @param <T> DomainObject to access.
 * @param <K> Type of id (i.e. Long).
 * @author klassm
 */
public interface IAbstractDao<T extends IGenericDomainModel<K>, K> {
  /**
   * Counts all elements.
   *
   * @return number of elements.
   */
  long countAll();

  /**
   * Method will look for all elements.
   *
   * @return List of all elements.
   */
  List<T> findAll();

  /**
   * Finds maxResults elements beginning at firstResult.
   *
   * @param firstResult starting position of result
   * @param maxResults  maximum number of elements to return
   * @return List of items
   */
  List<T> findAll(int firstResult, int maxResults);

  /**
   * Looks for an object identified by the respective entity id. Method will throw an {@link DataAccessException} exception if no
   * element can be found.
   *
   * @param id entity id
   * @return found element.
   */
  T findById(K id);

  /**
   * Merges an element to the database, so that all changes will be made persistent.
   *
   * @param item item to merge.
   * @return the merged object.
   * @throws IllegalArgumentException if <code>null</code> is given.
   * @throws ItemNotSavedException    if the object to merge is not present in the database.
   */
  T merge(T item);

  /**
   * Writes an element to the database (makes it persistent).
   *
   * @param item element to write.
   * @return persisted item.
   */
  T persist(T item);

  /**
   * Removes an element from the database. An {@link DataAccessException} exception will be thrown if the object to remove cannot
   * be found within the database.
   *
   * @param item item to remove
   */
  void remove(T item);

  /**
   * Removes all elements.
   */
  void removeAll();

  /**
   * Sets the {@link EntityManager}.
   *
   * @param entityManager {@link EntityManager}.
   */
  void setEntityManager(EntityManager entityManager);
}
