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

import hsa.awp.common.AbstractMandatorableDomainObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Query;
import java.util.List;

public class AbstractMandatorableDao<T extends AbstractMandatorableDomainObject<K>, K> extends AbstractDao<T, K> implements IAbstractMandatorableDao<T, K> {

  /**
   * Logger used for logging.
   */
  private Logger logger = LoggerFactory.getLogger(AbstractMandatorableDao.class);

  private Class<T> structure;

  /**
   * Creates an {@link AbstractDao}.
   *
   * @param structure Type of the class that is accessed.
   */
  public AbstractMandatorableDao(Class<T> structure) {
    super(structure);
    this.structure = structure;
  }

  @Override
  public long countByMandator(Long mandatorId) {

    logger.trace("call of countAll()");
    Query query = getEntityManager().createQuery("select count(o) from " + structure.getSimpleName() + " o where o.mandatorId = :mandatorId");
    query.setParameter("mandatorId", mandatorId);
    return (Long) query.getSingleResult();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<T> findByMandator(Long mandatorId) {

    logger.trace("call of findAll()");
    Query query = getEntityManager().createQuery("select o from " + structure.getSimpleName() + " o where o.mandatorId = :mandatorId");
    query.setParameter("mandatorId", mandatorId);
    List<T> list = query.getResultList();
    return list;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<T> findByMandator(int firstResult, int maxResults, Long mandatorId) {

    logger.trace("call of findAll(firstResult, maxResults");
    Query query = getEntityManager().createQuery("select o from " + structure.getSimpleName() + " o where o.mandatorId = :mandatorId");
    query.setParameter("mandatorId", mandatorId);
    return query.setFirstResult(firstResult)
        .setMaxResults(maxResults).getResultList();
  }
}
