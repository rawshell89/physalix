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
import hsa.awp.common.model.TemplateType;
import org.springframework.core.io.Resource;

import java.io.*;

public class TemplateFileSystemDao implements ITemplateDao {

  private String templatePath;

  private Resource resource;

  @Override
  public void saveTemplate(String content, TemplateDetail templateDetail) {

    try {
      File file = new File(generatePath(templateDetail));
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      out.write(content);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @Override
  public String loadTemplate(TemplateDetail templateDetail) {
    StringBuilder stringBuffer = null;

    try {
      File file = new File(generatePath(templateDetail));
      if (file == null || !file.isFile()) {
        return null;
      }
      BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
      stringBuffer = new StringBuilder();
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        stringBuffer.append(line);
        stringBuffer.append("\n");
      }

      bufferedReader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return stringBuffer.toString();
  }

  private String generatePath(TemplateDetail templateDetail) {

    return this.templatePath + generateFileName(templateDetail);
  }

  private String generateFileName(TemplateDetail templateDetail) {
    Long mandatorId = templateDetail.getMandatorId();
    TemplateType templateType = templateDetail.getTemplateType();
    if (mandatorId == null || mandatorId == 0L || templateType == null) {
      throw new IllegalArgumentException("no Mandator or TemplateType given");
    }

    return mandatorId + "_" + templateType.getFileName();
  }

  public String getTemplatePath() {
    return templatePath;
  }

  public void setTemplatePath(String templatePath) {
    this.templatePath = templatePath;
  }

  public void setResource(Resource resource) {
    this.resource = resource;
    String path = "";
    try {
      path = resource.getURL().getPath();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (path.contains("physalix")) {    //Tomcat environment
      setTemplatePath(path.substring(0, path.lastIndexOf("/") + 1) + "templates/");
    }
  }
}
