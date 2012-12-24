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

import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.event.model.Category;
import hsa.awp.event.model.Subject;
import org.apache.wicket.Response;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AbstractAutoCompleteRenderer;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IErrorMessageSource;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.validator.StringValidator;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Panel with the functionality to create & edit an subject.
 *
 * @author Rico
 */
public class SubjectPanel extends Panel {
  /**
   * generated UID.
   */
  private static final long serialVersionUID = 124468394855348482L;

  /**
   * category represents the category attribute in subject.
   */
  private TextField<String> category = new TextField<String>("category", new Model<String>(""));

  /**
   * part of the AJAX auto complete field for the category selection. renders the field.
   */
  private AbstractAutoCompleteRenderer<String> categoryRenderer = new AbstractAutoCompleteRenderer<String>() {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String getTextValue(String object) {

      return object;
    }

    @Override
    protected void renderChoice(String object, Response response, String criteria) {

      response.write(getTextValue(object));
    }
  };

  /**
   * part of the AJAX auto complete field for the category selection. handles the categories.
   */
  private AutoCompleteBehavior<String> categoryAutoCompleteBehavior = new AutoCompleteBehavior<String>(categoryRenderer) {
    /**
     * generated serialization id.
     */
    private static final long serialVersionUID = 1220805798637698821L;

    @Override
    protected Iterator<String> getChoices(String input) {

      List<String> names = new LinkedList<String>();
      for (Category cat : controller.getCategoriesByMandator(getSession())) {
        names.add(cat.getName());
      }
      Collections.sort(names);

      return names.iterator();
    }
  };

  /**
   * Controller which feeds the class with data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * desc represents the description attribute in subject.
   */
  private TextArea<String> desc = new TextArea<String>("description");

  /**
   * link represents the link attribute in subject.
   */
  private TextField<String> link = new TextField<String>("link");

  /**
   * name represents the eventID attribute in subject.
   */
  private TextField<String> name = new TextField<String>("name");

  /**
   * the text which will be shown in the panel.
   */
  private Label panelLabel = new Label("panelLabel", new Model<String>());

  private FeedbackPanel feedbackPanel = new FeedbackPanel("subjectpanel.feedback");

  /**
   * Constructor for CreateSubject. This is the setup for the panel and here are all components registered.
   *
   * @param id wicket:id which connects markup with code.
   */
  public SubjectPanel(String id) {

    this(id, null);
    // TODO Sprache:
    add(panelLabel.setDefaultModel(new Model<String>("Fach erstellen")));
  }

  /**
   * Constructor with {@link Panel}ID and {@link Subject}.
   *
   * @param id  the {@link Panel}ID
   * @param sub the subject which has to be edited
   */
  public SubjectPanel(String id, final Subject sub) {

    super(id);

    final Subject subject;
    if (sub == null) {
      subject = Subject.getInstance(controller.getActiveMandator(getSession()));
    } else {
      subject = sub;
    }

    final Form<Object> form = new Form<Object>("form");
    // form.setDefaultModel(new CompoundPropertyModel<Subject>(subject));

    name.setRequired(true);


    StringValidator stringValidator = new StringValidator() {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = -8354513043330314939L;

      @Override
      protected void onValidate(IValidatable<String> validatable) {

        Subject found = controller.getSubjectByNameAndMandator(validatable.getValue(), getSession());

        if (found != null) {
          validatable.error(new IValidationError() {
            @Override
            public String getErrorMessage(IErrorMessageSource messageSource) {

              return "Fachname existiert bereits";
            }
          });
        }
      }
    };

    if (subject.getId() == 0) {
      name.add(stringValidator);
    }

    category.setRequired(true);
    category.add(categoryAutoCompleteBehavior);

    feedbackPanel.setOutputMarkupId(true);

    // add(new FeedbackPanel("feedback").setOutputMarkupId(true));
    // TODO Sprache:
    add(panelLabel.setDefaultModel(new Model<String>("Fach bearbeiten")));

    form.add(name.setDefaultModel(new Model<String>(subject.getName())));
    try {
      form.add(category.setDefaultModel(new Model<String>(subject.getCategory().getName())));
    } catch (NullPointerException e) {
      form.add(category.setDefaultModel(new Model<String>("")));
    }
    form.add(desc.setDefaultModel(new Model<String>(subject.getDescription())));
    form.add(link.setDefaultModel(new Model<String>(subject.getLink())));

    form.add(new AjaxButton("submit") {
      /**
       * generated serialization id.
       */
      private static final long serialVersionUID = -6537464906539587006L;

      @Override
      protected void onError(final AjaxRequestTarget target, final Form<?> form) {

        target.addComponent(feedbackPanel);
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

        target.addComponent(form);
        target.addComponent(feedbackPanel);

        if (saveSubject(subject)) return;

        // TODO Sprache:
        feedbackPanel.info("Eingaben übernommen.");
        this.setVisible(false);
      }
    });
    add(feedbackPanel);
    add(form);
  }

  private boolean saveSubject(Subject subject) {
    Category cat = controller.getCategoryByNameAndMandator(category.getModelObject(), getSession());

    subject.setName(name.getModelObject());
    subject.setDescription(desc.getModelObject());
    subject.setLink(link.getModelObject());

    if (cat == null) {
      // TODO Sprache:
      feedbackPanel.error("Kategorie nicht gefunden.");
      return true;
    }

    // if a category is already associated with that subject remove it first
    if (subject.getCategory() != null) {
      Category oldCat = controller.getCategoryByNameAndMandator(subject.getCategory().getName(), getSession());
      if (oldCat != null) {
        oldCat.removeSubject(subject);
        controller.writeCategory(oldCat);
        controller.writeSubject(subject);
      }
    }


    // add subject to category
    cat.addSubject(subject);
    controller.writeSubject(subject);
    controller.writeCategory(cat);
    return false;
  }
}
