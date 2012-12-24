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
import java.util.Calendar;

/**
 * Class representing an occurrence of a {@link Timetable} object. The occurrence can occur periodically or only one time.
 *
 * @author klassm
 */
@Entity
@Table(name = "`occurence`")
public class Occurrence extends AbstractMandatorableDomainObject<Long> {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = -6571700558226647635L;

  /**
   * Date when this {@link Occurrence} ends.
   */
  private Calendar endDate;

  /**
   * unique id.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id = 0L;

  /**
   * Field for additional information.
   */
  private String information;

  /**
   * Date when this {@link Occurrence} begins.
   */
  private Calendar startDate;

  /**
   * Type of this {@link Occurrence}.
   */
  private OccurrenceType type;


  /**
   * Creates a new {@link Occurrence}.
   *
   * @return a new object of {@link Occurrence}
   */
  public static Occurrence getInstance(Long mandatorId) {

    Occurrence occurrence = new Occurrence();
    occurrence.setMandatorId(mandatorId);
    return occurrence;
  }

  /**
   * Creates a new {@link Occurrence}.
   *
   * @param startDate date when this occurrence begins.
   * @param endDate   date when this occurrence ends.
   * @param type      type of the {@link Occurrence}.
   * @return a new object of {@link Occurrence}
   * @throws IllegalArgumentException if startDate is null.
   * @throws IllegalArgumentException if endDate is null.
   * @throws IllegalArgumentException if startDate is not before endDate.
   */
  public static Occurrence getInstance(Calendar startDate, Calendar endDate, OccurrenceType type, Long mandatorId) {

    if (type == null) {
      throw new IllegalArgumentException("no type given.");
    }

    Occurrence occurence = new Occurrence();
    occurence.setInterval(startDate, endDate);
    occurence.type = type;
    occurence.setMandatorId(mandatorId);

    return occurence;
  }

  /**
   * Sets the start and endDate of this occurrence.
   *
   * @param startDate date when this occurrence begins.
   * @param endDate   date when this occurrence ends.
   * @throws IllegalArgumentException if startDate is null.
   * @throws IllegalArgumentException if endDate is null.
   * @throws IllegalArgumentException if startDate is not before endDate.
   */
  public void setInterval(Calendar startDate, Calendar endDate) {

    if (startDate == null) {
      throw new IllegalArgumentException("no startDate given.");
    } else if (endDate == null) {
      throw new IllegalArgumentException("no endDate given.");
    } else if (startDate.equals(endDate) || startDate.after(endDate)) {
      throw new IllegalArgumentException("startDate has to be before endDate.");
    }

    this.startDate = startDate;
    this.endDate = endDate;
  }

  /**
   * Default constructor - shall not be visible for other classes. Use static method getInstance() instead.
   */
  protected Occurrence() {

  }

  /*
  * (non-Javadoc)
  *
  * @see java.lang.Object#equals(java.lang.Object)
  */
  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Occurrence other = (Occurrence) obj;
    // if item is persisted use only the id for checking
    if (id != null && id != 0L) {
      return id.equals(other.id);
    }

    if (endDate == null) {
      if (other.endDate != null) {
        return false;
      }
    } else if (!endDate.equals(other.endDate)) {
      return false;
    }
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (startDate == null) {
      if (other.startDate != null) {
        return false;
      }
    } else if (!startDate.equals(other.startDate)) {
      return false;
    }
    if (type == null) {
      if (other.type != null) {
        return false;
      }
    } else if (!type.equals(other.type)) {
      return false;
    }
    return true;
  }

  /*
  * (non-Javadoc)
  *
  * @see java.lang.Object#hashCode()
  */
  @Override
  public int hashCode() {
    // if item is persisted use only the id for calculating hash code
    if (id != null && id != 0L) {
      return id.hashCode();
    }

    final int prime = 31;
    int result = 1;
    result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  /**
   * Getter for id.
   *
   * @return the id
   */
  public Long getId() {

    return id;
  }

  /**
   * Getter for endDate.
   *
   * @return the endDate
   */
  public Calendar getEndDate() {

    return endDate;
  }

  /**
   * Setter for endDate.
   *
   * @param endDate the endDate to set
   */
  void setEndDate(Calendar endDate) {

    this.endDate = endDate;
  }

  /**
   * Getter for information.
   *
   * @return the information
   */
  public String getInformation() {

    return information;
  }

  /**
   * Setter for information.
   *
   * @param information the information to set
   */
  public void setInformation(String information) {

    this.information = information;
  }

  /**
   * Getter for startDate.
   *
   * @return the startDate
   */
  public Calendar getStartDate() {

    return startDate;
  }

  /**
   * Setter for startDate.
   *
   * @param startDate the startDate to set
   */
  void setStartDate(Calendar startDate) {

    this.startDate = startDate;
  }

  /**
   * Getter for type.
   *
   * @return the type.
   */
  public OccurrenceType getType() {

    return type;
  }

  /**
   * Setter for type.
   *
   * @param type the type to set
   */
  public void setType(OccurrenceType type) {

    if (type == null) {
      throw new IllegalArgumentException("no type given");
    }
    this.type = type;
  }

  /**
   * Setter for id.
   *
   * @param id the id to set
   */
  void setId(long id) {

    this.id = id;
  }

}
