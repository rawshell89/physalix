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
import hsa.awp.user.model.Student;
import hsa.awp.user.model.StudyCourse;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * This {@link Rule} will successfully check a {@link Student} if he participates in the specified {@link StudyCourse}. The return
 * value of {@link #check(Student, Campaign, Event)} would be <code>true</code>. If he is in any other {@link StudyCourse}
 * <code>false</code> will be returned.
 *
 * @author johannes
 */
@Entity
@Table(name = "`studycourserule`")
public class StudyCourseRule extends StudentRule {
  /**
   * serial version UID used for serialization.
   */
  private static final long serialVersionUID = 8494621048772225596L;

  /**
   * The referenced id of the {@link StudyCourse}.
   */
  private Long studyCourse;

  /**
   * Creates a new instance of a {@link StudyCourseRule}.
   *
   * @return a new {@link StudyCourseRule}.
   */
  public static StudyCourseRule getInstance(Long mandatorId) {

    StudyCourseRule rule = new StudyCourseRule();
    rule.setStudyCourse(0L);
    rule.setMandatorId(mandatorId);
    return rule;
  }

  /**
   * Default constructor.
   */
  StudyCourseRule() {

  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof StudyCourseRule)) {
      return false;
    }
    StudyCourseRule other = (StudyCourseRule) obj;

    // equality is already checked with super.equals()
    if (getId() != null && getId() != 0L) {
      return true;
    }

    if (studyCourse == null) {
      if (other.studyCourse != null) {
        return false;
      }
    } else if (!studyCourse.equals(other.studyCourse)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    // if item is persisted use only the id for calculating hash code
    if (getId() != null && getId() != 0L) {
      return super.hashCode();
    }

    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((studyCourse == null) ? 0 : studyCourse.hashCode());
    return result;
  }

  /**
   * Checks if the given {@link Student} participates the specified {@link StudyCourse}. The {@link Student} is not allowed to be
   * <code>null</code>.
   *
   * @param student  the {@link Student} to be checked
   * @param campaign the associated {@link Campaign}
   * @param event    the associated {@link Event}
   * @return <code>true</code> if the student participates the {@link StudyCourse}. Otherwise <code>false</code>.
   */
  @Override
  protected boolean check(Student student, Campaign campaign, Event event) {

    StudyCourse course = student.getStudyCourse();

    if (studyCourse.equals(course.getId())) {
      return true;
    }

    return false;
  }

  /**
   * Getter for the referenced {@link StudyCourse} id.
   *
   * @return the id of the referenced {@link StudyCourse}.
   */
  public Long getStudyCourse() {

    return studyCourse;
  }

  /**
   * Sets the referenced {@link StudyCourse} id.
   *
   * @param studyCourse the new {@link StudyCourse} id.
   */
  public void setStudyCourse(Long studyCourse) {

    this.studyCourse = studyCourse;
  }
}
