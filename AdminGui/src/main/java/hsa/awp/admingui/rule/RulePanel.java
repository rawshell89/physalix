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

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import java.util.LinkedList;
import java.util.List;

/**
 * Panel representing the default layout of the rule panel including the tabs used for showing and creating rules.
 *
 * @author klassm
 */
public class RulePanel extends Panel {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = -3708876659435237712L;

  /**
   * TabbedPanel used as navigation.
   */
  private TabbedPanel tabs;

  /**
   * Creates a new {@link RulePanel}.
   *
   * @param id wicket-id.
   */
  public RulePanel(String id) {

    super(id);

    List<ITab> tabElements = new LinkedList<ITab>();

    tabElements.add(new AbstractTab(new Model<String>("Regeln")) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = -1178968139254947287L;

      @Override
      public Panel getPanel(String panelId) {

        return new RuleListPanel(panelId);
      }
    });

    tabElements.add(new AbstractTab(new Model<String>("Neue Regel")) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = -4536337784714353869L;

      @Override
      public Panel getPanel(String panelId) {

        return new RuleCreatePanel(panelId);
      }
    });

    tabs = new AjaxTabbedPanel("rule.nav", tabElements);
    add(tabs);
    tabs.setOutputMarkupId(true);
  }
}
