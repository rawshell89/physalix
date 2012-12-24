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

package hsa.awp.event.model;

import hsa.awp.common.AbstractMandatorableDomainObject;

import javax.persistence.*;

/**
 * Persistent object for Exams.
 *
 * @author klassm
 */
@Entity
@Table(name = "`exam`")
@Inheritance(strategy = InheritanceType.JOINED)
public class Exam extends AbstractMandatorableDomainObject<Long> {
  /**
   * Version UID which is used for serialization.
   */
  private static final long serialVersionUID = -294645849823900014L;

  /**
   * The duration of the {@link Exam} in minutes.
   */
  @Column(name = "duration")
  private Integer duration;

  private ExamType examType;

  /**
   * Identifier for an Exam.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id = 0L;

  /**
   * Name of the Exam.
   */
  private String name;

  /**
   * Allowed resources for the Exam.
   */
  @Column(name = "resources")
  private String resources;

  /**
   * Field for additional information.
   */
  private String text;


  /**
   * Creates a new {@link Exam} and initializes it with appropriate values (e.g. inserts a HashSet at appropriate places, so that
   * the Sets can be used without risking a {@link NullPointerException}.
   *
   * @return new domain object.
   */
  public static Exam getInstance(Long mandatorId) {

    Exam exam = new Exam();
    exam.setMandatorId(mandatorId);
    return exam;
  }

  /**
   * Default constructor.
   */
  protected Exam() {

  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Exam)) {
      return false;
    }
    Exam other = (Exam) obj;

    // if item is persisted use only the id for checking
    if (id != null && id != 0L) {
      return id.equals(other.id);
    }

    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    if (resources == null) {
      if (other.resources != null) {
        return false;
      }
    } else if (!resources.equals(other.resources)) {
      return false;
    }
    if (duration == null) {
      if (other.duration != null) {
        return false;
      }
    } else if (!duration.equals(other.duration)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    // if item is persisted use only the id for calculating hash code
    if (id != null && id != 0L) {
      return id.hashCode();
    }

    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((resources == null) ? 0 : resources.hashCode());
    return result;
  }

  /**
   * Getter for the identifier.
   *
   * @return identifier.
   */
  public Long getId() {

    return id;
  }

  /**
   * Getter for the duration.
   *
   * @return allowed resources.
   */
  public int getDuration() {

    if (duration == null) {
      return 0;
    }
    return duration;
  }

  /**
   * Setter for the duration.
   *
   * @param duration duration.
   */
  public void setDuration(int duration) {

    this.duration = duration;
  }

  /**
   * Getter for examTpye.
   *
   * @return the examTpye
   */
  public ExamType getExamType() {

    return examType;
  }

  /**
   * Setter for examTpye.
   *
   * @param examTpye the examTpye to set
   */
  public void setExamType(ExamType examType) {

    this.examType = examType;
  }

  /**
   * Getter for the name of the {@link Exam}.
   *
   * @return name of the {@link Exam}.
   */
  public String getName() {

    return name;
  }

  /**
   * Setter for the name.
   *
   * @param name name
   */
  public void setName(String name) {

    this.name = name;
  }

  /**
   * Getter for the allowed resources.
   *
   * @return allowed resources.
   */
  public String getResources() {

    return resources;
  }

  /**
   * Setter for the allowed resources.
   *
   * @param resources allowed resources.
   */
  public void setResources(String resources) {

    this.resources = resources;
  }

  /**
   * Getter for text.
   *
   * @return the text
   */
  public String getText() {

    return text;
  }

  /**
   * Setter for text.
   *
   * @param text the text to set
   */
  public void setText(String text) {

    this.text = text;
  }

  /**
   * Setter for the identifier.
   *
   * @param id identifier.
   */
  void setId(Long id) {

    this.id = id;
  }

}
