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

import hsa.awp.admingui.report.printer.TxtPrinter;
import hsa.awp.admingui.report.util.Alignment;
import hsa.awp.admingui.report.util.ExportList;
import hsa.awp.admingui.report.util.Row;
import hsa.awp.admingui.report.util.formats.txt.TxtCellProperties;
import hsa.awp.admingui.report.util.formats.txt.TxtPrintable;
import hsa.awp.admingui.report.util.formats.txt.TxtProperties;
import org.junit.Ignore;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Ignore
public final class TxtPrinterTest {
  /**
   * blocked default constructor.
   */
  private TxtPrinterTest() {

  }

  /**
   * Main method for the test.
   *
   * @param agrs commandline args
   */
  public static void main(String[] agrs) {

    TestTextList testList = new TestTextList();

    List<String> content = new ArrayList<String>();
    content.add("Test");
    content.add("test");
    content.add("test@physalix");
    content.add("Testing");
    content.add("123124");

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

    testList.setRows(rows);

    try {
      FileOutputStream stream = new FileOutputStream("test.txt");
      TxtPrinter printer = new TxtPrinter();
      printer.setOutputStream(stream);
      printer.print(testList);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private static class TestTextList extends ExportList implements TxtPrintable {

    @Override
    public TxtProperties getTxtProperties() {
      TxtCellProperties cellProperties = new TxtCellProperties(15, Alignment.LEFT);

      List<TxtCellProperties> propertiesList = new ArrayList<TxtCellProperties>();
      propertiesList.add(cellProperties);
      propertiesList.add(cellProperties);
      propertiesList.add(cellProperties);
      propertiesList.add(cellProperties);
      propertiesList.add(cellProperties);
      return new TxtProperties(propertiesList, " | ");
    }

    @Override
    public String toString() {
      return "";  //To change body of implemented methods use File | Settings | File Templates.
    }
  }
}
