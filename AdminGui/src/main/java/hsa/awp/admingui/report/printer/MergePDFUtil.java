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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import hsa.awp.admingui.report.util.ExportList;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for merging PDF files.
 *
 * @author basti
 */
public class MergePDFUtil {

  private PdfContentByte cb;
  private PdfWriter writer;

  /**
   * Concate pdf from printableList.
   *
   * @param pdfFiles     lists to print
   * @param outputStream outputStream.
   */
  public void concatPDFs(List<ExportList> pdfFiles, OutputStream outputStream) {
    List<InputStream> input = generateInputFromPdfLists(pdfFiles);

    concatPDFs(input, outputStream, false);
  }

  private List<InputStream> generateInputFromPdfLists(List<ExportList> pdfFiles) {
    List<InputStream> input = new LinkedList<InputStream>();

    for (final ExportList list : pdfFiles) {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      PdfPrinter printer = new PdfPrinter();
      printer.setOutputStream(out);
      printer.print(list);
      input.add(new ByteArrayInputStream(out.toByteArray()));
    }
    return input;
  }

  /**
   * merges PDF's.
   *
   * @param streamOfPDFFiles pdf files as inputstreams.
   * @param outputStream     outputstream where the pdf is written.
   * @param paginate         true if page numbers should be displayed.
   */
  public void concatPDFs(List<InputStream> streamOfPDFFiles, OutputStream outputStream, boolean paginate) {
    Document document = new Document();
    try {
      List<PdfReader> readers = createPdfReaders(streamOfPDFFiles);
      int totalPages = countAllPages(readers);

      // Create a writer for the outputstream
      writer = PdfWriter.getInstance(document, outputStream);

      document.open();
      cb = getContentByte(writer);

      // Loop through the PDF files and add to the output.
      for (PdfReader pdfReader : readers) {
        int pageOfCurrentReaderPDF = 0;
        // Create a new page in the target for each source page.
        while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
          int currentPageNumber = 0;

          document.newPage();
          pageOfCurrentReaderPDF++;
          currentPageNumber++;
          PdfImportedPage page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
          cb.addTemplate(page, 0, 0);

          // Code for pagination.
          if (paginate) {
            addPagination(totalPages, currentPageNumber);
          }
        }
        pageOfCurrentReaderPDF = 0;
      }
      outputStream.flush();
      document.close();
      outputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (document.isOpen()) {
        document.close();
      }
      try {
        if (outputStream != null) {
          outputStream.close();
        }
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
  }

  private PdfContentByte getContentByte(PdfWriter writer) {
    return writer.getDirectContent();
  }

  private void addPagination(int totalPages, int currentPageNumber) throws DocumentException, IOException {
    cb.beginText();
    cb.setFontAndSize(getBaseFont(), 9);
    cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + currentPageNumber + " of " + totalPages, 520, 5, 0);
    cb.endText();
  }

  private BaseFont getBaseFont() throws DocumentException, IOException {
    return BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
  }

  private List<PdfReader> createPdfReaders(List<InputStream> streamOfPDFFiles) throws IOException {
    List<PdfReader> readers = new ArrayList<PdfReader>();
    for (InputStream pdf : streamOfPDFFiles) {
      PdfReader pdfReader = new PdfReader(pdf);
      readers.add(pdfReader);
    }
    return readers;
  }

  private int countAllPages(List<PdfReader> readers) {
    int pages = 0;
    for (PdfReader reader : readers) {
      pages += reader.getNumberOfPages();
    }

    return pages;
  }

  public MergePDFUtil() {

  }
}
