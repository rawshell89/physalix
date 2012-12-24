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

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link MailSource}.
 *
 * @author johannes
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/config/spring/MailSourceTest.xml")
@Ignore // only for local tests (don't know how to check remote mails with Java)
public class MailSourceTest {

  private static final String RECIPIENT = "test@localhost";

  /**
   * {@link MailSource} which is used for connection configuration.
   */
  @Resource(name = "common.mail.source")
  private MailSource source;

  /**
   * {@link MailFactory} which creates {@link Mail}s.
   */
  @Resource(name = "common.mail.factory")
  private IMailFactory factory;

  /**
   * Tests if a mail can be sent to a recipient.
   */
  @Test
  public void test() {

    // TODO integrate dumpster instance to test remote mails
    // assertEquals("mail.fh-augsburg.de", source.getHost());

    String subject = "MailSource Test";
    String message = "Das ist eine TestMail";
    String sender = "test@physalix";

    IMail mail = factory.getInstance(RECIPIENT, subject, message, sender);
    mail.send();
  }
}
