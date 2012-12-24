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

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * This {@link Rule} provides the ability to check the term of a {@link Student}. If {@link #minTerm} or {@link #maxTerm} is set to
 * <code>0</code> then this restriction will be ignored. The limits are not exclusive! If {@link #minTerm} is set to 3, then a
 * student in term 3 will be accepted.
 * <p>
 * Some examples:
 * <ul>
 * <li>minTerm = 3 and maxTerm = 0<br>
 * So a student in term 2 is rejected and all students in term 3+ will be accepted.</li>
 * <li>minTerm = 0 and maxTerm = 5<br>
 * A student in term 6 will be rejected but all students in term less or equal than term 5 are accepted.</li>
 * <li>minTerm = 2 and maxTerm = 5<br>
 * Students in term 1 or term 6 are accepted and all students from term 2 until term 5 are accepted.</li>
 * </ul>
 * </p>
 *
 * @author johannes
 */
@Entity
@Table(name = "`termrule`")
public class TermRule extends StudentRule {
  /**
   * serial version UID used for serialization.
   */
  private static final long serialVersionUID = -181573786222886400L;

  /**
   * The minimal term a {@link Student} has to have.
   */
  private int minTerm;

  /**
   * The maximal term a {@link Student} has to have.
   */
  private int maxTerm;

  /**
   * Creates a new instance of a {@link TermRule}.
   *
   * @return a new {@link TermRule}.
   */
  public static TermRule getInstance(Long mandatorId) {

    TermRule rule = new TermRule();
    rule.setMandatorId(mandatorId);
    return rule;
  }

  /**
   * Default constructor.
   */
  TermRule() {

  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof TermRule)) {
      return false;
    }
    TermRule other = (TermRule) obj;

    // equality is already checked with super.equals()
    if (getId() != null && getId() != 0L) {
      return true;
    }

    if (maxTerm != other.maxTerm) {
      return false;
    }
    if (minTerm != other.minTerm) {
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
    result = prime * result + maxTerm;
    result = prime * result + minTerm;
    return result;
  }

  @Override
  protected boolean check(Student student, Campaign campaign, Event event) {

    int term = student.getTerm();

    return (minTerm == 0 || minTerm <= term) && (maxTerm == 0 || term <= maxTerm);
  }

  /**
   * Returns the maximal term.
   *
   * @return the maximal term
   */
  public int getMaxTerm() {

    return maxTerm;
  }

  /**
   * Sets the minimal term a {@link Student} can be to pass this rule.
   *
   * @param maxTerm the minimal term
   */
  public void setMaxTerm(int maxTerm) {

    this.maxTerm = maxTerm;
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
}
