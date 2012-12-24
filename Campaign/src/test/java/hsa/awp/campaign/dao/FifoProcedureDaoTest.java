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

import hsa.awp.campaign.model.FifoProcedure;
import hsa.awp.common.test.GenericDaoTest;
import hsa.awp.common.test.IObjectFactory;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * {@link FifoProcedureDaoTest}.
 *
 * @author kai
 */
public class FifoProcedureDaoTest extends GenericDaoTest<FifoProcedure, FifoProcedureDao, Long> {
  /**
   * Creates a {@link FifoProcedureDaoTest}.
   */
  public FifoProcedureDaoTest() {

    super();
    super.setDao(new FifoProcedureDao());

    super.setIObjectFactory(new IObjectFactory<FifoProcedure, Long>() {
      @Override
      public FifoProcedure getInstance() {

        FifoProcedure c = FifoProcedure.getInstance(0L);
        c.setName("böse Scarlett");
        return c;
      }
    });
  }

  /**
   * Test method for {@link hsa.awp.campaign.dao.FifoProcedureDao#FifoProcedureDao()}.
   */
  @Test
  public void testFifoProcedureDao() {

    assertEquals(FifoProcedureDao.class, getDao().getClass());
  }

  @Override
  public void testMerge() {

    List<FifoProcedure> fifoProds = generateAndPersistObjects(2);
    fifoProds.get(0).setName("Scarlett");
    assertEquals(fifoProds.get(0), getDao().merge(fifoProds.get(0)));
    assertEquals(2, getDao().findAll().size());
    assertEquals("Scarlett", getDao().findById(fifoProds.get(0).getId()).getName());
    assertEquals(fifoProds.get(0), getDao().findById(fifoProds.get(0).getId()));
    assertEquals(fifoProds.get(1), getDao().findById(fifoProds.get(1).getId()));
  }
}
