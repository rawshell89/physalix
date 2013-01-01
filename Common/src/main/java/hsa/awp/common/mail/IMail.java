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

import java.io.File;

/**
 * Interface for creating and initializing mails. Contains a method to send the mail to the recipient.
 *
 * @author klassm
 */
public interface IMail {
  /**
   * Adds a file as attachment.
   *
   * @param filename name of the file that shall be added.
   */
  void addAttachment(String filename);

  /**
   * Adds an attachment to the mail.
   *
   * @param file File object of the file that shall be added.
   */
  void addAttachment(File file);

  void addByteArrayAsFileAttachment(String name, byte[] bytes);

  /**
   * Getter for message.
   *
   * @return the message
   */
  String getMessage();

  /**
   * Getter for recipient.
   *
   * @return the recipient
   */
  String getRecipient();

  /**
   * Getter for sender.
   *
   * @return the sender
   */
  String getSender();

  /**
   * Getter for subject.
   *
   * @return the subject
   */
  String getSubject();

  /**
   * Sends the message.
   */
  void send();

  /**
   * Setter for message.
   *
   * @param message the message to set
   */
  void setMessage(String message);

  /**
   * Setter for recipient.
   *
   * @param recipient the recipient to set
   */
  void setRecipient(String recipient);

  /**
   * Setter for sender.
   *
   * @param sender the sender to set
   */
  void setSender(String sender);

  /**
   * Setter for subject.
   *
   * @param subject the subject to set
   */
  void setSubject(String subject);
}
