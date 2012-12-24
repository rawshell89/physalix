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

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import java.util.List;
import java.util.Set;

/**
 * Implementations of this interface provide code to do a lookup in a user
 * directory.
 *
 * @author alex
 */
public interface IDirectoryAdapter {
  /**
   * Returns all attributes for the given name.
   *
   * @param name - The name of the object from which to retrieve attributes.
   * @return All attributes matching the given name. If nothing was found, an
   *         empty Attributes object will be returned.
   * @throws NamingException - if a naming exception is encountered
   */
  Attributes getAttributes(String name) throws NamingException;

  /**
   * Returns all attributes for the given user id.
   *
   * @param uuid - The uuid of the object from which to retrieve attributes.
   * @return All attributes matching the given name. If nothing was found, an
   *         empty Attributes object will be returned.
   * @throws NamingException - if a naming exception is encountered
   */
  Attributes getAttributes(long uuid) throws NamingException;

  /**
   * Returns the given attributes for the given name.
   *
   * @param name    - The name of the object from which to retrieve attributes.
   * @param attrIds - the identifiers of the attributes to retrieve. null
   *                indicates that all attributes should be retrieved; an empty
   *                array indicates that none should be retrieved.
   * @return All attributes matching the given name. If nothing was found, an
   *         empty Attributes object will be returned.
   * @throws NamingException - if a naming exception is encountered
   */
  Attributes getAttributes(String name, String[] attrIds)
      throws NamingException;

  /**
   * Sets the InitalContext for directory lookup. This is just for testing
   * the implementation should provide its default context automatically.
   *
   * @param context DirectoryContext to perform the lookups.
   */
  void setDirContext(DirContext context);

  List<Attributes> searchDirectory(String searchString);

  Set<String> getAllStudyCourses();
}
