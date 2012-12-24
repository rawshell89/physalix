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

import hsa.awp.campaign.model.PriorityList;
import hsa.awp.campaign.model.Procedure;
import hsa.awp.common.dao.AbstractMandatorableDao;

import javax.persistence.Query;
import java.util.List;

/**
 * Class used for accessing all {@link PriorityList} objects.
 *
 * @author klassm
 */
public class PriorityListDao extends AbstractMandatorableDao<PriorityList, Long> implements IPriorityListDao {
  /**
   * Default constructor.
   */
  public PriorityListDao() {

    super(PriorityList.class);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<PriorityList> findByUserAndProcedure(Long userId, Procedure procedure) {

    Query query = getEntityManager().createQuery(
        "select o from " + PriorityList.class.getSimpleName() + " o where o.participant=:pId and o.procedure=:procedure");
    query.setParameter("pId", userId);
    query.setParameter("procedure", procedure);

    return query.getResultList();
  }

  @Override
  public List<PriorityList> findByUserAndProcedureAndMandatorId(Long userId, Procedure procedure, Long mandatorId) {
    Query query = getEntityManager().createQuery(
        "select o from " + PriorityList.class.getSimpleName() + " o where o.participant=:pId and o.procedure=:procedure and o.mandatorId = :mandatorId");
    query.setParameter("pId", userId);
    query.setParameter("procedure", procedure);
    query.setParameter("mandatorId", mandatorId);

    return query.getResultList();
  }
}
