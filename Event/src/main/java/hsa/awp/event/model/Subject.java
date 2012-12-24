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
import java.util.HashSet;
import java.util.Set;

/**
 * Persistent object for Subjects.
 *
 * @author klassm
 */
@Entity
@Table(name = "`subject`", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "mandatorId"})
})
public class Subject extends AbstractMandatorableDomainObject<Long> {
  /**
   * Version UID which is used for serialization.
   */
  private static final long serialVersionUID = 4346114863429719061L;

  /**
   * Related {@link Category}.
   */
  @ManyToOne(cascade = {CascadeType.MERGE})
  private Category category;

  /**
   * Description of the {@link Subject}.
   */
  private String description;

  /**
   * Contains all associated events.
   */
  @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
  private Set<Event> events;

  /**
   * Identifier of the {@link Subject}.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id = 0L;

  /**
   * Link to another external resource.
   */
  private String link;

  /**
   * Name of the {@link Subject}.
   */
  private String name;


  /**
   * Creates a new {@link Subject} and initializes it with appropriate values (e.g. inserts a HashSet at appropriate places, so
   * that the Sets can be used without risking a {@link NullPointerException}.
   *
   * @return new domain object.
   */
  public static Subject getInstance(Long mandatorId) {

    Subject subject = new Subject();
    subject.setEvents(new HashSet<Event>());
    subject.setMandatorId(mandatorId);

    return subject;
  }

  /**
   * Constructor for creating a {@link Subject}.
   */
  protected Subject() {

  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Subject)) {
      return false;
    }
    Subject other = (Subject) obj;

    // if item is persisted use only the id for checking
    if (id != null && id != 0L) {
      return id.equals(other.id);
    }

    if (category == null) {
      if (other.category != null) {
        return false;
      }
    } else if (!category.equals(other.category)) {
      return false;
    }
    if (description == null) {
      if (other.description != null) {
        return false;
      }
    } else if (!description.equals(other.description)) {
      return false;
    }
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (link == null) {
      if (other.link != null) {
        return false;
      }
    } else if (!link.equals(other.link)) {
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
    result = prime * result + ((category == null) ? 0 : category.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((link == null) ? 0 : link.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
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
   * Add a single Event.
   *
   * @param event the event to be added
   */
  public void addEvent(Event event) {

    this.events.add(event);
  }

  /**
   * Getter for the related {@link Category}.
   *
   * @return related {@link Category}
   */
  public Category getCategory() {

    return category;
  }

  /**
   * Sets the related {@link Category}. This method should only be used by {@link Category}.
   *
   * @param category Category.
   */
  void setCategory(Category category) {

    this.category = category;
  }

  /**
   * Getter for the description of the {@link Subject}.
   *
   * @return description
   */
  public String getDescription() {

    return description;
  }

  /**
   * Setter for the description of the {@link Subject}.
   *
   * @param description description.
   */
  public void setDescription(String description) {

    this.description = description;
  }

  /**
   * Getter for the associated {@link Event} Set.
   *
   * @return associated {@link Event}s
   */
  public Set<Event> getEvents() {

    return events;
  }

  /**
   * Setter for the associated {@link Event} Set.
   *
   * @param events associated {@link Event}s
   */
  void setEvents(Set<Event> events) {

    this.events = events;
  }

  /**
   * Returns the external resource link.
   *
   * @return link.
   */
  public String getLink() {

    return link;
  }

  /**
   * Sets the external link for the resource.
   *
   * @param link external resource.
   */
  public void setLink(String link) {

    this.link = link;
  }

  /**
   * Getter for the name of the {@link Subject}.
   *
   * @return name
   */
  public String getName() {

    return name;
  }

  /**
   * Sets the name of the {@link Subject} to 'name'.
   *
   * @param name name
   */
  public void setName(String name) {

    this.name = name;
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
