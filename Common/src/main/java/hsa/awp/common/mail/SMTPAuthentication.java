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

package hsa.awp.common.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Class used for Authenticating with the SMTP server.
 *
 * @author klassm
 */
class SMTPAuthentication extends Authenticator {
  /**
   * Username for login.
   */
  private String username;

  /**
   * Password for login.
   */
  private String password;

  /**
   * Creates a new {@link SMTPAuthentication}.
   *
   * @param username username for login.
   * @param password password for login.
   */
  public SMTPAuthentication(String username, String password) {

    this.username = username;
    this.password = password;
  }

  /**
   * Returns the {@link PasswordAuthentication} for interacting and authenticating with the SMTP server.
   *
   * @return authentication object
   */
  protected PasswordAuthentication getPasswordAuthentication() {

    return new PasswordAuthentication(username, password);
  }
}
