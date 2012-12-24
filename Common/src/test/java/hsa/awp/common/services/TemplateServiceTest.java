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


import hsa.awp.common.dao.template.TemplateFileSystemDao;
import hsa.awp.common.dao.template.TemplateJarDao;
import hsa.awp.common.model.TemplateDetail;
import hsa.awp.common.model.TemplateType;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.StringWriter;

import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/test-config.xml")
@Ignore
//Don't run on Server! In order to pass test the Directory ~/physalix/templates/ must be present on local system.
public class TemplateServiceTest {

  @Resource(name = "common.template.service")
  private TemplateService templateService;

  @Resource(name = "common.template.dao.jar")
  private TemplateJarDao templateJarDao;

  @Resource(name = "common.template.dao.filesystem")
  private TemplateFileSystemDao templateFileSystemDao;

  private String path;


  @Before
  public void setUp() throws Exception {
    path = System.getProperty("user.home") + "/physalix/templates/";
  }

  @Test
  public void testShouldLoadTemplateStringFromDisk() {

    TemplateDetail templateDetail = TemplateDetail.getInstance(1L, TemplateType.FIFO);

    templateFileSystemDao.setTemplatePath(path);

    String template = templateService.loadTemplate(templateDetail);

    assertTrue(template.contains("<p>haben Sie sich in der Phase \"$procedure\"</p>"));
  }

  @Test
  public void testTemplateMerging() {


    templateService.setVelocityEngine(setUpVelocityEngine());

    TemplateDetail templateDetail = TemplateDetail.getInstance(1L, TemplateType.FIFO);
    Template template = templateService.loadVelocityTemplate(templateDetail);

    String mailContent = mergeTemplate(template);

    assertTrue(mailContent.contains("<p>Sehr geehrte(r) Max Mustermann,</p>"));
  }

  private String mergeTemplate(Template template) {
    VelocityContext context = new VelocityContext();

    context.put("name", "Max Mustermann");
    context.put("eventlist", "English for Runnaways");
    context.put("campaign", "camp");
    context.put("procedure", "proc");

    StringWriter writer = new StringWriter();
    template.merge(context, writer);

    return writer.toString();
  }

  @Test
  public void testTemplateSaving() {

    String templateContent = "name: $name \n" +
        "eventlist: $eventlist \n" +
        "campaign: $campaign \n" +
        "procedure: $procedure \n";

    templateFileSystemDao.setTemplatePath(path);
    templateService.setVelocityEngine(setUpVelocityEngine());

    TemplateDetail templateDetail = TemplateDetail.getInstance(123L, TemplateType.FIFO);

    templateService.saveTemplate(templateContent, templateDetail);

    String template = mergeTemplate(templateService.loadVelocityTemplate(templateDetail));

    assertTrue(template.contains("name: Max Mustermann"));

  }

  @Test
  public void testLoadDefaultIfTemplateIsNonexisting() {
    templateFileSystemDao.setTemplatePath(path);

    TemplateDetail templateDetail = TemplateDetail.getInstance(432L, TemplateType.FIFO);
    String s = templateService.loadTemplate(templateDetail);

    assertTrue(s.contains("<p>haben Sie sich in der Phase \"$procedure\"</p>"));
  }

  private VelocityEngine setUpVelocityEngine() {

    VelocityEngine velocityEngine = new VelocityEngine();
    velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
    velocityEngine.setProperty("file.resource.loader.class", FileResourceLoader.class.getName());
    velocityEngine.setProperty("file.resource.loader.path", path);
    velocityEngine.init();

    return velocityEngine;
  }


}
