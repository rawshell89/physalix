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

import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.admingui.util.JavascriptEventConfirmation;
import hsa.awp.gui.util.LoadableDetachedModel;
import hsa.awp.rule.model.Rule;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Panel showing any saved rules.
 *
 * @author klassm
 */
public class RuleListPanel extends Panel {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = -4803376122945819149L;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * Creates a new {@link RuleListPanel}.
   *
   * @param id wicket-id.
   */
  public RuleListPanel(String id) {

    super(id);

    LoadableDetachedModel<List<Rule>> rules = new LoadableDetachedModel<List<Rule>>() {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 1L;

      @Override
      protected List<Rule> load() {

        List<Rule> rules = controller.getRulesByMandator(getSession());
        Collections.sort(rules, new Comparator<Rule>() {
          @Override
          public int compare(Rule o1, Rule o2) {

            return o1.getName().compareTo(o2.getName());
          }
        });
        return rules;
      }
    };

    final WebMarkupContainer listContainer = new WebMarkupContainer("rules.listContainer");
    add(listContainer);
    listContainer.setOutputMarkupId(true);

    listContainer.add(new ListView<Rule>("rules.list", rules) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 2060257002016027097L;

      @Override
      protected void populateItem(final ListItem<Rule> item) {

        item.add(new Label("rule.name", item.getModelObject().getName()));
        AjaxFallbackLink<Rule> delLink = new AjaxFallbackLink<Rule>("rule.delete") {
          /**
           * unique serialization id.
           */
          private static final long serialVersionUID = 1L;

          @Override
          public void onClick(AjaxRequestTarget target) {

            target.addComponent(listContainer);
            controller.deleteRule(item.getModelObject());
          }
        };
        delLink.add(new JavascriptEventConfirmation("onclick", "Sind Sie sicher?"));
        item.add(delLink);
      }
    });
  }
}
