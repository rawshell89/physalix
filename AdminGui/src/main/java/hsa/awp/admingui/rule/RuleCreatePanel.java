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

package hsa.awp.admingui.rule;

import hsa.awp.admingui.rule.rules.EmptyRulePanel;
import hsa.awp.admingui.rule.rules.StudyCourseAndTermRulePanel;
import hsa.awp.admingui.rule.rules.TermRulePanel;
import hsa.awp.rule.model.Rule;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

/**
 * Panel serving as holder for all rule edit or create panels. The {@link RuleType} enum holds the different rule types. This panel
 * will offer a dropdown box to choose a new rule type and show the appropriate edit / create panel.
 *
 * @author klassm
 */
public class RuleCreatePanel extends Panel {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = 565334934012320104L;

  /**
   * Logger used for logging.
   */
  private transient Logger logger;

  /**
   * Creates a new {@link RuleCreatePanel}.
   *
   * @param id wicket-id.
   */
  public RuleCreatePanel(String id) {

    super(id);

    Form<Rule> form = new Form<Rule>("rule.create.form");
    add(form);
    form.setOutputMarkupId(true);

    final WebMarkupContainer ruleContainer = new WebMarkupContainer("rule.create.container");
    add(ruleContainer);
    ruleContainer.setOutputMarkupId(true);

    ruleContainer.add(new EmptyRulePanel("rule.create.container.createPanel"));

    final DropDownChoice<String> choices = new DropDownChoice<String>("rule.create.typeSelect", new Model<String>(), RuleType
        .getNames());
    form.add(choices);
    choices.setOutputMarkupId(true);

    choices.add(new OnChangeAjaxBehavior() {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 5597414843387619927L;

      @Override
      protected void onUpdate(AjaxRequestTarget target) {

        target.addComponent(ruleContainer);

        try {
          RuleType ruleType = RuleType.findByName((String) choices.getDefaultModelObject());
          ruleContainer.removeAll();
          Panel panel;

          if (ruleType == null) {
            panel = new EmptyRulePanel("rule.create.container.createPanel");
          } else {
            Constructor<? extends Panel> constructor = ruleType.getRulePanel().getConstructor(
                new Class[]{String.class});
            panel = constructor.newInstance(new Object[]{"rule.create.container.createPanel"});
          }

          ruleContainer.add(panel);
        } catch (Exception e) {
          e.printStackTrace();
          getLogger().error(e.toString());

          return;
        }
      }
    });
  }

  /**
   * Getter for the logger. Whenever this object will be serialized, the logger will be gone. Therefore, in this case, a new
   * logger will be created.
   *
   * @return logger.
   */
  private Logger getLogger() {

    if (logger == null) {
      logger = LoggerFactory.getLogger(this.getClass());
    }
    return logger;
  }

  /**
   * RuleType serving to map the rule types and their names shown in the gui.
   *
   * @author klassm
   */
  private enum RuleType {
    TERMRULE("Semesterbeschränkung", TermRulePanel.class), STUDYCOURSEANDTERMRULE("Studiengangs- und Semesterbeschränkung",
        StudyCourseAndTermRulePanel.class);

    /**
     * Looks for a {@link RuleType} using its name attribute.
     *
     * @param name name to look for.
     * @return found {@link RuleType} or null if no {@link RuleType} was fitting.
     */
    public static RuleType findByName(String name) {

      if (name == null) {
        return null;
      }

      for (RuleType type : RuleType.values()) {
        if (name.equals(type.getName())) {
          return type;
        }
      }
      return null;
    }

    /**
     * Getter for a list of names that will be shown in gui.
     *
     * @return list of names.
     */
    public static List<String> getNames() {

      List<String> names = new LinkedList<String>();
      for (RuleType type : RuleType.values()) {
        names.add(type.getName());
      }
      return names;
    }

    /**
     * Name shown in the gui.
     */
    private String name;

    /**
     * Class of the panel that shall be shown when the appropriate rule type is selected.
     */
    private Class<? extends Panel> rulePanel;

    /**
     * Creates a RuleType.
     *
     * @param name      name shown in gui.
     * @param rulePanel type of panel to show.
     */
    private RuleType(String name, Class<? extends Panel> rulePanel) {

      this.name = name;
      this.rulePanel = rulePanel;
    }

    /**
     * Getter for the name shown in the gui.
     *
     * @return name shown in gui.
     */
    public String getName() {

      return name;
    }

    /**
     * Getter for the rule type panel.
     *
     * @return class of the rule panel.
     */
    public Class<? extends Panel> getRulePanel() {

      return rulePanel;
    }
  }
}
