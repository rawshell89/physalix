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
import hsa.awp.rule.model.Rule;

/**
 * Interface for {@link RuleDao}.
 *
 * @author johannes
 */
public interface IRuleDao extends IAbstractMandatorableDao<Rule, Long> {
  /**
   * Searches for a specific {@link Rule} with the given name. If no matching {@link Rule} can be found <code>null</code> will be
   * returned.
   *
   * @param name the name of the {@link Rule}
   * @return the found {@link Rule} or <code>null</code> if no matching {@link Rule} can be found.
   */
  Rule findByName(String name);

  Rule findByNameAndMandatorId(String name, Long mandatorId);
}
