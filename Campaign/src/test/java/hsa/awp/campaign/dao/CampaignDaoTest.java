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

package hsa.awp.campaign.dao;

import hsa.awp.campaign.model.Campaign;
import hsa.awp.common.exception.NoMatchingElementException;
import hsa.awp.common.test.GenericDaoTest;
import hsa.awp.common.test.IObjectFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * {@link CampaignDaoTest}.
 *
 * @author kai
 */
public class CampaignDaoTest extends GenericDaoTest<Campaign, CampaignDao, Long> {
  /**
   * Constructor for the {@link CampaignDaoTest}.
   */
  public CampaignDaoTest() {

    super();
    super.setDao(new CampaignDao());

    super.setIObjectFactory(new IObjectFactory<Campaign, Long>() {

      @Override
      public Campaign getInstance() {

        Campaign c = Campaign.getInstance(0L);
        c.setName("hallowelt_" + Math.random());

        return c;
      }
    });
  }

  /**
   * Test correct saving of new campaigns
   */
  @Test
  public void testCreateCampaign() {
    getDao().removeAll();

    List<Campaign> campList = new ArrayList<Campaign>();
    campList.addAll(generateAndPersistObjects(10));

    assertTrue(campList.containsAll(getDao().findAll()));
  }

  /**
   * Test whether the eventIds actually are written to the database.
   */
  @Test
  public void testEventIds() {

    startTransaction();

    Campaign c = createCampaign("abc", 1, 5);
    c.getEventIds().add(1L);
    c.getEventIds().add(4L);
    c.getEventIds().add(6L);
    getDao().persist(c);

    commit();

    startTransaction();

    Campaign cFound = getDao().findById(c.getId());
    assertEquals(c.getEventIds().size(), cFound.getEventIds().size());

    commit();
  }

  /**
   * Creates a given {@link Campaign} using the given parameters.
   *
   * @param name           name of the {@link Campaign}.
   * @param minuteAddStart add x minutes
   * @param minuteAddEnd   add x minutes
   * @return new domain object.
   */
  private Campaign createCampaign(String name, int minuteAddStart, int minuteAddEnd) {

    Campaign c = Campaign.getInstance(0L);

    Calendar startShow = Calendar.getInstance();
    Calendar endShow = Calendar.getInstance();
    startShow.add(Calendar.MINUTE, minuteAddStart);
    endShow.add(Calendar.MINUTE, minuteAddEnd);

    c.setEndShow(endShow);
    c.setStartShow(startShow);
    c.setName(name);

    return c;
  }

  /**
   * Looks for all active {@link Campaign}s.
   */
  @Test
  public void testFindActive() {

    startTransaction();

    getDao().persist(createCampaign("campaign " + Math.random(), -1, 10));
    getDao().persist(createCampaign("campaign " + Math.random(), 5, 20));

    assertEquals(1, getDao().findActive().size());

    commit();
  }

  /**
   * Looks for a {@link Campaign} using its unique name.
   */
  @Test(expected = NoMatchingElementException.class)
  public void testFindByName() {

    startTransaction();

    Campaign c = Campaign.getInstance(0L);
    c.setName("hallowelt" + Math.random());
    getDao().persist(c);

    Campaign c1 = Campaign.getInstance(0L);
    c1.setName(("hallo" + Math.random()));
    getDao().persist(c1);

    assertEquals(c.getName(), getDao().findByName(c.getName()).getName());
    assertEquals(c1.getName(), getDao().findByName(c1.getName()).getName());

    getDao().findByName("xaz");

    commit();
  }

  /**
   * Looks for a {@link Campaign} using its unique name.
   */
  @Test(expected = NoMatchingElementException.class)
  public void testFindByNameAndMandator() {

    startTransaction();

    Campaign c = Campaign.getInstance(1L);
    c.setName("hallowelt" + Math.random());
    getDao().persist(c);

    Campaign c1 = Campaign.getInstance(2L);
    c1.setName(("hallo" + Math.random()));
    getDao().persist(c1);

    assertEquals(c.getName(), getDao().findByNameAndMandator(c.getName(), 1L).getName());
    assertEquals(c1.getName(), getDao().findByNameAndMandator(c1.getName(), 1L).getName());

    getDao().findByNameAndMandator("xaz", 3L);

    commit();
  }

  @Test
  public void testFindCampaignsByEventId() {

    Campaign c1 = Campaign.getInstance(0L);
    c1.setName("c1");
    for (Long i = 1L; i < 20L; i++) {
      c1.getEventIds().add(i);
    }

    Campaign c2 = Campaign.getInstance(0L);
    c2.setName("c2");
    for (Long i = 10L; i < 30L; i++) {
      c2.getEventIds().add(i);
    }

    startTransaction();
    getDao().persist(c1);
    getDao().persist(c2);
    commit();


    startTransaction();

    List<Campaign> testList;

    testList = getDao().findCampaignsByEventId(3L);
    assertEquals(1, testList.size());
    assertTrue(testList.contains(c1));

    testList = getDao().findCampaignsByEventId(10L);
    assertEquals(2, testList.size());
    assertTrue(testList.contains(c1));
    assertTrue(testList.contains(c2));

    testList = getDao().findCampaignsByEventId(19L);
    assertEquals(2, testList.size());
    assertTrue(testList.contains(c1));
    assertTrue(testList.contains(c2));

    testList = getDao().findCampaignsByEventId(25L);
    assertEquals(1, testList.size());
    assertTrue(testList.contains(c2));

    commit();
  }

  @Override
  public void testMerge() {

    List<Campaign> campaigns = generateAndPersistObjects(2);
    Calendar date = Calendar.getInstance();
    campaigns.get(0).setStartShow(date);
    assertEquals(campaigns.get(0), getDao().merge(campaigns.get(0)));
    assertEquals(2, getDao().findAll().size());
    assertEquals(date, getDao().findById(campaigns.get(0).getId()).getStartShow());
    assertEquals(campaigns.get(0), getDao().findById(campaigns.get(0).getId()));
    assertEquals(campaigns.get(1), getDao().findById(campaigns.get(1).getId()));
  }


}
