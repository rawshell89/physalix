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
import hsa.awp.campaign.model.Campaign;
import hsa.awp.campaign.model.Procedure;
import hsa.awp.event.model.Category;
import hsa.awp.event.model.Event;
import hsa.awp.event.model.Subject;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.text.SimpleDateFormat;
import java.util.*;

import static hsa.awp.event.util.EventFormattingUtils.formatIdSubjectNameAndDetailInformation;

/**
 * Shows a {@link List} of {@link Event}s of a given {@link Campaign}.
 *
 * @author klassm
 */
public class CampaignDetailPanel extends Panel {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = -5259592958761273357L;

  /**
   * {@link Campaign} whose elements the panel shall list.
   */
  private Campaign campaign;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;


  /**
   * Creates a new {@link CampaignDetailPanel}.
   *
   * @param id         wicket id.
   * @param campaignId id of the {@link Campaign} whose elements the page shall view.
   */
  public CampaignDetailPanel(String id, Long campaignId) {

    super(id);

    campaign = controller.getCampaignById(campaignId);
    add(new Label("campaignDetail.campaignName", campaign.getName()));

    float progress = 100 * (System.currentTimeMillis() - campaign.getStartShow().getTimeInMillis())
        / (campaign.getEndShow().getTimeInMillis() - campaign.getStartShow().getTimeInMillis());
    if (progress > 100f) {
      progress = 100f;
    }

    Label progressLabel = new Label("campaignDetail.campaignProgress");
    progressLabel.add(new AttributeAppender("style", new Model<String>("width: " + (int) progress + "%"), ";"));
    add(progressLabel);

    add(new Label("campaignDetail.progressPercentage", String.valueOf(progress) + "%"));

    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    add(new Label("campaignDetail.startShow", format.format(campaign.getStartShow().getTime())));
    add(new Label("campaignDetail.endShow", format.format(campaign.getEndShow().getTime())));

    Procedure activeProcedure = campaign.findCurrentProcedure();
    String activeProcedureName;
    if (activeProcedure != null) {
      activeProcedureName = activeProcedure.getName();
    } else {
      activeProcedureName = "----";
    }
    add(new Label("campaignDetail.activeProcedure", activeProcedureName));

    List<Event> events = controller.getEventsById(campaign.getEventIds());

    final TreeMap<Category, TreeMap<Subject, List<Event>>> categorySubject = renderMappingCategorySubjectEvent(events);

    RepeatingView categoryRepeating = new RepeatingView("campaignDetail.categoryList");
    add(categoryRepeating);
    for (Category category : categorySubject.descendingKeySet()) {
      WebMarkupContainer categoryContainer = new WebMarkupContainer(categoryRepeating.newChildId());

      categoryContainer.add(new Label("campaignDetail.categoryName", category.getName()));
      categoryRepeating.add(categoryContainer);

      TreeMap<Subject, List<Event>> subjectEvent = categorySubject.get(category);

      RepeatingView subjectRepeating = new RepeatingView("campaignDetail.subjectList");
      categoryContainer.add(subjectRepeating);
      for (Subject subject : subjectEvent.descendingKeySet()) {
        WebMarkupContainer item = new WebMarkupContainer(subjectRepeating.newChildId());
        subjectRepeating.add(item);

        item.add(new Label("campaignDetail.subjectName", subject.getName()));

        List<Event> eventsOfSubject = subjectEvent.get(subject);
        Collections.sort(events, new Comparator<Event>() {
          @Override
          public int compare(Event o1, Event o2) {

            return ((Integer) o1.getEventId()).compareTo(o2.getEventId());
          }
        });

        item.add(new ListView<Event>("campaignDetail.eventList", eventsOfSubject) {
          /**
           * unique serialization id.
           */
          private static final long serialVersionUID = 1L;

          @Override
          protected void populateItem(final ListItem<Event> item) {

            Event event = item.getModelObject();
            String eventItemString = formatIdSubjectNameAndDetailInformation(event);
            item.add(new Label("campaignDetail.eventItemString", eventItemString));

            item.add(new Link<EventDetailPanel>("campaignDetail.eventDetailLink", new PropertyModel<EventDetailPanel>(
                AlterCampaignPanel.class, "eventDetail")) {
              /**
               * unique serialization id.
               */
              private static final long serialVersionUID = 6714807243260648039L;

              @Override
              public void onClick() {


                setResponsePage(new OnePanelPage(new EventDetailPanel(OnePanelPage.getPanelIdOne(), item
                    .getModelObject().getId())));

              }
            });
          }
        });
      }
    }
  }

  /**
   * Renders a mapping table containing [category => subject => events].
   *
   * @param events list of events to use
   * @return mapping table.
   */
  private TreeMap<Category, TreeMap<Subject, List<Event>>> renderMappingCategorySubjectEvent(List<Event> events) {

    TreeMap<Category, TreeMap<Subject, List<Event>>> categorySubject = new TreeMap<Category, TreeMap<Subject, List<Event>>>(
        new Comparator<Category>() {
          @Override
          public int compare(Category o1, Category o2) {

            return o1.getName().compareTo(o2.getName());
          }
        });

    for (Event event : events) {
      if (!categorySubject.containsKey(event.getSubject().getCategory())) {
        categorySubject.put(event.getSubject().getCategory(), new TreeMap<Subject, List<Event>>(new Comparator<Subject>() {
          @Override
          public int compare(Subject o1, Subject o2) {

            return o1.getName().compareTo(o2.getName());
          }
        }));
      }
      TreeMap<Subject, List<Event>> subjectEvent = categorySubject.get(event.getSubject().getCategory());
      if (!subjectEvent.containsKey(event.getSubject())) {
        subjectEvent.put(event.getSubject(), new LinkedList<Event>());
      }
      subjectEvent.get(event.getSubject()).add(event);
    }

    return categorySubject;
  }
}
