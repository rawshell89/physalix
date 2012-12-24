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

import hsa.awp.campaign.model.Campaign;
import hsa.awp.common.dao.AbstractMandatorableDao;
import hsa.awp.common.exception.NoMatchingElementException;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Calendar;
import java.util.List;

/**
 * Class used for accessing all {@link Campaign} objects.
 *
 * @author klassm
 */
public class CampaignDao extends AbstractMandatorableDao<Campaign, Long> implements ICampaignDao {
  /**
   * Default constructor.
   */
  public CampaignDao() {

    super(Campaign.class);
  }

  // public void remove(Campaign item) {
  //
  // if (item == null) {
  // throw new IllegalArgumentException("no item given");
  // }
  //
  // for (Procedure p : item.getAppliedProcedures()) {
  // item.removeProcedure(p);
  // }
  // super.remove(item);
  // }

  @Override
  @SuppressWarnings("unchecked")
  public List<Campaign> findActive() {

    Query query = getEntityManager().createQuery(
        "select o from " + Campaign.class.getSimpleName() + " o where :now between o.startShow and o.endShow");
    query.setParameter("now", Calendar.getInstance());

    return query.getResultList();
  }

  @Override
  public List<Campaign> findActiveByMandator(Long mandator) {
    Query query = getEntityManager().createQuery(
        "select o from " + Campaign.class.getSimpleName() + " o where o.mandatorId = :mandator and :now between o.startShow and o.endShow");
    query.setParameter("now", Calendar.getInstance());
    query.setParameter("mandator", mandator);

    return query.getResultList();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Campaign> findActiveSince(Calendar since) {

    Query query = getEntityManager().createQuery(
        "select o from " + Campaign.class.getSimpleName() + " o where o.startshow between :since and :now");
    query.setParameter("since", since);
    query.setParameter("now", Calendar.getInstance());

    return query.getResultList();
  }

  @Override
  public Campaign findByName(String name) {

    Query query = getEntityManager().createQuery("select o from " + Campaign.class.getSimpleName() + " o where o.name=:name");
    query.setParameter("name", name);

    try {
      return (Campaign) query.getSingleResult();
    } catch (NoResultException e) {
      throw new NoMatchingElementException("no matching element", e);
    }
  }

  @Override
  public Campaign findByNameAndMandator(String name, Long mandatorId) {

    Query query = getEntityManager().createQuery("select o from " + Campaign.class.getSimpleName() + " o where o.name=:name and o.mandatorId = :mandatorId");
    query.setParameter("name", name);
    query.setParameter("mandatorId", mandatorId);

    try {
      return (Campaign) query.getSingleResult();
    } catch (NoResultException e) {
      throw new NoMatchingElementException("no matching element", e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Campaign> findCampaignsByEventId(Long id) {

    Query query = getEntityManager().createQuery(
        "select o from " + Campaign.class.getSimpleName() + " o where :id in elements(o.eventIds)");
    query.setParameter("id", id);
    return query.getResultList();
  }
}
