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
import hsa.awp.admingui.edit.DrawProcedurePanel;
import hsa.awp.admingui.edit.FifoProcedurePanel;
import hsa.awp.admingui.util.AbstractDeleteLink;
import hsa.awp.campaign.model.DrawProcedure;
import hsa.awp.campaign.model.FifoProcedure;
import hsa.awp.campaign.model.Procedure;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.CollectionModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * ProcedureListPanel. This panel shows all existing Procedures.
 *
 * @author Rico Lieback
 */
public class ProcedureListPanel extends Panel {
  /**
   * generated UID.
   */

  private static final long serialVersionUID = 6197456865823351891L;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * the existing procedures.
   */
  private transient List<Procedure> procs;

  /**
   * default constuctor. create a list of all existing procedures and adds a edit link.
   *
   * @param id id of the ProcedureList
   */
  public ProcedureListPanel(String id) {

    super(id);
    procs = controller.getProceduresByMandator(getSession()); // get the existing procedures

    // create a wicket ListView
    ListView<Procedure> lv = new ListView<Procedure>("procView", procs) {
      /**
       * generated UID
       */
      private static final long serialVersionUID = 1049749247094208402L;

      @Override
      protected void populateItem(final ListItem<Procedure> item) {
        // adds the name of the procedure to the ListView
        item.add(new Label("listName", item.getModelObject().getName()));

        // adds the edit link to the ListView
        item.add(new Link<DrawProcedurePanel>("procLink", new PropertyModel<DrawProcedurePanel>(
            DrawProcedurePanel.class, "propModel")) {
          private static final long serialVersionUID = 2608105750093364620L;

          // creating the link for editing a procedure
          @Override
          public void onClick() {

            if (item.getModelObject().getClass() == DrawProcedure.class) {
              setResponsePage(new OnePanelPage(new DrawProcedurePanel(OnePanelPage.getPanelIdOne(),
                  (DrawProcedure) item.getModelObject())));
            } else if (item.getModelObject().getClass() == FifoProcedure.class) {
              setResponsePage(new OnePanelPage(new FifoProcedurePanel(OnePanelPage.getPanelIdOne(),
                  (FifoProcedure) item.getModelObject())));
            }
          }
        });

        item.add(new AbstractDeleteLink<Procedure>("deleteLink", item.getModelObject()) {
          @Override
          public void modifyItem(Procedure procedure) {
            controller.deleteProcedure(procedure);
            setResponsePage(new OnePanelPage(new ProcedureListPanel(OnePanelPage.getPanelIdOne())));
          }
        });
      }
    };

    // create a model which holds the procedures
    CollectionModel<Procedure> model = new CollectionModel<Procedure>();
    Collection<Procedure> modelCollection = new LinkedList<Procedure>();
    modelCollection.addAll(procs);
    model.setObject(modelCollection);
    lv.setDefaultModel(model);

    // adding the ListView to the panel
    add(lv);
  }
}
