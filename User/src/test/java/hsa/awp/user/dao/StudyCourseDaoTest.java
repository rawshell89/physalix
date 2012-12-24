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

package hsa.awp.user.dao;

import hsa.awp.common.test.GenericDaoTest;
import hsa.awp.common.test.IObjectFactory;
import hsa.awp.user.model.StudyCourse;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for {@link StudyCourseDao}.
 *
 * @author johannes
 */
public class StudyCourseDaoTest extends GenericDaoTest<StudyCourse, StudyCourseDao, Long> {
  /**
   * Initializes the GenericDaoTest with a {@link StudyCourseDao} instance and an in-line {@link IObjectFactory}.
   */
  public StudyCourseDaoTest() {

    super();
    super.setDao(new StudyCourseDao());

    super.setIObjectFactory(new IObjectFactory<StudyCourse, Long>() {
      private int counter = 0;

      @Override
      public StudyCourse getInstance() {

        return StudyCourse.getInstance("StudyCourseDaoTest" + counter++);
      }
    });
  }

  /**
   * Test method for {@link StudyCourseDao#findByName(String)}. Creates some {@link StudyCourse}s and tries to find them using
   * their names.
   */
  @Test
  public void testFindByName() {

    List<StudyCourse> courses = generateAndPersistObjects(2);

    StudyCourse course1 = courses.get(0);
    StudyCourse course2 = courses.get(1);

    assertEquals(course1, getDao().findByName(course1.getName()));
    assertEquals(course2, getDao().findByName(course2.getName()));

    String name = "TestCourse";
    StudyCourse course3 = StudyCourse.getInstance(name);

    super.startTransaction();
    getDao().persist(course3);
    super.commit();

    StudyCourse found = getDao().findByName(name);

    assertEquals(course3, found);
    assertEquals(name, found.getName());
  }

  /**
   * Test method for {@link StudyCourseDao#findByName(String)}.
   */
  @Test
  public void testFindByNameNonExistend() {

    assertNull(getDao().findByName("NonExistingStudyCourse416473137"));
  }

  /**
   * Test method for {@link StudyCourseDao#findByName(String)}. Tries to throw a {@link IllegalArgumentException} by searching
   * with <code>null</code>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFindByNameNull() {

    getDao().findByName(null);
  }

  /**
   * Test method for {@link StudyCourseDao#getInstance(String)}.
   */
  @Test
  public void testGetInstance() {

    String name = "TestCourse";
    StudyCourse s = StudyCourse.getInstance(name);

    // check values
    assertEquals(StudyCourse.class, s.getClass());
    assertEquals(0L, s.getId().longValue());
    assertEquals(name, s.getName());
    assertEquals("", s.getProgram());
  }

  @Test
  @Override
  public void testMerge() {
    // create StudyCourse
    List<StudyCourse> courses = generateAndPersistObjects(1);
    StudyCourse s = courses.get(0);

    // values to changes
    String name = "NewName";
    String program = "NewProgram";

    // change some values
    s.setName(name);
    s.setProgram(program);

    // merge changes
    super.startTransaction();
    StudyCourse merged = getDao().merge(s);
    super.commit();

    // check values
    assertEquals(s, merged);
    assertTrue(merged.getId() != 0L);
    assertEquals(name, merged.getName());
    assertEquals(program, s.getProgram());
  }
}
