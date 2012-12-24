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

package hsa.awp.admingui.report.util.formats.pdf;

import hsa.awp.admingui.report.util.Alignment;

public class PdfCellProperties {
  private float width;
  private String headline;

  private Alignment alignment;

  public PdfCellProperties(String headline) {
    this(1f, headline);
  }

  public PdfCellProperties(String headline, Alignment alignment) {
    this(1f, headline, alignment);
  }

  public PdfCellProperties(float width, String headline) {
    this(width, headline, Alignment.LEFT);
  }

  public PdfCellProperties(float width, String headline, Alignment alignment) {
    this.width = width;
    this.headline = headline;
    this.alignment = alignment;
  }

  public Alignment getAlignment() {
    return alignment;
  }

  public void setAlignment(Alignment alignment) {
    this.alignment = alignment;
  }

  public String getHeadline() {
    return headline;
  }

  public float getWidth() {
    return width;
  }
}
