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

import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.admingui.util.IValidationErrorSerializable;
import hsa.awp.user.model.StudyCourse;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IErrorMessageSource;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * Validator checking whether a given name matches a saved {@link StudyCourse}.
 *
 * @author klassm
 */
public class StudyCourseValidator extends StringValidator {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = -7910158874599492323L;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  public StudyCourseValidator() {

    InjectorHolder.getInjector().inject(this);
  }

  @Override
  protected void onValidate(final IValidatable<String> validatable) {

    if (validatable.getValue() == null || controller.getStudyCourseByName(validatable.getValue()) == null) {
      validatable.error(new IValidationErrorSerializable() {
        /**
         * unique serialization id.
         */
        private static final long serialVersionUID = 159288399191L;

        @Override
        public String getErrorMessage(IErrorMessageSource messageSource) {

          return "kein Studiengang '" + validatable.getValue() + "' gefunden.";
        }
      });
    }
  }
}
