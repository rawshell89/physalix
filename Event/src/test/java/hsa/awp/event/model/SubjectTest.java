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

package hsa.awp.event.model;

import org.junit.Test;

import java.util.HashSet;

import static hsa.awp.common.test.TestUtil.checkEquality;
import static hsa.awp.common.test.TestUtil.checkNoEquality;

/**
 * Unit test for {@link Subject}.
 *
 * @author johannes
 */
public class SubjectTest {
  /**
   * Test method for hashCode and equals methods of {@link Subject}. This test checks if any change of parameters has an effect on
   * this methods. When the item is persisted the check has only to include the id.
   */
  @Test
  public void testHashCodeAndEquals() {

    Subject a = Subject.getInstance(0L);
    Subject b = Subject.getInstance(0L);
    checkEquality(a, b);

    Category category = Category.getInstance("", 0L);
    a.setCategory(category);
    checkNoEquality(a, b);
    b.setCategory(category);
    checkEquality(a, b);

    a.setDescription("testing");
    checkNoEquality(a, b);
    b.setDescription("testing");
    checkEquality(a, b);

    HashSet<Event> events = new HashSet<Event>();
    events.add(Event.getInstance(456, 0L));
    a.setEvents(events);
    checkEquality(a, b);

    a.setLink("link");
    checkNoEquality(a, b);
    b.setLink("link");
    checkEquality(a, b);

    a.setName("test");
    checkNoEquality(a, b);
    b.setName("test");
    checkEquality(a, b);

    // persist item
    a.setId(8L);
    checkNoEquality(a, b);
    b.setId(8L);
    checkEquality(a, b);

    a.setCategory(Category.getInstance("unequal", 0L));
    a.setDescription("description");
    a.setEvents(new HashSet<Event>());
    b.setLink("newlink");
    b.setName("temp");
    checkEquality(a, b);
  }
}
