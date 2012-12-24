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

import hsa.awp.admingui.report.util.Alignment;
import hsa.awp.admingui.report.util.ExportList;
import hsa.awp.admingui.report.util.Row;
import hsa.awp.admingui.report.util.formats.txt.TxtCellProperties;
import hsa.awp.admingui.report.util.formats.txt.TxtPrintable;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;


public class TxtPrinter implements Printer {

  protected PrintWriter printer;

  @Override
  public void print(ExportList list) {

    if (printer == null) {
      throw new IllegalArgumentException("OutputStream undefined, please use setOutputStream()");
    }

    if (!(list instanceof TxtPrintable)) {
      throw new IllegalArgumentException("TxtPrinter can only print TxtPrintables");
    }

    printList(list);
    printer.flush();
    printer.close();

  }

  private void printList(ExportList list) {
    TxtPrintable txtPrintable = (TxtPrintable) list;

    for (Row row : list.getRows()) {
      StringBuffer line = new StringBuffer();
      int i = 0;
      for (String s : row.getContent()) {

        TxtCellProperties properties = txtPrintable.getTxtProperties().getCellProperties().get(i);
        line.append(fillStringToSize(s, properties.getWidth(), properties.getAlignment()) + txtPrintable.getTxtProperties().getSeperator());
        i++;
      }
      printer.write(line.toString() + "\r\n");
    }
  }

  @Override
  public void print(List<ExportList> lists) {

    for (ExportList list: lists) {
      printList(list);
    }

    printer.flush();
    printer.close();

  }

  @Override
  public void setOutputStream(OutputStream outputStream) {
    this.printer = new PrintWriter(outputStream);
  }

  private String fillStringToSize(String s, int size, Alignment alignment) {

    while (s.length() < size) {
      switch (alignment) {
        case RIGHT:
          s = " " + s;
          break;
        case LEFT:
          s += " ";
          break;
        default:
          throw new IllegalArgumentException("wrong textAlign given");
      }
    }

    return s;
  }
}
