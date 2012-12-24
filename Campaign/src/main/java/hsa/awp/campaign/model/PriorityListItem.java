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

package hsa.awp.campaign.model;

import hsa.awp.common.AbstractMandatorableDomainObject;

import javax.persistence.*;

/**
 * Class for mapping the items of the {@link PriorityList} class.
 *
 * @author klassm
 */
@Entity
@Table(name = "`prioritylistitem`")
public class PriorityListItem extends AbstractMandatorableDomainObject<Long> {
  /**
   * Version UID which is used for serialization.
   */
  private static final long serialVersionUID = -7926096568480550771L;

  /**
   * associated {@link Event}.
   */
  @Column(nullable = false)
  private Long event;

  /**
   * unique identifier.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id = 0L;

  /**
   * Priority of the current item.
   */
  private int priority;

  /**
   * associated {@link PriorityList}.
   */
  @ManyToOne(targetEntity = PriorityList.class, cascade = {CascadeType.MERGE})
  private PriorityList priorityList;

  /**
   * Creates a new {@link PriorityListItem} and initializes it with appropriate values (e.g. inserts a HashSet at appropriate
   * places, so that the Sets can be used without risking a {@link NullPointerException}.
   *
   * @param prioList {@link PriorityList} the {@link PriorityListItem} applies to.
   * @param eventId  id of the {@link Event} the {@link PriorityListItem} applies to.
   * @param priority the priority of the {@link PriorityListItem},
   * @return new domain object.
   */
  public static PriorityListItem getInstance(PriorityList prioList, Long eventId, int priority, Long mandatorId) {

    if (prioList == null) {
      throw new IllegalArgumentException("No PriorityList given");
    } else if (eventId == null) {
      throw new IllegalArgumentException("No Event id given");
    }

    PriorityListItem priorityListItem = new PriorityListItem();

    priorityListItem.setEvent(eventId);
    priorityListItem.setPriorityList(prioList);
    priorityListItem.setPriority(priority);
    priorityListItem.setMandatorId(mandatorId);

    return priorityListItem;
  }

  /**
   * Constructs a new {@link PriorityListItem}.
   */
  PriorityListItem() {

  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof PriorityListItem)) {
      return false;
    }
    PriorityListItem other = (PriorityListItem) obj;

    // if item is persisted use only the id for checking
    if (id != null && id != 0L) {
      return id.equals(other.id);
    }

    if (event == null) {
      if (other.event != null) {
        return false;
      }
    } else if (!event.equals(other.event)) {
      return false;
    }
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (priority != other.priority) {
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
    result = prime * result + ((event == null) ? 0 : event.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + priority;
    return result;
  }

  /**
   * Returns the unique identifier.
   *
   * @return unique identifier
   */
  public Long getId() {

    return id;
  }

  /**
   * Returns the associated {@link Event}.
   *
   * @return associated {@link Event}.
   */
  public Long getEvent() {

    return event;
  }

  /**
   * Sets the associated {@link Event}.
   *
   * @param event associated {@link Event}.
   */
  public void setEvent(Long event) {

    this.event = event;
  }

  /**
   * Returns the priority of this {@link PriorityListItem}.
   *
   * @return the priority
   */
  public int getPriority() {

    return priority;
  }

  /**
   * Sets the priority of this {@link PriorityListItem}.
   *
   * @param priority the priority to set
   */
  public void setPriority(int priority) {

    this.priority = priority;
  }

  /**
   * Returns the {@link PriorityList}.
   *
   * @return {@link PriorityList}
   */
  public PriorityList getPriorityList() {

    return priorityList;
  }

  /**
   * Sets the {@link PriorityList}.
   *
   * @param priorityList {@link PriorityList}
   */
  void setPriorityList(PriorityList priorityList) {

    this.priorityList = priorityList;
  }

  /**
   * Sets the unique identifier.
   *
   * @param id unique identifier
   */
  void setId(Long id) {

    this.id = id;
  }


}
