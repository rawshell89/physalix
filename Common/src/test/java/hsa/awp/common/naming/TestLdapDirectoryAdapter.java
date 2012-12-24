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

package hsa.awp.common.naming;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.InitialDirContext;
import java.io.IOException;
import java.util.Properties;

/**
 * This unit test validates the LdapDirectoryAdapter. The underlying directorycontext will be mocked out. The test checks if the
 * right calls will be submittet to the dircontext.
 *
 * @author alex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/test-config.xml"})
public class TestLdapDirectoryAdapter {
  /**
   * Path to ldap configuration file.
   */
  private static final String LDAP_CONFIG_FILE = "config/naming.test.properties";

  /**
   * The class under test.
   */
  @Resource(name = "common.naming.ldapdirectoryAdapter")
  private LdapDirectoryAdapter adapter;

  /**
   * Mockery from JMOCK.
   */
  private Mockery mockery;

  /**
   * Mocks the direcotycontext.
   */
  private InitialDirContext directoryContext;

  /**
   * The ldap configuration.
   */
  private Properties ldapConfig;

  /**
   * Instantiates the mockery.
   *
   * @throws IOException If the ldap configuration file was not found.
   */
  public TestLdapDirectoryAdapter() throws IOException {

    mockery = new JUnit4Mockery() {
      {
        setImposteriser(ClassImposteriser.INSTANCE);
      }
    };
    ldapConfig = new Properties();
    ldapConfig.load(ClassLoader.getSystemResourceAsStream(LDAP_CONFIG_FILE));
  }

  /*
  * HELPER METHODS TO CONFIGURE MOCK BEHAVIOR
  *
  * Convention: mockExpect<description>() - adds new expectations to the mock mockIgnore<description>() - ignores calls onto the
  * mock
  */

  /**
   * Adds expectations for context configuration to the adapter.
   *
   * @throws Exception if something went wrong.
   */
  private void mockExpectConfiguration() throws Exception {

    mockery.checking(new Expectations() {
      {
        oneOf(directoryContext).addToEnvironment(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

        oneOf(directoryContext).addToEnvironment(Context.PROVIDER_URL, ldapConfig.getProperty("naming.providerURL"));

        oneOf(directoryContext).addToEnvironment(Context.SECURITY_PRINCIPAL, ldapConfig.getProperty("naming.securityPrincipal"));

        oneOf(directoryContext).addToEnvironment(Context.SECURITY_CREDENTIALS,
            ldapConfig.getProperty("naming.securityCredentials"));

        oneOf(directoryContext).addToEnvironment(Context.SECURITY_PROTOCOL, ldapConfig.getProperty("naming.securityProtocol"));

        oneOf(directoryContext).addToEnvironment(Context.SECURITY_AUTHENTICATION,
            ldapConfig.getProperty("naming.securityAuthentication"));
      }
    });
  }

  /**
   * Does initial configuration tasks before every test.
   *
   * @throws Exception if something went wrong.
   */
  @Before
  public void setUp() throws Exception {

    directoryContext = mockery.mock(InitialDirContext.class);
    setUpAdapter();
  }

  /*
  * SOME HELPER METHODS
  */

  /**
   * Sets up the adapter.
   *
   * @throws Exception if something went wrong.
   */
  private void setUpAdapter() throws Exception {
    // mockExpectConfiguration();
    adapter.setDirContext(directoryContext);
  }

  /**
   * Checks if the mockery is satisfied.
   *
   * @throws Exception if something went wrong.
   */
  @After
  public void tearDown() throws Exception {

    mockery.assertIsSatisfied();
  }

  /**
   * If a new configuration would be set, I expect the dircontext to be reconfigured automatically at the next lookup.
   *
   * @throws Exception if something went wrong.
   */
  @Test
  public void testAutoConfiguration() throws Exception {

    mockIgnoreAttributesLookup();
    adapter.getAttributes("test");
  }

  /**
   * Ignores a call of getAttributes(String, String) at the mockobject.
   *
   * @throws Exception if something went wrong.
   */
  private void mockIgnoreAttributesLookup() throws Exception {

    mockery.checking(new Expectations() {
      {
        ignoring(directoryContext).getAttributes(with(any(String.class)), with(any(String[].class)));
        will(returnValue(new BasicAttributes()));
      }
    });
  }

  /**
   * Tests if all attributes will be fetched.
   *
   * @throws Exception if an exception occurred.
   */
  @Test
  public void testGetAttributesString() throws Exception {

    final String testname = "hans";
    mockery.checking(new Expectations() {
      {
        oneOf(directoryContext).getAttributes("uid=" + testname + ",ou=People,dc=domain,dc=com",
            ldapConfig.getProperty("naming.fields").split(","));
      }
    });
    adapter.getAttributes(testname);
  }

  /**
   * Tests if it is possible to get only a minor set of attributes.
   *
   * @throws Exception if something went wrong.
   */
  @Test
  public void testGetAttributesStringStringArray() throws Exception {

    final String testname = "hans";
    final String[] testAttribs = {"uid", "name", "semester"};
    mockery.checking(new Expectations() {
      {
        oneOf(directoryContext).getAttributes("uid=" + testname + ",ou=People,dc=domain,dc=com", testAttribs);
      }
    });
    adapter.getAttributes(testname, testAttribs);
  }
}
