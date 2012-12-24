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

import hsa.awp.user.model.StudyCourse;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * JUnit test class for domain model {@link StudyCourseAndTermRule}.
 *
 * @author johannes
 */
public class StudyCourseAndTermRuleTest extends AbstractStudentRuleTest {
  /**
   * {@link StudyCourseAndTermRule} instance under test.
   */
  private StudyCourseAndTermRule rule;

  /**
   * {@link StudyCourse} instance used for testing.
   */
  private StudyCourse course;

  /**
   * Sets up all instances used in this test class.
   */
  @Before
  public void setUp() {

    super.setUp();

    rule = StudyCourseAndTermRule.getInstance(0L);

    course = StudyCourse.getInstance("test");
    course.setId(10L);

    rule.setStudyCourse(course.getId());
    student.setStudyCourse(course);
  }

  /**
   * A Student in the adjusted study course has to have the term restriction. But only the minimal term should be validated.
   */
  @Test
  public void testCheck() {

    student.setTerm(3);
    rule.setMinTerm(3);

    // student too short
    student.setTerm(2);
    assertFalse(rule.check(student, campaign, event));

    // student is correct
    student.setTerm(3);
    assertTrue(rule.check(student, campaign, event));
    student.setTerm(15);
    assertTrue(rule.check(student, campaign, event));
  }

  /**
   * A Student in an other study course has to have no restrictions.
   */
  @Test
  public void testCheckNotInStudyCourse() {

    course.setId(course.getId() + 100);

    rule.setMinTerm(3);

    // student too short but not in critical course
    student.setTerm(2);
    assertTrue(rule.check(student, campaign, event));
  }

  /**
   * Test method for {@link StudyCourseRule#getInstance()}. Checks that the initial values are correctly set.
   */
  @Test
  public void testGetInstance() {

    rule = StudyCourseAndTermRule.getInstance(0L);

    assertEquals(0L, rule.getId().longValue());
    assertEquals(0, rule.getMinTerm());
  }
}
