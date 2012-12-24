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


import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.panel.Panel;
import org.hibernate.cfg.NotYetImplementedException;

/**
 * The {@link OnePanelPage} which allows to use the inheritance in the wicket markup for one {@link Panel}.
 *
 * @author Rico
 */
public class OnePanelPage extends BasePage {
  public static String getPanelIdOne() {

    return "p1";
  }

  /**
   * Constructor with {@link Panel}.
   *
   * @param panel the {@link Panel} which has to be add to the markup.
   */
  public OnePanelPage(Panel panel) {

    add(panel);
  }

  /**
   * Constructor with {@link Panel} and {@link PageParameters}.
   *
   * @param panel  the {@link Panel} which has to be add to the markup.
   * @param params the {@link PageParameters} which the {@link Panel} wants.
   */
  public OnePanelPage(Panel panel, PageParameters params) {

    throw new NotYetImplementedException("not implemented with params");
  }
}
