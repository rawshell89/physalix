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

package hsa.awp.rule.model;

import hsa.awp.campaign.model.Campaign;
import hsa.awp.event.model.Event;
import hsa.awp.user.model.SingleUser;


public interface IRule {
  /**
   * Checks if the given {@link SingleUser} is allowed to pass this the rule.
   *
   * @param user     the {@link SingleUser} to be checked
   * @param campaign the associated {@link Campaign}
   * @param event    the associated {@link Event}
   * @return <code>true</code> if the {@link SingleUser} is allowed to pass. Otherwise <code>false</code>.
   */
  boolean check(SingleUser user, Campaign campaign, Event event);
}
