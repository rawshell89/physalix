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

package hsa.awp.usergui;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/spring/TestHomePage.xml")
@Ignore
public class TestHomePage {
  private WicketTester tester;

  /**
   * Prepared {@link WicketApplication} which has dependency injection enabled
   */
  @Resource(name = "usergui.testContext")
  private WicketApplication app;

  /**
   * Initializes the {@link WicketTester}.
   */
  @Before
  public void before() {

    tester = new WicketTester(app);
  }

  @Test
  @Transactional
  public void testRenderMyPage() {
    // start and render the test page
    tester.startPage(HomePage.class);

    // assert rendered page class
    tester.assertRenderedPage(HomePage.class);

    // assert rendered panel component
    tester.assertComponent("welcome", WelcomePanel.class);
    tester.assertComponent("Anmelden", Link.class);

    tester.clickLink("Anmelden");
    tester.assertComponent("p1", CampaignListPanel.class);
  }
}
