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
import hsa.awp.common.exception.NoMatchingElementException;
import hsa.awp.user.model.Role;
import hsa.awp.user.model.RoleMapping;
import hsa.awp.user.model.SingleUser;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

/**
 * Data Access Object for CRUD methods of {@link SingleUser}.
 *
 * @author johannes
 */
public class SingleUserDbDao extends AbstractDao<SingleUser, Long> implements ISingleUserDao {
  /**
   * Constructor for creating a {@link SingleUserDbDao}.
   */
  public SingleUserDbDao() {

    super(SingleUser.class);
  }

  @Override
  public SingleUser findByUsername(String name) {

    Query query = getEntityManager().createQuery(
        "select o from " + SingleUser.class.getSimpleName() + " o where o.username=:username");
    query.setParameter("username", name);

    try {
      return (SingleUser) query.getSingleResult();
    } catch (NoResultException e) {
      throw new NoMatchingElementException("no matching element", e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<SingleUser> findUsersByRole(Role role) {

    Query query = getEntityManager().createQuery(
        "select mapping.singleUser from " + RoleMapping.class.getSimpleName() + " mapping  where mapping.role = :role");
    query.setParameter("role", role);

    return query.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<SingleUser> getAllTeachers() {

    Query query = getEntityManager().createQuery(
        "select o from " + SingleUser.class.getSimpleName() + " o where size(o.lectures) > 0");
    return query.getResultList();
  }

  @Override
  public List<SingleUser> searchForUser(String searchString) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void readAllStudyCourses() {
    throw new UnsupportedOperationException();
  }
}
