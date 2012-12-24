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

package hsa.awp.campaign.dao;

import hsa.awp.campaign.model.Campaign;
import hsa.awp.campaign.model.DrawProcedure;
import hsa.awp.campaign.model.FifoProcedure;
import hsa.awp.campaign.model.Procedure;
import hsa.awp.common.test.GenericDaoTest;
import hsa.awp.common.test.IObjectFactory;
import org.junit.Test;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * {@link ProcedureDaoTest}.
 *
 * @author kai
 */
public class ProcedureDaoTest extends GenericDaoTest<Procedure, ProcedureDao, Long> {
  /**
   * Creates a {@link ProcedureDaoTest}.
   */
  public ProcedureDaoTest() {

    super();
    super.setDao(new ProcedureDao());

    super.setIObjectFactory(new IObjectFactory<Procedure, Long>() {
      @Override
      public Procedure getInstance() {

        Procedure prod = Procedure.getInstance(0L);

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(start.getTimeInMillis());

        prod.setInterval(start, end);
        prod.setName("test");
        return prod;
      }
    });
  }

  @Test
  public void findActiveSince() {

    startTransaction();


    // TODO All : try to get this unnecessary
    getDao().removeAll();

    CampaignDao campDao = new CampaignDao();
    super.initDao(campDao);

    Campaign camp = Campaign.getInstance(0L);
    camp.setName("hallowelt");
    campDao.persist(camp);


    Procedure p1 = DrawProcedure.getInstance(0L);

    Calendar p1StartDate = Calendar.getInstance();
    p1StartDate.add(Calendar.HOUR, -1);

    Calendar p1EndDate = Calendar.getInstance();
    p1EndDate.add(Calendar.HOUR, 1);

    while (p1StartDate.compareTo(p1EndDate) >= 0) {
      p1EndDate.add(Calendar.DAY_OF_YEAR, 1);
    }

    p1.setInterval(p1StartDate, p1EndDate);
    p1.setName("Hallowelt");

    p1 = getDao().persist(p1);
    camp.addProcedure(p1);

    commit();

    // --------------
    startTransaction();

    Calendar since = Calendar.getInstance();
    since.add(Calendar.HOUR_OF_DAY, -2);

    List<Procedure> res = getDao().findActiveSince(since);
    assertEquals(1, res.size());

    commit();

    // remove campaign
    startTransaction();
    camp.removeProcedure(p1);
    campDao.merge(camp);
    commit();
  }

  /**
   * Test method for findAll method.
   */
  @Test
  public void testFindAll() {

    startTransaction();
    getDao().removeAll();
    commit();

    List<Procedure> prods = generateAndPersistObjects(5);

    List<Procedure> found = getDao().findAll();

    assertEquals(prods.size(), found.size());

    assertTrue(found.get(0).getId() instanceof Long);
  }

  @Test
  public void testFindUnused() {

    FifoProcedureDao fifoDao = super.initDao(new FifoProcedureDao());
    DrawProcedureDao drawDao = super.initDao(new DrawProcedureDao());
    CampaignDao campaignDao = super.initDao(new CampaignDao());

    List<Procedure> procedure = new LinkedList<Procedure>();
    startTransaction();
    for (int i = 0; i < 5; i++) {
      if (i % 2 == 0) {
        FifoProcedure p = FifoProcedure.getInstance(0L);
        p.setName("proc " + i);
        fifoDao.persist(p);
        procedure.add(p);
      } else {
        DrawProcedure p = DrawProcedure.getInstance(0L);
        p.setName("proc " + i);
        drawDao.persist(p);
        procedure.add(p);
      }
    }
    commit();

    super.startTransaction();
    Campaign c = Campaign.getInstance(0L);
    c.setName("ProcedureDaoTest");
    campaignDao.persist(c);
    super.commit();

    startTransaction();
    assertEquals(procedure.size(), getDao().findAll().size());
    assertEquals(procedure.size(), getDao().findUnused().size());

    c.addProcedure(getDao().findById(procedure.get(1).getId()));

    commit();

    startTransaction();
    assertEquals(procedure.size() - 1, getDao().findUnused().size());
    commit();

    super.startTransaction();
    for (Procedure p : c.getAppliedProcedures()) {
      c.removeProcedure(p);
    }
    campaignDao.remove(c);
    super.commit();
  }

  @Override
  public void testMerge() {

    List<Procedure> prods = generateAndPersistObjects(2);
    prods.get(0).setName("Johanna");
    assertEquals(prods.get(0), getDao().merge(prods.get(0)));
    assertEquals(2, getDao().findAll().size());
    assertEquals("Johanna", getDao().findById(prods.get(0).getId()).getName());
    assertEquals(prods.get(0), getDao().findById(prods.get(0).getId()));
    assertEquals(prods.get(1), getDao().findById(prods.get(1).getId()));
  }

  /**
   * Test method for {@link hsa.awp.campaign.dao.ProcedureDao#ProcedureDao()}.
   */
  @Test
  public void testProcedureDao() {

    assertEquals(ProcedureDao.class, getDao().getClass());
  }
}
