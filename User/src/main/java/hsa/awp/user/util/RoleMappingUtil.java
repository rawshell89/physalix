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

package hsa.awp.user.util;


import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class RoleMappingUtil {

  private Properties roleMapping;

  public RoleMappingUtil() {

    loadProperties("/config/roleMapping.properties");
  }

  public List<String> getRightsForRole(String role) {

    if (role == null || role.equals("")) {
      throw new IllegalArgumentException("String role can not be empty or null.");
    }

    String rights = (String) roleMapping.get(role);
    if (rights == null || rights.equals("")) {
      return Collections.emptyList();
    }
    return Arrays.asList(rights.split(","));
  }

  private void loadProperties(String path) {

    roleMapping = new Properties();
    URL url = this.getClass().getResource(path);
    if (url == null) {
      throw new IllegalArgumentException("file not found");
    }
    try {
      roleMapping.load(url.openStream());
    } catch (IOException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
  }

  public void setPropertyPath(String path) {
    loadProperties(path);
  }

}
