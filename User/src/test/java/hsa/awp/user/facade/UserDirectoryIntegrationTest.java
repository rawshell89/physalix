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

package hsa.awp.user.facade;

import hsa.awp.user.model.SingleUser;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.naming.NamingException;

import static org.junit.Assert.assertEquals;

/**
 * This unit test does an ldap lookup and tests the integration from UserDirectory with all lower layers.
 *
 * @author alex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/UserDirectoryIntegrationTest.xml"})
@Ignore //We have uncommented the spring config for this test due to problems
// with a NullPointerException.
public class UserDirectoryIntegrationTest {
  @Resource(name = "user.facade")
  private IUserFacade facade;

  /**
   * Does a simple lookup.
   *
   * @throws NamingException if there was something wrong with LDAP
   */
  @Test
  public void testLookupAlex() throws NamingException {

    // TODO transform this to a github ticket
    // use embedded ldap server
    // http://stackoverflow.com/questions/5471033/embedded-opends-ldap-server-with-memory-back-end
    // http://stackoverflow.com/questions/7269697/embedded-memory-ldap-server-solution
    final String targetName = "Test User";
    final String targetLogin = "test";
    final String targetEmail = "test@physalix";

    SingleUser singleUser = facade.getSingleUserByLogin("test");
    assertEquals(targetName, singleUser.getName());
    assertEquals(targetLogin, singleUser.getUsername());
    assertEquals(targetEmail, singleUser.getMail());
  }
}
