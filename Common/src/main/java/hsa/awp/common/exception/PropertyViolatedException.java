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

import org.hibernate.PropertyValueException;

/**
 * Will be thrown if a constraint of a property is violated.
 * Examples:
 * <ul>
 * <li>duplicate entry violating Unique constraint</li>
 * <li>missing or duplicate primary key</li>
 * </ul>
 *
 * @author klassm
 */
public class PropertyViolatedException extends DataAccessException {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = 7154322291967471465L;

  /**
   * Creates a new {@link PropertyViolatedException} using a cause.
   *
   * @param cause {@link PropertyValueException}.
   */
  public PropertyViolatedException(Throwable cause) {

    super(cause.getMessage(), cause);
  }

  /**
   * Creates a new {@link PropertyViolatedException} using a {@link PropertyValueException} and a message.
   *
   * @param message - A message you want to give out.
   * @param cause   - The exception that caused the exception.
   */
  public PropertyViolatedException(String message, Throwable cause) {

    super(message, cause);
  }
}
