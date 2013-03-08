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

import hsa.awp.common.dao.IAbstractMandatorableDao;
import hsa.awp.rule.model.RegistrationRuleSet;

import java.util.List;

/**
 * Interface for {@link RegistrationRuleSetDao}.
 *
 * @author johannes
 */
public interface IRegistrationRuleSetDao extends IAbstractMandatorableDao<RegistrationRuleSet, Long> {
  /**
   * Searches for a {@link RegistrationRuleSet} which is associated with the given campaign and event.
   *
   * @param campaign the campaign id to use for searching
   * @param event    the event id to use for searching
   * @return the found {@link RegistrationRuleSet} of <code>null</code> if no element was found.
   */
  RegistrationRuleSet findByCampaignAndEvent(Long campaign, Long event);

  RegistrationRuleSet findByCampaignAndEventAndMandatorId(Long campaign, Long event, Long mandatorId);

  List<RegistrationRuleSet> findByEventId(Long id);

  List<RegistrationRuleSet> findByEventIdAndMandatorId(Long id, Long mandatorId);

  List<RegistrationRuleSet> findByCampaign(Long campaign);
}
