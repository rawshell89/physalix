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

import hsa.awp.campaign.model.Campaign;
import hsa.awp.campaign.model.ConfirmedRegistration;
import hsa.awp.campaign.model.FifoProcedure;
import hsa.awp.campaign.model.Procedure;
import hsa.awp.common.test.GenericDaoTest;
import hsa.awp.common.test.IObjectFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * {@link ConfirmedRegistrationDaoTest}.
 *
 * @author kai
 */
public class ConfirmedRegistrationDaoTest extends GenericDaoTest<ConfirmedRegistration, ConfirmedRegistrationDao, Long> {
  /**
   * Creates a {@link ConfirmedRegistrationDaoTest}.
   */
  public ConfirmedRegistrationDaoTest() {

    super();
    super.setDao(new ConfirmedRegistrationDao());

    super.setIObjectFactory(new IObjectFactory<ConfirmedRegistration, Long>() {
      @Override
      public ConfirmedRegistration getInstance() {

        long id = 100;

        ConfirmedRegistration c = ConfirmedRegistration.getInstance(id++, 0L);
        c.setDate(Calendar.getInstance());
        c.setInitiator(id++);
        c.setParticipant(id++);
        c.setEventId(id++);

        return c;
      }
    });
  }

  /**
   * Test method for {@link hsa.awp.campaign.dao.ConfirmedRegistrationDao#ConfirmedRegistrationDao()}.
   */
  @Test
  public void testConfirmedRegistrationDao() {

    ConfirmedRegistrationDao confDao = new ConfirmedRegistrationDao();
    assertEquals(ConfirmedRegistrationDao.class, confDao.getClass());
  }

  @Test
  public void testCountByProcedure() {

    ProcedureDao procDao = super.initDao(new ProcedureDao());
    Procedure proc = FifoProcedure.getInstance(0L);
    proc.setName("TestFifoProcedure");

    ConfirmedRegistration cr = ConfirmedRegistration.getInstance(100L, 0L);
    cr.setInitiator(1L);
    cr.setParticipant(1L);
    cr.setProcedure(proc);

    super.startTransaction();
    procDao.persist(proc);
    getDao().persist(cr);
    super.commit();

    super.startTransaction();
    assertEquals(1, getDao().countItemsByProcedure(proc));
    super.commit();
  }

  @Test
  public void testCountByProcedureAndMandator() {

    ProcedureDao procDao = super.initDao(new ProcedureDao());
    Procedure proc = FifoProcedure.getInstance(1L);
    proc.setName("TestFifoProcedure");

    ConfirmedRegistration cr = ConfirmedRegistration.getInstance(100L, 1L);
    cr.setInitiator(1L);
    cr.setParticipant(1L);
    cr.setProcedure(proc);

    super.startTransaction();
    procDao.persist(proc);
    getDao().persist(cr);
    super.commit();

    super.startTransaction();
    assertEquals(1, getDao().countItemsByProcedureAndMandator(proc, 1L));
    super.commit();
  }

  /**
   * Test method for {@link hsa.awp.campaign.dao.ConfirmedRegistrationDao#createConfirmedRegistration()}.
   */
  @Test
  public void testCreateConfirmedRegistration() {

    List<ConfirmedRegistration> confList = new ArrayList<ConfirmedRegistration>();
    confList.addAll(generateAndPersistObjects(5));

    assertTrue(confList.containsAll(getDao().findAll()));
  }

  @Test
  public void testFindByCampaign() {

    CampaignDao campaignDao = super.initDao(new CampaignDao());
    Campaign campaign = Campaign.getInstance(0L);

    campaign.setStartShow(Calendar.getInstance());
    campaign.setEndShow(Calendar.getInstance());
    campaign.setName("camp1");


    Long event1 = new Long(111L);
    Long event2 = new Long(222L);

    ConfirmedRegistration confirmedRegistration1 = ConfirmedRegistration.getInstance(event1, 0L);
    confirmedRegistration1.setInitiator(1L);
    confirmedRegistration1.setParticipant(1L);

    ConfirmedRegistration confirmedRegistration2 = ConfirmedRegistration.getInstance(event2, 0L);
    confirmedRegistration2.setInitiator(2L);
    confirmedRegistration2.setParticipant(2L);

    HashSet<Long> eventIds = new HashSet<Long>();
    eventIds.add(event1);

    campaign.setEventIds(eventIds);

    super.startTransaction();
    campaignDao.persist(campaign);
    getDao().persist(confirmedRegistration1);
    getDao().persist(confirmedRegistration2);
    super.commit();

    super.startTransaction();
    assertEquals(1, getDao().findByCampaign(campaign).size());
    assertEquals(confirmedRegistration1, getDao().findByCampaign(campaign).get(0));
    super.commit();
  }

  @Test
  public void testFindByProcedure() {

    ProcedureDao procDao = super.initDao(new ProcedureDao());
    Procedure proc = FifoProcedure.getInstance(0L);
    proc.setName("TestFifoProcedure");

    ConfirmedRegistration cr = ConfirmedRegistration.getInstance(100L, 0L);
    cr.setInitiator(1L);
    cr.setParticipant(1L);
    cr.setProcedure(proc);

    super.startTransaction();
    procDao.persist(proc);
    getDao().persist(cr);
    super.commit();

    super.startTransaction();
    assertEquals(1, getDao().findByProcedure(proc).size());
    super.commit();
  }

  @Override
  public void testMerge() {

    List<ConfirmedRegistration> confirmedRegs = generateAndPersistObjects(2);
    Calendar date = Calendar.getInstance();
    confirmedRegs.get(0).setDate(date);
    assertEquals(confirmedRegs.get(0), getDao().merge(confirmedRegs.get(0)));
    assertEquals(2, getDao().findAll().size());
    assertEquals(date, getDao().findById(confirmedRegs.get(0).getId()).getDate());
    assertEquals(confirmedRegs.get(0), getDao().findById(confirmedRegs.get(0).getId()));
    assertEquals(confirmedRegs.get(1), getDao().findById(confirmedRegs.get(1).getId()));
  }
}
