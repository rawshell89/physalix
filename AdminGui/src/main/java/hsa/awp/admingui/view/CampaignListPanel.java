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

import hsa.awp.admingui.OnePanelPage;
import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.admingui.edit.AlterCampaignPanel;
import hsa.awp.admingui.util.AbstractDeleteLink;
import hsa.awp.admingui.util.AccessUtil;
import hsa.awp.campaign.model.Campaign;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * The {@link Panel} class which allows to list all existing {@link Campaign}s.
 *
 * @author Rico
 */
public class CampaignListPanel extends Panel {
  /**
   * generated UID.
   */
  private static final long serialVersionUID = 124468394855348482L;

  /**
   * String for ascending order.
   */
  private String ascending = "aufsteigend";

  /**
   * {@link ListView} for viewing all {@link Campaign}s.
   */
  private PageableListView<Campaign> campaignList;

  /**
   * Container for holding the PageNavigation and the campaignList.
   */
  private WebMarkupContainer campaignListMarkupContainer;

  /**
   * Holder for all existing {@link Campaign}s.
   */
  private List<Campaign> campaigns;

  /**
   * FeedbackPanel
   */
  private FeedbackPanel feedbackPanel = new FeedbackPanel("campaignList.feedback");

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * String for descending order.
   */
  private String descending = "absteigend";

  /**
   * Number of items to show on one page.
   */
  private int pagesize = 15;


  /**
   * Constructor with {@link Panel}ID.
   *
   * @param id the id of the {@link Panel}.
   */
  public CampaignListPanel(String id) {

    super(id);

    Form<Object> sortChoiceForm = new Form<Object>("campaignList.sortChoices");
    add(sortChoiceForm);

    LinkedList<String> sortFieldChoices = new LinkedList<String>();
    sortFieldChoices.addAll(SortChoice.names());

    LinkedList<String> sortDirectionChoices = new LinkedList<String>();
    sortDirectionChoices.add(ascending);
    sortDirectionChoices.add(descending);

    final DropDownChoice<String> sortFields = new DropDownChoice<String>("campaignList.sortFields",
        new Model<String>(ascending), sortFieldChoices);
    sortFields.setOutputMarkupId(true);
    sortChoiceForm.add(sortFields);

    final DropDownChoice<String> sortDirections = new DropDownChoice<String>("CampaignList.sortDirections", new Model<String>(
        SortChoice.NAME.name), sortDirectionChoices);
    sortDirections.setOutputMarkupId(true);
    sortChoiceForm.add(sortDirections);

    sortFields.add(new AjaxFormComponentUpdatingBehavior("onchange") {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 1L;

      @Override
      protected void onUpdate(AjaxRequestTarget target) {

        boolean asc = false;
        if (sortDirections.getModelObject() != null && sortDirections.getModelObject().equals(ascending)) {
          asc = false;
        }
        sort(SortChoice.choiceByName(sortFields.getModelObject()), asc);
        target.addComponent(campaignListMarkupContainer);
      }
    });

    sortDirections.add(new AjaxFormComponentUpdatingBehavior("onchange") {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 1L;

      @Override
      protected void onUpdate(AjaxRequestTarget target) {

        boolean asc = false;
        if (sortDirections.getModelObject() != null && sortDirections.getModelObject().equals(ascending)) {
          asc = false;
        }
        sort(SortChoice.choiceByName(sortFields.getModelObject()), asc);
        target.addComponent(campaignListMarkupContainer);
      }
    });

    campaigns = controller.getCampaignsByMandator(getSession());

    ListActiveCampaignsPanel activeCampaigns = new ListActiveCampaignsPanel("campaignList.activeCampaigns");
    activeCampaigns.add(new AjaxSelfUpdatingTimerBehavior(Duration.minutes(5d)));
    add(activeCampaigns);

    final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    campaignList = new PageableListView<Campaign>("campaignList", campaigns, pagesize) {
      /**
       * unique identifier.
       */
      private static final long serialVersionUID = 5446468946546848946L;

      @Override
      protected void populateItem(final ListItem<Campaign> item) {

        Campaign campaign = item.getModelObject();

        Label state = new Label("campaignState");
        String stateString;
        if (campaign.isRunning()) {
          stateString = "running";
        } else if (campaign.isTerminated()) {
          stateString = "terminated";
        } else {
          stateString = "notYetStarted";
        }
        state.add(new AttributeAppender("class", new Model<String>(stateString), " "));
        item.add(state);

        item.add(new Label("startShow", format.format(item.getModelObject().getStartShow().getTime())));
        item.add(new Label("endShow", format.format(item.getModelObject().getEndShow().getTime())));

        item.add(new Label("campaignName", item.getModelObject().getName()));

        item.add(createEditLink(item));
        item.add(createDetailLink(item));
        AbstractDeleteLink<Campaign> deleteLink = new AbstractDeleteLink<Campaign>("deleteLink", item.getModelObject()) {
          @Override
          public void modifyItem(Campaign campaign) {
            controller.deleteCampaign(campaign);
            setResponsePage(new OnePanelPage(new CampaignListPanel(OnePanelPage.getPanelIdOne())));
          }
        };
        deleteLink.setVisible(!(item.getModelObject().getAppliedProcedures().size() > 0));
        item.add(deleteLink);
      }

      private Link<AlterCampaignPanel> createEditLink(final ListItem<Campaign> item) {
        Link<AlterCampaignPanel> alterCampaign = new Link<AlterCampaignPanel>("campaignLink", new PropertyModel<AlterCampaignPanel>(
            AlterCampaignPanel.class, "alterCampaign")) {
          /**
           * unique serialization id.
           */
          private static final long serialVersionUID = -5466954901705080742L;

          @Override
          public void onClick() {

            setResponsePage(new OnePanelPage(new AlterCampaignPanel(OnePanelPage.getPanelIdOne(), item.getModelObject()
                .getId())));
          }
        };

        AccessUtil.allowRender(alterCampaign, "editCampaign");

        return alterCampaign;
      }

      private Link<CampaignDetailPanel> createDetailLink(final ListItem<Campaign> item) {
        Link<CampaignDetailPanel> campDetail;
        campDetail = new Link<CampaignDetailPanel>("eventList", new PropertyModel<CampaignDetailPanel>(
            CampaignDetailPanel.class, "eventList")) {
          /**
           * unique serialization id.
           */
          private static final long serialVersionUID = -5466954901732080742L;

          @Override
          public void onClick() {

            setResponsePage(new OnePanelPage(new CampaignDetailPanel(OnePanelPage.getPanelIdOne(), item
                .getModelObject().getId())));
          }
        };
        return campDetail;
      }
    };
    campaignList.setOutputMarkupId(true);
    sort(SortChoice.NAME, true);

    campaignListMarkupContainer = new WebMarkupContainer("campaignList.listContainer");
    campaignListMarkupContainer.add(campaignList);
    campaignListMarkupContainer.setOutputMarkupId(true);

    PagingNavigation navigation = new PagingNavigation("campaignList.navigation", campaignList);
    navigation.setOutputMarkupId(true);
    campaignListMarkupContainer.add(navigation);

    if (campaignList.getModelObject().size() < pagesize) {
      navigation.setVisible(false);
    }

    add(campaignListMarkupContainer);
    add(feedbackPanel);
  }

