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

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/config/spring/MailStressTest.xml")
public class MailStressTest {

  @Resource(name = "common.mail.factory")
  private IMailFactory mailFactory;

  private List<String> recipients = new ArrayList<String>();

  private String subject = "Physalix MailStressTest";

  private String message = "this mail has no relevance!";

  private String sender = "mailstress";

  private int sent = 0;

  private int amount = 50;

  private ExecutorService service;

  private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();

  {
    recipients.add("stress1@localhost");
    recipients.add("stress2@localhost");
    recipients.add("stress3@localhost");
    recipients.add("stress4@localhost");
  }

  @Before
  public void setUp() {
    service = new ThreadPoolExecutor(50, 50, 10, TimeUnit.SECONDS, queue);
  }

  @Test
  public void test() {

    int size = recipients.size();

    for (int i = 1; i <= amount; i++) {
      long start = System.currentTimeMillis();

      IMail mail = mailFactory.getInstance(recipients.get(i % size), subject, message, sender);
      mail.send();

      long end = System.currentTimeMillis();

      System.out.println(i + ". mail sent time : " + (end - start));

      if (i % (amount / 10) == 0) {
        int percent = i * 100 / amount;
        System.out.println("--------------------------------------------------");
        System.out.println(" > " + percent + "% completed : " + i + " mail sent");
      }
    }
  }

  @Test
  public void testSingle() {

    for (int i = 0; i < 5; i++) {
      IMail mail = mailFactory.getInstance(recipients.get(0), subject, message, sender);
      mail.send();
    }
  }

  @Test
  public void testThreaded() throws InterruptedException {

    for (int i = 1; i <= amount; i++) {
      service.execute(new Runnable() {
        @Override
        public void run() {

          long start = System.currentTimeMillis();

          IMail mail = mailFactory.getInstance(recipients.get(sent % recipients.size()), subject, message, sender);
          mail.send();
          inc();

          long end = System.currentTimeMillis();

          System.out.println(sent + ". mail sent time : " + (end - start));

          if (sent % (amount / 10) == 0) {
            int percent = sent * 100 / amount;
            System.out.println("--------------------------------------------------");
            System.out.println(" > " + percent + "% completed : " + sent + " mail sent");
          }
        }
      });
    }

    System.out.println("Tasks in Queue : " + queue.size());
    while (queue.size() != 0) {
      service.awaitTermination(1, TimeUnit.SECONDS);
      System.out.println("Tasks in Queue : " + queue.size());
    }
    service.shutdown();
    service.awaitTermination(1, TimeUnit.MINUTES);
    System.out.println("Shutdown complete.");
  }

  private synchronized void inc() {

    sent++;
  }
}
