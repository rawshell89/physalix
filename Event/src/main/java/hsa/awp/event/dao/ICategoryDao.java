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

package hsa.awp.event.dao;

import hsa.awp.common.dao.IAbstractMandatorableDao;
import hsa.awp.event.model.Category;

/**
 * Interface for CategoryDao.
 *
 * @author klassm
 */
public interface ICategoryDao extends IAbstractMandatorableDao<Category, Long> {
  /**
   * Finds a category by its name. Method will throw a {@link DataAccessException} if the element cannot be found.
   *
   * @param name search name
   * @return category if found, else a {@link DataAccessException} will be thrown.
   */
  Category findByName(String name);

  Category findByNameAndMandator(String name, Long mandatorId);
}
