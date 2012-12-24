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
 * Test class for model object {@link User}.
 *
 * @author johannes
 */
public class UserTest {
  /**
   * Test method for hashCode and equals methods of {@link User}. This test checks if any change of parameters has an effect on
   * this methods. When the item is persisted the check has only to include the id.
   * <p/>
   * Please note that the {@link User} is abstract. So there is no simple way to test {@link User#equals(Object)} or
   * {@link User#hashCode()}. This test uses the {@link Group} domain model to test the methods of {@link User}.
   */
  @Test
  public void testHashCodeAndEquals() {

    User a = Group.getInstance();
    User b = Group.getInstance();
    checkEquality(a, b);

    // persist item
    a.setId(8L);
    checkNoEquality(a, b);
    b.setId(8L);
    checkEquality(a, b);
  }
}
