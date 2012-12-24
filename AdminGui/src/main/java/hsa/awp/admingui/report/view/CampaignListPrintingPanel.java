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
import hsa.awp.admingui.report.lists.BookingList;
import hsa.awp.admingui.report.lists.ConflictList;
import hsa.awp.admingui.report.lists.CourseParticipationList;
import hsa.awp.admingui.report.lists.ExamOfficeList;
import hsa.awp.admingui.report.printer.CsvPrinter;
import hsa.awp.admingui.report.printer.PdfPrinter;
import hsa.awp.admingui.report.printer.Printer;
import hsa.awp.admingui.report.printer.TxtPrinter;
import hsa.awp.campaign.model.Campaign;
import hsa.awp.event.model.Event;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;

import java.io.OutputStream;
import java.util.LinkedList;

/**
 * Panel for printing campaign based reports.
 *
 * @author basti
 */
public class CampaignListPrintingPanel extends Panel {
  /**
   * generated uid.
   */
  private static final long serialVersionUID = -4058408888179250245L;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * Form.
   */
  private Form<Object> form;

  /**
   * Constructor.
   *
   * @param id wicket:id
   */
  public CampaignListPrintingPanel(String id) {

    super(id);

    add(createBookinglistForm());
    add(createExamOfficelistForm());
    add(createCourseParticipationForm());
    add(createConflictListForm());
    add(new FeedbackPanel("feedback"));
  }

  private Form<Object> createConflictListForm() {
    Form<Object> form = new Form<Object>("conflict.form");

    final EventSortedListSelectorPanel listSelector = new EventSortedListSelectorPanel("conflict.listSelector", controller.getEventsByMandator(getSession()),
        new LinkedList<Event>());
    listSelector.setOutputMarkupId(true);


    Button downloadButton = new Button("conflict.txtDownload") {
      public void onSubmit() {

        final ConflictList list = new ConflictList(listSelector.getSelected());

        IResourceStream resourceStream = new AbstractResourceStreamWriter() {
          /**
           * generated UID.
           */
          private static final long serialVersionUID = -7988815924081508988L;

          @Override
          public String getContentType() {

            return list.getCsvProperties().getMimeType();
          }

          public void write(OutputStream output) {
            Printer printer = new CsvPrinter();
            printer.setOutputStream(output);
            printer.print(list);
          }
        };

        getRequestCycle().setRequestTarget(
            new ResourceStreamRequestTarget(resourceStream).setFileName(list.toString() + list.getCsvProperties().getFileExtension()));
      }
    };

    form.add(listSelector);
    form.add(createListInfoLabel("conflict.listInfo", PrintableLists.COURSECONFLICTLIST));
    form.add(downloadButton);
    return form;
  }


  private Form<Object> createBookinglistForm() {
    Form<Object> form = new Form<Object>("bookingList.form");

    final DropDownChoice<Campaign> campaignList = createCampaignList("bookingList.sourceList");

    Button downloadButton = new Button("bookingList.pdfDownload") {
      public void onSubmit() {

        final BookingList list = new BookingList(controller.getCampaignById(campaignList.getModelObject().getId()));

        IResourceStream resourceStream = new AbstractResourceStreamWriter() {
          /**
           * generated UID.
           */
          private static final long serialVersionUID = -7988815924081508988L;

          @Override
          public String getContentType() {

            return list.getPdfProperties().getMimeType();
          }

          public void write(OutputStream output) {
            Printer printer = new PdfPrinter();
            printer.setOutputStream(output);
            printer.print(list);
          }
        };

        getRequestCycle().setRequestTarget(
            new ResourceStreamRequestTarget(resourceStream).setFileName(campaignList.getModelObject().getName() + " " + list.toString() + list.getPdfProperties().getFileExtension()));
      }
    };

    form.add(campaignList);
    form.add(createListInfoLabel("bookingList.listInfo", PrintableLists.BOOKINGLIST));
    form.add(downloadButton);

    return form;
  }

  private Form<Object> createCourseParticipationForm() {
    Form<Object> form = new Form<Object>("courseParticipation.form");

    final DropDownChoice<Campaign> campaignList = createCampaignList("courseParticipation.sourceList");

    Button downloadButton = new Button("courseParticipation.txtDownload") {
      public void onSubmit() {

        final CourseParticipationList list = new CourseParticipationList(controller.getCampaignById(campaignList.getModelObject().getId()));

        IResourceStream resourceStream = new AbstractResourceStreamWriter() {
          /**
           * generated UID.
           */
          private static final long serialVersionUID = -798881592408150838L;

          @Override
          public String getContentType() {

            return list.getTxtProperties().getMimeType();
          }

          public void write(OutputStream output) {
            Printer printer = new TxtPrinter();
            printer.setOutputStream(output);
            printer.print(list);
          }
        };

        getRequestCycle().setRequestTarget(
            new ResourceStreamRequestTarget(resourceStream).setFileName(campaignList.getModelObject().getName() + " " + list.toString() + list.getTxtProperties().getFileExtension()));
      }
    };

    form.add(campaignList);
    form.add(createListInfoLabel("courseParticipation.listInfo", PrintableLists.COURSEPARTICIPATIONLIST));
    form.add(downloadButton);

    return form;
  }

  private Form<Object> createExamOfficelistForm() {
    Form<Object> form = new Form<Object>("examOffice.form");

    final DropDownChoice<Campaign> campaignList = createCampaignList("examOffice.sourceList");

    Button downloadButton = new Button("examOffice.txtDownload") {
      public void onSubmit() {

        final ExamOfficeList list = new ExamOfficeList(controller.getCampaignById(campaignList.getModelObject().getId()));

        IResourceStream resourceStream = new AbstractResourceStreamWriter() {
          /**
           * generated UID.
           */
          private static final long serialVersionUID = -7988815924081508988L;

          @Override
          public String getContentType() {

            return list.getTxtProperties().getMimeType();
          }

          public void write(OutputStream output) {
            Printer printer = new TxtPrinter();
            printer.setOutputStream(output);
            printer.print(list);
          }
        };

        getRequestCycle().setRequestTarget(
            new ResourceStreamRequestTarget(resourceStream).setFileName(campaignList.getModelObject().getName() + " " + list.toString() + list.getTxtProperties().getFileExtension()));
      }
    };

    form.add(campaignList);
    form.add(createListInfoLabel("examOffice.listInfo", PrintableLists.EXAMOFFICELIST));
    form.add(downloadButton);

    return form;
  }

  private Label createListInfoLabel(String id, PrintableLists list) {
    Label infoLabel = new Label(id, list.getDescription());
    return infoLabel;
  }

  private DropDownChoice<Campaign> createCampaignList(String id) {
    DropDownChoice<Campaign> sourceCampaign = new DropDownChoice<Campaign>(id,
        new Model<Campaign>(), controller.getCampaignsByMandator(getSession()), new ChoiceRenderer<Campaign>("name"));
    sourceCampaign.setRequired(true);

    return sourceCampaign;
  }

}
