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
 * Exception that will be thrown when a Entity was already saved and shall be saved again. (equals method on another saved entity
 * returns true).
 *
 * @author klassm
 */
public class ItemAlreadyExistsException extends DataAccessException {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = 8308339858437926083L;

  /**
   * Default constructor.
   */
  public ItemAlreadyExistsException() {

    super();
  }

  /**
   * Creates a {@link ItemAlreadyExistsException} using a given String as cause.
   *
   * @param s cause.
   */
  public ItemAlreadyExistsException(String s) {

    super(s);
  }

  /**
   * Creates a {@link ItemAlreadyExistsException} using a given String as cause and a given Exception as stack trace.
   *
   * @param s     cause.
   * @param cause stack trace.
   */
  public ItemAlreadyExistsException(String s, Throwable cause) {

    super(s, cause);
  }
}
