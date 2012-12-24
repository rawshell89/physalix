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

package hsa.awp.admingui.selenium;

import com.thoughtworks.selenium.SeleneseTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests the createCampagneGui.
 *
 * @author kai
 */
public class CreateCampaignSeleniumTest extends SeleneseTestCase {
  //TODO: ignore entfernen

  /**
   * Sets up the link the tests refers to and the web browser to use.
   */
  public void setUp() throws Exception {

    setUp("http://localhost:8080/AdminGui", "*firefox");
  }

  /**
   * Tests the creation of the an campaign and fills the fields with suitable values.
   *
   * @throws Exception
   */
  public void testKampagneerstellen() throws Exception {

    selenium.open("/AdminGui/");
    assertEquals("AWP Anmeldesystem", selenium.getTitle());
    selenium.click("link=Kampagne erstellen");
    selenium.waitForPageToLoad("30000");
    assertEquals("AWP Anmeldesystem", selenium.getTitle());
    assertTrue(selenium.isTextPresent("Beginn des Anmeldezeitraums"));
    assertTrue(selenium.isElementPresent("startShow"));
    assertTrue(selenium.isTextPresent("Ende des Anmeldezeitraums"));
    assertTrue(selenium.isTextPresent("Verfahren"));
    assertTrue(selenium.isTextPresent("Veranstaltungen"));
    assertTrue(selenium.isElementPresent("endShow"));
    assertTrue(selenium.isElementPresent("procedures"));
    assertTrue(selenium.isElementPresent("events"));
    selenium.type("startShow", "12.03.2010");
    selenium.type("endShow", "15.03.2010");
    selenium.type("procedures", "verfahren");
    assertTrue(selenium.isElementPresent("link=Kampagne erstellen"));
  }

  public static void main(String args[]) {

    junit.textui.TestRunner.run(suite());
  }

  public static Test suite() {

    return new TestSuite(CreateCampaignSeleniumTest.class);
  }
}
