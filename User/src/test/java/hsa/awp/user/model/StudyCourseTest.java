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

package hsa.awp.user.model;

import org.junit.Test;

import static hsa.awp.common.test.TestUtil.checkEquality;
import static hsa.awp.common.test.TestUtil.checkNoEquality;

/**
 * Test class for model object {@link StudyCourse}.
 *
 * @author johannes
 */
public class StudyCourseTest {
  /**
   * Test method for hashCode and equals methods of {@link StudyCourse}. This test checks if any change of parameters has an effect on
   * this methods. When the item is persisted the check has only to include the id.
   */
  @Test
  public void testHashCodeAndEquals() {

    StudyCourse a = StudyCourse.getInstance("dummyName");
    StudyCourse b = StudyCourse.getInstance("dummyName");
    checkEquality(a, b);

    a.setName("name");
    checkNoEquality(a, b);
    b.setName("name");
    checkEquality(a, b);

    a.setProgram("programm");
    checkNoEquality(a, b);
    b.setProgram("programm");
    checkEquality(a, b);

    // persist item
    a.setId(8L);
    checkNoEquality(a, b);
    b.setId(8L);
    checkEquality(a, b);

    a.setName("temp");
    b.setProgram("temp");
    checkEquality(a, b);   //Equal, because only id is checked
  }
}
