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
import org.junit.Ignore;

/**
 * Tests the createEventGui.
 *
 * @author kai
 */
@Ignore
public class CreateEventSeleniumTest extends SeleneseTestCase {
  //TODO: ignore entfernen

  /**
   * Sets up the link the tests refers to and the web browser to use.
   */
  public void setUp() throws Exception {

    setUp("http://localhost:8080/AdminGui", "*firefox");
  }

  /**
   * Tests the creation of an Event and fills the fields with suitable values.
   *
   * @throws Exception
   */
  public void testAWPVeranstaltungErstellen() throws Exception {

    selenium.open("/AdminGui/");
    assertEquals("AWP Anmeldesystem", selenium.getTitle());
    selenium.click("link=Veranstaltung erstellen");
    selenium.waitForPageToLoad("30000");
    assertTrue(selenium.isElementPresent("id"));
    selenium.type("id", "13");
    assertTrue(selenium.isElementPresent("maxParticipants"));
    assertTrue(selenium.isElementPresent("teacher"));
    assertTrue(selenium.isElementPresent("exams"));
    assertTrue(selenium.isElementPresent("term"));
    assertTrue(selenium.isElementPresent("link=Veranstaltung erstellen"));
    selenium.type("term", "semester");
    selenium.type("exams", "prüfung");
    selenium.type("teacher", "dozent");
    selenium.type("maxParticipants", "30");
  }

  public static void main(String args[]) {

    junit.textui.TestRunner.run(suite());
  }

  public static Test suite() {

    return new TestSuite(CreateEventSeleniumTest.class);
  }
}
