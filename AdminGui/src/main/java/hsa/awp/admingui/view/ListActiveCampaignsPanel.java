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

package hsa.awp.admingui.view;

import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.campaign.model.Campaign;
import hsa.awp.campaign.model.Procedure;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Panel listing all active {@link Campaign}s.
 *
 * @author klassm
 */
public class ListActiveCampaignsPanel extends Panel {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = -6424733983016885188L;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * Creates a new {@link ListActiveCampaignsPanel}.
   *
   * @param id wicket id.
   */
  public ListActiveCampaignsPanel(String id) {

    super(id);

    List<Campaign> activeCampaigns = controller.getActiveCampaignsByMandator(getSession());
    Collections.sort(activeCampaigns, new Comparator<Campaign>() {
      @Override
      public int compare(Campaign o1, Campaign o2) {

        return o1.getEndShow().compareTo(o2.getEndShow());
      }
    });


    ListView<Campaign> activeCampaignsListView = new ListView<Campaign>("listActiveCampaigns.list", activeCampaigns) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 2533736463077515139L;

      @Override
      protected void populateItem(ListItem<Campaign> item) {

        Campaign campaign = item.getModelObject();

        item.add(new Label("listActiveCampaigns.name", campaign.getName()));

        String activeProcName = "keine";
        for (Procedure proc : campaign.getAppliedProcedures()) {
          if (proc.isActive()) {
            activeProcName = proc.getName();
            break;
          }
        }
        item.add(new Label("listActiveCampaigns.activeProcedureName", activeProcName));

        float progress = 100 * (System.currentTimeMillis() - campaign.getStartShow().getTimeInMillis())
            / (campaign.getEndShow().getTimeInMillis() - campaign.getStartShow().getTimeInMillis());
        if (progress > 100) {
          progress = 100;
        }

        Label progressLabel = new Label("listActiveCampaigns.campaignProgress");
        progressLabel.add(new AttributeAppender("style", new Model<String>("width: " + (int) progress + "%"), ";"));
        item.add(progressLabel);

        item.add(new Label("listActiveCampaigns.campaignProgress.progressPercentage", String.valueOf(progress) + "%"));

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        item.add(new Label("listActiveCampaigns.startShow", format.format(campaign.getStartShow().getTime())));
        item.add(new Label("listActiveCampaigns.endShow", format.format(campaign.getEndShow().getTime())));
      }
    };

    add(activeCampaignsListView);

    if (activeCampaigns.size() == 0) {
      activeCampaignsListView.setVisible(false);
    }
  }
}
