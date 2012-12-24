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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * JUnit test class for domain model {@link TermRule}.
 *
 * @author johannes
 */
public class TermRuleTest extends AbstractStudentRuleTest {
  /**
   * {@link TermRule} instance under test.
   */
  private TermRule rule;

  /**
   * Sets up all instances used in this test class.
   */
  @Before
  public void setUp() {

    super.setUp();

    rule = new TermRule();
  }

  /**
   * When the minimal term is 0 then the maximal term would be the only criteria.
   */
  @Test
  public void testCheckMax() {

    rule.setMaxTerm(3);

    // student too long
    student.setTerm(4);
    assertFalse(rule.check(student, campaign, event));

    // student is correct
    student.setTerm(3);
    assertTrue(rule.check(student, campaign, event));
  }

  /**
   * When the maximal term is 0 then the minimal term would be the only criteria.
   */
  @Test
  public void testCheckMin() {

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
   * When both parameters are initialized, then a student between this limits should pass the verification.
   */
  @Test
  public void testCheckPeriod() {

    rule.setMinTerm(2);
    rule.setMaxTerm(3);

    // student too short
    student.setTerm(1);
    assertFalse(rule.check(student, campaign, event));

    // student too long
    student.setTerm(4);
    assertFalse(rule.check(student, campaign, event));

    // student is correct
    student.setTerm(2);
    assertTrue(rule.check(student, campaign, event));
    student.setTerm(3);
    assertTrue(rule.check(student, campaign, event));
  }

  /**
   * When the minimal term and the maximal term are equal there has to be a restriction to this single term.
   */
  @Test
  public void testCheckStrict() {

    rule.setMinTerm(3);
    rule.setMaxTerm(3);

    // student too short
    student.setTerm(2);
    assertFalse(rule.check(student, campaign, event));

    // student too long
    student.setTerm(4);
    assertFalse(rule.check(student, campaign, event));

    // student is correct
    student.setTerm(3);
    assertTrue(rule.check(student, campaign, event));
  }

  /**
   * Test method for {@link TermRule#getInstance()}. Checks that the initial values are correctly set.
   */
  @Test
  public void testGetInstance() {

    rule = TermRule.getInstance(0L);

    assertEquals(0L, rule.getId().longValue());
    assertEquals(0, rule.getMinTerm());
    assertEquals(0, rule.getMaxTerm());
  }
}
