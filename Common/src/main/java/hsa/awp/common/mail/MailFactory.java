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

/**
 * Factory for producing mails.
 *
 * @author klassm
 */
public class MailFactory implements IMailFactory {
  /**
   * {@link MailSource} used for connection configuration.
   */
  private MailSource mailSource;

  private String senderDomain;

  /**
   * Creates an instance of an {@link IMail} object and initializes the specified values.
   *
   * @param recipient the recipient of the mail
   * @param subject   the subject of the mail
   * @param message   the message of the mail
   * @param senderName    the mail address of the sender or a simple username of the senderDomain
   * @return a {@link IMail} instance
   */
  public IMail getInstance(String recipient, String subject, String message, String senderName) {

    String sender = senderName.contains("@") ? senderName : senderName + "@" + senderDomain;

    Mail mail = new Mail(recipient, subject, message, sender);
    mail.setMailSource(mailSource);

    return mail;
  }

  /**
   * Setter for mailSource.
   *
   * @param mailSource the mailSource to set
   */
  public void setMailSource(MailSource mailSource) {

    this.mailSource = mailSource;
  }

  public void setSenderDomain(String senderDomain) {
    this.senderDomain = senderDomain;
  }
}
