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

package hsa.awp.admingui.report.view;

import hsa.awp.admingui.util.AccessUtil;
import org.apache.wicket.Session;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import java.util.LinkedList;
import java.util.List;

/**
 * Panel for printing all kinds of reports.
 *
 * @author Basti
 */
public class ReportPrintingPanel extends Panel {
  /**
   * uuid.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constructor which needs the wicket:id string.
   *
   * @param id wicker id
   */
  public ReportPrintingPanel(String id) {

    super(id);

    List<ITab> tabs = new LinkedList<ITab>();

    if (AccessUtil.hasRole(Session.get(), "printEventLists")) {
      tabs.add(createEventListTab());
    }
    if (AccessUtil.hasRole(Session.get(), "printCampaignLists")) {
      tabs.add(createCampaignListTab());
    }

    TabbedPanel tabbedPanel = new AjaxTabbedPanel("tabbedPanel", tabs);
    tabbedPanel.setOutputMarkupId(true);

    add(tabbedPanel);
  }

  private AbstractTab createCampaignListTab() {
    return new AbstractTab(new Model<String>("Kampagnenlisten")) {
      /**
       * generated uid.
       */
      private static final long serialVersionUID = -4224618806229437702L;

      @Override
      public Panel getPanel(String panelId) {

        Panel panel = new CampaignListPrintingPanel(panelId);
        AccessUtil.allowRender(panel, "printCampaignLists");

        return panel;
      }
    };
  }

  private AbstractTab createEventListTab() {
    return new AbstractTab(new Model<String>("Veranstaltungslisten")) {
      /**
       * generated uid.
       */
      private static final long serialVersionUID = 2498347750078505679L;

      @Override
      public Panel getPanel(String panelId) {

        Panel panel = new EventListPrintingPanel(panelId);

        AccessUtil.allowRender(panel, "printEventLists");
        return panel;
      }
    };
  }
}
