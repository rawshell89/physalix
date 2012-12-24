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

import hsa.awp.common.exception.ConfigurationException;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * This class is a dummy implementation of a LDAP adapter.
 *
 * @author alex
 */
public class DummyDirectoryAdapter implements IDirectoryAdapter {
  /**
   * All dummy users.
   */
  private HashMap<String, Attributes> uid2user;

  /**
   * All dummy users.
   */
  private HashMap<Long, Attributes> uidNumber2user;

  /**
   * Stores the config.
   */
  private Properties config;

  /**
   * Constructor.
   */
  public DummyDirectoryAdapter() {

    uid2user = new HashMap<String, Attributes>();
    uid2user.put(DummyUserData.getStudentLogin(), DummyUserData.getStudentAttributes());
    uid2user.put(DummyUserData.getTeacherLogin(), DummyUserData.getTeacherAttributes());
    uid2user.put(DummyUserData.getSecretaryLogin(), DummyUserData.getSecretaryAttributes());

    uidNumber2user = new HashMap<Long, Attributes>();
    uidNumber2user.put(DummyUserData.getStudentId(), DummyUserData.getStudentAttributes());
    uidNumber2user.put(DummyUserData.getTeacherId(), DummyUserData.getTeacherAttributes());
    uidNumber2user.put(DummyUserData.getSecretaryId(), DummyUserData.getSecretaryAttributes());
  }

  @Override
  public Attributes getAttributes(String name) throws NamingException {

    Attributes result = uid2user.get(name);
    if (result != null) {
      return selectConfiguredAttributes(result);
    } else {
      throw new NamingException("Login " + name + " not found.");
    }
  }

  @Override
  public Attributes getAttributes(long uuid) throws NamingException {

    Attributes result = uidNumber2user.get(uuid);
    if (result != null) {
      return selectConfiguredAttributes(result);
    } else {
      String uuidStr = String.valueOf(uuid);
      return DummyUserData.create(uuidStr, uuidStr, uuidStr, "", "dummy-"+uuid, uuid+"@physalix", "dummy-faculty", "dummy-studycourse", "1", "Angestellte");
    }
  }

  @Override
  public Attributes getAttributes(String name, String[] attrIds) throws NamingException {

    Attributes result = uid2user.get("name");
    if (result != null) {
      return selectConfiguredAttributes(result, Arrays.asList(attrIds));
    } else {
      throw new NamingException();
    }
  }

  @Override
  public void setDirContext(DirContext context) {
    // in this dummy, this method does nothing.
  }

  @Override
  public List<Attributes> searchDirectory(String searchString) {

    return Collections.emptyList();  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public Set<String> getAllStudyCourses() {
    return Collections.emptySet();  //To change body of implemented methods use File | Settings | File Templates.
  }

  /**
   * Returns the dummy users of this class.
   *
   * @return A Hashmap containing the attributes of the dummy users.
   */
  public HashMap<String, Attributes> getUsers() {

    return uid2user;
  }

  private Attributes selectConfiguredAttributes(Attributes attrs) {

    List<String> fields = Arrays.asList(config.getProperty("naming.fields").split(","));
    return selectConfiguredAttributes(attrs, fields);
  }

  @SuppressWarnings("unchecked")
  private Attributes selectConfiguredAttributes(Attributes attrs, List<String> fields) {

    Attributes result = new BasicAttributes();

    NamingEnumeration<Attribute> attrsEnum = (NamingEnumeration<Attribute>) attrs.getAll();
    while (attrsEnum.hasMoreElements()) {
      Attribute currentAttribute;
      try {
        currentAttribute = attrsEnum.next();
        if (fields.contains(currentAttribute.getID())) {
          result.put(currentAttribute);
        }
      } catch (NamingException e) {
        throw new RuntimeException(e);
      }
    }
    return result;
  }

  public void setConfigFile(String configFile) {

    config = new Properties();
    try {
      InputStream in = ClassLoader.getSystemResourceAsStream(configFile);
      if (in == null) {
        throw new ConfigurationException("PropertyFile " + configFile + " is not in CLASSPATH");
      }
      config.load(in);
    } catch (IOException e) {
      throw new ConfigurationException("Could not load configFile", e);
    }
  }
}
