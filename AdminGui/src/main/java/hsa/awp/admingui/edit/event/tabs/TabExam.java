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

package hsa.awp.admingui.edit.event.tabs;

import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.admingui.edit.event.ExamPanel;
import hsa.awp.event.model.Event;
import hsa.awp.event.model.Exam;
import hsa.awp.gui.util.LoadableDetachedModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.LinkedList;
import java.util.List;

/**
 * Tab for tabbedpanel which handles exam actions.
 *
 * @author Basti
 * @author Matze
 */
public class TabExam extends Panel {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = 6445018013058803175L;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * Container which holds the list which displays the exams in database.
   */
  private WebMarkupContainer examListContainer;

  /**
   * Container which holds the {@link ExamPanel}.
   */
  private WebMarkupContainer examEditContainer;

  /**
   * event whose exam is edited.
   */
  private Event event;

  /**
   * Constructor for tab.
   *
   * @param id  wicket id
   * @param evt event whose exams are edited.
   */
  public TabExam(String id, Event evt) {

    super(id);

    this.event = evt;

    examEditContainer = new WebMarkupContainer("event.tabExam.examEditContainer");
    examEditContainer.setOutputMarkupId(true);
    add(examEditContainer);

    ExamPanel examPanel = new ExamPanel("event.tabExam.examPanel", event, examListContainer);
    examEditContainer.add(examPanel);
    examPanel.setVisible(false);

    examListContainer = new WebMarkupContainer("event.tabExam.examListContainer");
    examListContainer.setOutputMarkupId(true);
    add(examListContainer);

    examListContainer.add(new AjaxFallbackLink<Exam>("event.tabExam.createNewExamLink") {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 1L;

      @Override
      public void onClick(AjaxRequestTarget target) {

        target.addComponent(examEditContainer);
        examEditContainer.removeAll();
        examEditContainer.add(new ExamPanel("event.tabExam.examPanel", event, examListContainer));
      }
    });

    LoadableDetachedModel<List<Exam>> exams = new LoadableDetachedModel<List<Exam>>() {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 7140862438222206869L;

      @Override
      protected List<Exam> load() {

        if (event.getId().equals(0L)) {
          return new LinkedList<Exam>();
        }
        return new LinkedList<Exam>(controller.getEventById(event.getId()).getExams());
      }
    };

    event = controller.getEventById(event.getId());
    ListView<Exam> examList = new ListView<Exam>("event.tabExam.examList", exams) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 999046116701991766L;

      @Override
      protected void populateItem(final ListItem<Exam> item) {

        final Exam exam = item.getModelObject();

        item.add(new Label("event.tabExam.type", new Model<String>(exam.getExamType().name())));
        item.add(new Label("event.tabExam.duration", new Model<String>(String.valueOf(exam.getDuration()))));
        item.add(new Label("event.tabExam.resources", new Model<String>(exam.getResources())));
        item.add(new AjaxFallbackLink<Exam>("event.tabExam.edit") {
          /**
           * unique serialization id.
           */
          private static final long serialVersionUID = 1L;

          @Override
          public void onClick(AjaxRequestTarget target) {

            target.addComponent(examEditContainer);
            examEditContainer.removeAll();
            examEditContainer.add(new ExamPanel("event.tabExam.examPanel", event, examListContainer, exam));
          }
        });

        item.add(new AjaxFallbackLink<Exam>("event.tabExam.delete") {
          /**
           * unique serialization id.
           */
          private static final long serialVersionUID = 1L;

          @Override
          public void onClick(AjaxRequestTarget target) {

            target.addComponent(examListContainer);
            controller.deleteExam(item.getModelObject(), event);
          }
        });
      }
    };
    examListContainer.add(examList);
    examListContainer.setOutputMarkupId(true);
  }

  public void makeExamEditContainerInvisible(AjaxRequestTarget target) {

    target.addComponent(examEditContainer);
    examEditContainer.setVisible(false);
  }
}
