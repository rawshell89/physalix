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

package hsa.awp.rule.model;

import hsa.awp.campaign.model.Campaign;
import hsa.awp.event.model.Event;
import hsa.awp.user.model.SingleUser;
import org.junit.Before;

/**
 * This class holds some test instances which can be used in subclasses. All subclasses have to call {@link #setUp()} to instantiate
 * all provided objects.
 *
 * @author johannes
 */
public abstract class AbstractRuleTest {
  /**
   * The {@link SingleUser} used for checking.
   */
  public SingleUser user;

  /**
   * The {@link Campaign} used for checking.
   */
  public Campaign campaign;

  /**
   * The {@link Event} used for checking.
   */
  public Event event;

  /**
   * Initializes the test instances.
   */
  @Before
  public void setUp() {

    user = SingleUser.getInstance();

    campaign = Campaign.getInstance(0L);
    campaign.setId(5L);

    event = Event.getInstance(0, 0L);
    event.setId(6L);
  }
}
