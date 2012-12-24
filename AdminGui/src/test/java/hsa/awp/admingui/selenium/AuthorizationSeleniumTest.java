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
import org.junit.Ignore;

/**
 * Tests that check authorization related stuff like login.
 *
 * @author lothar
 */
@Ignore
public class AuthorizationSeleniumTest extends SeleneseTestCase {
  @Override
  public void setUp() throws Exception {

    setUp("http://localhost:8080/AdminGui",
        "*firefox");
  }

  /**
   * Test that logs in.
   *
   * @throws Exception I have no idea.
   */
  public void testLogin() throws Exception {

    CommonSeleniumFunctions.login(selenium);
    // TODO: assert something here
  }

  /**
   * Test that logs out.
   *
   * @throws Exception I have no idea.
   */
  public void testLogout() throws Exception {

    CommonSeleniumFunctions.login(selenium);
    CommonSeleniumFunctions.logout(selenium);
    // TODO: assert something here
  }
}
