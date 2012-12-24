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

package hsa.awp.admingui.report.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Class to support Header and Footers.
 *
 * @author Basti
 */
public class HeaderFooter extends PdfPageEventHelper {
  /**
   * Strings for title and subtitle.
   */
  private String title, subtitle;

  /**
   * Constructor without subtitle.
   *
   * @param title title of document
   */
  public HeaderFooter(String title) {

    this(title, null);
  }

  /**
   * Constructor with title and subtitle.
   *
   * @param title    title of document
   * @param subtitle subtitle of document
   */
  public HeaderFooter(String title, String subtitle) {

    this.title = title;
    this.subtitle = subtitle;
  }

  /**
   * this method is called by Pageevent.
   *
   * @param writer   write of the document
   * @param document document it self
   */
  public void onEndPage(PdfWriter writer, Document document) {

    Rectangle rect = writer.getBoxSize("art");
    /* title */
    ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(title, FontFactory.getFont(
        FontFactory.HELVETICA_BOLD, 16)), rect.getLeft(), rect.getTop() + 20, 0);
    /* subtitle */
    ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(subtitle, FontFactory.getFont(
        FontFactory.HELVETICA, 9)), rect.getLeft(), rect.getTop() + 9, 0);
    /* date */

    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    java.util.Date date = new java.util.Date();

    ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase(dateFormat.format(date)), rect
        .getRight(), rect.getTop() + 15, 0);
    /* pagenumber */
    ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(String.format("Seite %d", writer
        .getPageNumber())), (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 25, 0);
  }
}
