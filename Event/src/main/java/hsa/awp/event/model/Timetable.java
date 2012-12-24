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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Persistent object for Timetables.
 *
 * @author klassm
 */
@Entity
@Table(name = "`timetable`")
public class Timetable extends AbstractMandatorableDomainObject<Long> {
  /**
   * Version UID which is used for serialization.
   */
  private static final long serialVersionUID = 6976426469767546165L;

  /**
   * Identifier for a {@link Timetable}.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id = 0L;

  /**
   * List of associated {@link Occurrence} instances.
   */
  @OneToMany(targetEntity = Occurrence.class, cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
  private List<Occurrence> occurrences;


  /**
   * Creates a new {@link Timetable} and initializes it with appropriate values (e.g. inserts a HashSet at appropriate places, so
   * that the Sets can be used without risking a {@link NullPointerException}.
   *
   * @return new domain object.
   */
  public static Timetable getInstance(Long mandatorId) {

    Timetable timetable = new Timetable();
    timetable.occurrences = new LinkedList<Occurrence>();
    timetable.setMandatorId(mandatorId);

    return timetable;
  }

  /**
   * Constructs a new {@link Timetable}.
   */
  protected Timetable() {

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
    Timetable other = (Timetable) obj;

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
    if (occurrences == null) {
      if (other.occurrences != null) {
        return false;
      }
    } else if (!occurrences.equals(other.occurrences)) {
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
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((occurrences == null) ? 0 : occurrences.hashCode());
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
   * Adds a new {@link Occurrence} to the list of associated {@link Occurrence}s.
   *
   * @param occurrence {@link Occurrence} to add.
   * @throws IllegalArgumentException if occurrence is null
   * @throws IllegalArgumentException if occurrence is already contained in the list of associated {@link Occurrence}s.
   */
  public void addOccurrence(Occurrence occurrence) {

    if (occurrence == null) {
      throw new IllegalArgumentException("no occurrence given.");
    } else if (occurrences.contains(occurrence)) {
      throw new IllegalArgumentException(
          "cannot add occurrence - it is already contained in the list of associated occurences");
    }

    occurrences.add(occurrence);
  }

  /**
   * Getter for occurences.
   *
   * @return the occurences
   */
  public Collection<Occurrence> getOccurrences() {

    return (Collection<Occurrence>) occurrences;
  }

  /**
   * Adds a new {@link Occurrence} to the list of associated {@link Occurrence}s.
   *
   * @param occurrence {@link Occurrence} to remove.
   * @throws IllegalArgumentException if occurrence is null
   * @throws IllegalArgumentException if occurrence is not contained in the list of associated {@link Occurrence}s.
   */
  public void removeOccurrence(Occurrence occurrence) {

    if (occurrence == null) {
      throw new IllegalArgumentException("no occurrence given.");
    } else if (!occurrences.contains(occurrence)) {
      throw new IllegalArgumentException(
          "cannot remove occurrence - it is not contained in the list of associated occurences");
    }

    occurrences.remove(occurrence);
  }

  /**
   * Setter for the identifier.
   *
   * @param id identifier.
   */
  void setId(long id) {

    this.id = id;
  }

  /**
   * Setter for id.
   *
   * @param id the id to set
   */
  public void setId(Long id) {

    this.id = id;
  }

  /**
   * Setter for occurences.
   *
   * @param occurences the occurences to set
   */
  void setOccurences(List<Occurrence> occurences) {

    this.occurrences = occurences;
  }

}
