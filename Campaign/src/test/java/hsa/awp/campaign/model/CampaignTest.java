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

package hsa.awp.campaign.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import static hsa.awp.common.test.TestUtil.checkEquality;
import static hsa.awp.common.test.TestUtil.checkNoEquality;
import static org.junit.Assert.*;

/**
 * Test class for model object {@link Campaign}.
 *
 * @author johannes
 */
public class CampaignTest {
  /**
   * Normal {@link Campaign} test object.
   */
  private Campaign c1;

  /**
   * Secondary {@link Campaign} test object.
   */
  private Campaign c2;

  /**
   * Normal procedure for testing. |--x--|
   */
  private Procedure p1;

  /**
   * {@link Procedure} which surrounds p1 at both sides. |---x---|
   */
  private Procedure p2;

  /**
   * {@link Procedure} which is completely surrounded by p1. |-x-|
   */
  private Procedure p3;

  /**
   * {@link Procedure} which has its end date equals to p1s start date. |---x|
   */
  private Procedure p4;

  /**
   * {@link Procedure} which has its start date equals to p1s end date. |x---|
   */
  private Procedure p5;

  /**
   * This {@link Procedure} has no overlaps with p1. x...|-|
   */
  private Procedure p6;

  /**
   * Sets up some standard test objects.
   */
  @Before
  public void before() {

    c1 = new Campaign();
    c1.setId(1L);
    c1.setProcedures(new HashSet<Procedure>());

    c2 = new Campaign();
    c2.setId(2L);
    c2.setProcedures(new HashSet<Procedure>());

    Calendar startDate;
    Calendar endDate;

    startDate = Calendar.getInstance();
    endDate = Calendar.getInstance();
    startDate.add(Calendar.MONTH, -2);
    endDate.add(Calendar.MONTH, 2);
    p1 = new Procedure();
    p1.setId(1L);
    p1.setName("|--x--|");
    p1.setInterval(startDate, endDate);

    startDate = Calendar.getInstance();
    endDate = Calendar.getInstance();
    startDate.add(Calendar.MONTH, -3);
    endDate.add(Calendar.MONTH, 3);
    p2 = new Procedure();
    p2.setId(2L);
    p2.setName("|---x---|");
    p2.setInterval(startDate, endDate);

    startDate = Calendar.getInstance();
    endDate = Calendar.getInstance();
    startDate.add(Calendar.MONTH, -1);
    endDate.add(Calendar.MONTH, 1);
    p3 = new Procedure();
    p3.setId(3L);
    p3.setName("|-x-|");
    p3.setInterval(startDate, endDate);

    startDate = Calendar.getInstance();
    endDate = Calendar.getInstance();
    startDate.add(Calendar.MONTH, -3);
    p4 = new Procedure();
    p4.setId(4L);
    p4.setName("|---x|");
    p4.setInterval(startDate, endDate);

    startDate = Calendar.getInstance();
    endDate = Calendar.getInstance();
    endDate.add(Calendar.MONTH, 3);
    p5 = new Procedure();
    p5.setId(5L);
    p5.setName("|x---|");
    p5.setInterval(startDate, endDate);

    startDate = Calendar.getInstance();
    endDate = Calendar.getInstance();
    startDate.add(Calendar.MONTH, 3);
    endDate.add(Calendar.MONTH, 4);
    p6 = new Procedure();
    p6.setId(6L);
    p6.setName("x...|-|");
    p6.setInterval(startDate, endDate);
  }

  /**
   * Test method for {@link Campaign#addProcedure(Procedure)}. Tries to add the already prepared {@link Procedure}s to a
   * {@link Campaign}. First try to add the conflicting one. After that try to add the good one.
   */
  @Test
  public void testAddProcedure() {

    List<Procedure> procedures = new ArrayList<Procedure>();
    procedures.add(p2);
    procedures.add(p3);
    procedures.add(p4);
    procedures.add(p5);

    // init with standard test procedure
    c1.addProcedure(p1);

    // try to add all conflicting procedures
    int i = 2;
    for (Procedure p : procedures) {
      try {
        c1.addProcedure(p);
      } catch (IllegalArgumentException e) {
        assertFalse("p" + i++ + " must not be in the procedure list", c1.getAppliedProcedures().contains(p));
      }
    }

    // add the non conflicting one
    c1.addProcedure(p6);
    assertTrue("p6 has to be in the procedure list", c1.getAppliedProcedures().contains(p6));
  }

  /**
   * Tries to add the same procedures to the campaign.
   */
  @Test
  public void testAddProcedureDuplicated() {

    c1.addProcedure(p1);
    c1.addProcedure(p1);

    assertEquals(1, c1.getAppliedProcedures().size());
  }

