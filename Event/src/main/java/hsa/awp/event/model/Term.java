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
 * Persistent object for Terms.
 */
@Entity
@Table(name = "`term`")
public class Term extends AbstractMandatorableDomainObject<Long> {
  /**
   * Version UID which is used for serialization.
   */
  private static final long serialVersionUID = 6655845085945228964L;

  /**
   * Identifier for a {@link Term}.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id = 0L;

  /**
   * Type of term.
   */
  private String termDesc;


  /**
   * Creates a new {@link Term} and initializes it with appropriate values (e.g. inserts a HashSet at appropriate places, so that
   * the Sets can be used without risking a {@link NullPointerException}.
   *
   * @return new domain object.
   */
  public static Term getInstance(Long mandatorId) {
    Term term = new Term();
    term.setTermDesc("");
    term.setMandatorId(mandatorId);
    return term;
  }

  /**
   * Constructs a new {@link Term}.
   */
  protected Term() {

  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Term term = (Term) o;

    if (id != null ? !id.equals(term.id) : term.id != null) return false;
    return termDesc.equals(termDesc);
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
    result = prime * result + ((termDesc == null) ? 0 : termDesc.hashCode());
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

  public String getTermDesc() {
    return termDesc;
  }

  public void setTermDesc(String termDesc) {
    this.termDesc = termDesc;
  }

  /**
   * Setter for the identifier.
   *
   * @param id identifier.
   */
  void setId(Long id) {
    this.id = id;
  }

  public String toString() {
    return termDesc;
  }

}