  /**
   * Sorts the campaign list by a given choice.
   *
   * @param choice    chosen sort choice.
   * @param ascending order to sort.
   */
  private void sort(SortChoice choice, final boolean ascending) {

    if (choice == null) {
      choice = SortChoice.NAME;
    }
    final SortChoice renderChoice = choice;

    Collections.sort(campaigns, new Comparator<Campaign>() {
      @Override
      public int compare(Campaign o1, Campaign o2) {

        int val;
        switch (renderChoice) {
          case STARTDATE:
            val = o1.getStartShow().compareTo(o2.getStartShow());
            break;
          case ENDDATE:
            val = o1.getEndShow().compareTo(o2.getEndShow());
            break;
          case NAME:
          default:
            val = o1.getName().compareTo(o2.getName());
        }
        if (!ascending) {
          val *= -1;
        }
        return val;
      }
    });

    campaignList.setCurrentPage(0);
    campaignList.setList(campaigns);
  }

  /**
   * Enum representing the sort choices.
   *
   * @author klassm
   */
  private enum SortChoice {
    /**
     * End of a {@link Campaign}.
     */
    ENDDATE("Enddatum"),
    /**
     * Name of a {@link Campaign}.
     */
    NAME("Name"),
    /**
     * StartDate of a {@link Campaign}.
     */
    STARTDATE("Startdatum");

    /**
     * Looks for a choice by its name.
     *
     * @param name name of the choice.
     * @return SortChoice.
     */
    public static SortChoice choiceByName(String name) {

      for (SortChoice choice : SortChoice.values()) {
        if (choice.name.equals(name)) {
          return choice;
        }
      }
      return null;
    }

    /**
     * Getter for a List of all names of all available sort options.
     *
     * @return names of available sort options.
     */
    public static List<String> names() {

      List<String> names = new LinkedList<String>();
      for (SortChoice choice : SortChoice.values()) {
        names.add(choice.name);
      }
      return names;
    }

    /**
     * Name being viewed for a sort option.
     */
    private String name;

    /**
     * Creates a new {@link SortChoice}.
     *
     * @param name name of the {@link SortChoice}.
     */
    private SortChoice(String name) {

      this.name = name;
    }
  }
}
