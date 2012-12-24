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

package hsa.awp.scire.procedureLogic.util;

import hsa.awp.campaign.model.PriorityListItem;

import java.util.Comparator;

public class PriorityListItemPrioritySorter implements Comparator<PriorityListItem> {

  @Override
  public int compare(PriorityListItem thisItem, PriorityListItem thatItem) {

    Integer thisPriority = thisItem.getPriority();
    Integer thatPriority = thatItem.getPriority();

    return thisPriority.compareTo(thatPriority);
  }
}
