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
import hsa.awp.admingui.rule.validator.StudyCourseValidator;
import hsa.awp.rule.model.StudyCourseAndTermRule;
import hsa.awp.user.model.StudyCourse;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AbstractAutoCompleteRenderer;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class StudyCourseAndTermRulePanel extends AbstractRulePanel<StudyCourseAndTermRule> {

  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = 933325068897987721L;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * Minimum term the participant has to be in.
   */
  private TextField<Integer> minTerm;

  /**
   * StudyCourse the rule shall apply on.
   */
  private TextField<String> studyCourse;

  /**
   * Creates a new {@link StudyCourseAndTermRulePanel}. This will use a new {@link StudyCourseAndTermRule}.
   *
   * @param id wicket-id.
   */
  public StudyCourseAndTermRulePanel(String id) {

    this(id, StudyCourseAndTermRule.getInstance(((AuthenticatedSpringSecuritySession) Session.get()).getMandatorId()));
  }

  /**
   * Creates a new {@link StudyCourseAndTermRule}.
   *
   * @param id   wicket-id.
   * @param rule rule to edit.
   */
  public StudyCourseAndTermRulePanel(String id, final StudyCourseAndTermRule rule) {

    super(id, rule);

    studyCourse = new TextField<String>("rule.studyCourse", new Model<String>(""));
    form.add(studyCourse);
    studyCourse.setRequired(true);
    studyCourse.add(new AutoCompleteBehavior<String>(new AbstractAutoCompleteRenderer<String>() {

      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 2464228194248252417L;

      @Override
      protected String getTextValue(String object) {

        return object;
      }

      @Override
      protected void renderChoice(String object, Response response, String criteria) {

        response.write(getTextValue(object));

      }
    }) {

      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 338482863240127475L;

      @Override
      protected Iterator<String> getChoices(String input) {

        input = input.toLowerCase();
        List<String> choices = new LinkedList<String>();

        for (StudyCourse studyCourse : controller.getAllStudyCourses()) {
          String name = studyCourse.getName().toLowerCase();
          if (name.startsWith(input)) {
            choices.add(studyCourse.getName());
          }
        }

        return choices.iterator();
      }
    });

    studyCourse.add(new StudyCourseValidator());

    minTerm = new TextField<Integer>("rule.minTerm", new Model<Integer>(1), Integer.class);
    minTerm.add(new MinTermValidator(0));
    minTerm.setRequired(true);
    form.add(minTerm);

  }

  @Override
  protected void submitOnClick(AjaxRequestTarget target, Form<?> form) {

    rule.setMinTerm(minTerm.getModelObject());
    rule.setName(name.getModelObject());
    rule.setStudyCourse(controller.getStudyCourseByName(studyCourse.getModelObject()).getId());

    controller.writeRule(rule, getSession());

    feedbackPanel.info("Regel wurde erfolgreich erstellt");

  }

}
