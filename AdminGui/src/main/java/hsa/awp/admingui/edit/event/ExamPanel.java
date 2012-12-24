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

package hsa.awp.admingui.edit.event;

import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.event.model.Event;
import hsa.awp.event.model.Exam;
import hsa.awp.event.model.ExamType;
import hsa.awp.event.model.OccurrenceType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.LinkedList;
import java.util.List;

/**
 * Panel for creation and alteration of exams.
 *
 * @author Basti
 * @author Matze
 */
public class ExamPanel extends Panel {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = -6453866645349887282L;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * RadioChoice which displays the {@link OccurrenceType}.
   */
  private RadioChoice<String> examTypeChoice;

  /**
   * Textfield for the exam field duration.
   */
  private TextField<Integer> durationField;

  /**
   * Textfield for the exam field resources.
   */
  private TextField<String> resourcesField;

  /**
   * Textfield for the information field.
   */
  private TextField<String> informationField;

  /**
   * Feedbackpanel which displays information and errors.
   */
  private FeedbackPanel feedbackPanel;

  /**
   * Exam to edit.
   */
  private Exam exam;

  /**
   * Component which will be updated.
   */
  private WebMarkupContainer toUpdate;

  /**
   * Event whose exam is created or altered.
   */
  private Event event;

  /**
   * Constructor for new Exam creation.
   *
   * @param id       wicket id
   * @param event    Event whose exam is created
   * @param toUpdate component to update
   */
  public ExamPanel(String id, Event event, WebMarkupContainer toUpdate) {

    this(id, event, toUpdate, null);
  }

  /**
   * Constructor for altering an exam.
   *
   * @param id       wicket id
   * @param evt      Event whose exam is created
   * @param toUpdate component to update
   * @param ex       exam to edit
   */
  public ExamPanel(String id, Event evt, WebMarkupContainer toUpdate, Exam ex) {

    super(id);

    this.toUpdate = toUpdate;

    if (ex == null) {
      this.exam = Exam.getInstance(controller.getActiveMandator(getSession()));
    } else {
      this.exam = ex;
    }

    this.event = evt;

    if (!exam.getId().equals(0L)) {
      exam = controller.updateExam(exam);
    }

    Form<Exam> form = new Form<Exam>("event.examPanel.form");
    add(form);

    feedbackPanel = new FeedbackPanel("event.examPanel.feedbackPanel");
    feedbackPanel.setOutputMarkupId(true);
    form.add(feedbackPanel);

    List<String> examTypeString = new LinkedList<String>();
    for (ExamType type : ExamType.values()) {
      examTypeString.add(type.name());
    }
    examTypeChoice = new RadioChoice<String>("event.examPanel.examTypeChoice", new Model<String>(), examTypeString);
    if (exam != null && exam.getExamType() != null) {
      examTypeChoice.setModelObject(exam.getExamType().name());
    }

    form.add(examTypeChoice);

    durationField = new TextField<Integer>("event.examPanel.duration", new Model<Integer>(), Integer.class);
    if (exam != null) {
      exam.getDuration();
      durationField.setModelObject(exam.getDuration());
    }
    form.add(durationField);

    resourcesField = new TextField<String>("event.examPanel.resources", new Model<String>());
    if (exam != null) {
      resourcesField.setModelObject(exam.getResources());
    }

    informationField = new TextField<String>("event.examPanel.information", new Model<String>(exam.getText()));
    form.add(informationField);

    form.add(resourcesField);

    form.add(new AjaxButton("event.examPanel.submit") {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = -3346661392645147244L;

      @Override
      protected void onError(AjaxRequestTarget target, Form<?> form) {

        target.addComponent(feedbackPanel);
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

        if (ExamPanel.this.toUpdate != null) {
          target.addComponent(ExamPanel.this.toUpdate);
        }
        target.addComponent(feedbackPanel);

        boolean error = false;

        boolean found = false;
        for (ExamType type : ExamType.values()) {
          if (type.name().equals(examTypeChoice.getModelObject())) {
            exam.setExamType(type);
            found = true;
            break;
          }
        }

        if (!found) {
          feedbackPanel.error("Kein Typ gegeben");
          error = true;
        }

        if (durationField.getModelObject() <= 0) {
          feedbackPanel.error("Dauer muss größer 0 sein!");
          error = true;
        }

        if (!error) {
          getParent().setVisible(false);
          getParent().setOutputMarkupId(true);
          target.addComponent(getParent());

          exam.setResources(resourcesField.getModelObject());
          exam.setDuration(new Integer(durationField.getModelObject()));
          exam.setText(informationField.getModelObject());

          if (exam.getId().equals(0L)) {
            event = controller.getEventById(event.getId());
            controller.writeExam(exam);
            event.getExams().add(exam);
            event = controller.updateEvent(event);
          } else {
            controller.updateExam(exam);
          }
        }
      }
    });
  }
}
