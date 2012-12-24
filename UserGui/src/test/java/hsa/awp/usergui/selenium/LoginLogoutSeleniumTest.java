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

package hsa.awp.usergui.selenium;

import com.thoughtworks.selenium.SeleneseTestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.junit.Ignore;

/**
 * Tests the LoginPage in the UserGui.
 *
 * @author kai
 */
@Ignore
public class LoginLogoutSeleniumTest extends SeleneseTestCase {
  //TODO: ignore entfernen

  /**
   * Sets up the link the tests refers to and the web browser to use.
   */
  public void setUp() throws Exception {

    setUp("http://localhost/UserGui", "*firefox");
  }

  /**
   * Tests the login of the userGui with either the admin or the student.
   *
   * @throws Exception
   */
  public void testAWPVeranstaltungErstellen() throws Exception {

    selenium.open("/UserGui/login/LoginPage");

//		Tests the admin login.

    assertTrue(selenium.isTextPresent("Login"));
    assertTrue(selenium.isTextPresent("Benutzername"));
    assertTrue(selenium.isTextPresent("Password"));
    assertTrue(selenium.isTextPresent("AWP Anmeldesystem SingleUser Login"));
    assertEquals("AWP Anmeldesystem", selenium.getTitle());
    assertTrue(selenium.isElementPresent("j_username"));
    assertTrue(selenium.isElementPresent("j_password"));
    selenium.type("j_username", "admin");
    selenium.type("j_password", "password");
    selenium.click("//input[@value='Einloggen ']");
    selenium.waitForPageToLoad("30000");
    assertTrue(selenium.isElementPresent("link=Logout"));
    selenium.click("link=Logout");
    selenium.waitForPageToLoad("30000");

//		Tests the student login.

    assertTrue(selenium.isTextPresent("Benutzername"));
    assertTrue(selenium.isTextPresent("Password"));
    assertTrue(selenium.isElementPresent("j_username"));
    assertTrue(selenium.isElementPresent("j_password"));
    selenium.type("j_username", "student");
    selenium.type("j_password", "password");
    selenium.click("//input[@value='Einloggen ']");
    selenium.waitForPageToLoad("30000");
    assertTrue(selenium.isElementPresent("link=Logout"));
    selenium.click("link=Logout");
    selenium.waitForPageToLoad("30000");

//		Tests if the logout was correct.

    assertTrue(selenium.isTextPresent("Benutzername"));
    assertTrue(selenium.isTextPresent("Password"));
    assertTrue(selenium.isElementPresent("j_username"));
    assertTrue(selenium.isElementPresent("j_password"));
  }

  public static void main(String args[]) {

    junit.textui.TestRunner.run(suite());
  }

  public static Test suite() {

    return new TestSuite(LoginLogoutSeleniumTest.class);
  }
}
