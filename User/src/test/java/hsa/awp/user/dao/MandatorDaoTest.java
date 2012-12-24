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

package hsa.awp.user.dao;

import hsa.awp.common.test.GenericDaoTest;
import hsa.awp.common.test.IObjectFactory;
import hsa.awp.user.model.Mandator;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for {@link hsa.awp.user.dao.MandatorDao}.
 *
 * @author Basti
 */
public class MandatorDaoTest extends GenericDaoTest<Mandator, MandatorDao, Long> {
  /**
   * Initializes the GenericDaoTest with a {@link hsa.awp.user.dao.MandatorDao} instance and an in-line {@link hsa.awp.common.test.IObjectFactory}.
   */
  public MandatorDaoTest() {

    super();
    super.setDao(new MandatorDao());

    super.setIObjectFactory(new IObjectFactory<Mandator, Long>() {
      private int counter = 0;

      @Override
      public Mandator getInstance() {

        return Mandator.getInstance("MandatorDaoTest" + counter++);
      }
    });
  }

  /**
   * Test method for {@link hsa.awp.user.dao.MandatorDao#findByName(String)}. Creates some {@link hsa.awp.user.model.Mandator}s and tries to find them using
   * their names.
   */
  @Test
  public void testFindByName() {

    List<Mandator> mandators = generateAndPersistObjects(2);

    Mandator mandator1 = mandators.get(0);
    Mandator mandator2 = mandators.get(1);

    assertEquals(mandator1, getDao().findByName(mandator1.getName()));
    assertEquals(mandator2, getDao().findByName(mandator2.getName()));

    String name = "TestMandator";
    Mandator mandator3 = Mandator.getInstance(name);

    super.startTransaction();
    getDao().persist(mandator3);
    super.commit();

    Mandator found = getDao().findByName(name);

    assertEquals(mandator3, found);
    assertEquals(name, found.getName());
  }

  /**
   * Test method for {@link hsa.awp.user.dao.MandatorDao#findByName(String)}.
   */
  @Test
  public void testFindByNameNonExistend() {

    assertNull(getDao().findByName("NonExistingMandator416473137"));
  }

  /**
   * Test method for {@link hsa.awp.user.dao.MandatorDao#findByName(String)}. Tries to throw a {@link IllegalArgumentException} by searching
   * with <code>null</code>
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFindByNameNull() {

    getDao().findByName(null);
  }

  /**
   * Test method for {@link hsa.awp.user.dao.Mandator#getInstance(String)}.
   */
  @Test
  public void testGetInstance() {

    String name = "TestMandator";
    Mandator mandator = Mandator.getInstance(name);

    // check values
    assertEquals(Mandator.class, mandator.getClass());
    assertEquals(0L, mandator.getId().longValue());
    assertEquals(name, mandator.getName());
  }

  @Test
  @Override
  public void testMerge() {
    // create StudyCourse
    List<Mandator> mandators = generateAndPersistObjects(1);
    Mandator mandator = mandators.get(0);

    // values to changes
    String name = "NewName";

    // change some values
    mandator.setName(name);

    // merge changes
    super.startTransaction();
    Mandator merged = getDao().merge(mandator);
    super.commit();

    // check values
    assertEquals(mandator, merged);
    assertTrue(merged.getId() != 0L);
    assertEquals(name, merged.getName());
  }
}
