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

package hsa.awp.user.dao;

import hsa.awp.common.dao.IAbstractDao;
import hsa.awp.user.model.Role;
import hsa.awp.user.model.SingleUser;
import hsa.awp.user.model.Student;

import java.util.List;

/**
 * The user DAO provides access to the underlying user directory. You can get the information of every user as a {@link SingleUser}
 * object. Also you can get additional information of special users (e. g. students or teachers) as specials objects (e. g.
 * {@link Student} or {@link Teacher}.
 *
 * @author alex
 */
public interface ISingleUserDao extends IAbstractDao<SingleUser, Long> {
  /**
   * Searches for a user with the given login name.
   *
   * @param login - The login name for the user to search for. If there is no user matching the <code>login</code>, a
   *              {@link NoMatchingElementException} will be thrown.
   * @return - The requested <code>SingleUser</code> object.
   */
  SingleUser findByUsername(String login);


  /**
   * Looks for Users to whom a given role is applied.
   *
   * @param role role to look for.
   * @return List of users.
   */
  List<SingleUser> findUsersByRole(Role role);

  /**
   * Returns all users who are teachers. A teacher is a person with an event.
   *
   * @return A list of teachers.
   */
  List<SingleUser> getAllTeachers();

  List<SingleUser> searchForUser(String searchString);

  void readAllStudyCourses();
}
