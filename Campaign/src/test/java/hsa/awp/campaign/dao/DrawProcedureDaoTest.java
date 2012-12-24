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

import hsa.awp.campaign.model.DrawProcedure;
import hsa.awp.common.test.GenericDaoTest;
import hsa.awp.common.test.IObjectFactory;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * {@link DrawProcedureDaoTest}.
 *
 * @author kai
 */
public class DrawProcedureDaoTest extends GenericDaoTest<DrawProcedure, DrawProcedureDao, Long> {
  /**
   * Creates a {@link DrawProcedureDaoTest}.
   */
  public DrawProcedureDaoTest() {

    super();
    super.setDao(new DrawProcedureDao());

    super.setIObjectFactory(new IObjectFactory<DrawProcedure, Long>() {
      @Override
      public DrawProcedure getInstance() {

        DrawProcedure draw = DrawProcedure.getInstance(0L);

        draw.setDrawDate(Calendar.getInstance());
        draw.setInterval(Calendar.getInstance(), Calendar.getInstance());
        draw.setName("testName");
        return draw;
      }
    });
  }

  /**
   * Test method for {@link hsa.awp.campaign.dao.DrawProcedureDao#DrawProcedureDao()}.
   */
  @Test
  public void testDrawProcedureDao() {

    assertEquals(DrawProcedureDao.class, getDao().getClass());
  }

  @Override
  public void testMerge() {

    List<DrawProcedure> drawProds = generateAndPersistObjects(2);
    drawProds.get(0).setName("Meixner");
    assertEquals(drawProds.get(0), getDao().merge(drawProds.get(0)));
    assertEquals(2, getDao().findAll().size());
    assertEquals("Meixner", getDao().findById(drawProds.get(0).getId()).getName());
    assertEquals(drawProds.get(0), getDao().findById(drawProds.get(0).getId()));
    assertEquals(drawProds.get(1), getDao().findById(drawProds.get(1).getId()));
  }
}
