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

package hsa.awp.admingui.edit;

import hsa.awp.admingui.OnePanelPage;
import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.admingui.util.AbstractDeleteLink;
import hsa.awp.common.exception.DataAccessException;
import hsa.awp.event.model.Category;
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
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Panel for creating a {@link Category} and viewing all persisted categories.
 *
 * @author klassm & Rico
 */
public class CategoryPanel extends Panel {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = 5086059009742235583L;

  /**
   * name of the Category.
   */
  private IModel<String> nameModel = new Model<String>();

  /**
   * Performing feedback.
   */
  private FeedbackPanel feedbackPanel = new FeedbackPanel("categorypanel.feedback");

  /**
   * Controller which feeds the class with data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * Creates a new {@link CategoryPanel}.
   *
   * @param id wicket id.
   */
  public CategoryPanel(String id) {

    super(id);
    feedbackPanel.setOutputMarkupId(true);

    Form<String> form = new Form<String>("categoryPanel.form");

    final WebMarkupContainer availableCategories = new WebMarkupContainer("availableCategories");

    add(availableCategories);
    availableCategories.setOutputMarkupId(true);

    Comparator<Category> categoryComparator = new Comparator<Category>() {
      @Override
      public int compare(Category o1, Category o2) {

        return o1.getName().compareTo(o2.getName());
      }
    };

    List<Category> categories = new ArrayList<Category>(controller.getCategoriesByMandator(getSession()));

    Collections.sort(categories, categoryComparator);

    availableCategories.add(new ListView<Category>("knownCategories", categories) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 7541183670558921101L;

      @Override
      protected void populateItem(ListItem<Category> item) {

        final Category category = controller.getCategoryById(item.getModelObject().getId());

        item.add(new Label("name", category.getName()));
        AbstractDeleteLink<Category> deleteLink = new AbstractDeleteLink<Category>("delete", category) {
          @Override
          public void modifyItem(Category category) {
            controller.deleteCategory(category);
            setResponsePage(new OnePanelPage(new CategoryPanel(OnePanelPage.getPanelIdOne())));
          }
        };
        deleteLink.setVisible(category.getSubjects().size() == 0);
        item.add(deleteLink);
      }
    });


    form.add(new TextField<String>("categoryPanel.name", nameModel).setRequired(true));
    form.add(new AjaxButton("categoryPanel.button") {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 1L;

      @Override
      protected void onError(final AjaxRequestTarget target, final Form<?> form) {

        target.addComponent(feedbackPanel);
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

        try {
          Category cat = Category.getInstance(nameModel.getObject(), controller.getActiveMandator(getSession()));
          controller.writeCategory(cat);
          feedbackPanel.info("Eingaben übernommen.");
          this.setVisible(false);

          target.addComponent(feedbackPanel);
          target.addComponent(form);
          target.addComponent(availableCategories);

          setResponsePage(new OnePanelPage(new CategoryPanel(OnePanelPage.getPanelIdOne())));
        } catch (DataAccessException e) {
          feedbackPanel.error("'" + nameModel.getObject() + "' existiert bereits");
        }
      }
    });


    add(form);
    form.add(feedbackPanel);
  }
}