  /**
   * Test method for {@link Campaign#findCurrentProcedure()}. Adds a {@link Procedure} to the {@link Campaign}. The added
   * {@link Procedure} starts or ends at the current date so it has to be returned by the {@link Campaign#findCurrentProcedure()}
   * method.
   */
  @Test
  public void testFindCurrentProcedureBorderToday() {
    // create test objects
    Procedure found;
    Calendar startDate = Calendar.getInstance();
    Calendar endDate = Calendar.getInstance();
    Calendar checkDate = Calendar.getInstance();
    endDate.setTimeInMillis(System.currentTimeMillis() + 60000);
    startDate.setTimeInMillis(System.currentTimeMillis() - 60000);

    // perpare test objects
    endDate.add(Calendar.MONTH, 1);
    p1.setInterval(startDate, endDate);

    // add procedure
    c1.addProcedure(p1);

    // search for it
    found = c1.findCurrentProcedure(checkDate);
    assertEquals(p1, found);

    // ------------------------------------------------------------------------------------------------------------------------

    // perpare test objects
    startDate.add(Calendar.MONTH, -1);
    endDate.setTimeInMillis(System.currentTimeMillis() + 60000);
    checkDate.setTimeInMillis(System.currentTimeMillis() + 15000);
    p2.setInterval(startDate, endDate);

    // add procedure
    c2.addProcedure(p2);

    // search for it
    found = c2.findCurrentProcedure(checkDate);
    assertEquals(p2, found);
  }

  /**
   * Test method for {@link Campaign#findCurrentProcedure()}. Adds a {@link Procedure} to the {@link Campaign}. The added
   * {@link Procedure} surrounds the current date so it has to be returned by the {@link Campaign#findCurrentProcedure()} method.
   */
  @Test
  public void testFindCurrentProcedureSurround() {
    // add procedure
    c1.addProcedure(p1);
    // search for it
    Procedure found = c1.findCurrentProcedure();
    // check
    assertEquals(p1, found);
  }

  /**
   * Test method for {@link Campaign#getAppliedProcedures()}. This test checks if an unmodifiable Set is returned by the test
   * candidate.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testGetAppliedProcedures() {

    c1.addProcedure(p1);
    c1.addProcedure(p6);

    c1.getAppliedProcedures().remove(p1);
  }

  /**
   * Test method for hashCode and equals methods of {@link Campaign}. This test checks if any change of parameters has an effect
   * on this methods. When the item is persisted the check has only to include the id.
   */
  @Test
  public void testHashCodeAndEquals() {

    Calendar temp;
    Campaign a = Campaign.getInstance(0L);
    Campaign b = Campaign.getInstance(0L);

    // init times at campaigns
    temp = Calendar.getInstance();
    a.setEndShow(temp);
    b.setEndShow(temp);
    temp = Calendar.getInstance();

    temp.add(Calendar.HOUR, -1);
    a.setStartShow(temp);
    checkNoEquality(a, b);
    b.setStartShow(temp);
    checkEquality(a, b);

    temp = Calendar.getInstance();
    temp.add(Calendar.HOUR, 1);
    a.setEndShow(temp);
    checkNoEquality(a, b);
    b.setEndShow(temp);
    checkEquality(a, b);

    a.setName("test");
    checkNoEquality(a, b);
    b.setName("test");
    checkEquality(a, b);

    HashSet<Long> events = new HashSet<Long>();
    events.add(3L);
    events.add(5L);
    a.setEventIds(events);
    checkEquality(a, b);

    HashSet<Procedure> procs = new HashSet<Procedure>();
    Procedure p = Procedure.getInstance(0L);
    procs.add(p);
    a.setProcedures(procs);
    checkEquality(a, b);

    // now persist the item
    a.setId(6L);
    checkNoEquality(a, b);
    b.setId(6L);
    checkEquality(a, b);

    a.setName("temp");
    checkEquality(a, b);
    b.setStartShow(temp);
    checkEquality(a, b);
  }

  /**
   * Test method for {@link Campaign#removeProcedure(Procedure)}. First adds two {@link Procedure}s to a {@link Campaign}. Then
   * removes one and checks the values. At least tries to remove a <code>null</code> as {@link Procedure} from the
   * {@link Campaign}.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveProcedure() {

    c1.addProcedure(p1);
    c1.addProcedure(p6);

    assertEquals(2, c1.getAppliedProcedures().size());
    assertTrue(c1.getAppliedProcedures().contains(p1));
    assertTrue(c1.getAppliedProcedures().contains(p6));

    c1.removeProcedure(p6);

    assertEquals(1, c1.getAppliedProcedures().size());
    assertTrue(c1.getAppliedProcedures().contains(p1));
    assertFalse(c1.getAppliedProcedures().contains(p6));

    c1.removeProcedure(null);
  }
}
