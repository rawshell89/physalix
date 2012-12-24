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

package hsa.awp.event.dao;

import hsa.awp.common.exception.DataAccessException;
import hsa.awp.common.test.GenericDaoTest;
import hsa.awp.common.test.IObjectFactory;
import hsa.awp.event.model.Category;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link CategoryDao}.
 *
 * @author alex
 */
public class TestCategoryDao extends GenericDaoTest<Category, ICategoryDao, Long> {
  /**
   * Initializes the {@link GenericDaoTest} with a newly created {@link CategoryDao} and an {@link IObjectFactory}.
   */
  public TestCategoryDao() {

    super();
    super.setDao(new CategoryDao());

    super.setIObjectFactory(new IObjectFactory<Category, Long>() {
      /**
       * Counter for unique category names.
       */
      private Long id = 0L;

      @Override
      public Category getInstance() {

        return Category.getInstance("abc" + id++, 0L);
      }
    });
  }

  /**
   * {@link CategoryDao#getInstance(String)} test method.
   */
  @Test
  public void testCreateCategory() {

    String name = "hmm";

    startTransaction();
    Category test = Category.getInstance(name, 0L);
    test = getDao().persist(test);
    assertEquals(test.getName(), name);
    commit();

    startTransaction();
    assertEquals(getDao().findById(test.getId()), test);
    commit();
  }

  /**
   * Persists 2 objects and tries to get one by its name.
   */
  @Test
  public void testGetByName() {

    Category cat1 = Category.getInstance("Susi", 0L);
    Category cat2 = Category.getInstance("Rico und Lisa", 0L);

    startTransaction();
    cat1 = getDao().persist(cat1);
    cat2 = getDao().persist(cat2);
    commit();

    assertEquals(cat1, getDao().findByName(cat1.getName()));
    assertEquals(cat2, getDao().findByName(cat2.getName()));
  }

  /**
   * Persists 2 objects and tries to get one by its name.
   */
  @Test
  public void testGetByMandator() {

    Category cat1 = Category.getInstance("Susi", 1L);
    Category cat2 = Category.getInstance("Rico und Lisa", 2L);

    startTransaction();
    cat1 = getDao().persist(cat1);
    cat2 = getDao().persist(cat2);
    commit();

    assertEquals(cat1, getDao().findByMandator(cat1.getMandatorId()).get(0));
    assertEquals(cat2, getDao().findByMandator(cat2.getMandatorId()).get(0));
  }

  /**
   * Persists 2 objects and tries to get one by its name.
   */
  @Test
  public void testGetByNameAndMandator() {

    Category cat1 = Category.getInstance("Susi", 2L);
    Category cat2 = Category.getInstance("Susi", 3L);

    startTransaction();
    cat1 = getDao().persist(cat1);
    cat2 = getDao().persist(cat2);
    commit();

    assertEquals(cat1, getDao().findByNameAndMandator(cat1.getName(), cat1.getMandatorId()));
    assertEquals(cat2, getDao().findByNameAndMandator(cat2.getName(), cat2.getMandatorId()));
  }

  /**
   * Tries to find an object with a nonexistent name.
   */
  @Test(expected = DataAccessException.class)
  public void testGetByNameNonExistent() {

    getDao().findByName("does_not_exist");
  }

  /**
   * Tries to find an object with the name <code>null</code>.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetByNameNull() {

    getDao().findByName(null);
  }

  @Override
  public void testMerge() {

    startTransaction();
    List<Category> categories = new LinkedList<Category>();

    Category c1 = Category.getInstance("Heidi", 0L);
    getDao().persist(c1);
    categories.add(c1);

    Category c2 = Category.getInstance("Ursula", 0L);
    getDao().persist(c2);
    categories.add(c2);

    commit();

    startTransaction();

    assertEquals(categories.get(0), getDao().merge(categories.get(0)));
    assertEquals(2, getDao().findAll().size());
    assertEquals("Heidi", getDao().findById(categories.get(0).getId()).getName());
    assertEquals(categories.get(0), getDao().findById(categories.get(0).getId()));
    assertEquals(categories.get(1), getDao().findById(categories.get(1).getId()));

    commit();
  }
}
