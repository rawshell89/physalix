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

import hsa.awp.admingui.report.util.HeaderFooter;
import hsa.awp.admingui.report.util.formats.FormatProperties;

import java.util.List;

public class PdfProperties extends FormatProperties {

  private HeaderFooter headerFooter;

  private List<PdfCellProperties> cellProperties;

  public HeaderFooter getHeaderFooter() {
    return headerFooter;
  }

  public List<PdfCellProperties> getCellProperties() {
    return cellProperties;
  }

  public PdfProperties(HeaderFooter headerFooter, List<PdfCellProperties> properties) {

    this.headerFooter = headerFooter;
    this.cellProperties = properties;
  }

  @Override
  public String getMimeType() {
    return "application/pdf";
  }

  @Override
  public String getFileExtension() {
    return ".pdf";
  }
}

