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

package hsa.awp.admingui.report.printer;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hsa.awp.admingui.report.util.ExportList;
import hsa.awp.admingui.report.util.HeaderFooter;
import hsa.awp.admingui.report.util.Row;
import hsa.awp.admingui.report.util.formats.pdf.PdfCellProperties;
import hsa.awp.admingui.report.util.formats.pdf.PdfPrintable;

import java.io.OutputStream;

public class PdfPrinter implements Printer {

  private OutputStream outputStream;

  @Override
  public void print(ExportList list) {

    if (outputStream == null) {
      throw new IllegalArgumentException("OutputStream undefined, please use setOutputStream()");
    }

    if (!(list instanceof PdfPrintable)) {
      throw new IllegalArgumentException("PdfPrinter can only print PdfPrintables");
    }

    PdfPrintable pdfPrintable = (PdfPrintable) list;
    Document document = setUpDocument(pdfPrintable);

    try {
      document.open();

      int numberOfColumns = pdfPrintable.getPdfProperties().getCellProperties().size();
      int numberOfRows = list.getRows().size();

      PdfPTable table = new PdfPTable(numberOfColumns);

      float[] widths = new float[numberOfColumns];


      for (int i = 0; i < widths.length; i++) {
        widths[i] = pdfPrintable.getPdfProperties().getCellProperties().get(i).getWidth();
      }

      table.setWidths(widths);

      /* header */
      for (PdfCellProperties cellProperties : pdfPrintable.getPdfProperties().getCellProperties()) {
        PdfPCell cell = new PdfPCell(new Phrase(cellProperties.getHeadline(), FontFactory
            .getFont(FontFactory.HELVETICA_BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
      }


      /* content */
      int rowIndex = 0;
      for (Row row : list.getRows()) {
        int columnIndex = 0;
        for (String content : row.getContent()) {
          if (content != null) {
            PdfPCell cell = new PdfPCell(
                new Phrase(content, FontFactory.getFont(FontFactory.HELVETICA)));
            cell.setHorizontalAlignment(pdfPrintable.getPdfProperties().getCellProperties().get(columnIndex).getAlignment().getPdfAlign());
            table.addCell(cell);
          } else {
            table.addCell("");
          }
          columnIndex++;
        }
        rowIndex++;
      }

//      /* footer */
//      if (footerFound) {
//        for (PdfTableColumn tableColumn : cols) {
//          table.addCell(tableColumn.getFooter());
//        }
//      }


      document.add(table);

      document.add(new Phrase(" "));

      document.close();
    } catch (DocumentException e) {
      e.printStackTrace();
    }


  }

  @Override
  public void print(java.util.List<ExportList> lists) {
    MergePDFUtil mergePDFUtil = new MergePDFUtil();
    mergePDFUtil.concatPDFs(lists, outputStream);
  }

  private Document setUpDocument(PdfPrintable pdfPrintable) {
    Document document = new Document(PageSize.A4, 20, 20, 60, 50); // left,right,top,bottom

    PdfWriter writer = null;
    try {
      writer = PdfWriter.getInstance(document, outputStream);
      writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));

      HeaderFooter event = pdfPrintable.getPdfProperties().getHeaderFooter();
      writer.setPageEvent(event);
    } catch (DocumentException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }

    return document;
  }

  @Override
  public void setOutputStream(OutputStream outputStream) {
    this.outputStream = outputStream;
  }
}
