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
import hsa.awp.event.model.Term;
import hsa.awp.event.model.TermType;
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
 * Panel for creating a {@link hsa.awp.event.model.Category} and viewing all persisted categories.
 *
 * @author klassm & Rico
 */
public class TermPanel extends Panel {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = 5086059009742235583L;

  /**
   * name of the Category.
   */
  private IModel<TermType> typeModel = new Model<TermType>();

  /**
   * name of the Category.
   */
  private IModel<String> descModel = new Model<String>();
  /**
   * Performing feedback.
   */
  private FeedbackPanel feedbackPanel = new FeedbackPanel("termPanel.feedback");

  /**
   * Controller which feeds the class with data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * Creates a new {@link hsa.awp.admingui.edit.TermPanel}.
   *
   * @param id wicket id.
   */
  public TermPanel(String id) {

    super(id);
    feedbackPanel.setOutputMarkupId(true);

    Form<String> form = new Form<String>("termPanel.form");

    final WebMarkupContainer availableTerms = new WebMarkupContainer("availableTerms");

    add(availableTerms);
    availableTerms.setOutputMarkupId(true);

    Comparator<Term> termComparator = new Comparator<Term>() {
      @Override
      public int compare(Term o1, Term o2) {
        return o1.getTermDesc().compareToIgnoreCase(o2.getTermDesc());
      }
    };

    List<Term> terms = new ArrayList<Term>(controller.getTermsByMandator(getSession()));

    Collections.sort(terms, termComparator);

    availableTerms.add(new ListView<Term>("knownTerms", terms) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 7541183670558921101L;

      @Override
      protected void populateItem(ListItem<Term> item) {

        item.add(new Label("name", item.getModelObject().toString()));
        item.add(new AbstractDeleteLink<Term>("deleteLink", item.getModelObject()) {
          @Override
          public void modifyItem(Term term) {
            controller.deleteTerm(term);
            setResponsePage(new OnePanelPage(new TermPanel(OnePanelPage.getPanelIdOne())));
          }
        });
      }
    });


    form.add(new TextField<String>("termPanel.desc", descModel, String.class).setRequired(true));
    form.add(new AjaxButton("termPanel.button") {
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

          controller.createTerm(descModel.getObject(), getSession());

          // TODO Sprache:
          feedbackPanel.info("Eingaben übernommen.");
          this.setVisible(false);

          target.addComponent(feedbackPanel);
          target.addComponent(form);
          target.addComponent(availableTerms);

          setResponsePage(new OnePanelPage(new TermPanel(OnePanelPage.getPanelIdOne())));
        } catch (DataAccessException e) {
          // TODO Sprache:
          feedbackPanel.error("'" + typeModel.getObject() + "' already exists");
        }
      }
    });


    add(form);
    form.add(feedbackPanel);
  }
}
