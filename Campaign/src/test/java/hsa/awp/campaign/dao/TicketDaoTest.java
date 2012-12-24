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

/**
 *
 */
package hsa.awp.campaign.dao;

import hsa.awp.campaign.model.ConfirmedRegistration;
import hsa.awp.campaign.model.Ticket;
import hsa.awp.common.test.GenericDaoTest;
import hsa.awp.common.test.IObjectFactory;
import org.junit.Test;

/**
 * {@link TicketDaoTest}.
 *
 * @author kai
 * @author rico
 */
public class TicketDaoTest extends GenericDaoTest<Ticket, TicketDao, Long> {
//    private IConfirmedRegistrationDao confDao;
//    
//    private EntityManager entityManager;

  /**
   * Creates a {@link TicketDaoTest}.
   */
  public TicketDaoTest() {

    super.setDao(new TicketDao());
    super.setIObjectFactory(new IObjectFactory<Ticket, Long>() {
      private long counter = 0;

      @Override
      public Ticket getInstance() {

        ConfirmedRegistration cr = ConfirmedRegistration.getInstance(counter++, 0L);
        cr.setInitiator(counter++);
        cr.setParticipant(counter++);
        return cr;
      }
    });
  }

  @Test
  public void testMerge() {

  }

//    @After
//    public void teardown() {
//
//        if (entityManager.getTransaction().isActive()) {
//            entityManager.getTransaction().rollback();
//        }
//    }

//    @Test
//    public void testFindAll() {
//
//        entityManager.getTransaction().begin();
//        
//        confDao.persist(createRandomConfirmedRegistration());
//        confDao.persist(createRandomConfirmedRegistration());
//        
//        entityManager.getTransaction().commit();
//        
//        entityManager.getTransaction().begin();
//        
//        assertEquals(2, ticketDao.findAll().size());
//        
//        entityManager.getTransaction().commit();
//    }

//    @Test
//    public void testFindAllAnzResults() {
//
//        entityManager.getTransaction().begin();
//        
//        for (int i = 0; i < 25; i++) {
//            confDao.persist(createRandomConfirmedRegistration());
//        }
//        
//        entityManager.getTransaction().commit();
//        
//        entityManager.getTransaction().begin();
//        
//        assertEquals(10, ticketDao.findAll(5, 10).size());
//        assertEquals(5, ticketDao.findAll(20, 5).size());
//        
//        entityManager.getTransaction().commit();
//    }

//    private ConfirmedRegistration createRandomConfirmedRegistration() {
//
//        Random r = new Random();
//        ConfirmedRegistration c = ConfirmedRegistration.getInstance(r.nextLong());
//        c.setInitiator(r.nextLong());
//        c.setParticipant(r.nextLong());
//        
//        return c;
//    }
}
