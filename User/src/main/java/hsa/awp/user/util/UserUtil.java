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

package hsa.awp.user.util;

import hsa.awp.common.exception.ProgrammingErrorException;
import hsa.awp.user.model.Group;
import hsa.awp.user.model.SingleUser;
import hsa.awp.user.model.User;

import java.util.LinkedList;
import java.util.List;

public class UserUtil {
  /**
   * Generates a {@link SingleUser} list from a given {@link User}. A {@link User} can be a {@link Group} and a {@link SingleUser}.
   * The result will be a list only consisting of {@link SingleUser}s.
   *
   * @param user user to generate a {@link SingleUser} list from.
   * @return list of {@link SingleUser}s.
   */
  public static List<SingleUser> generateSingleUserFromUser(User user) {

    List<SingleUser> users = new LinkedList<SingleUser>();
    if (user instanceof Group) {
      Group group = (Group) user;

      for (User u : group.getMembers()) {
        users.addAll(generateSingleUserFromUser(u));
      }
    } else if (user instanceof SingleUser) {
      users.add((SingleUser) user);
    } else {
      throw new ProgrammingErrorException("unsupported user type.");
    }
    return users;
  }
}