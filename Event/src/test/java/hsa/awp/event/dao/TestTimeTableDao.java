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
import hsa.awp.event.model.Timetable;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link EventDao}.
 *
 * @author alex
 */
public class TestTimeTableDao extends GenericDaoTest<Timetable, TimetableDao, Long> {
  /**
   * Initializes the {@link GenericDaoTest} with a newly created {@link TimetableDao} and an {@link IObjectFactory}.
   */
  public TestTimeTableDao() {

    super();
    super.setDao(new TimetableDao());

    super.setIObjectFactory(new IObjectFactory<Timetable, Long>() {
      @Override
      public Timetable getInstance() {

        return Timetable.getInstance(0L);
      }
    });
  }

  @Test
  @Override
  public void testMerge() {

    OccurrenceDao occDao = new OccurrenceDao();
    initDao(occDao);

    startTransaction();
    Timetable timetable = getInstance();
    getDao().persist(timetable);
    commit();

    startTransaction();

    Calendar startDate = Calendar.getInstance();
    Calendar endDate = Calendar.getInstance();
    endDate.add(Calendar.MONTH, 1);
    Occurrence occ = Occurrence.getInstance(startDate, endDate, OccurrenceType.SINGLE, 0L);
    occDao.persist(occ);

    timetable.addOccurrence(occ);
    getDao().merge(timetable);

    commit();

    startTransaction();
    Timetable t = getDao().findById(timetable.getId());
    assertEquals(1, t.getOccurrences().size());
    assertEquals(occ, t.getOccurrences().toArray()[0]);

    commit();
  }
}
