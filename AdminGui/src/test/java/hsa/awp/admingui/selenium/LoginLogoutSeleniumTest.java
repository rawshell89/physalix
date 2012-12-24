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
 * Tests the LoginGui and if the LogoutButton works.
 *
 * @author kai
 */
public class LoginLogoutSeleniumTest extends SeleneseTestCase {
  //TODO: Ignore entfernen


  /* (non-Javadoc)
  * @see com.thoughtworks.selenium.SeleneseTestCase#setUp()
  */
  @Override
  public void setUp() throws Exception {

    setUp("http://localhost:8080/AdminGui",
        "*firefox");
  }

  /**
   * Tests the LoginGui and if the LogoutButton works.
   *
   * @throws Exception
   */
  public void testCreateSubject() throws Exception {

    selenium.open("/AdminGui/login/LoginPage");
    assertTrue(selenium.isTextPresent("Benutzername"));
    assertTrue(selenium.isTextPresent("Password"));
    assertEquals("AWP Anmeldesystem", selenium.getTitle());
    assertTrue(selenium.isTextPresent("AWP Anmeldesystem Admin Login"));
    assertTrue(selenium.isElementPresent("j_username"));
    selenium.type("j_username", "admin");
    assertTrue(selenium.isElementPresent("j_password"));
    selenium.type("j_password", "password");
    selenium.click("//input[@value='Einloggen ']");
    for (int second = 0; ; second++) {
      if (second >= 60) fail("timeout");
      try {
        if (selenium.isElementPresent("link=Logout")) break;
      } catch (Exception e) {
      }
      Thread.sleep(1000);
    }

    assertTrue(selenium.isElementPresent("link=Logout"));
    selenium.click("link=Logout");
    for (int second = 0; ; second++) {
      if (second >= 60) fail("timeout");
      try {
        if (selenium.isElementPresent("j_username")) break;
      } catch (Exception e) {
      }
      Thread.sleep(1000);
    }

    assertTrue(selenium.isElementPresent("j_username"));
    assertTrue(selenium.isTextPresent("Benutzername"));
    assertTrue(selenium.isElementPresent("j_password"));
    verifyTrue(selenium.isTextPresent("Password"));
    assertEquals("AWP Anmeldesystem", selenium.getTitle());
    assertTrue(selenium.isTextPresent("AWP Anmeldesystem Admin Login"));
  }

  public static void main(String args[]) {

    junit.textui.TestRunner.run(suite());
  }

  public static Test suite() {

    return new TestSuite(LoginLogoutSeleniumTest.class);
  }
}
