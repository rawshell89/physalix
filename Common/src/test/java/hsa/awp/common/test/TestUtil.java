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

package hsa.awp.common.test;

import org.junit.Ignore;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class provides some utility methods for testing.
 *
 * @author johannes
 */
@Ignore
public final class TestUtil {
  /**
   * Checks the equality of the given objects with <b>both</b> equals methods and compares the hash codes.
   *
   * @param a the first object
   * @param b the second object
   * @throws AssertionError with an error message, if at least one check was negative.
   */
  public static void checkEquality(Object a, Object b) {

    assertTrue("a.equals( b ) has to be true.", a.equals(b));
    assertTrue("b.equals( a ) has to be true.", b.equals(a));

    assertTrue("The two hash codes do not match.", a.hashCode() == b.hashCode());
  }

  /**
   * Checks that the two given objects are not equal. This is done by using both equals methods and by comparing the hash codes.
   *
   * @param a the first object
   * @param b the second object
   * @throws AssertionError with an error message, if at least one check was negative.
   */
  public static void checkNoEquality(Object a, Object b) {

    assertFalse("a.equals( b ) has to be false.", a.equals(b));
    assertFalse("b.equals( a ) has to be false.", b.equals(a));

    assertFalse("The two hash codes are not allowed to match.", a.hashCode() == b.hashCode());
  }

  /**
   * Constructors of utility classes should not be public.
   */
  private TestUtil() {

  }
}
