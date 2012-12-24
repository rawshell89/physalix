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

package hsa.awp.rule.dao;

import hsa.awp.common.test.GenericDaoTest;
import hsa.awp.common.test.IObjectFactory;
import hsa.awp.rule.model.Rule;
import hsa.awp.rule.model.TermRule;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for {@link RuleDao}.
 *
 * @author johannes
 */
public class RuleDaoTest extends GenericDaoTest<Rule, RuleDao, Long> {
  /**
   * Initializes the {@link GenericDaoTest} with a newly created {@link RuleDao} and an {@link IObjectFactory}.
   */
  public RuleDaoTest() {

    super.setDao(new RuleDao());
    super.setIObjectFactory(new IObjectFactory<Rule, Long>() {
      private int counter = 0;

      @Override
      public Rule getInstance() {

        Rule rule = TermRule.getInstance(0L);
        rule.setName("RuleDaoTest-" + counter++);
        return rule;
      }
    });
  }

  @Test
  public void testFindByNameGood() {

    String name = "RuleDaoFindByNameTest";

    Rule rule = TermRule.getInstance(0L);
    rule.setName(name);
    super.startTransaction();
    getDao().persist(rule);
    super.commit();

    // try to find the rule by its name
    super.startTransaction();
    Rule found = getDao().findByName(name);
    super.commit();

    assertEquals(name, found.getName());
    assertEquals(rule.getId(), found.getId());
  }

  @Test
  public void testFindByNameAndMandatorId() {

    String name = "RuleDaoFindByNameTest";

    Rule rule = TermRule.getInstance(1L);
    rule.setName(name);
    super.startTransaction();
    getDao().persist(rule);
    super.commit();

    // try to find the rule by its name
    super.startTransaction();
    Rule found = getDao().findByNameAndMandatorId(name, 1L);
    super.commit();

    assertEquals(name, found.getName());
    assertEquals(rule.getId(), found.getId());
  }

  @Test
  public void testFindByNameNoMatching() {

    super.startTransaction();
    Rule rule = getDao().findByName("dfasfdsa");
    super.commit();

    assertNull(rule);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindByNameNull() {

    super.startTransaction();
    getDao().findByName(null);
    super.commit();
  }

  @Test
  @Ignore
  @Override
  public void testMerge() {

    fail("Not yet implemented.");
  }
}
