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

package hsa.awp.common.dao.template;

import hsa.awp.common.model.TemplateDetail;
import org.hibernate.cfg.NotYetImplementedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class TemplateJarDao implements ITemplateDao {

  private String defaultTemplatePath;

  @Override
  public void saveTemplate(String content, TemplateDetail templateDetail) {
    throw new NotYetImplementedException("Will never be implemented");
  }

  @Override
  public String loadTemplate(TemplateDetail templateDetail) {
    return loadTemplateFromPath(generateDefaultPath(templateDetail));
  }

  private String loadTemplateFromPath(String path) {
    InputStream inputstream = getInputStreamFromPath(path);
    return convertToString(inputstream);
  }

  private InputStream getInputStreamFromPath(String path) {
    InputStream inputStream = getClass().getResourceAsStream(path);
    if (inputStream == null) {
      throw new IllegalArgumentException("file not found");
    }
    return inputStream;
  }

  private String convertToString(InputStream inputstream) {
    StringBuilder sb = new StringBuilder();
    try {
      sb = new StringBuilder(Math.max(16, inputstream.available()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    char[] tmp = new char[4096];

    try {
      InputStreamReader reader = new InputStreamReader(inputstream, "UTF-8");
      for (int cnt; (cnt = reader.read(tmp)) > 0; )
        sb.append(tmp, 0, cnt);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        inputstream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return sb.toString();
  }

  private String generateDefaultPath(TemplateDetail templateDetail) {
    return defaultTemplatePath + templateDetail.getTemplateType().getFileName();
  }

  public void setDefaultTemplatePath(String defaultTemplatePath) {
    this.defaultTemplatePath = defaultTemplatePath;
  }
}
