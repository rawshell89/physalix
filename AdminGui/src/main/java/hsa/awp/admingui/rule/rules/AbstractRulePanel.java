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

import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.admingui.util.IValidationErrorSerializable;
import hsa.awp.rule.model.Rule;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IErrorMessageSource;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * Super interface for all rules. This includes a FeedbackPanel, a form that is used for all input actions and a name field being
 * common to all rules.
 *
 * @author klassm
 */
public abstract class AbstractRulePanel<T extends Rule> extends Panel {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = 1573541536861054011L;

  /**
   * Component returning any feedback.
   */
  protected FeedbackPanel feedbackPanel;

  /**
   * Form used for all input actions.
   */
  protected Form<Object> form;

  /**
   * Name of the rule.
   */
  protected TextField<String> name;

  protected T rule;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * Creates a new RulePanel.
   *
   * @param id   wicket-id.
   * @param rule rule that is created or edited. The values will be used to fill the rule's input fields with default values.
   */
  public AbstractRulePanel(String id, T rule) {

    super(id);

    if (rule == null) {
      throw new IllegalArgumentException("No rule given");
    }

    this.rule = rule;

    feedbackPanel = new FeedbackPanel("feedbackPanel");
    feedbackPanel.setOutputMarkupId(true);
    add(feedbackPanel);

    form = new Form<Object>("rule.form");
    add(form);

    name = new TextField<String>("rule.name", new Model<String>(rule.getName()));
    name.setRequired(true);
    name.add(new StringValidator() {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = -3503841430415001646L;

      @Override
      protected void onValidate(IValidatable<String> validatable) {

        if (validatable.getValue() == null || validatable.getValue().equals("")) {
          validatable.error(new IValidationErrorSerializable() {
            /**
             * unique serialization id.
             */
            private static final long serialVersionUID = 2893129433111740381L;

            @Override
            public String getErrorMessage(IErrorMessageSource messageSource) {

              return "Kein Name ausgewählt";
            }
          });
        }

        if (validatable.isValid() && controller.getRuleByNameAndMandator(validatable.getValue(), getSession()) != null) {
          validatable.error(new IValidationErrorSerializable() {
            /**
             * unique serialization id.
             */
            private static final long serialVersionUID = 4583676857544619819L;

            @Override
            public String getErrorMessage(IErrorMessageSource messageSource) {

              return "Regelname existiert bereits.";
            }
          });
        }
      }
    });

    form.add(name);

    form.add(new AjaxButton("rule.submit") {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = -725179447540520694L;

      @Override
      protected void onError(AjaxRequestTarget target, Form<?> form) {

        target.addComponent(feedbackPanel);
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

        target.addComponent(feedbackPanel);

        submitOnClick(target, form);
      }
    });
  }

  /**
   * Method that is called when the submit button is pressed. At that time the feedback panel is already added as target in the
   * onSubmit and in the onError method.
   *
   * @param target target matching the onSubmit method.
   * @param form   form matching the onSubmit method.
   */
  protected abstract void submitOnClick(AjaxRequestTarget target, Form<?> form);
}
