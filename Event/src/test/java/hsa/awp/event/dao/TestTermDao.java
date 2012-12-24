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

import hsa.awp.common.test.GenericDaoTest;
import hsa.awp.common.test.IObjectFactory;
import hsa.awp.event.model.Term;
import org.junit.Ignore;

/**
 * Test class for {@link TermDao}.
 *
 * @author alex
 */
public class TestTermDao extends GenericDaoTest<Term, TermDao, Long> {
  /**
   * Initializes the {@link GenericDaoTest} with a newly created {@link TermDao} and an {@link IObjectFactory}.
   */
  public TestTermDao() {

    super();
    super.setDao(new TermDao());

    super.setIObjectFactory(new IObjectFactory<Term, Long>() {
      @Override
      public Term getInstance() {

        return Term.getInstance(0L);
      }
    });
  }

  @Ignore
  @Override
  // TODO: Implement when TermDao is extended.
  public void testMerge() {

  }
}
