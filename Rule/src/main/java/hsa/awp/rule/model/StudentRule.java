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
import hsa.awp.user.model.Student;
import hsa.awp.user.model.StudyCourse;

/**
 * This {@link Rule} is able to check a {@link Student}.
 *
 * @author johannes
 */
public abstract class StudentRule extends RegistrationRule {
  /**
   * serial version UID used for serialization.
   */
  private static final long serialVersionUID = -4277901682066006474L;

  /**
   * Default constructor.
   */
  StudentRule() {

  }

  @Override
  public final boolean check(SingleUser user, Campaign campaign, Event event) {

    if (!(user instanceof Student)) {
      // throw new IllegalArgumentException("The given SingleUser is not an instance of Student");
      return true;
    }

    return check((Student) user, campaign, event);
  }

  /**
   * Checks if the given {@link Student} participates the specified {@link StudyCourse}. The {@link Student} is not allowed to be
   * <code>null</code>.
   *
   * @param student  the {@link Student} to be checked
   * @param campaign the associated {@link Campaign}
   * @param event    the associated {@link Event}
   * @return <code>true</code> if the student is allowed to pa. Otherwise <code>false</code>.
   */
  protected abstract boolean check(Student student, Campaign campaign, Event event);
}
