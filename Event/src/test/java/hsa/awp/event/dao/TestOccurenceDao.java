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

package hsa.awp.event.dao;

import hsa.awp.common.test.GenericDaoTest;
import hsa.awp.common.test.IObjectFactory;
import hsa.awp.event.model.Occurrence;
import hsa.awp.event.model.OccurrenceType;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class TestOccurenceDao extends GenericDaoTest<Occurrence, OccurrenceDao, Long> {
  /**
   * Initializes the {@link GenericDaoTest} with a newly created {@link TimetableDao} and an {@link IObjectFactory}.
   */
  public TestOccurenceDao() {

    super();
    super.setDao(new OccurrenceDao());

    super.setIObjectFactory(new IObjectFactory<Occurrence, Long>() {
      @Override
      public Occurrence getInstance() {

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MINUTE, 1);
        Occurrence o = Occurrence.getInstance(Calendar.getInstance(), endDate, OccurrenceType.SINGLE, 0L);

        return o;
      }
    });
  }

  @Test
  @Override
  public void testMerge() {

    Occurrence occ = getInstance();
    startTransaction();
    getDao().persist(occ);
    commit();

    Calendar startDateNew = Calendar.getInstance();
    startDateNew.add(Calendar.MONTH, 1);

    Calendar endDateNew = Calendar.getInstance();
    endDateNew.add(Calendar.MONTH, 2);

    startTransaction();
    occ.setInterval(startDateNew, endDateNew);
    occ.setType(OccurrenceType.PERIODICAL);
    getDao().merge(occ);
    Occurrence occNew = getDao().findById(occ.getId());
    commit();

    assertEquals(occ.getStartDate(), occNew.getStartDate());
    assertEquals(occ.getEndDate(), occNew.getEndDate());
    assertEquals(occ.getType(), occNew.getType());
  }
}
