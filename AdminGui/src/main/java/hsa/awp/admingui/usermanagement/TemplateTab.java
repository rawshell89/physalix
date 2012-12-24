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

import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.common.model.TemplateType;
import hsa.awp.user.model.Mandator;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Arrays;

public class TemplateTab extends Panel {

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  public TemplateTab(String panelId, final Mandator mandator) {
    super(panelId);

    final DropDownChoice<TemplateType> templateTypeDropDownChoice = new DropDownChoice<TemplateType>("templateTypes",
        new Model<TemplateType>(TemplateType.DRAWN), Arrays.asList(TemplateType.values()), new ChoiceRenderer<TemplateType>("desc"));

    final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
    feedbackPanel.setOutputMarkupId(true);

    final IModel<String> templateModel = new Model<String>(findTemplateAsString(mandator, templateTypeDropDownChoice.getModelObject()));

    final TextArea<String> textArea = new TextArea<String>("template.area", templateModel);
    textArea.setOutputMarkupId(true);

    templateTypeDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {

      @Override
      protected void onUpdate(AjaxRequestTarget target) {
        String s = findTemplateAsString(mandator, templateTypeDropDownChoice.getModelObject());
        templateModel.setObject(s);
        Session.get().getFeedbackMessages().clear();
        target.addComponent(textArea);
        target.addComponent(feedbackPanel);
      }
    });

    Button button = new Button("submitButton", new Model<String>("Speichern")) {
      @Override
      public void onSubmit() {
        controller.saveTemplate(mandator.getId(), textArea.getModelObject(), templateTypeDropDownChoice.getModelObject());
        feedbackPanel.info("Erfolgreich gespreichert");
      }
    };

    AjaxButton resetButton = new AjaxButton("resetButton", new Model<String>("Standardvariante Wiederherstellen")) {
      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
        String s = controller.loadDefaultTemplate(templateTypeDropDownChoice.getModelObject());
        templateModel.setObject(s);
        feedbackPanel.info("Standard wiederhergestellt. Es wurde noch nicht gespeichert!");
        target.addComponent(textArea);
        target.addComponent(feedbackPanel);
      }
    };

    final ModalWindow detailWindow = new ModalWindow("templateTest.detailWindow");
    detailWindow.setContent(new AjaxLazyLoadPanel(detailWindow.getContentId()) {
      /**
       *
       */
      private static final long serialVersionUID = -822132746613326567L;

      @Override
      public Component getLazyLoadComponent(String markupId) {

        return new TemplateTestPanel(markupId, textArea.getModelObject());
      }
    });
    detailWindow.setTitle(new Model<String>("Template Test"));

    AjaxButton testButton = new AjaxButton("testButton", new Model<String>("Template Testen")) {
      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
        detailWindow.show(target);
      }
    };

    Form<String> modalForm = new Form<String>("modalForm");
    modalForm.add(detailWindow);

    Form<String> form = new Form<String>("template.form");
    form.add(textArea);
    form.add(templateTypeDropDownChoice);
    form.add(button);
    form.add(resetButton);
    form.add(testButton);
    add(form);
    add(modalForm);
    add(feedbackPanel);
  }

  private String findTemplateAsString(Mandator mandator, TemplateType templateType) {

    if (mandator == null) {
      return "";
    }
    if (templateType == null) {
      return "";
    }

    return controller.getTemplateAsString(mandator.getId(), templateType);
  }

}
