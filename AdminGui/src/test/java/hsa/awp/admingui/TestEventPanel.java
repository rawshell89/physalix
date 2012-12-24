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

package hsa.awp.admingui;

import hsa.awp.admingui.edit.event.EventPanel;
import hsa.awp.event.facade.IEventFacade;
import hsa.awp.event.model.Category;
import hsa.awp.event.model.Event;
import hsa.awp.event.model.Subject;
import hsa.awp.event.model.Term;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@Ignore //Can't get AuthenticatedSpringSecuritySession working with this test (NPE from controller call)
public class TestEventPanel extends AbstractSecurityTest {
  /**
   * The {@link WicketTester} used for testing the app.
   */
  private WicketTester tester;

  @Resource(name = "event.facade")
  private IEventFacade eventFacade;

  /**
   * Prepared {@link WicketApplication} which has dependency injection enabled
   */
  @Resource(name = "admingui.testContext")
  private WicketApplication app;

  /**
   * Initializes the {@link WicketTester}.
   */
  @Before
  public void before() {

    tester = new WicketTester(app);
  }

  /**
   * Tests the {@link WicketApplication}.
   */
  @Test
  @Transactional
  public void testEventCreation() {

    login("appadmin", "appadmin");

    Category category = Category.getInstance("Cat", 1L);
    eventFacade.saveCategory(category);

    Term term = Term.getInstance(1L);
    term.setTermDesc("WS");
    eventFacade.saveTerm(term);

    Subject subject = Subject.getInstance(1L);
    category.addSubject(subject);
    subject.setName("English");
    eventFacade.saveSubject(subject);

    tester.startPage(new OnePanelPage(new EventPanel(OnePanelPage.getPanelIdOne())));

    tester.assertComponent("p1:tabbedPanel", AjaxTabbedPanel.class);

    tester.debugComponentTrees();

    tester.assertComponent("p1:tabbedPanel:panel:event.tabGeneral.form", Form.class);

    FormTester form = tester.newFormTester("p1:tabbedPanel:panel:event.tabGeneral.form");
    form.setValue("event.tabGeneral.eventId", "10");
    form.setValue("event.tabGeneral.maxParticipants", "10");
    form.setValue("event.tabGeneral.subject", "English");
    form.select("event.tabGeneral.term", 0);

    tester.executeAjaxEvent("p1:tabbedPanel:panel:event.tabGeneral.form:event.tabGeneral.submit", "onclick");

    Event event = eventFacade.getAllEvents().get(0);
    assertEquals(event.getSubject(), subject);
    assertEquals(event.getEventId(), 10);
    assertEquals(event.getMaxParticipants(), 10);
    assertEquals(event.getTerm(), term);

  }
}
