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

package hsa.awp.campaign.dao;

import hsa.awp.campaign.model.Procedure;
import hsa.awp.common.dao.AbstractMandatorableDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Query;
import java.util.Calendar;
import java.util.List;

/**
 * Class used for accessing all {@link Procedure} objects.
 *
 * @author klassm
 */
public class ProcedureDao extends AbstractMandatorableDao<Procedure, Long> implements IProcedureDao {
  /**
   * {@link Logger}.
   */
  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * Default constructor.
   */
  public ProcedureDao() {

    super(Procedure.class);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Procedure> findActive() {

    Query query = getEntityManager().createQuery(
        "select o from " + Procedure.class.getSimpleName() + " o where now() between o.startDate and o.endDate");

    return query.getResultList();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Procedure> findActiveSince(Calendar since) {

    Calendar now = Calendar.getInstance();
    logger.debug("since = {}", since.getTime());
    logger.debug("now = {}", now.getTime());

    Query query = getEntityManager().createQuery(
        "select o from " + Procedure.class.getSimpleName()
            + " o where o.startDate between :since and :now and o.endDate > :now and o.campaign IS NOT NULL");
    query.setParameter("since", since);
    query.setParameter("now", now);

    query.setHint("org.hibernate.cacheable", false);

    return query.getResultList();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Procedure> findUnused() {

    List<Procedure> list = getEntityManager().createQuery(
        "select o from " + Procedure.class.getSimpleName() + " o where o.campaign=null").getResultList();
    return list;
  }

  @Override
  public List<Procedure> findUnusedByMandator(Long mandator) {

    Query query = getEntityManager().createQuery(
        "select o from " + Procedure.class.getSimpleName() + " o where o.campaign=null and o.mandatorId = :mandator");
    query.setParameter("mandator", mandator);

    List<Procedure> list = query.getResultList();
    return list;
  }
}
