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

package hsa.awp.common.exception;

/**
 * This will be thrown when a user tries to retrieve an object with a wrong
 * type. For example, he tries to lookup a teacher in a UserDirectory but the
 * found object was a student.
 *
 * @author alex
 */
public class WrongTypeException extends DataAccessException {
  /**
   * Serialisation ID.
   */
  private static final long serialVersionUID = -1190517323347891227L;

  /**
   * A new WrongTypeException.
   */
  public WrongTypeException() {

    super();
  }

  /**
   * A new WrongTypeException.
   *
   * @param message - An error message.
   */
  public WrongTypeException(String message) {

    super(message);
  }

  /**
   * A new WrongTypeException.
   *
   * @param message - An error message.
   * @param cause   - The throwable which caused the error.
   */
  public WrongTypeException(String message, Throwable cause) {

    super(message, cause);
  }
}
