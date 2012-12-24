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

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.junit.Ignore;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Simple test using the WicketTester with spring context.
 */
@Ignore //Can't get AuthenticatedSpringSecuritySession working with this test (NPE from controller call)
public class TestHomePage extends AbstractSecurityTest {


  /**
   * Tests the {@link WicketApplication}.
   */
  @Test
  public void testRenderMyPage() {
    login("sysadmin", "sysadmin");

    tester.startPage(HomePage.class);

    tester.assertRenderedPage(HomePage.class);

    tester.assertComponent("welcome", WelcomePanel.class);
  }

  @Test
  public void testHomePageLinkStructureForSysAdmin() {

    login("sysadmin", "sysadmin");
    tester.startPage(HomePage.class);

    tester.assertComponent("userManagement", Link.class);

    tester.assertInvisible("createCategory");
    tester.assertInvisible("createSubject");
    tester.assertInvisible("subjectList");
    tester.assertInvisible("createTerm");
    tester.assertInvisible("createEvent");
    tester.assertInvisible("eventList");
    tester.assertInvisible("createDrawProcedure");
    tester.assertInvisible("createFifoProcedure");
    tester.assertInvisible("procView");
    tester.assertInvisible("createCampaign");
    tester.assertInvisible("campaignList");
    tester.assertInvisible("rulePanel");
  }

  @Test
  public void testTimestamp() {
    login("sysadmin", "sysadmin");
    Date now = new Date();
    tester.startPage(HomePage.class);
    tester.assertComponent("timestamp", Label.class);
    Component timestampLabel = tester.getComponentFromLastRenderedPage("timestamp");
    String timestamp = (String) timestampLabel.getDefaultModelObject();
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    assertEquals(timestamp, dateFormat.format(now));
  }


  @Test
  public void testPageShouldReferToLoginForRoleAnonymous() {

    tester.startPage(HomePage.class);
    tester.assertRenderedPage(LoginPage.class);
  }
}
