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
import hsa.awp.common.dao.IAbstractMandatorableDao;
import hsa.awp.user.model.SingleUser;

import java.util.List;

/**
 * Interface for accessing all {@link PriorityList} model objects.
 *
 * @author klassm
 */
public interface IPriorityListDao extends IAbstractMandatorableDao<PriorityList, Long> {
  /**
   * Looks for {@link PriorityList}s associated with a given {@link SingleUser} and a given {@link Procedure}.
   *
   * @param userId    SingleUser to look for.
   * @param procedure {@link Procedure} to look for.
   * @return associated {@link PriorityList}s.
   */
  List<PriorityList> findByUserAndProcedure(Long userId, Procedure procedure);

  List<PriorityList> findByUserAndProcedureAndMandatorId(Long userId, Procedure procedure, Long mandatorId);
}
