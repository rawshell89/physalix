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

import hsa.awp.common.dao.AbstractMandatorableDao;
import hsa.awp.common.exception.NoMatchingElementException;
import hsa.awp.event.model.Category;

import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 * Data Access Object for CRUD methods of Category.
 *
 * @author klassm
 */
public class CategoryDao extends AbstractMandatorableDao<Category, Long> implements ICategoryDao {
  /**
   * Constructor for creating a CategoryDao.
   */
  public CategoryDao() {

    super(Category.class);
  }

  @Override
  public Category findByName(String name) {

    if (name == null) {
      throw new IllegalArgumentException("no name given");
    }

    try {
      return (Category) getEntityManager().createQuery("select o from Category o where o.name='" + name + "'")
          .getSingleResult();
    } catch (NoResultException e) {
      throw new NoMatchingElementException("no matching element", e);
    }
  }

  @Override
  public Category findByNameAndMandator(String name, Long mandatorId) {

    if (name == null || mandatorId == null || mandatorId == 0L) {
      throw new IllegalArgumentException("no name or mandatorId given");
    }

    try {
      Query query = getEntityManager().createQuery("select o from Category o where o.name = :name and o.mandatorId = :mandatorId");
      query.setParameter("name", name);
      query.setParameter("mandatorId", mandatorId);
      return (Category) query
          .getSingleResult();
    } catch (NoResultException e) {
      throw new NoMatchingElementException("no matching element", e);
    }
  }
}
