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
import hsa.awp.common.AbstractMandatorableDomainObject;
import hsa.awp.event.model.Event;
import hsa.awp.user.model.SingleUser;

import javax.persistence.*;

/**
 * This class represents the interface of {@link Rule}s. All the {@link Rule}s have some basic conventions who are checked by
 * {@link #check(SingleUser, Campaign, Event)}.
 * <p>
 * <b>Basic conventions:</b>
 * <ul>
 * <li>If an attribute value is <code>null</code> then it will be ignored and the {@link Rule} is checked without that value</li>
 * <li>If an attribute value is initialized then the appropriate parameter must not be <code>null</code></li>
 * <li>The user must not be <code>null</code>. Never!</li>
 * </ul>
 * </p>
 *
 * @author johannes
 */
@Entity
@Table(name = "`rule`", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "mandatorId"})
})
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Rule extends AbstractMandatorableDomainObject<Long> implements IRule {
  /**
   * serial version UID used for serialization.
   */
  private static final long serialVersionUID = 4463728913513364046L;

  /**
   * Auto-generated database identifier. This id represents the {@link Rule} in the database.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id = 0L;

  /**
   * The unique name of this {@link Rule}.
   */
  @Column(nullable = false)
  private String name;


  /**
   * Default constructor.
   */
  Rule() {

  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Rule)) {
      return false;
    }
    Rule other = (Rule) obj;

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
    return result;
  }

  @Override
  public Long getId() {

    return id;
  }

  /**
   * Returns the name of this rule.
   *
   * @return the name of the rule
   */
  public String getName() {

    return name;
  }

  /**
   * Sets the name which is used to identify this rule. This name has to be unique!
   *
   * @param name the name to set
   */
  public void setName(String name) {

    this.name = name;
  }

  /**
   * Sets the id of this {@link Rule}. This method should only be used by tests and hibernate!
   *
   * @param id the new id
   */
  void setId(Long id) {

    this.id = id;
  }

}
