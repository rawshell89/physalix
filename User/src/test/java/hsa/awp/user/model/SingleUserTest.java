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

package hsa.awp.user.model;

import hsa.awp.common.test.TestUtil;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertEquals;

/**
 * Test class for model object {@link SingleUser}.
 *
 * @author johannes
 */
public class SingleUserTest {

  /**
   * Test method for hashCode and equals methods of {@link SingleUser}. This test checks if any change of parameters has an effect on
   * this methods. When the item is persisted the check has only to include the id.
   */
  @Test
  public void testHashCodeAndEquals() {

    SingleUser a = SingleUser.getInstance();
    SingleUser b = SingleUser.getInstance();
    TestUtil.checkEquality(a, b);

    a.setFaculty("faculty");
    TestUtil.checkNoEquality(a, b);
    b.setFaculty("faculty");
    TestUtil.checkEquality(a, b);

    HashSet<Long> lectures = new HashSet<Long>();
    lectures.add(7L);
    a.setLectures(lectures);
    TestUtil.checkEquality(a, b);
    b.setLectures(lectures);

    a.setMail("mail");
    TestUtil.checkNoEquality(a, b);
    b.setMail("mail");
    TestUtil.checkEquality(a, b);

    a.setName("name");
    TestUtil.checkNoEquality(a, b);
    b.setName("name");
    TestUtil.checkEquality(a, b);

    Set<RoleMapping> roleMappings = new HashSet<RoleMapping>();
    roleMappings.add(RoleMapping.getInstance(Role.REGISTERED));
    a.setRolemappings(roleMappings);
    TestUtil.checkNoEquality(a, b);
    b.setRolemappings(roleMappings);
    TestUtil.checkEquality(a, b);

    a.setUsername("username");
    TestUtil.checkNoEquality(a, b);
    b.setUsername("username");
    TestUtil.checkEquality(a, b);

    a.setUuid(67L);
    TestUtil.checkNoEquality(a, b);
    b.setUuid(67L);
    TestUtil.checkEquality(a, b);

    // persist item
    a.setId(8L);
    TestUtil.checkNoEquality(a, b);
    b.setId(8L);
    TestUtil.checkEquality(a, b);

    a.setFaculty("temp");
    a.setLectures(new HashSet<Long>());
    a.setMail("temp");
    a.setName("temp");
    b.setRolemappings(new HashSet<RoleMapping>());
    b.setUsername("temp");
    b.setUuid(7854L);
    TestUtil.checkEquality(a, b);

    // remove persistence item
    a.setId(0L);
    TestUtil.checkNoEquality(a, b);
    b.setId(0L);
    TestUtil.checkNoEquality(a, b);
  }

  @Test
  public void testAddRoleMapping() {

    SingleUser user = SingleUser.getInstance();

    assertEquals(0, user.getRolemappings().size());

    Mandator mandator = Mandator.getInstance("abc");

    RoleMapping roleMapping1 = RoleMapping.getInstance(Role.REGISTERED, mandator, user);

    user.addRoleMapping(roleMapping1);
    assertEquals(1, user.getRolemappings().size());

    user.addRoleMapping(roleMapping1);
    assertEquals(1, user.getRolemappings().size());

  }
}
