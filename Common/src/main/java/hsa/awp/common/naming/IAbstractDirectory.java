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

package hsa.awp.common.naming;

import hsa.awp.common.exception.ConfigurationException;

import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * This interface provides an abstract access to user information.
 *
 * @author alex
 */
public interface IAbstractDirectory {
  /**
   * Directory field for the e-mail address of a user.
   */
  String EMAIL = "e-mail";

  /**
   * Directory field for the faculty of a user.
   */
  String FACULTY = "faculty";

  /**
   * Directory field for the login name of the user.
   */
  String LOGIN = "login";

  /**
   * Directory field for the name of a user.
   */
  String NAME = "name";

  /**
   * Directory field for "Matrikelnummer" of a user.
   */
  String MATRICULATIONNUMBER = "matriculationnumber";

  /**
   * Directory field for the role of a user.
   */
  String ROLE = "role";

  /**
   * Directory field for the semester of a user.
   */
  String TERM = "term";

  /**
   * Directory field of the course, a student is in.
   */
  String STUDYCOURSE = "studycourse";

  /**
   * Directory field for the title (e. g. Dr.) of the person.
   */
  String TITLE = "title";

  /**
   * Directory field for the name of a user.
   */
  String UUID = "uuid";

  /**
   * Does a mapping from the low level field names (e. g. ldap fields) to our
   * field names used in this application. If the lowLevelName does not exist
   * in the mapping, <code>null</code> will be returned.
   *
   * @param lowLevelName - The name of the field in the directory service
   * @return The name of this field used in this application.
   */
  String getAbstractFieldName(String lowLevelName);

  String getLowLevelFieldName(String abstractFieldName);

  /**
   * Returns all LDAP attributes for a given username.
   *
   * @param username The username to get the attributes.
   * @return Properties for the given username. Null if the username was not
   *         found.
   */
  Properties getUserProperties(String username);

  /**
   * Returns all LDAP attributes for a given username.
   *
   * @param uuid The uuid of the user to get the attributes.
   * @return Properties for the given username. Null if the username was not
   *         found.
   */
  Properties getUserProperties(Long uuid);

  /**
   * Adds an adapter that implements the directory lookup.
   *
   * @param adapter - Implementation of the directory lookup.
   */
  void setDirectoryAdapter(IDirectoryAdapter adapter);

  /**
   * Adds a file that stores the mapping from directory fields to abstract
   * names.
   *
   * @param propertyFile - The file to read the configuration from.
   * @throws ConfigurationException if the file could not be read.
   */
  void setFieldMappingFile(String propertyFile);

  List<Properties> searchForUser(String searchString);

  Set<String> readAllStudyCourses();
}
