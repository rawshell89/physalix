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

package hsa.awp.admingui;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/spring/TestSecuredPage.xml")
abstract public class AbstractSecurityTest {

  protected WicketTester tester;

  @Resource(name = "admingui.testContext")
  private WicketApplication app;

  @Resource(name = "authenticationManager")
  private AuthenticationManager am;

  @Before
  public void before() {

    tester = new WicketTester(app);
    initialLogin();
  }

  public void initialLogin() {
    login("anonymous", "anonymous");
  }

  @After
  public void clear() {
    SecurityContextHolder.clearContext();
  }

  protected void login(String name, String password) {
    Authentication auth = new UsernamePasswordAuthenticationToken(name, password);
    SecurityContextHolder.getContext().setAuthentication(am.authenticate(auth));
    System.out.println(auth);
  }
}
