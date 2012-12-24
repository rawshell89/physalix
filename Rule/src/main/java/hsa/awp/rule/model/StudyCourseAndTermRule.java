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
 * This {@link Rule} combines the study course (SC) constraint and the term (T) constraint. The logic is as follows:
 * <p>
 * This logic is equal to the logical implication.<br>
 * <code>A -> B</code><br>
 * A : participates the student the adjusted study course?<br>
 * B : is the student minimal in the adjusted term?
 * </p>
 * <p/>
 * <pre>
 * When we look at a Rule that says: SC: WI and T: 2
 * +----+---+--------+
 * | SC | T | ACCEPT |
 * +----+---+--------+
 * |  G | 1 |   true |
 * |  G | 3 |   true |
 * | WI | 1 |  false |
 * | WI | 3 |   true |
 * +----+---+--------+
 * </pre>
 *
 * @author johannes
 */
@Entity
@Table(name = "`studycourseandtermrule`")
public class StudyCourseAndTermRule extends StudentRule {
  /**
   * serial version UID used for serialization.
   */
  private static final long serialVersionUID = -7158150813030949182L;

  /**
   * The referenced id of the {@link StudyCourse}.
   */
  private Long studyCourse;

  /**
   * The minimal term a {@link Student} has to have.
   */
  private int minTerm;

  /**
   * Creates a new instance of a {@link StudyCourseAndTermRule}.
   *
   * @return a new {@link StudyCourseAndTermRule}.
   */
  public static StudyCourseAndTermRule getInstance(Long mandatorId) {

    StudyCourseAndTermRule rule = new StudyCourseAndTermRule();
    rule.setStudyCourse(0L);
    rule.setMandatorId(mandatorId);
    return rule;
  }

  /**
   * Default constructor.
   */
  StudyCourseAndTermRule() {

  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof StudyCourseAndTermRule)) {
      return false;
    }
    StudyCourseAndTermRule other = (StudyCourseAndTermRule) obj;

    // equality is already checked with super.equals()
    if (getId() != null && getId() != 0L) {
      return true;
    }

    if (minTerm != other.minTerm) {
      return false;
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
    result = prime * result + minTerm;
    result = prime * result + ((studyCourse == null) ? 0 : studyCourse.hashCode());
    return result;
  }

  @Override
  protected boolean check(Student student, Campaign campaign, Event event) {

    if (minTerm == 0) {
      throw new IllegalStateException("the minimal term is not set");
    }

    int term = student.getTerm();
    StudyCourse course = student.getStudyCourse();

    // if the student participates the adjusted study course
    if (studyCourse.equals(course.getId())) {
      // then check the term too
      return minTerm <= term;
    } else {
      // otherwise there is no restriction
      return true;
    }
  }

  /**
   * Returns the minimal term.
   *
   * @return the minimal term
   */
  public int getMinTerm() {

    return minTerm;
  }

  /**
   * Sets the minimal term a {@link Student} can be to pass this rule.
   *
   * @param minTerm the minimal term
   */
  public void setMinTerm(int minTerm) {

    this.minTerm = minTerm;
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
