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
import hsa.awp.common.dao.IAbstractMandatorableDao;

import java.util.Calendar;
import java.util.List;

/**
 * Interface for accessing all {@link Procedure} model objects.
 *
 * @author klassm
 */
public interface IProcedureDao extends IAbstractMandatorableDao<Procedure, Long> {
  /**
   * Looks for all active {@link Procedure}s.
   *
   * @return all active {@link Procedure}s.
   */
  List<Procedure> findActive();

  /**
   * Looks for newly active {@link Procedure}s where the startDate of the Procedure is between a given since data and now.
   *
   * @param since since interval
   * @return {@link List} of newly active {@link Procedure}s.
   */
  List<Procedure> findActiveSince(Calendar since);

  /**
   * Looks for all {@link Procedure}s not used by any {@link Campaign}.
   *
   * @return {@link List} of {@link Procedure}s.
   */
  List<Procedure> findUnused();

  List<Procedure> findUnusedByMandator(Long mandator);
}
