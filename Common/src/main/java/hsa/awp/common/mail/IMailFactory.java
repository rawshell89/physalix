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
 * Interface for a MailFactory.
 *
 * @author klassm
 */
public interface IMailFactory {
  /**
   * Creates a new mail.
   *
   * @param recipient Recipient of the mail.
   * @param subject   Subject of the mail.
   * @param message   Message content.
   * @param sender    Sender of the mail.
   * @return new mail.
   */
  IMail getInstance(String recipient, String subject, String message, String sender);
}
