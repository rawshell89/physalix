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

package hsa.awp.rule.model;

import hsa.awp.user.model.SingleUser;
import hsa.awp.user.model.StudyCourse;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * JUnit test class for domain model {@link StudyCourseRule}.
 *
 * @author johannes
 */
public class StudyCourseRuleTest extends AbstractStudentRuleTest {
  /**
   * Plain {@link StudyCourseRule}.
   */
  private StudyCourseRule plain;

  /**
   * Initialized {@link StudyCourseRule}. This instance has the identifiers of {@link #course}, {@link #campaign} and
   * {@link #event} set.
   */
  private StudyCourseRule rule;

  /**
   * {@link StudyCourse} instance used for testing.
   */
  private StudyCourse course;

  /**
   * Initializes the test instances and sets up the references.
   */
  @Before
  public void setUp() {

    super.setUp();

    plain = StudyCourseRule.getInstance(0L);
    course = StudyCourse.getInstance("test");
    course.setId(10L);

    student.setStudyCourse(course);

    rule = StudyCourseRule.getInstance(0L);
    rule.setStudyCourse(course.getId());
  }

  /**
   * A correct set of data should be verified as <code>true</code>.
   */
  @Test
  public void testCheckGood() {

    assertTrue(rule.check(student, campaign, event));
  }

  /**
   * A {@link StudyCourseRule} with <code>null</code> references to campaign or event should ignored. Regardless the
   * {@link StudyCourse} has to be validated.
   */
  @Test
  public void testCheckGoodIgnore() {

    plain.setStudyCourse(course.getId());

    // rule parameters campaign and event are null and should be ignored
    assertTrue(plain.check(student, campaign, event));
  }

  /**
   * A different {@link StudyCourse} should be trigger the return value <code>false</code>.
   */
  @Test
  public void testCheckWrong() {

    plain.setStudyCourse(course.getId() + 100);
    assertFalse(plain.check(student, campaign, event));

    rule.setStudyCourse(course.getId() + 100);
    assertFalse(rule.check(student, campaign, event));
  }

  /**
   * A normal {@link SingleUser} should pass the test without any restriction.
   */
  @Test
  public void testCheckWrongInstance() {

    assertTrue(plain.check(SingleUser.getInstance(), campaign, event));
  }

  /**
   * Test method for {@link StudyCourseRule#getInstance()}. Checks that the initial values are correctly set.
   */
  @Test
  public void testGetInstance() {

    assertEquals(0L, plain.getId().longValue());
    assertEquals(0L, plain.getStudyCourse().longValue());
  }
}
