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

import hsa.awp.campaign.model.ConfirmProcedure;
import hsa.awp.common.test.GenericDaoTest;
import hsa.awp.common.test.IObjectFactory;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link ConfirmProcedureDao}.
 *
 * @author johannes
 */
public class ConfirmProcedureDaoTest extends GenericDaoTest<ConfirmProcedure, ConfirmProcedureDao, Long> {
  /**
   * Creates a {@link ConfirmProcedureDaoTest}.
   */
  public ConfirmProcedureDaoTest() {

    super();
    super.setDao(new ConfirmProcedureDao());

    super.setIObjectFactory(new IObjectFactory<ConfirmProcedure, Long>() {
      private int counter = 0;

      @Override
      public ConfirmProcedure getInstance() {

        ConfirmProcedure draw = ConfirmProcedure.getInstance(0L);
        draw.setName("ConfirmProcedureDaoTest" + counter++);
        return draw;
      }
    });
  }

  @Override
  public void testMerge() {

    ConfirmProcedure p = super.generateAndPersistObjects(1).get(0);

    String name = "new ConfirmProcedure name";
    p.setName(name);

    super.startTransaction();
    getDao().merge(p);
    super.commit();

    ConfirmProcedure merged = getDao().findById(p.getId());

    assertEquals(p, merged);
    assertEquals(name, merged.getName());
    assertEquals(1, getDao().findAll().size());
  }
}
