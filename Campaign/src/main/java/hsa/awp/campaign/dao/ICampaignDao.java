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
import hsa.awp.common.dao.IAbstractMandatorableDao;

import java.util.Calendar;
import java.util.List;

/**
 * Interface for accessing all {@link Campaign} model objects.
 *
 * @author klassm
 */
public interface ICampaignDao extends IAbstractMandatorableDao<Campaign, Long> {
  /**
   * Looks for all currently active {@link Campaign}s.
   *
   * @return {@link List} of newly active {@link Campaign}s.
   */
  List<Campaign> findActive();

  /**
   * Looks for newly active {@link Campaign}s where the startShow of the {@link Campaign} is between a given since data and now.
   *
   * @param since since interval
   * @return {@link List} of newly active {@link Campaign}s.
   */
  List<Campaign> findActiveSince(Calendar since);

  /**
   * Looks for a {@link Campaign} using its unique name.
   *
   * @param name name of the {@link Campaign}.
   * @return found {@link Campaign}.
   */
  Campaign findByName(String name);

  Campaign findByNameAndMandator(String name, Long mandatorId);

  List<Campaign> findCampaignsByEventId(Long id);

  List<Campaign> findActiveByMandator(Long mandator);
}
