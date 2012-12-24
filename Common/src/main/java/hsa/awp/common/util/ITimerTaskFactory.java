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

package hsa.awp.common.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A factory to create {@link TimerTask}s for the JDK {@link Timer}.
 *
 * @author johannes
 */
public interface ITimerTaskFactory {
  /**
   * Creates a new {@link TimerTask} with the given {@link Runnable}.
   *
   * @param task the code body to be executed
   * @return a new {@link TimerTask}
   */
  TimerTask getTask(final Runnable task);
}
