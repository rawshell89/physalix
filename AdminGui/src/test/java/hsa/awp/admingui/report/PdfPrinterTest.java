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

package hsa.awp.admingui.report;

import hsa.awp.admingui.report.printer.PdfPrinter;
import hsa.awp.admingui.report.util.ExportList;
import hsa.awp.admingui.report.util.HeaderFooter;
import hsa.awp.admingui.report.util.Row;
import hsa.awp.admingui.report.util.formats.pdf.PdfCellProperties;
import hsa.awp.admingui.report.util.formats.pdf.PdfPrintable;
import hsa.awp.admingui.report.util.formats.pdf.PdfProperties;
import org.junit.Ignore;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Ignore
public final class PdfPrinterTest {
  /**
   * blocked default constructor.
   */
  private PdfPrinterTest() {

  }

  /**
   * Main method for the test.
   *
   * @param agrs commandline args
   */
  public static void main(String[] agrs) {

    TestPdfList testList = new TestPdfList();

    List<String> content = new ArrayList<String>();
    content.add("Test User");
    content.add("test-usr");
    content.add("test-usr@physalix");
    content.add("123456");
    content.add("TS 1");

    List<Row> rows = new ArrayList<Row>();
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));
    rows.add(new Row(content));

    testList.setRows(rows);

    try {
      FileOutputStream stream = new FileOutputStream("test.pdf");
      PdfPrinter printer = new PdfPrinter();
      printer.setOutputStream(stream);
      printer.print(testList);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private static class TestPdfList extends ExportList implements PdfPrintable {


    @Override
    public PdfProperties getPdfProperties() {
      List<PdfCellProperties> propertiesList = new ArrayList<PdfCellProperties>();

      propertiesList.add(new PdfCellProperties(1, "Name"));
      propertiesList.add(new PdfCellProperties(1, "RZ"));
      propertiesList.add(new PdfCellProperties(3, "Mail"));
      propertiesList.add(new PdfCellProperties(1, "Mat.-Nr."));
      propertiesList.add(new PdfCellProperties(0.7f, "Sem"));

      PdfProperties properties = new PdfProperties(new HeaderFooter("Testlist", "nur für Testzwecke"), propertiesList);

      return properties;
    }

    @Override
    public String toString() {
      return "";  //To change body of implemented methods use File | Settings | File Templates.
    }
  }
}
