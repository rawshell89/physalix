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
import hsa.awp.campaign.model.FifoProcedure;
import hsa.awp.event.model.Category;
import hsa.awp.event.model.Event;
import hsa.awp.event.model.Subject;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.*;

/**
 * Flatlist. This panel displays all events for all Categories in a two level list.
 *
 * TODO [adopt] show event details
 *
 * @deprecated the FlatListPanel is deprecated until it is adopted
 *
 * @author basti & kai
 */
public class FlatListPanel extends Panel {
  /**
   * generated UID.
   */
  private static final long serialVersionUID = 124468394855348482L;

  /**
   * List of Categories which will be displayed in the FlatList.
   */
  private List<Category> categories;

  /**
   * List of Events which will be displayed in each Category.
   */
  private List<Event> events;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  private transient FifoProcedure fifoProcedure;

  /**
   * Constructor for the FlatList panel, which defines all needed components.
   *
   * @param id ID which declares the location in the markup.
   */
  public FlatListPanel(String id, FifoProcedure fifoProcedure) {

    super(id);

    this.fifoProcedure = fifoProcedure;
    categories = getCategoriesFromFifo(fifoProcedure);
    add(new ListView<Category>("categorylist", categories) {
      /**
       * Generated serialization id.
       */
      private static final long serialVersionUID = 490760462608363776L;

      @Override
      protected void populateItem(ListItem<Category> item) {

        events = getEventList(item.getModelObject());

        item.add(new Label("categoryname", item.getModelObject().getName()));
        item.add(new ListView<Event>("eventlist", events) {
          /**
           * Generated serialization id.
           */
          private static final long serialVersionUID = 497760462608363776L;

          @Override
          protected void populateItem(final ListItem<Event> item) {

            item.add(new Label("subjectname", item.getModelObject().getSubject().getName()));

            item.add(new Link<String>("submited") {
              /**
               * Generated serialization id.
               */
              private static final long serialVersionUID = -3933500021842052545L;

              public void onClick() {
//                                controller.registerWithFifoProcedure(FlatListPanel.this.fifoProcedure, item.getModelObject(),
//                                        SecurityContextHolder.getContext().getAuthentication().getName(), SecurityContextHolder
//                                                .getContext().getAuthentication().getName(), true);
              }
            });
          }
        });
      }
    });
  }

  private List<Category> getCategoriesFromFifo(FifoProcedure fifoProcedure) {

    Set<Category> categorySet = new HashSet<Category>();

    fifoProcedure = controller.getFifoProcedureById(fifoProcedure.getId());

    for (Long eventId : fifoProcedure.getCampaign().getEventIds()) {
      Event event = controller.getEventById(eventId);

      categorySet.add(event.getSubject().getCategory());
    }

    return new ArrayList<Category>(categorySet);
  }

  /**
   * Generates a List<Event> with all Events, which belong to the given Category cat.
   *
   * @param cat Category whose events should be represented in a list.
   * @return List of Events
   */
  private List<Event> getEventList(Category cat) {

    List<Event> ret = new LinkedList<Event>();

    for (Subject sub : cat.getSubjects()) {
      ret.addAll(sub.getEvents());
    }

    return ret;
  }
}
