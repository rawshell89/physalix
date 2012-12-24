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
import hsa.awp.event.model.Occurrence;
import hsa.awp.event.model.OccurrenceType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.yui.calendar.DateTimeField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Panel which lets the user creating and editing of an {@link Occurrence}.
 *
 * @author Basti
 */
public class OccurrencePanel extends Panel {
  /**
   * generated UID.
   */
  private static final long serialVersionUID = 9171749576816496588L;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * DateTimeField for endDate in {@link Occurrence}.
   */
  private DateTimeField endDateField;

  /**
   * Event whose {@link Occurrence} is edited oder created.
   */
  private Event event;

  /**
   * FeedbackPanel which displays errors and infos.
   */
  private FeedbackPanel feedbackPanel;

  /**
   * Field for additional information.
   */
  private TextField<String> informationField;

  /**
   * {@link Occurrence} which is edited oder created.
   */
  private Occurrence occurrence;

  /**
   * RadioChoice for {@link OccurrenceType}.
   */
  private RadioChoice<OccurrenceType> occurrenceTypeChoice;

  /**
   * DatetimeField for startDate in {@link Occurrence}.
   */
  private DateTimeField startDateField;

  /**
   * Component which should be updated.
   */
  private WebMarkupContainer toUpdate;

  /**
   * Constructor for creating an {@link Occurrence}.
   *
   * @param id       wicket id
   * @param event    event whose {@link Occurrence} is created.
   * @param toUpdate component to update.
   */
  public OccurrencePanel(String id, Event event, WebMarkupContainer toUpdate) {

    this(id, event, toUpdate, null);
  }

  /**
   * Constructor for editing an {@link Occurrence}.
   *
   * @param id       wicket id
   * @param evt      event whose {@link Occurrence} is edited
   * @param toUpdate component to update.
   * @param occ      {@link Occurrence} which is edited.
   */
  public OccurrencePanel(String id, final Event evt, WebMarkupContainer toUpdate, Occurrence occ) {

    super(id);

    this.toUpdate = toUpdate;
    if (occ == null) {
      this.occurrence = Occurrence.getInstance(controller.getActiveMandator(getSession()));
    } else {
      this.occurrence = occ;
    }
    this.event = evt;

    if (!occurrence.getId().equals(0L)) {
      occurrence = controller.updateOccurrence(occurrence);
    }

    Form<Occurrence> form = new Form<Occurrence>("event.occurrencePanel.form");
    add(form);

    feedbackPanel = new FeedbackPanel("event.occurrencePanel.feedbackPanel");
    feedbackPanel.setOutputMarkupId(true);
    form.add(feedbackPanel);

    List<OccurrenceType> occurrenceType = new LinkedList<OccurrenceType>();
    for (OccurrenceType type : OccurrenceType.values()) {
      occurrenceType.add(type);
    }

    ChoiceRenderer<OccurrenceType> choiceRenderer = new ChoiceRenderer<OccurrenceType>("desc");

    occurrenceTypeChoice = new RadioChoice<OccurrenceType>("event.occurrencePanel.occurrenceTypeChoice",
        new Model<OccurrenceType>(), occurrenceType, choiceRenderer);
    if (occurrence != null && occurrence.getType() != null) {
      occurrenceTypeChoice.setModelObject(occurrence.getType());
    }

    form.add(occurrenceTypeChoice);

    startDateField = new DateTimeField("event.occurrencePanel.start", new Model<Date>());
    if (occurrence != null && occurrence.getStartDate() != null) {
      startDateField.setModelObject(occurrence.getStartDate().getTime());
    }
    form.add(startDateField);

    endDateField = new DateTimeField("event.occurrencePanel.end", new Model<Date>());
    if (occurrence != null && occurrence.getEndDate() != null) {
      endDateField.setModelObject(occurrence.getEndDate().getTime());
    }
    form.add(endDateField);

    informationField = new TextField<String>("event.occurencePanel.information", new Model<String>(occurrence.getInformation()));
    form.add(informationField);

    form.add(new AjaxButton("event.occurrencePanel.submit") {
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

        if (OccurrencePanel.this.toUpdate != null) {
          target.addComponent(OccurrencePanel.this.toUpdate);
        }
        target.addComponent(feedbackPanel);

        boolean error = false;

        if (occurrenceTypeChoice.getModelObject() != null) {
          occurrence.setType(occurrenceTypeChoice.getModelObject());
        } else {
          feedbackPanel.error("Kein Typ gegeben");
          error = true;
        }

        if (startDateField.getModelObject() == null) {
          feedbackPanel.error("Ein Startdatum muss gegeben sein");
          error = true;
        }

        if (!error) {
          getParent().setVisible(false);
          target.addComponent(getParent());
          getParent().setOutputMarkupId(true);

          Calendar start = Calendar.getInstance();
          start.setTime(startDateField.getModelObject());
          Calendar end = Calendar.getInstance();
          end.setTime(endDateField.getModelObject());
          occurrence.setInterval(start, end);
          occurrence.setInformation(informationField.getModelObject());

          controller.writeOccurrenceToEvent(occurrence, event, getSession());
        }
      }
    });
  }
}
