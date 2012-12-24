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

import hsa.awp.common.IGenericDomainModel;

import javax.persistence.*;

/**
 * Persistent object for StudyCourse.
 *
 * @author johannes
 */
@Entity
@Table(name = "`studycourse`")
public class StudyCourse implements IGenericDomainModel<Long> {
  /**
   * Version UID which is used for serialization.
   */
  private static final long serialVersionUID = -4855346125131156370L;

  /**
   * Unique identifier.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id = 0L;

  /**
   * Name of the {@link StudyCourse}.
   */
  @Column(unique = true, nullable = false)
  private String name;

  /**
   * Study program of the {@link StudyCourse}.
   */
  private String program;


  /**
   * Creates a new {@link StudyCourse} and initializes it with appropriate values (e.g. inserts a HashSet at appropriate places,
   * so that the Sets can be used without risking a {@link NullPointerException}.
   *
   * @param name name of the {@link StudyCourse}
   * @return new domain object.
   */
  public static StudyCourse getInstance(String name) {

    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("no name given");
    }

    StudyCourse s = new StudyCourse();
    s.setName(name);
    s.setProgram("");

    return s;
  }

  /**
   * Default constructor.
   */
  StudyCourse() {

  }

  /**
   * Constructor for creating a {@link StudyCourse}.
   *
   * @param name name of the {@link StudyCourse} (unique!)
   */
  public StudyCourse(String name) {

    this.name = name;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof StudyCourse)) {
      return false;
    }
    StudyCourse other = (StudyCourse) obj;

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
    if (program == null) {
      if (other.program != null) {
        return false;
      }
    } else if (!program.equals(other.program)) {
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
    result = prime * result + ((program == null) ? 0 : program.hashCode());
    return result;
  }

  /**
   * Getter for the unique identifier.
   *
   * @return unique identifier.
   */
  public Long getId() {

    return id;
  }

  /**
   * Name of the {@link StudyCourse}.
   *
   * @return name of the {@link StudyCourse}.
   */
  public String getName() {

    return name;
  }

  /**
   * Setter for the name of the {@link StudyCourse}.
   *
   * @param name name of the {@link StudyCourse}.
   */
  public void setName(String name) {

    this.name = name;
  }

  /**
   * Getter for the program of the {@link StudyCourse}.
   *
   * @return program of the {@link StudyCourse}.
   */
  public String getProgram() {

    return program;
  }

  /**
   * Setter for the program of the {@link StudyCourse}.
   *
   * @param program program of the {@link StudyCourse}
   */
  public void setProgram(String program) {

    this.program = program;
  }

  /**
   * Setter for the unique identifier.
   *
   * @param id unique identifier.
   */
  public void setId(Long id) {

    this.id = id;
  }


}
