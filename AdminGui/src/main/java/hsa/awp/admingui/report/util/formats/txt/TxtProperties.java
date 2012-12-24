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

package hsa.awp.admingui.report.util.formats.txt;

import hsa.awp.admingui.report.util.formats.FormatProperties;

import java.util.List;

public class TxtProperties extends FormatProperties {

  private List<TxtCellProperties> cellProperties;

  private String seperator;

  public TxtProperties(List<TxtCellProperties> cellProperties) {
    this(cellProperties, " ");
  }

  public TxtProperties(List<TxtCellProperties> cellProperties, String seperator) {
    this.cellProperties = cellProperties;
    this.seperator = seperator;
  }

  public String getSeperator() {
    return seperator;
  }

  public void setSeperator(String seperator) {
    this.seperator = seperator;
  }

  public List<TxtCellProperties> getCellProperties() {
    return cellProperties;
  }

  public void setCellProperties(List<TxtCellProperties> cellProperties) {
    this.cellProperties = cellProperties;
  }

  @Override
  public String getMimeType() {
    return "text/plain";
  }

  @Override
  public String getFileExtension() {
    return ".txt";
  }


}
