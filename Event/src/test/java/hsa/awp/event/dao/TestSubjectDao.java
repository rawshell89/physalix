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
import hsa.awp.event.model.Subject;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link SubjectDao}.
 *
 * @author alex
 */
public class TestSubjectDao extends GenericDaoTest<Subject, SubjectDao, Long> {
  /**
   * Initializes the {@link GenericDaoTest} with a newly created {@link SubjectDao} and an {@link IObjectFactory}.
   */
  public TestSubjectDao() {

    super();
    super.setDao(new SubjectDao());

    super.setIObjectFactory(new IObjectFactory<Subject, Long>() {
      @Override
      public Subject getInstance() {

        return Subject.getInstance(0L);
      }
    });
  }

  @Override
  public void testMerge() {

    List<Subject> subjects = generateAndPersistObjects(2);
    subjects.get(0).setName("Tanja");
    assertEquals(subjects.get(0), getDao().merge(subjects.get(0)));
    assertEquals(2, getDao().findAll().size());
    assertEquals("Tanja", getDao().findById(subjects.get(0).getId()).getName());
    assertEquals(subjects.get(0), getDao().findById(subjects.get(0).getId()));
    assertEquals(subjects.get(1), getDao().findById(subjects.get(1).getId()));
  }
}
