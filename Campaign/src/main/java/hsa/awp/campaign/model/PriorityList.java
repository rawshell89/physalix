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

import hsa.awp.event.model.Event;

import javax.persistence.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Persistent object for {@link PriorityList}.
 *
 * @author klassm
 * @author johannes
 */
@Entity
@Table(name = "`prioritylist`")
@DiscriminatorValue(value = "prioritylist")
public class PriorityList extends Ticket {
  /**
   * Version UID which is used for serialization.
   */
  private static final long serialVersionUID = 4831742390570284243L;

  /**
   * List of the {@link PriorityListItem}s.
   */
  @OneToMany(targetEntity = PriorityListItem.class, fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
  private Set<PriorityListItem> items;

  /**
   * Creates a new {@link PriorityList} and initializes it with appropriate values (e.g. inserts a HashSet at appropriate places,
   * so that the Sets can be used without risking a {@link NullPointerException}.
   *
   * @return new domain object.
   */
  public static PriorityList getInstance(Long mandatorId) {

    PriorityList priorityList = new PriorityList();
    priorityList.setItems(new HashSet<PriorityListItem>());
    priorityList.setDate(Calendar.getInstance());
    priorityList.setMandatorId(mandatorId);

    return priorityList;
  }

  /**
   * Creates a new {@link PriorityList} with initialized values. The List of events has to be in the order of the requested
   * priorities.
   *
   * @param participant the participant of the {@link PriorityList}
   * @param initiator   the initiator for the {@link PriorityList}
   * @param events      the sorted list of events
   * @return the generated {@link PriorityList}
   */
  public static PriorityList getInstance(Long participant, Long initiator, List<Long> events, Long mandatorId) {

    if (events == null) {
      throw new IllegalArgumentException("the given event list is null");
    }

    PriorityList list = getInstance(mandatorId);
    list.setParticipant(participant);
    list.setInitiator(initiator);

    for (int i = 0; i < events.size(); i++) {
      list.addItem(events.get(i), i + 1);
    }

    return list;
  }

  /**
   * Adds the given event with the specified priority to this {@link PriorityList}.
   *
   * @param event    the id of the event to be added
   * @param priority the priority of the entry in this list
   * @throws IllegalArgumentException if event is <code>null</code>
   * @throws IllegalArgumentException if priority is 0 or negative
   * @throws IllegalArgumentException if a duplicate entry for the specified priority is present
   */
  public void addItem(Long event, int priority) {

    if (event == null) {
      throw new IllegalArgumentException("no event given (:null)");
    } else if (priority <= 0) {
      throw new IllegalArgumentException("negative or zero priority given");
    }

    for (PriorityListItem item : items) {
      if (item.getPriority() == priority) {
        throw new IllegalArgumentException("duplicate entry for priority '" + priority + "' given");
      }
    }

    items.add(PriorityListItem.getInstance(this, event, priority, getMandatorId()));
  }

  /**
   * Construct a new {@link PriorityList}.
   */
  PriorityList() {

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
    if (!(obj instanceof PriorityList)) {
      return false;
    }
    PriorityList other = (PriorityList) obj;

    // equality is already checked with super.equals()
    if (getId() != null && getId() != 0L) {
      return true;
    }

    if (items == null) {
      if (other.items != null) {
        return false;
      }
    } else if (!items.equals(other.items)) {
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
    result = prime * result + ((items == null) ? 0 : items.hashCode());
    return result;
  }

  @Override
  public String toString() {

    StringBuffer sb = new StringBuffer();
    sb.append("<priolist>\n");
    sb.append("id: ").append(getId()).append("\n");
    sb.append("Participant: ").append(getParticipant()).append("\n");
    sb.append("Procedure: ").append(getProcedure()).append("\n");
//        sb.append("Items:").append("\n");
//        for (PriorityListItem item : items) {
//            sb.append(" - ").append(item.getEvent()).append(" (").append(item.getPriority()).append(")\n");
//        }
    sb.append("</priolist>\n");
    return sb.toString();
  }

  /**
   * Finds the {@link PriorityListItem} with the given priority.
   *
   * @param priority the priority of the {@link PriorityListItem}
   * @return the found {@link PriorityListItem}, or <code>null</code> when no {@link PriorityListItem} can be found
   */
  public PriorityListItem getItem(int priority) {

    for (PriorityListItem item : items) {
      if (item.getPriority() == priority) {
        return item;
      }
    }
    return null;
  }

  /**
   * Getter for the associated {@link PriorityListItem}s.
   *
   * @return List of {@link Event}
   */
  public Set<PriorityListItem> getItems() {

    return items;
  }

  /**
   * Getter for the associated {@link PriorityListItem}s.
   *
   * @param items new List of {@link Event}
   */
  public void setItems(Set<PriorityListItem> items) {

    this.items = items;
  }

  /**
   * Getter of procedure.
   *
   * @return the associated {@link DrawProcedure}.
   */
  @Override
  public DrawProcedure getProcedure() {

    return (DrawProcedure) super.getProcedure();
  }

  /**
   * Checks whether a {@link PriorityList} contains an {@link Event}.
   *
   * @param eventId id of the {@link Event} to look for.
   * @return true if the event was found in the list.
   */
  public boolean hasEventId(Long eventId) {

    for (PriorityListItem item : items) {
      if (item.getEvent().equals(eventId)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Sets the referenced {@link DrawProcedure} of this {@link PriorityList}. This method should only be called from
   * {@link DrawProcedure}.
   *
   * @param procedure the {@link DrawProcedure} to reference.
   */
  void setDrawProcedure(DrawProcedure procedure) {

    super.setProcedure(procedure);
  }

  /**
   * This method is replaced by {@link #setDrawProcedure(DrawProcedure)} because of the bidirectional connection between
   * {@link DrawProcedure} and {@link PriorityList}. The initialization will be done out of the {@link DrawProcedure}.
   *
   * @param procedure a {@link Procedure}
   * @throws UnsupportedOperationException at any time.
   * @see #setDrawProcedure(DrawProcedure)
   */
  @Override
  @Deprecated
  public void setProcedure(Procedure procedure) {

    throw new UnsupportedOperationException("Please use PriorityList.setDrawProcedure instead.");
  }
}
