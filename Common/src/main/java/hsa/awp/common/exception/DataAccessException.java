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
 * Exception that is thrown when an Exception occurs when accessing the database.
 *
 * @author klassm
 */
public class DataAccessException extends RuntimeException {
  /**
   * UID.
   */
  private static final long serialVersionUID = -1227160015605800819L;

  /**
   * Creates a {@link DataAccessException}.
   */
  public DataAccessException() {

    super();
  }

  /**
   * Creates a {@link DataAccessException}.
   *
   * @param s reason for throwing the exception.
   */
  public DataAccessException(String s) {

    super(s);
  }

  /**
   * Creates a {@link DataAccessException}.
   *
   * @param s     reason for throwing the exception.
   * @param cause stacktrace.
   */
  public DataAccessException(String s, Throwable cause) {

    super(s, cause);
  }
}
