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
import hsa.awp.user.model.Role;
import hsa.awp.user.model.RoleMapping;
import hsa.awp.user.model.SingleUser;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestSingleUserDBDao extends GenericDaoTest<SingleUser, SingleUserDbDao, Long> {

  public TestSingleUserDBDao() {

    super();
    super.setDao(new SingleUserDbDao());

    super.setIObjectFactory(new IObjectFactory<SingleUser, Long>() {
      @Override
      public SingleUser getInstance() {

        return SingleUser.getInstance();
      }
    });
  }

  @Test
  public void testFindByRole() {

    int userNumber = 50;

    startTransaction();

    List<SingleUser> users = new LinkedList<SingleUser>();

    for (int i = 0; i < userNumber; i++) {
      SingleUser user = SingleUser.getInstance();
      user.setName("abc" + i);
      user.setUuid(Long.valueOf(i));
      user.setUsername("abc" + i);

      getDao().persist(user);

      RoleMappingDao roleMappingDao = initDao(new RoleMappingDao());

      if (i % 10 == 0) {
        RoleMapping roleMapping = RoleMapping.getInstance(Role.SYSADMIN);
        roleMapping = roleMappingDao.persist(roleMapping);
        user.addRoleMapping(roleMapping);
        roleMappingDao.merge(roleMapping);
      }

      getDao().merge(user);

      users.add(user);
    }

    assertEquals((int) (userNumber / 10), getDao().findUsersByRole(Role.SYSADMIN).size());

    commit();
  }

  @Override
  public void testMerge() {

    startTransaction();

    SingleUser single = SingleUser.getInstance();
    single.setName("abc");
    single.setUsername("abc");
    single.setUuid(1583992L);

    getDao().persist(single);

    single.setName("def");

    assertEquals("def", getDao().findByUsername("abc").getName());

    commit();
  }
}
