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

package hsa.awp.usergui;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.lang.PackageName;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 *
 * @see hsa.awp.usergui.Start#main(String[])
 */
public class WicketApplication extends WebApplication {
  /**
   * Constructor.
   */
  public WicketApplication() {

    mount("web", PackageName.forClass(HomePage.class));
    mount("login", PackageName.forClass(LoginPage.class));
  }

  /**
   * Returns the Homepage, which will be displayed on startup.
   *
   * @return Classfile of the Homepage
   * @see org.apache.wicket.Application#getHomePage()
   */
  public Class<HomePage> getHomePage() {

    return HomePage.class;
  }

  @Override
  protected void init() {

    super.init();
    addComponentInstantiationListener(new SpringComponentInjector(this));
  }
}
