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

package hsa.awp.admingui.rule.rules;

import hsa.awp.admingui.AuthenticatedSpringSecuritySession;
import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.admingui.rule.validator.MinTermValidator;
import hsa.awp.rule.model.TermRule;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IErrorMessageSource;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.IValidator;

/**
 * Class representing the creation and alteration of any {@link TermRule}s.
 *
 * @author klassm
 */
public class TermRulePanel extends AbstractRulePanel<TermRule> {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = -4445991936131020843L;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * Maximum term which the participant may not exceed.
   */
  private TextField<Integer> maxTerm;

  /**
   * Minimum term the participant has to be in.
   */
  private TextField<Integer> minTerm;

  public TermRulePanel(String id) {

    this(id, TermRule.getInstance(((AuthenticatedSpringSecuritySession) Session.get()).getMandatorId()));
  }

  /**
   * Creates a new {@link TermRulePanel}.
   *
   * @param id   wicket-id.
   * @param rule rule to create or edit.
   */
  public TermRulePanel(String id, final TermRule rule) {

    super(id, rule);

    minTerm = new TextField<Integer>("rule.minTerm", new Model<Integer>(1), Integer.class);
    minTerm.add(new MinTermValidator(0));

    maxTerm = new TextField<Integer>("rule.maxTerm", new Model<Integer>(2), Integer.class);
    maxTerm.add(new IValidator<Integer>() {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = -6147680454718888847L;

      @Override
      public void validate(IValidatable<Integer> validatable) {

        if (validatable.getValue() < minTerm.getModelObject()) {
          validatable.error(new IValidationError() {
            @Override
            public String getErrorMessage(IErrorMessageSource messageSource) {
              // TODO Language
              return "Obere Semestergrenze muss größer der unteren Semestergrenze sein.";
            }
          });
        }
      }
    });

    form.add(maxTerm);
    form.add(minTerm);
  }

  @Override
  protected void submitOnClick(AjaxRequestTarget target, Form<?> form) {

    rule.setMaxTerm(maxTerm.getModelObject());
    rule.setMinTerm(minTerm.getModelObject());
    rule.setName(name.getModelObject());

    controller.writeRule(rule, getSession());

    info("Regel wurde erfolgreich erstellt");
  }
}
