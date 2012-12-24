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

package hsa.awp.user.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Persistent object for {@link Student}.
 *
 * @author johannes
 */
@Entity
@Table(name = "`student`")
public class Student extends SingleUser {
  /**
   * Version UID which is used for serialization.
   */
  private static final long serialVersionUID = 6022342947995997727L;

  /**
   * Matriculation number.
   */
  // removed unique constraint. Students are also identified over the id number (alex).
  private int matriculationNumber;

  /**
   * term number.
   */
  private int term;

  /**
   * study course.
   */
  @ManyToOne(cascade = {CascadeType.MERGE})
  private StudyCourse studyCourse;

  /**
   * Default factory method.
   *
   * @return new domain object
   */
  public static Student getInstance() {

    Student s = new Student();
    s.initialize();
    s.setTerm(0);
    return s;
  }

  /**
   * Creates a new {@link Student} and initializes it with appropriate values (e.g. inserts a HashSet at appropriate places, so
   * that the Sets can be used without risking a {@link NullPointerException}.
   *
   * @param username            username of the {@link Student}.
   * @param matriculationNumber matriculation number of the {@link Student}.
   * @return new domain object.
   */
  public static Student getInstance(String username, int matriculationNumber) {

    if (username == null || username.isEmpty()) {
      throw new IllegalArgumentException("no username given");
    }
    if (matriculationNumber < 0) {
      throw new IllegalArgumentException("matriculation number is negative");
    }

    Student s = getInstance();
    s.setUsername(username);
    s.setMatriculationNumber(matriculationNumber);
    return s;
  }

  /**
   * Default constructor.
   */
  Student() {

    super();
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof Student)) {
      return false;
    }
    Student other = (Student) obj;

    // equality is already checked with super.equals()
    if (getId() != null && getId() != 0L) {
      return true;
    }

    if (matriculationNumber != other.matriculationNumber) {
      return false;
    }
    if (term != other.term) {
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
    result = prime * result + matriculationNumber;
    result = prime * result + term;
    return result;
  }

  /**
   * Returns matriculationNumber.
   *
   * @return the matriculationNumber
   */
  public int getMatriculationNumber() {

    return matriculationNumber;
  }

  /**
   * Setter for matriculationNumber.
   *
   * @param matriculationNumber the matriculationNumber to set
   */
  public void setMatriculationNumber(int matriculationNumber) {

    this.matriculationNumber = matriculationNumber;
  }

  /**
   * Returns studyCourse.
   *
   * @return the studyCourse
   */
  public StudyCourse getStudyCourse() {

    return studyCourse;
  }

  /**
   * Setter for studyCourse.
   *
   * @param studyCourse the studyCourse to set
   */
  public void setStudyCourse(StudyCourse studyCourse) {

    this.studyCourse = studyCourse;
  }

  /**
   * Returns term.
   *
   * @return the term
   */
  public int getTerm() {

    return term;
  }

  /**
   * Setter for term.
   *
   * @param term the term to set
   */
  public void setTerm(int term) {

    this.term = term;
  }
}
