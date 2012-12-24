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

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Abstract Test for all Facades.
 *
 * @param <T> Class Type of each Facade.
 * @author klassm
 */
public abstract class AbstractFacadeTest<T> extends OpenEntityManagerInTest {
  /**
   * Class of the facade to test.
   */
  private Class<T> facadeToTest;

  /**
   * Constructor with the facade to test.
   *
   * @param facadeToTest facade class object used for testing.
   */
  public AbstractFacadeTest(Class<T> facadeToTest) {

    this.facadeToTest = facadeToTest;
  }

  /**
   * Tests all facade methods for their @Transactional annotations.
   */
  @Test
  public void testTransactionalAnnotations() {

    for (Method method : facadeToTest.getMethods()) {
      if (isTransactionalNecessary(method.getName()) && method.getAnnotation(Transactional.class) == null) {
        fail("Every logic method in a facade must have a @Transactional annotation: " + method.getName());
      }
    }
  }

  /**
   * Determines whether a @Transactional annotation is necessary at a specific facade methods.
   *
   * @param name name of the facade method.
   * @return true if the annotation is necessary.
   */
  private boolean isTransactionalNecessary(String name) {

    List<String> notNecessary = new LinkedList<String>();
    notNecessary.add("set");

    for (Method method : Object.class.getMethods()) {
      if (method.getName().equals(name)) {
        return false;
      }
    }

    for (String s : notNecessary) {
      if (name.startsWith(s)) {
        return false;
      }
    }
    return true;
  }
}
