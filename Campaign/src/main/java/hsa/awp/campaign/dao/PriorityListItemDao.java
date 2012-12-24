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

import hsa.awp.campaign.model.ConfirmedRegistration;
import hsa.awp.campaign.model.PriorityListItem;
import hsa.awp.common.dao.AbstractMandatorableDao;

import javax.persistence.Query;
import java.util.List;

/**
 * Class used for accessing all {@link ConfirmedRegistration} objects.
 *
 * @author klassm
 */
public class PriorityListItemDao extends AbstractMandatorableDao<PriorityListItem, Long> implements IPriorityListItemDao {
  /**
   * Default constructor.
   */
  public PriorityListItemDao() {

    super(PriorityListItem.class);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<PriorityListItem> findItemsByEventId(Long eventId) {

    Query query = getEntityManager().createQuery(
        "select o from " + PriorityListItem.class.getSimpleName() + " o where o.event=:eventId");
    query.setParameter("eventId", eventId);

    return query.getResultList();
  }

  @Override
  public List<PriorityListItem> findItemsByEventIdAndMandatorId(Long eventId, Long mandatorId) {

    Query query = getEntityManager().createQuery(
        "select o from " + PriorityListItem.class.getSimpleName() + " o where o.event=:eventId and o.mandatorId = :mandatorId");
    query.setParameter("eventId", eventId);
    query.setParameter("mandatorId", mandatorId);

    return query.getResultList();
  }
}
