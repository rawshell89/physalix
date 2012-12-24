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

package hsa.awp.admingui.report.view;

import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.admingui.report.lists.ParticipantList;
import hsa.awp.admingui.report.printer.CsvPrinter;
import hsa.awp.admingui.report.printer.PdfPrinter;
import hsa.awp.admingui.report.printer.Printer;
import hsa.awp.admingui.report.util.ExportList;
import hsa.awp.admingui.report.util.formats.csv.CsvProperties;
import hsa.awp.admingui.report.util.formats.pdf.PdfPrintable;
import hsa.awp.admingui.util.AccessUtil;
import hsa.awp.campaign.model.Campaign;
import hsa.awp.event.model.Event;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Panel for printing event lists.
 *
 * @author basti
 */
public class EventListPrintingPanel extends Panel {
  /**
   * uuid.
   */
  private static final long serialVersionUID = 1L;

  /**
   * ListSelector for events.
   */
  private EventSortedListSelectorPanel listSelector;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * Constructor.
   *
   * @param id wicket:id
   */
  public EventListPrintingPanel(String id) {

    super(id);

    final DropDownChoice<Campaign> source = new DropDownChoice<Campaign>("eventListPrintingPanel.sourceList",
        new Model<Campaign>(), controller.getCampaignsByMandator(getSession()), new ChoiceRenderer<Campaign>("name"));

    final CheckBox matNumber = new CheckBox("eventListPrintingPanel.matNumber", new Model<Boolean>());
    final CheckBox email = new CheckBox("eventListPrintingPanel.email", new Model<Boolean>());

    listSelector = new EventSortedListSelectorPanel("eventListPrintingPanel.listSelector", getSelectableEvents(),
        new LinkedList<Event>());
    listSelector.setOutputMarkupId(true);

    final Form<Object> form = new Form<Object>("eventListPrintingPanel.form");
    form.setOutputMarkupId(true);

    source.add(new AjaxFormComponentUpdatingBehavior("onchange") {
      /**
       * uuid.
       */
      private static final long serialVersionUID = 1L;

      @Override
      protected void onUpdate(AjaxRequestTarget target) {

        if (source.getModelObject() == null) {
          listSelector.setVisible(false);
        } else {
          EventSortedListSelectorPanel eventSelector = new EventSortedListSelectorPanel(
              "eventListPrintingPanel.listSelector", controller.getEventsByCampaign(source.getModelObject()),
              new LinkedList<Event>());
          listSelector.replaceWith(eventSelector);
          listSelector = eventSelector;
          listSelector.setVisible(true);
        }
        target.addComponent(form);
      }
    });

    Button pdfPrint = new Button("eventListPrintingPanel.pdfDownload") {
      @Override
      public void onSubmit() {

        if (listSelector.getSelected().size() <= 0) {
          error("Keine Veranstaltung gewählt");
          return;
        }

        final List<ExportList> lists = new ArrayList<ExportList>();
        for (Event event : listSelector.getSelected()) {
          lists.add(new ParticipantList(event, matNumber.getModelObject(), email.getModelObject()));
        }

        IResourceStream resourceStream = new AbstractResourceStreamWriter() {
          /**
           * generated UID.
           */
          private static final long serialVersionUID = -7988815924081508988L;

          @Override
          public String getContentType() {

            return ((PdfPrintable) lists.get(0)).getPdfProperties().getMimeType();
          }

          public void write(OutputStream output) {
            Printer printer = new PdfPrinter();
            printer.setOutputStream(output);
            printer.print(lists);
          }
        };

        getRequestCycle().setRequestTarget(
            new ResourceStreamRequestTarget(resourceStream).setFileName("Teilnehmerliste" + ((PdfPrintable) lists.get(0)).getPdfProperties().getFileExtension()));
        return;
      }
    };

    Button csvPrint = new Button("eventListPrintingPanel.csvDownload") {
      @Override
      public void onSubmit() {

        if (listSelector.getSelected().size() <= 0) {
          error("Keine Veranstaltung gewählt");
          return;
        }

        final List<ExportList> lists = new ArrayList<ExportList>();
        for (Event event : listSelector.getSelected()) {
          lists.add(new ParticipantList(event, matNumber.getModelObject(), email.getModelObject(), true));
        }

        IResourceStream resourceStream = new AbstractResourceStreamWriter() {
          /**
           * generated UID.
           */
          private static final long serialVersionUID = -7988815924081508988L;

          @Override
          public String getContentType() {

            return new CsvProperties().getMimeType();
          }

          public void write(OutputStream output) {
            Printer printer = new CsvPrinter();
            printer.setOutputStream(output);
            printer.print(lists);
          }
        };

        getRequestCycle().setRequestTarget(
            new ResourceStreamRequestTarget(resourceStream).setFileName("Teilnehmerliste" + new CsvProperties().getFileExtension()));
      }
    };

    form.add(source);
    form.add(matNumber);
    form.add(email);
    form.add(listSelector);
    form.add(pdfPrint);
    form.add(csvPrint);

    add(form);

    add(new FeedbackPanel("feedback")

    );
  }

  private List<Event> getSelectableEvents() {
    if (AccessUtil.isTeacher()) {
      return controller.getEventsByTeacher(SecurityContextHolder.getContext().getAuthentication().getName());
    }
    return controller.getEventsByMandator(getSession());
  }

}
