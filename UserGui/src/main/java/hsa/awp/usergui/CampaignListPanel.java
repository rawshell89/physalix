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

package hsa.awp.usergui;

import hsa.awp.campaign.model.Campaign;
import hsa.awp.gui.util.LoadableDetachedModel;
import hsa.awp.user.model.Mandator;
import hsa.awp.user.model.SingleUser;
import hsa.awp.usergui.controller.IUserGuiController;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

public class CampaignListPanel extends Panel {
  /**
   * generated UID.
   */
  private static final long serialVersionUID = -4894530488669068334L;

  /**
   * {@link IUserGuiController}.
   */
  @SpringBean(name = "usergui.controller")
  private IUserGuiController controller;

  public CampaignListPanel(String id) {

    super(id);

    final SingleUser singleUser = controller.getUserById(SecurityContextHolder.getContext().getAuthentication().getName());

    final IModel<List<List<Campaign>>> campaignListModel = new LoadableDetachedModel<List<List<Campaign>>>() {
      /**
       * generated UID.
       */
      private static final long serialVersionUID = -7972310896986835363L;

      @Override
      protected List<List<Campaign>> load() {

        List<Campaign> campaignList = fetchList();


        return sortList(campaignList);
      }

      private List<List<Campaign>> sortList(List<Campaign> campaignList) {

        Map<Long, List<Campaign>> campaignMap = new HashMap<Long, List<Campaign>>();

        for (Campaign campaign : campaignList) {
          List<Campaign> campaigns = campaignMap.get(campaign.getMandatorId());

          if (campaigns == null) {
            campaigns = new ArrayList<Campaign>();
          }

          campaigns.add(campaign);
          campaignMap.put(campaign.getMandatorId(), campaigns);
        }

        return new ArrayList<List<Campaign>>(campaignMap.values());
      }

      private List<Campaign> fetchList() {
        List<Campaign> campaignList = new LinkedList<Campaign>();

        for (Campaign campaign : controller.getCampaignsWithActiveProcedures(singleUser)) {
          if (controller.hasCampaignAllowedRegistrations(singleUser, campaign)) {
            campaignList.add(campaign);
          }
        }

        Collections.sort(campaignList, new Comparator<Campaign>() {
          @Override
          public int compare(Campaign o1, Campaign o2) {

            return o1.getName().compareTo(o2.getName());
          }
        });
        return campaignList;
      }
    };

    ListView<List<Campaign>> listsList = new ListView<List<Campaign>>("campaignListPanel.listsList", campaignListModel) {
      /**
       * generated UID.
       */
      private static final long serialVersionUID = 4377825244454840372L;

      @Override
      protected void populateItem(final ListItem<List<Campaign>> item) {

        ListView<Campaign> list = new ListView<Campaign>("campaignListPanel.list", item.getModel()) {
          /**
           * generated UID.
           */
          private static final long serialVersionUID = 4377825244454840372L;

          @Override
          protected void populateItem(final ListItem<Campaign> item) {

            final Campaign camp = controller.getCampaignById(item.getModelObject().getId());


            item.add(new CampaignPreviewPanel("campaignListPanel.campaignPreview", camp));
          }
        };

        item.add(list);
        Mandator mandator = controller.getMandatorById(item.getModelObject().get(0).getMandatorId());
        item.add(new Label("campaignListPanel.mandatorName", mandator.getName()));
      }
    };

    boolean campaignsDisplayed = campaignListModel.getObject().size() > 0;

    Label label = new Label("campaignListPanel.message", "Momentan stehen keine Anmeldemöglichkeiten zur Verfügung");

    label.setVisible(!campaignsDisplayed);
    listsList.setVisible(campaignsDisplayed);

    add(label);
    add(listsList);
  }
}
