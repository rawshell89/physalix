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

package hsa.awp.admingui.rule.rules;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * Rule being shown every time the CreateRulePanel is called. Usually this has no content and is only a place holder.
 *
 * @author klassm
 */
public class EmptyRulePanel extends Panel {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = 3362471401467548159L;

  /**
   * Creates a new {@link EmptyRulePanel}.
   *
   * @param id wicket-id.
   */
  public EmptyRulePanel(String id) {

    super(id);
  }
}
