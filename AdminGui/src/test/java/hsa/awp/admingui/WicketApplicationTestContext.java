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

import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * WicketApplication class for test context. This class overrides the {@link WicketApplication#init()} method because there are some
 * incompatible Spring related actions.
 * <p>
 * When the Spring context starts up the current {@link ApplicationContext} will be injected by {@link Autowired} annotation. Now
 * the {@link #init()} method can use the injected {@link ApplicationContext} to create a new {@link SpringComponentInjector}
 * </p>
 * <p/>
 * This class is configured as bean candidate through {@link Component} annotation.
 *
 * @author johannes
 */
@Component(value = "admingui.testContext")
public class WicketApplicationTestContext extends WicketApplication {
  /**
   * The Spring {@link ApplicationContext} used by the {@link SpringComponentInjector} to find the Spring beans to inject.
   */
  @Autowired
  private ApplicationContext ctx;

  /**
   * Creates a new {@link SpringComponentInjector} mapped to the injected {@link ApplicationContext}.
   */
  @Override
  protected void init() {
    super.initForTest();
    addComponentInstantiationListener(new SpringComponentInjector(this, ctx, true));
  }
}
