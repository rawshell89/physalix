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

import hsa.awp.campaign.model.PriorityList;
import hsa.awp.campaign.model.PriorityListItem;
import hsa.awp.common.test.GenericDaoTest;
import hsa.awp.common.test.IObjectFactory;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * {@link PriorityListItemDaoTest}.
 *
 * @author kai
 */
public class PriorityListItemDaoTest extends GenericDaoTest<PriorityListItem, PriorityListItemDao, Long> {
  /**
   * Creates a {@link PriorityListItemDaoTest}.
   */
  public PriorityListItemDaoTest() {

    super();
    super.setDao(new PriorityListItemDao());

    super.setIObjectFactory(new IObjectFactory<PriorityListItem, Long>() {
      private Long id = 0L;

      private PriorityList prioList;

      {
        PriorityListDao prioListDao = initDao(new PriorityListDao());
        prioList = PriorityList.getInstance(0L);
        prioList.setInitiator(5L);
        prioList.setParticipant(3L);

        prioListDao.getEntityManager().getTransaction().begin();

        prioListDao.persist(prioList);
        prioListDao.getEntityManager().getTransaction().commit();
      }

      @Override
      public PriorityListItem getInstance() {

        PriorityListItem item = PriorityListItem.getInstance(prioList, id++, id.intValue() % 5, 0L);

        return item;
      }
    });
  }

  @Override
  public void testMerge() {

    List<PriorityListItem> items = generateAndPersistObjects(2);
    items.get(0).setEvent(6L);

    startTransaction();

    assertEquals(items.get(0), getDao().merge(items.get(0)));
    assertEquals(2, getDao().findAll().size());

    commit();
  }

  /**
   * Test method for {@link hsa.awp.campaign.dao.PriorityListDao#PriorityListDao()}.
   */
  @Test
  public void testPriorityListDao() {

    assertEquals(PriorityListItemDao.class, getDao().getClass());
  }
}
