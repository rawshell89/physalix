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

import hsa.awp.admingui.report.printer.CsvPrinter;
import hsa.awp.admingui.report.printer.PdfPrinter;
import hsa.awp.admingui.report.printer.Printer;
import hsa.awp.admingui.report.printer.TxtPrinter;
import hsa.awp.admingui.report.util.ExportList;
import hsa.awp.admingui.report.util.formats.csv.CsvPrintable;
import hsa.awp.admingui.report.util.formats.pdf.PdfPrintable;
import hsa.awp.admingui.report.util.formats.txt.TxtPrintable;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;

import java.io.OutputStream;

public abstract class FormatSelectorPanel extends Panel {

  public FormatSelectorPanel(String id) {
    super(id);

    Button pdfPrint = new Button("pdf") {
      @Override
      public void onSubmit() {
        final ExportList list = getExportList();
        if (list instanceof PdfPrintable) {
          IResourceStream resourceStream = new AbstractResourceStreamWriter() {
            /**
             * generated UID.
             */
            private static final long serialVersionUID = -7988815924081508988L;

            @Override
            public String getContentType() {

              return ((PdfPrintable) list).getPdfProperties().getMimeType();
            }

            public void write(OutputStream output) {
              Printer printer = new PdfPrinter();
              printer.setOutputStream(output);
              printer.print(list);
            }
          };

          getRequestCycle().setRequestTarget(
              new ResourceStreamRequestTarget(resourceStream).setFileName(list.toString() + ((PdfPrintable) list).getPdfProperties().getFileExtension()));
        }
      }
    };

    Button txtPrint = new Button("txt") {
      @Override
      public void onSubmit() {
        final ExportList list = getExportList();
        if (list instanceof TxtPrintable) {
          IResourceStream resourceStream = new AbstractResourceStreamWriter() {
            /**
             * generated UID.
             */
            private static final long serialVersionUID = -7988815924081508988L;

            @Override
            public String getContentType() {

              return ((TxtPrintable) list).getTxtProperties().getMimeType();
            }

            public void write(OutputStream output) {
              Printer printer = new TxtPrinter();
              printer.setOutputStream(output);
              printer.print(list);
            }
          };

          getRequestCycle().setRequestTarget(
              new ResourceStreamRequestTarget(resourceStream).setFileName(list.toString() + ((TxtPrintable) list).getTxtProperties().getFileExtension()));
        }
      }
    };

    Button csvPrint = new Button("csv") {
      @Override
      public void onSubmit() {
        final ExportList list = getExportList();
        if (list instanceof CsvPrintable) {
          IResourceStream resourceStream = new AbstractResourceStreamWriter() {
            /**
             * generated UID.
             */
            private static final long serialVersionUID = -7988815924081508988L;

            @Override
            public String getContentType() {

              return ((CsvPrintable) list).getCsvProperties().getMimeType();
            }

            public void write(OutputStream output) {
              Printer printer = new CsvPrinter();
              printer.setOutputStream(output);
              printer.print(list);
            }
          };

          getRequestCycle().setRequestTarget(
              new ResourceStreamRequestTarget(resourceStream).setFileName(list.toString() + ((CsvPrintable) list).getCsvProperties().getFileExtension()));
        }
      }
    };

    pdfPrint.setOutputMarkupId(true);
    txtPrint.setOutputMarkupId(true);
    csvPrint.setOutputMarkupId(true);

    ExportList list = getExportList();
    boolean isPdfDocument = list instanceof PdfPrintable;
    boolean isTxtDocument = list instanceof TxtPrintable;
    boolean isCsvDocument = list instanceof CsvPrintable;

    pdfPrint.setVisible(isPdfDocument);
    txtPrint.setVisible(isTxtDocument);
    csvPrint.setVisible(isCsvDocument);

    add(pdfPrint);
    add(txtPrint);
    add(csvPrint);
  }

  protected abstract ExportList getExportList();


}
