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

package hsa.awp.admingui.usermanagement;

import hsa.awp.admingui.OnePanelPage;
import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.admingui.util.AbstractDeleteLink;
import hsa.awp.admingui.util.AbstractDetailLink;
import hsa.awp.gui.util.LoadableDetachedModel;
import hsa.awp.user.model.Mandator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MandatorPanel extends Panel {

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  private WebMarkupContainer container;

  private FeedbackPanel feedbackPanel;

  public MandatorPanel(String id) {
    super(id);

    feedbackPanel = new FeedbackPanel("feedbackpanel");
    feedbackPanel.setOutputMarkupId(true);
    add(feedbackPanel);

    addMandatorCreation();
    addPersistentMandatorList();
  }

  private void addPersistentMandatorList() {

    LoadableDetachedModel<List<Mandator>> mandatorModel = new LoadableDetachedModel<List<Mandator>>() {
      @Override
      protected List<Mandator> load() {

        Set<Mandator> mandatorSet = new HashSet<Mandator>(controller.getAllMandators());

        mandatorSet.remove(controller.getTheAllMandator());

        return new ArrayList<Mandator>(mandatorSet);
      }
    };

    ListView<Mandator> listView = new ListView<Mandator>("listView", mandatorModel) {
      @Override
      protected void populateItem(ListItem<Mandator> mandatorListItem) {
        final Mandator mandator = mandatorListItem.getModelObject();
        mandatorListItem.add(new Label("mandatorName", new Model<String>(mandator.getName())));

        AbstractDetailLink<Mandator> mandatorDetailPanelLink = new AbstractDetailLink<Mandator>("mandatorDetailLink", mandator) {
          @Override
          public void modifyItem(Mandator mandator) {
            setResponsePage(new OnePanelPage(new MandatorDetailPanel(OnePanelPage.getPanelIdOne(), mandator)));
          }
        };
        mandatorListItem.add(mandatorDetailPanelLink);
        AbstractDeleteLink<Mandator> deleteLink = new AbstractDeleteLink<Mandator>("deleteLink", mandator) {
          @Override
          public void modifyItem(Mandator mandator) {
            controller.deleteMandator(mandator);
            setResponsePage(new OnePanelPage(new MandatorPanel(OnePanelPage.getPanelIdOne())));
          }
        };
        deleteLink.setVisible(controller.isMandatorDeletable(mandator));
        mandatorListItem.add(deleteLink);
      }
    };

    container = new WebMarkupContainer("availableMandators");
    container.setOutputMarkupId(true);
    container.add(listView);
    add(container);
  }

  private void addMandatorCreation() {

    final Form form = new Form("form");

    final Model<String> nameModel = new Model<String>();
    final TextField<String> textField = new TextField<String>("name", nameModel);
    textField.setOutputMarkupId(true);
    AjaxButton button = new AjaxButton("button", new Model<String>("anlegen")) {
      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

        Mandator mandator = controller.getMandatorByName(nameModel.getObject());

        if (mandator == null) {
          controller.createMandator(nameModel.getObject());
          textField.clearInput();
          nameModel.setObject("");
        } else {
          feedbackPanel.error("Mandant mit dem Namen '" + nameModel.getObject() + "' existiert bereits.");
        }
        target.addComponent(textField);
        target.addComponent(container);
        target.addComponent(feedbackPanel);
      }
    };

    form.add(textField);
    form.add(button);

    add(form);
  }
}
