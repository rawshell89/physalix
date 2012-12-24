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
import hsa.awp.rule.model.RegistrationRuleSet;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for {@link RegistrationRuleSetDao}.
 *
 * @author johannes
 */
public class RegistrationRuleSetDaoTest extends GenericDaoTest<RegistrationRuleSet, RegistrationRuleSetDao, Long> {
  /**
   * Test instance.
   */
  private RegistrationRuleSet set;

  /**
   * Initializes the {@link GenericDaoTest} with a newly created {@link RegistrationRuleSetDao} and an {@link IObjectFactory}.
   */
  public RegistrationRuleSetDaoTest() {

    super.setDao(new RegistrationRuleSetDao());
    super.setIObjectFactory(new IObjectFactory<RegistrationRuleSet, Long>() {
      @Override
      public RegistrationRuleSet getInstance() {

        return RegistrationRuleSet.getInstance(0L);
      }
    });
  }

  /**
   * Initializes the test instances.
   */
  @Before
  public void setUp() {

    set = RegistrationRuleSet.getInstance(0L);
  }

  /**
   * Verifies that a {@link RegistrationRuleSet} can be correctly found by campaign and event parameters.
   */
  @Test
  public void testFindByCampaignAndEvent() {

    Long campaign = 5L;
    Long event = 6L;

    // initialize test instance
    set.setCampaign(campaign);
    set.setEvent(event);

    // persist item
    super.startTransaction();
    getDao().persist(set);
    super.commit();

    assertTrue(set.getId() != 0L);

    // try to find this RuleSet again
    super.startTransaction();
    assertEquals(set.getId(), getDao().findByCampaignAndEvent(campaign, event).getId());
    super.commit();
  }

  @Test
  public void testFindByCampaignAndEventAndMandatorId() {

    Long campaign = 5L;
    Long event = 6L;
    Long mandatorId = 1L;

    // initialize test instance
    set.setCampaign(campaign);
    set.setEvent(event);
    set.setMandatorId(mandatorId);

    // persist item
    super.startTransaction();
    getDao().persist(set);
    super.commit();

    assertTrue(set.getId() != 0L);

    // try to find this RuleSet again
    super.startTransaction();
    assertEquals(set.getId(), getDao().findByCampaignAndEventAndMandatorId(campaign, event, mandatorId).getId());
    super.commit();
  }

  @Test
  public void testFindRegistrationRuleSetsByEventId() {

    Long c1 = 50L;
    Long c2 = 60L;

    Long e1 = 20L;
    Long e2 = 30L;

    RegistrationRuleSet s1 = RegistrationRuleSet.getInstance(0L);
    s1.setCampaign(c1);
    s1.setEvent(e1);

    RegistrationRuleSet s2 = RegistrationRuleSet.getInstance(0L);
    s2.setCampaign(c1);
    s2.setEvent(e2);

    RegistrationRuleSet s3 = RegistrationRuleSet.getInstance(0L);
    s3.setCampaign(c2);
    s3.setEvent(e2);

    // save rule sets
    super.startTransaction();
    getDao().persist(s1);
    getDao().persist(s2);
    getDao().persist(s3);
    super.commit();

    // find sets again
    super.startTransaction();
    List<RegistrationRuleSet> f1 = getDao().findByEventId(e1);
    assertEquals(f1.size(), 1);
    assertTrue(f1.contains(s1));

    List<RegistrationRuleSet> f2 = getDao().findByEventId(e2);
    assertEquals(f2.size(), 2);
    assertTrue(f2.contains(s2));
    assertTrue(f2.contains(s3));
    super.commit();
  }

  @Test
  public void testFindRegistrationRuleSetsByEventIdAndMandatorId() {

    Long campaign1 = 50L;
    Long campaign2 = 60L;

    Long event1 = 20L;
    Long event2 = 30L;

    Long mandator1 = 1L;
    Long mandator2 = 2L;

    RegistrationRuleSet ruleSet1 = RegistrationRuleSet.getInstance(0L);
    ruleSet1.setCampaign(campaign1);
    ruleSet1.setEvent(event1);
    ruleSet1.setMandatorId(mandator1);

    RegistrationRuleSet ruleSet2 = RegistrationRuleSet.getInstance(0L);
    ruleSet2.setCampaign(campaign1);
    ruleSet2.setEvent(event2);
    ruleSet2.setMandatorId(mandator2);

    RegistrationRuleSet ruleSet3 = RegistrationRuleSet.getInstance(0L);
    ruleSet3.setCampaign(campaign2);
    ruleSet3.setEvent(event2);
    ruleSet3.setMandatorId(mandator2);

    // save rule sets
    super.startTransaction();
    getDao().persist(ruleSet1);
    getDao().persist(ruleSet2);
    getDao().persist(ruleSet3);
    super.commit();

    // find sets again
    super.startTransaction();
    List<RegistrationRuleSet> f1 = getDao().findByEventIdAndMandatorId(event1, mandator1);
    assertEquals(f1.size(), 1);
    assertTrue(f1.contains(ruleSet1));

    List<RegistrationRuleSet> f2 = getDao().findByEventIdAndMandatorId(event2, mandator2);
    assertEquals(f2.size(), 2);
    assertTrue(f2.contains(ruleSet2));
    assertTrue(f2.contains(ruleSet3));
    super.commit();
  }

  @Test
  @Ignore
  @Override
  public void testMerge() {

    fail("Not yet implemented.");
  }
}
