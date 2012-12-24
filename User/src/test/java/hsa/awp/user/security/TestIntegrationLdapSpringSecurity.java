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

package hsa.awp.user.security;


import hsa.awp.common.naming.IAbstractDirectory;
import hsa.awp.common.naming.IDirectoryAdapter;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * This test case checks if it is possible to authenticate a user against ldap.
 *
 * @author alex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/IntegrationTest_LDAP_SpringSecurity.xml"})
@Ignore //should only be run manually
public class TestIntegrationLdapSpringSecurity extends AbstractJUnit4SpringContextTests {
  /**
   * Security context to do authentication against.
   */
  private SecurityContext securityContext;

  /**
   * Adapter for the ldap directory.
   */
  @Resource(name = "common.naming.ldapdirectoryAdapter")
  private IDirectoryAdapter ldapAdapter;

  @Resource(name = "ldapAuthProvider")
  private AuthenticationProvider ldapAuthProvider;

  /**
   * Initializes securitycontext.
   */
  public TestIntegrationLdapSpringSecurity() {

    securityContext = SecurityContextHolder.getContext();
  }

  @Before
  public void setUp() {
    // we want to use the real ldap implementation for this test
    IAbstractDirectory directory = (IAbstractDirectory) applicationContext.getBean("common.naming.directory");
    directory.setDirectoryAdapter(ldapAdapter);
  }

  @Test
  public void testArampp() {

    Authentication auth = new UsernamePasswordAuthenticationToken("arampp", "IbI98S35");
    securityContext.setAuthentication(auth);
    ldapAuthProvider.authenticate(auth);
  }
}
