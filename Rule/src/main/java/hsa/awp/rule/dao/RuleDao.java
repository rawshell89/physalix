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

package hsa.awp.rule.dao;

import hsa.awp.common.dao.AbstractMandatorableDao;
import hsa.awp.rule.model.Rule;

import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 * Data Access Object for CRUD methods of {@link Rule}.
 *
 * @author johannes
 */
public class RuleDao extends AbstractMandatorableDao<Rule, Long> implements IRuleDao {
  /**
   * Default constructor.
   */
  public RuleDao() {

    super(Rule.class);
  }

  @Override
  public Rule persist(Rule r) {

    return super.persist(r);
  }

  @Override
  public Rule findByName(String name) {

    if (name == null) {
      throw new IllegalArgumentException("no name given");
    }

    Query query = getEntityManager().createQuery(
        "select o from " + Rule.class.getSimpleName() + " o where o.name=:name");
    query.setParameter("name", name);

    try {
      return (Rule) query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  @Override
  public Rule findByNameAndMandatorId(String name, Long mandatorId) {

    if (name == null) {
      throw new IllegalArgumentException("no name given");
    }

    Query query = getEntityManager().createQuery(
        "select o from " + Rule.class.getSimpleName() + " o where o.name=:name and o.mandatorId = :mandatorId");
    query.setParameter("name", name);
    query.setParameter("mandatorId", mandatorId);

    try {
      return (Rule) query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
}
