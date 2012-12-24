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

import com.thoughtworks.selenium.Selenium;

/**
 * Functions that all Selenium tests might need, like logging in or loggin out.
 *
 * @author lothar
 */
public final class CommonSeleniumFunctions {
  /**
   * username used when loggin in.
   */
  private static String username = "admin";

  /**
   * password used when loggin in.
   */
  private static String password = "password";

  /**
   * Login to the AdminGui.
   *
   * @param selenium The Selenium session
   */
  public static void login(Selenium selenium) {

    selenium.open("/AdminGui");
    selenium.type("j_username", username);
    selenium.type("j_password", password);
    selenium.click("//input[@value='Einloggen ']");
    selenium.waitForPageToLoad("30000");
  }

  /**
   * Logout of the AdminGui.
   *
   * @param selenium The Selenium session
   */
  public static void logout(Selenium selenium) {

    selenium.click("link=Logout");
    selenium.waitForPageToLoad("30000");
  }

  /**
   * Default Constructor, made private.
   */
  private CommonSeleniumFunctions() {

  }
}
