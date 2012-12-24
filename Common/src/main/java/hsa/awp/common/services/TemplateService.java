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

package hsa.awp.common.services;

import hsa.awp.common.dao.template.ITemplateDao;
import hsa.awp.common.model.TemplateDetail;
import hsa.awp.common.model.TemplateType;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class TemplateService {

  private VelocityEngine velocityEngine;
  private VelocityEngine jarVelocityEngine;

  private ITemplateDao templateFileSystemDao;
  private ITemplateDao templateJarDao;

  public TemplateService() {
    jarVelocityEngine = new VelocityEngine();
    jarVelocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
    jarVelocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
    jarVelocityEngine.init();
  }

  public Template loadVelocityTemplate(TemplateDetail templateDetail) {
    try {
      return velocityEngine.getTemplate(generateFileName(templateDetail));
    } catch (ResourceNotFoundException e) {
      return jarVelocityEngine.getTemplate("templates/" + templateDetail.getTemplateType().getFileName());
    }
  }

  public void saveTemplate(String content, TemplateDetail templateDetail) {
    templateFileSystemDao.saveTemplate(content, templateDetail);
  }

  public String loadTemplate(TemplateDetail templateDetail) {

    String content = templateFileSystemDao.loadTemplate(templateDetail);
    if (content == null || "".equals(content)) {
      content = templateJarDao.loadTemplate(templateDetail);
      templateFileSystemDao.saveTemplate(content, templateDetail);
    }
    return content;
  }

  public String loadDefaultTemplate(TemplateDetail templateDetail) {
    return templateJarDao.loadTemplate(templateDetail);
  }

  private String generateFileName(TemplateDetail templateDetail) {
    Long mandatorId = templateDetail.getMandatorId();
    TemplateType templateType = templateDetail.getTemplateType();
    if (mandatorId == null || mandatorId == 0L || templateType == null) {
      throw new IllegalArgumentException("no Mandator or TemplateType given");
    }

    return mandatorId + "_" + templateType.getFileName();
  }

  public void setVelocityEngine(VelocityEngine velocityEngine) {
    this.velocityEngine = velocityEngine;
  }

  public void setTemplateFileSystemDao(ITemplateDao templateFileSystemDao) {
    this.templateFileSystemDao = templateFileSystemDao;
  }

  public void setTemplateJarDao(ITemplateDao templateJarDao) {
    this.templateJarDao = templateJarDao;
  }
}
