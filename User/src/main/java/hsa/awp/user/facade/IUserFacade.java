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

package hsa.awp.user.facade;

import hsa.awp.common.exception.DataAccessException;
import hsa.awp.user.model.*;

import java.util.List;

/**
 * Class includes all access methods for the SingleUser Context.
 *
 * @author johannes
 */
public interface IUserFacade {
  /*
  * Search methods for model objects.
  */

  /**
   * Looks for all persistent {@link Group}s.
   *
   * @return List of {@link Group}s.
   */
  List<Group> getAllGroups();

  /**
   * Looks for all persistent {@link SingleUser}s.
   *
   * @return List of {@link SingleUser}s.
   */
  List<SingleUser> getAllSingleUsers();

  /**
   * Looks for {@link SingleUser}s being in a specified {@link Role}.
   *
   * @param role role the users belong to.
   * @return List of users.
   */
  List<SingleUser> getAllSingleUsersByRole(Role role);

  /**
   * Looks for all persistent {@link StudyCourse}s.
   *
   * @return List of {@link StudyCourse}s.
   */
  List<StudyCourse> getAllStudyCourses();

  /**
   * Looks for all persistent Users who have events attached with.
   *
   * @return List of {@link Teacher}s.
   */
  List<SingleUser> getAllTeachers();

  /**
   * Looks for all persistent {@link User}s.
   *
   * @return List of {@link User}s.
   */
  List<User> getAllUsers();

  /**
   * Looks for a {@link Group} using its id.
   *
   * @param id identifier
   * @return found {@link Group}
   * @throws DataAccessException      if no matching {@link Group} was found
   * @throws IllegalArgumentException if id is invalid
   */
  Group getGroupById(Long id);

  /**
   * Looks for a {@link SingleUser} using its id.
   *
   * @param id identifier
   * @return found {@link SingleUser}
   * @throws DataAccessException      if no matching {@link SingleUser} was found
   * @throws IllegalArgumentException if id is invalid
   */
  SingleUser getSingleUserById(Long id);

  /**
   * Looks for a {@link SingleUser} using its login name.
   *
   * @param login - The login name to get the user object for.
   * @return A user object.
   * @throws DataAccessException if no matching {@link SingleUser} was found
   */
  SingleUser
  getSingleUserByLogin(String login);

  /**
   * Looks for a {@link StudyCourse} using its id.
   *
   * @param id identifier
   * @return found {@link StudyCourse}
   * @throws DataAccessException      if no matching {@link StudyCourse} was found
   * @throws IllegalArgumentException if id is invalid
   * @see IUserFacade#getStudyCourseByName(String)
   */
  StudyCourse getStudyCourseById(Long id);

  /*
  * Persist and merge methods for model objects.
  */

  /**
   * Looks for a {@link StudyCourse} using its name.
   *
   * @param name the name to use for lookup
   * @return the found {@link StudyCourse}
   * @throws DataAccessException if no matching {@link StudyCourse} was found
   * @see IUserFacade#getStudyCourseById(Long)
   */
  StudyCourse getStudyCourseByName(String name);

  /**
   * Looks for a {@link User} using its id.
   *
   * @param id identifier
   * @return found {@link User}
   * @throws DataAccessException      if no matching {@link User} was found
   * @throws IllegalArgumentException if id is invalid
   */
  User getUserById(Long id);

  /**
   * Removes all persisted {@link Group}s from the database.
   */
  void removeAllGroups();

  /**
   * Removes all persisted {@link SingleUser}s from the database.
   */
  void removeAllSingleUsers();

  /**
   * Removes all persisted {@link StudyCourse}s from the database.
   */
  void removeAllStudyCourses();

  /**
   * Removes a given {@link Group} form the database.
   *
   * @param g {@link Group} to remove.
   */
  void removeGroup(Group g);

  /**
   * Removes a given {@link SingleUser} form the database.
   *
   * @param u {@link SingleUser} to remove.
   */
  void removeSingleUser(SingleUser u);

  /*
  * Remove methods for model objects.
  */

  /**
   * Removes a given {@link StudyCourse} form the database.
   *
   * @param s {@link StudyCourse} to remove.
   */
  void removeStudyCourse(StudyCourse s);

  /**
   * Removes a given {@link User} form the database.
   *
   * @param p {@link User} to remove.
   */
  void removeUser(User p);

  /**
   * Makes a {@link Group} persistent.
   *
   * @param g {@link Group} to make persistent
   * @return persistent {@link Group}
   * @throws DataAccessException
   */
  Group saveGroup(Group g);

  /**
   * Makes a {@link SingleUser} persistent.
   *
   * @param u {@link SingleUser} to make persistent
   * @return persistent {@link SingleUser}
   * @throws DataAccessException
   */
  SingleUser saveSingleUser(SingleUser u);

  /**
   * Makes a {@link StudyCourse} persistent.
   *
   * @param s {@link StudyCourse} to make persistent
   * @return persistent {@link StudyCourse}
   * @throws DataAccessException
   */
  StudyCourse saveStudyCourse(StudyCourse s);

  /**
   * Makes a {@link User} persistent.
   *
   * @param p {@link User} to make persistent
   * @return persistent {@link User}
   * @throws DataAccessException
   */
  User saveUser(User p);

  /**
   * Puts all changes of a given {@link Group} into the database.
   *
   * @param g {@link Group} to synchronize changes
   * @return merged {@link Group}
   */
  Group updateGroup(Group g);

  /**
   * Puts all changes of a given {@link SingleUser} into the database.
   *
   * @param u {@link SingleUser} to synchronize changes
   * @return merged {@link SingleUser}
   */
  SingleUser updateSingleUser(SingleUser u);

  /**
   * Puts all changes of a given {@link StudyCourse} into the database.
   *
   * @param s {@link StudyCourse} to synchronize changes
   * @return merged {@link StudyCourse}
   */
  StudyCourse updateStudyCourse(StudyCourse s);

  /**
   * Puts all changes of a given {@link User} into the database.
   *
   * @param p {@link User} to synchronize changes
   * @return merged {@link User}
   */
  User updateUser(User p);

  List<SingleUser> searchForUser(String searchString);

  void readAllStudyCourses();

  Mandator getAllMandator();

  Mandator getMandatorByName(String name);

  Mandator getMandatorById(Long id);

  void removeMandator(Mandator mandator);

  Mandator saveMandator(Mandator mandator);

  Mandator updateMandator(Mandator mandator);

  RoleMapping getRoleMappingById(Long id);

  List<RoleMapping> getRoleMappingsByRole(Role role);

  List<RoleMapping> getRoleMappingsByUser(SingleUser singleUser);

  void removeRoleMapping(RoleMapping roleMapping);

  RoleMapping saveRoleMapping(RoleMapping roleMapping);

  RoleMapping updateRoleMapping(RoleMapping roleMapping);

  List<Mandator> getAllMandators();

  RoleMapping getRoleMappingByExample(SingleUser user, String mandator, Role role);
}
