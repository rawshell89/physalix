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

package hsa.awp.admingui.rule.validator;

import hsa.awp.admingui.util.IValidationErrorSerializable;
import org.apache.wicket.validation.IErrorMessageSource;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

/**
 * Validator checking whether a given value is bigger than a minimum value. The default validator from wicket was not uses because
 * of a required specified error text.
 *
 * @author klassm
 */
public class MinTermValidator implements IValidator<Integer> {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = 159288399191L;

  /**
   * Minimum value the value to check must exceed.
   */
  private int minTerm;

  /**
   * Creates a new {@link MinTermValidator}.
   *
   * @param minTerm Minimum value the value to check must exceed.
   */
  public MinTermValidator(int minTerm) {

    this.minTerm = minTerm;
  }

  @Override
  public void validate(IValidatable<Integer> validatable) {

    if (validatable.getValue() <= minTerm) {
      validatable.error(new IValidationErrorSerializable() {
        /**
         * unique serialization id.
         */
        private static final long serialVersionUID = -7106224749695536132L;

        @Override
        public String getErrorMessage(IErrorMessageSource messageSource) {
          // TODO Language
          return "Untere Semestergrenze muss größer als " + minTerm + " sein";
        }
      });
    }
  }
}
