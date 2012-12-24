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
import hsa.awp.user.model.Group;
import hsa.awp.user.model.SingleUser;
import hsa.awp.user.model.User;
import org.junit.Ignore;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link GroupDao}.
 *
 * @author johannes
 */
@Ignore // Does not work, yet (alex)
public class GroupDaoTest extends GenericDaoTest<Group, GroupDao, Long> {
  /**
   * Initializes the GenericDaoTest with a {@link GroupDao} instance and an in-line {@link IObjectFactory}.
   */
  public GroupDaoTest() {

    super();
    super.setDao(new GroupDao());

    super.setIObjectFactory(new IObjectFactory<Group, Long>() {
      @Override
      public Group getInstance() {

        return Group.getInstance();
      }
    });
  }

  /**
   * {@link GroupDao#getInstance()} test method.
   */
  public void testGetInstance() {
    // create usergroup
    Group g = Group.getInstance();

    // check object
    assertEquals(Group.class, g.getClass());

    // check sets
    assertTrue(g.getMembers() instanceof Set<?>);
    assertEquals(0L, g.getId().longValue());
  }

  /**
   * Adds a persisted {@link SingleUser} object to the member list of an {@link Group}. After successful checks this user will be
   * removed again from the list and also checked. At the end of this test the persisted {@link SingleUser} object will be removed.
   */
  @Override
  public void testMerge() {
    // create usergroups
    List<Group> groups = super.generateAndPersistObjects(1);
    Group g = groups.get(0);

    // Create Dao for creating SingleUser objects
    SingleUserDirectoryDao userDao = super.initDao(new SingleUserDirectoryDao());

    // Create SingleUser object for member list.
    SingleUser u = SingleUser.getInstance("UserGroupDummyUser");

    // persist user
    super.startTransaction();
    u = userDao.persist(u);
    super.commit();

    // ------------------------------------------------------------------------------------------------------------------------

    // Add user to member list of the userGroup
    Set<User> members = new HashSet<User>();
    members.add(u);
    g.setMembers(members);
    assertEquals(1, g.getMembers().size());

    // merge changes
    super.startTransaction();
    Group mergedG0 = getDao().merge(g);
    super.commit();

    // check the changes
    assertEquals(g, mergedG0);
    assertEquals(1, mergedG0.getMembers().size());
    assertTrue("Member list doesn't contain the specified user.", g.getMembers().contains(u));

    // ------------------------------------------------------------------------------------------------------------------------

    // remove member again
    members.remove(u);
    g.setMembers(members);

    // merge changes
    super.startTransaction();
    mergedG0 = getDao().merge(g);
    super.commit();

    // check the changes
    assertEquals(g, mergedG0);
    assertEquals(0, mergedG0.getMembers().size());

    // ------------------------------------------------------------------------------------------------------------------------

    // clean up created objects (user groups will removed by tear down method)
    super.startTransaction();
    getDao().remove(g);
    userDao.remove(u);
    super.commit();

    // assert clean up
    assertEquals(0, userDao.countAll());
  }
}
