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

import antlr.debug.Event;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Calendar;

/**
 * Persistent object for {@link ConfirmedRegistration}.
 *
 * @author klassm
 */
@Entity
@Table(name = "`confirmedregistration`")
@DiscriminatorValue(value = "confirmedregistration")
public class ConfirmedRegistration extends Ticket {
  /**
   * Version UID which is used for serialization.
   */
  private static final long serialVersionUID = -2871554270139469396L;

  /**
   * Associated event.
   */
  @Column(nullable = false)
  private Long eventId;

  /**
   * True if the participant will not hear the subject but will only show up to the exam.
   */
  @Column(nullable = false)
  private boolean examOnly;

  /**
   * Creates a new {@link ConfirmedRegistration} and initializes it with appropriate values (e.g. inserts a HashSet at appropriate
   * places, so that the Sets can be used without risking a {@link NullPointerException}.
   *
   * @param eventId id of the {@link Event} the {@link ConfirmedRegistration} applies to.
   * @return new domain object.
   */
  public static ConfirmedRegistration getInstance(Long eventId, Long mandatorId) {

    ConfirmedRegistration confirmedRegistration = new ConfirmedRegistration();

    confirmedRegistration.setDate(Calendar.getInstance());
    confirmedRegistration.setEventId(eventId);
    confirmedRegistration.setExamOnly(false);
    confirmedRegistration.setMandatorId(mandatorId);

    return confirmedRegistration;
  }

  /**
   * Creates a new {@link ConfirmedRegistration} and initializes it with appropriate values (e.g. inserts a HashSet at appropriate
   * places, so that the Sets can be used without risking a {@link NullPointerException}.
   *
   * @param item the {@link PriorityListItem} which delivers the data to create a new {@link ConfirmedRegistration}.
   * @return new domain object.
   */
  public static ConfirmedRegistration getInstance(PriorityListItem item, Long mandatorId) {

    PriorityList list = item.getPriorityList();

    ConfirmedRegistration c = ConfirmedRegistration.getInstance(item.getEvent(), mandatorId);
    c.setInitiator(list.getInitiator());
    c.setParticipant(list.getParticipant());
    c.setProcedure(list.getProcedure());

    return c;
  }

  /**
   * Creates a new {@link ConfirmedRegistration}.
   */
  ConfirmedRegistration() {

  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof ConfirmedRegistration)) {
      return false;
    }
    ConfirmedRegistration other = (ConfirmedRegistration) obj;

    // equality is already checked with super.equals()
    if (getId() != null && getId() != 0L) {
      return true;
    }

    if (eventId == null) {
      if (other.eventId != null) {
        return false;
      }
    } else if (!eventId.equals(other.eventId)) {
      return false;
    }
    if (examOnly != other.examOnly) {
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
    result = prime * result + ((eventId == null) ? 0 : eventId.hashCode());
    result = prime * result + (examOnly ? 1231 : 1237);
    return result;
  }

  /**
   * Getter for the associated {@link Event}.
   *
   * @return Identifier of the associated {@link Event}.
   */
  public Long getEventId() {

    return eventId;
  }

  /**
   * Sets the eventId.
   *
   * @param eventId identifier.
   */
  public void setEventId(long eventId) {

    this.eventId = eventId;
  }

  /**
   * Getter for whether a {@link ConfirmedRegistration} is only for joining the exam.
   *
   * @return true if examOnly.
   */
  public boolean isExamOnly() {

    return examOnly;
  }

  /**
   * Getter for whether a {@link ConfirmedRegistration} is only for joining the exam.
   *
   * @param examOnly true if examOnly.
   */
  public void setExamOnly(boolean examOnly) {

    this.examOnly = examOnly;
  }
}
