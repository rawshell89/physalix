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
 * Tests the createFifoProcedureGui.
 *
 * @author kai
 */
public class CreateFifoProcedureSeleniumTest extends SeleneseTestCase {
  //TODO: ignore entfernen

  /**
   * Sets up the link the tests refers to and the web browser to use.
   */
  public void setUp() throws Exception {

    setUp("http://localhost:8080/AdminGui", "*firefox");
  }

  /**
   * Tests the creation of an FifoProcedure and fills the fields with suitable values.
   *
   * @throws Exception
   */
  public void testDrawProzedureerstellen() throws Exception {

    selenium.click("link=Fifo-Prozedur erstellen");
    selenium.waitForPageToLoad("30000");
    selenium.open("/AdminGui/?wicket:interface=:5:1:::");
    assertEquals("AWP Anmeldesystem", selenium.getTitle());
    assertTrue(selenium.isTextPresent("Fifo-Prozedur erstellen"));
    assertTrue(selenium.isTextPresent("Fifo Name"));
    assertTrue(selenium.isTextPresent("Start Date"));
    assertTrue(selenium.isTextPresent("End Date"));
    assertTrue(selenium.isElementPresent("fifoName"));
    assertTrue(selenium.isElementPresent("startDate:hours"));
    assertTrue(selenium.isElementPresent("startDate:minutes"));
    assertTrue(selenium.isElementPresent("endDate:hours"));
    assertTrue(selenium.isElementPresent("endDate:minutes"));
    selenium.type("fifoName", "fifoname");
    selenium.type("startDate:hours", "13");
    selenium.type("startDate:minutes", "50");
    selenium.type("endDate:hours", "13");
    selenium.type("endDate:minutes", "50");
  }

  public static void main(String args[]) {

    junit.textui.TestRunner.run(suite());
  }

  public static Test suite() {

    return new TestSuite(CreateFifoProcedureSeleniumTest.class);
  }
}
