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

import hsa.awp.admingui.report.printer.CsvPrinter;
import hsa.awp.admingui.report.util.ExportList;
import hsa.awp.admingui.report.util.Row;
import hsa.awp.admingui.report.util.formats.csv.CsvPrintable;
import hsa.awp.admingui.report.util.formats.csv.CsvProperties;
import org.junit.Ignore;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Ignore
public final class CsvPrinterTest {
  /**
   * blocked default constructor.
   */
  private CsvPrinterTest() {

  }

  /**
   * Main method for the test.
   *
   * @param agrs commandline args
   */
  public static void main(String[] agrs) {

    TestCsvList testList = new TestCsvList();

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
      FileOutputStream stream = new FileOutputStream("test.csv");
      CsvPrinter printer = new CsvPrinter();
      printer.setOutputStream(stream);
      printer.print(testList);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private static class TestCsvList extends ExportList implements CsvPrintable {


    @Override
    public CsvProperties getCsvProperties() {
      return new CsvProperties();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String toString() {
      return "";  //To change body of implemented methods use File | Settings | File Templates.
    }
  }
}
