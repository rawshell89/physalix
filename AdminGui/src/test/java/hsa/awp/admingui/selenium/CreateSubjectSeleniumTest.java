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
 * Tests the createSubjectGui.
 *
 * @author kai
 */
public class CreateSubjectSeleniumTest extends SeleneseTestCase {
  //TODO: ignore entfernen

  /**
   * Sets up the link the tests refers to and the web browser to use.
   */
  public void setUp() throws Exception {

    setUp("http://localhost:8080/AdminGui", "*firefox");
  }

  /**
   * Some tests which create an subject and fills out the fields with some suitable values.
   *
   * @throws Exception
   */
  public void testUntitled() throws Exception {

    CommonSeleniumFunctions.login(selenium);
    selenium.click("link=Fach erstellen");
    selenium.waitForPageToLoad("30000");
    assertEquals("AWP Anmeldesystem", selenium.getTitle());
    assertTrue(selenium.isElementPresent("name"));
    selenium.type("name", "name");
    assertTrue(selenium.isElementPresent("category"));
    assertTrue(selenium.isElementPresent("description"));
    assertTrue(selenium.isElementPresent("link"));
    verifyTrue(selenium.isTextPresent("Name"));
    assertTrue(selenium.isTextPresent("Kategorie"));
    assertTrue(selenium.isTextPresent("Beschreibung"));
    assertTrue(selenium.isTextPresent("Link"));
    assertTrue(selenium.isElementPresent("link=Fach erstellen"));
    selenium.type("category", "kategorie");
    selenium.type("description", "beschreibung");
    selenium.type("link", "link@link.de");
    assertTrue(selenium.isTextPresent("AWP Anmeldesystem"));
    assertTrue(selenium.isTextPresent("Fach erstellen"));
  }

  public static void main(String args[]) {

    junit.textui.TestRunner.run(suite());
  }

  public static Test suite() {

    return new TestSuite(CreateSubjectSeleniumTest.class);
  }
}

