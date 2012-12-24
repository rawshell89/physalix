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
import hsa.awp.user.model.Role;
import hsa.awp.user.model.RoleMapping;
import hsa.awp.user.model.SingleUser;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class RoleMappingDao extends AbstractDao<RoleMapping, Long> implements IRoleMappingDao {

  /**
   * Creates an {@link hsa.awp.common.dao.AbstractDao}.
   */
  public RoleMappingDao() {
    super(RoleMapping.class);
  }


  @Override
  public List<RoleMapping> findByRole(Role role) {

    if (role == null) {
      throw new IllegalArgumentException("no role given");
    }

    try {
      Query query = getEntityManager().createQuery("select o from " + RoleMapping.class.getSimpleName() + " o where o.role = :role");
      query.setParameter("role", role);

      return query.getResultList();
    } catch (NoResultException e) {
      return null;
    }
  }

  @Override
  public List<RoleMapping> findByUser(SingleUser user) {

    if (user == null) {
      throw new IllegalArgumentException("no user given");
    }

    try {
      Query query = getEntityManager().createQuery("select o from " + RoleMapping.class.getSimpleName() + " o where o.singleUser = :user");
      query.setParameter("user", user);

      return query.getResultList();
    } catch (NoResultException e) {
      return null;
    }
  }

  @Override
  public RoleMapping findByExample(SingleUser user, String mandator, Role role) {

    if (user == null || mandator == null || role == null) {
      throw new IllegalArgumentException("user, mandator or role is null");
    }

    try {
      Query query = getEntityManager().createQuery(
          "select rolemapping from " + RoleMapping.class.getSimpleName() + " rolemapping " +
              "where rolemapping.singleUser = :user and rolemapping.role = :role and " +
              "(select mandator from " + Mandator.class.getSimpleName() + " mandator where mandator.name = :mandator) " +
              "in elements(rolemapping.mandators)");
      query.setParameter("user", user);
      query.setParameter("mandator", mandator);
      query.setParameter("role", role);

      return (RoleMapping) query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
}
