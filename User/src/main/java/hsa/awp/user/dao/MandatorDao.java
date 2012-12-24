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
import hsa.awp.user.model.Mandator;

import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 * Data Access Object for CRUD methods of {@link hsa.awp.user.model.StudyCourse}.
 *
 * @author johannes
 */
public class MandatorDao extends AbstractDao<Mandator, Long> implements IMandatorDao {
  /**
   * Constructor for creating a {@link hsa.awp.user.dao.MandatorDao}.
   */
  public MandatorDao() {

    super(Mandator.class);
  }

  @Override
  public Mandator findByName(String name) {

    if (name == null) {
      throw new IllegalArgumentException("no name given");
    }

    try {
      Query query = getEntityManager().createQuery("select o from " + Mandator.class.getSimpleName() + " o where o.name = :name");
      query.setParameter("name", name);

      return (Mandator) query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
}
