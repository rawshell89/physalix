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

package hsa.awp.user.dao;

import hsa.awp.common.dao.AbstractDao;
import hsa.awp.user.model.StudyCourse;

import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 * Data Access Object for CRUD methods of {@link StudyCourse}.
 *
 * @author johannes
 */
public class StudyCourseDao extends AbstractDao<StudyCourse, Long> implements IStudyCourseDao {
  /**
   * Constructor for creating a {@link StudyCourseDao}.
   */
  public StudyCourseDao() {

    super(StudyCourse.class);
  }

  @Override
  public StudyCourse findByName(String name) {

    if (name == null) {
      throw new IllegalArgumentException("no name given");
    }

    try {
      Query query = getEntityManager().createQuery("select o from StudyCourse o where o.name=:name");
      query.setParameter("name", name);

      return (StudyCourse) query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
}
