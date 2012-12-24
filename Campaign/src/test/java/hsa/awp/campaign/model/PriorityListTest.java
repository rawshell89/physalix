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

import org.junit.Test;

import java.util.Calendar;

import static hsa.awp.common.test.TestUtil.checkEquality;
import static hsa.awp.common.test.TestUtil.checkNoEquality;
import static org.junit.Assert.assertEquals;

/**
 * Test class for model object {@link PriorityList}.
 *
 * @author johannes
 */
public class PriorityListTest {
  @Test
  public void testAddItem() {

    PriorityList p = PriorityList.getInstance(0L);

    p.addItem(5L, 1);
    assertEquals(1, p.getItems().size());
    assertEquals(Long.valueOf(5L), p.getItem(1).getEvent());
    assertEquals(1, p.getItem(1).getPriority());

    p.addItem(6L, 2);
    assertEquals(2, p.getItems().size());
    assertEquals(Long.valueOf(6L), p.getItem(2).getEvent());
    assertEquals(2, p.getItem(2).getPriority());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddItemNegativePriority() {

    PriorityList p = PriorityList.getInstance(0L);
    p.addItem(5L, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddItemNullEvent() {

    PriorityList p = PriorityList.getInstance(0L);
    p.addItem(null, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddItemZeroPriority() {

    PriorityList p = PriorityList.getInstance(0L);
    p.addItem(5L, 0);
  }

  /**
   * Test method for hashCode and equals methods of {@link PriorityList}. This test checks if any change of parameters has an
   * effect on this methods. When the item is persisted the check has only to include the id.
   */
  @Test
  public void testHashCodeAndEquals() {

    Calendar temp = Calendar.getInstance();
    PriorityList a = PriorityList.getInstance(0L);
    PriorityList b = PriorityList.getInstance(0L);
    a.setDate(temp);
    b.setDate(temp);

    temp = Calendar.getInstance();
    temp.add(Calendar.HOUR, 1);
    a.setDate(temp);
    checkNoEquality(a, b);
    b.setDate(temp);
    checkEquality(a, b);

    a.setInitiator(5L);
    checkNoEquality(a, b);
    b.setInitiator(5L);
    checkEquality(a, b);

    a.setParticipant(9L);
    checkNoEquality(a, b);
    b.setParticipant(9L);
    checkEquality(a, b);

    DrawProcedure proc = DrawProcedure.getInstance(0L);
    a.setDrawProcedure(proc);
    checkNoEquality(a, b);
    b.setDrawProcedure(proc);
    checkEquality(a, b);
    proc.setName("test");
    checkEquality(a, b);

    a.addItem(5L, 1);
    a.addItem(7L, 2);
    checkNoEquality(a, b);
    b.addItem(5L, 1);
    checkNoEquality(a, b);
    b.addItem(7L, 2);
    checkEquality(a, b);

    // persist item
    a.setId(7L);
    checkNoEquality(a, b);
    b.setId(7L);
    checkEquality(a, b);

    a.setDate(Calendar.getInstance());
    a.setInitiator(545L);
    b.setParticipant(759L);
    b.setDrawProcedure(DrawProcedure.getInstance(0L));
    checkEquality(a, b);
  }
}
