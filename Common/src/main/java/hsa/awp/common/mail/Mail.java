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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

/**
 * Class representing a mail.
 *
 * @author klassm
 * @author johannes
 */
public class Mail implements IMail {
  /**
   * Logger for this class.
   */
  private Logger log = LoggerFactory.getLogger(getClass());

  /**
   * Recipient of the mail.
   */
  private String recipient;

  /**
   * Subject of the mail.
   */
  private String subject;

  /**
   * Message content of the mail.
   */
  private String message;

  /**
   * Sender of the mail.
   */
  private String sender;

  /**
   * Source which holds the connection configuration.
   */
  private MailSource mailSource;

  /**
   * Collection containing all attachments as {@link MimeBodyPart}.
   */
  private Collection<MimeBodyPart> attachments;

  /**
   * Constructor for creating a Mail. Attachments can be set via addAttachment().
   *
   * @param recipient Recipient of the mail.
   * @param subject   Subject of the mail.
   * @param message   Message content.
   * @param sender    Sender of the mail.
   */
  public Mail(String recipient, String subject, String message, String sender) {

    setRecipient(recipient);
    setSubject(subject);
    setMessage(message);
    setSender(sender);

    attachments = new LinkedList<MimeBodyPart>();
  }

  /* (non-Javadoc)
  * @see hsa.awp.common.mail.IMail#setRecipient(java.lang.String)
  */
  @Override
  public void setRecipient(String recipient) {

    this.recipient = recipient;
  }

  /* (non-Javadoc)
  * @see hsa.awp.common.mail.IMail#setSubject(java.lang.String)
  */
  @Override
  public void setSubject(String subject) {

    this.subject = subject;
  }

  /* (non-Javadoc)
  * @see hsa.awp.common.mail.IMail#setMessage(java.lang.String)
  */
  @Override
  public void setMessage(String message) {
//        message = StringEscapeUtils.escapeHtml(message);
    this.message = message;
  }

  /* (non-Javadoc)
  * @see hsa.awp.common.mail.IMail#setSender(java.lang.String)
  */
  @Override
  public void setSender(String sender) {

    this.sender = sender;
  }

  /* (non-Javadoc)
  * @see hsa.awp.common.mail.IMail#addAttachment(java.lang.String)
  */
  @Override
  public void addAttachment(String filename) {

    addAttachment(new File(filename));
  }

  /* (non-Javadoc)
  * @see hsa.awp.common.mail.IMail#addAttachment(java.io.File)
  */
  @Override
  public void addAttachment(File file) {

    if (!file.exists()) {
      throw new IllegalArgumentException("file not found");
    }

    FileDataSource filesource = new FileDataSource(file);
    addAttachment(file.getName(), filesource);
  }

  @Override
  public void addByteArrayAsFileAttachment(String name, byte[] bytes) {
    ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(bytes, "text/xml");
    addAttachment(name, byteArrayDataSource);
  }

  private void addAttachment(String name, DataSource dataSource) {
    try {
      MimeBodyPart part = new MimeBodyPart();
      part.setDataHandler(new DataHandler(dataSource));
      part.setFileName(name);
      attachments.add(part);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  /* (non-Javadoc)
  * @see hsa.awp.common.mail.IMail#getMessage()
  */
  @Override
  public String getMessage() {

    return message;
  }

  /* (non-Javadoc)
  * @see hsa.awp.common.mail.IMail#getRecipient()
  */
  @Override
  public String getRecipient() {

    return recipient;
  }

  /* (non-Javadoc)
  * @see hsa.awp.common.mail.IMail#getSender()
  */
  @Override
  public String getSender() {

    return sender;
  }

  /* (non-Javadoc)
  * @see hsa.awp.common.mail.IMail#getSubject()
  */
  @Override
  public String getSubject() {

    return subject;
  }

  /* (non-Javadoc)
  * @see hsa.awp.common.mail.IMail#send()
  */
  @Override
  public void send() {

    try {


      InternetAddress addressFrom = new InternetAddress(sender);
      InternetAddress addressTo = new InternetAddress(recipient);

      MimeMultipart content = new MimeMultipart();

      MimeBodyPart text = new MimeBodyPart();
      text.setContent(message, "text/html");
      content.addBodyPart(text);

      for (MimeBodyPart part : attachments) {
        content.addBodyPart(part);
      }

      Session session = mailSource.getSession();
      Message msg = new MimeMessage(session);
      msg.setFrom(addressFrom);
      msg.setSentDate(new Date());
      msg.setRecipient(Message.RecipientType.TO, addressTo);
      msg.setSubject(subject);
      msg.setContent(content);

      if (log.isDebugEnabled()) {
        log.debug("Sending mail [" + msg + "] to '" + recipient + "'");
      }
      Transport.send(msg);
    } catch (MessagingException e) {
      throw new MailSendException("Sending the mail failed", e);
    }
  }

  /**
   * Returns mailSource.
   *
   * @return the mailSource
   */
  public MailSource getMailSource() {

    return mailSource;
  }

  /**
   * Setter for mailSource.
   *
   * @param mailSource the mailSource to set
   */
  public void setMailSource(MailSource mailSource) {

    this.mailSource = mailSource;
  }
}
