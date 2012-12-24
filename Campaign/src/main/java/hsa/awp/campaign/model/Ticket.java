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
import java.util.Calendar;

/**
 * Persistent object for Ticket. Purpose of this object is logging!
 *
 * @author klassm
 */
@Entity
@Table(name = "`ticket`")
@Inheritance(strategy = InheritanceType.JOINED)
public class Ticket extends AbstractMandatorableDomainObject<Long> {
  /**
   * Version UID which is used for serialization.
   */
  private static final long serialVersionUID = 2740571311726479433L;

  /**
   * Action was done on a certain date.
   */
  @Column(nullable = false)
  private Calendar date;

  /**
   * Unique identifier.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id = 0L;

  /**
   * Action was done by a certain person.
   */
  @Column(nullable = false)
  private Long initiator;

  /**
   * Ticket concerns a certain person.
   */
  @Column(nullable = false)
  private Long participant;

  /**
   * {@link Ticket} was created by a distinct {@link Procedure}.
   */
  @OneToOne(cascade = {CascadeType.MERGE})
  @JoinColumn(name = "PROCEDURE_ID")
  private Procedure procedure;


  /**
   * Constructs a new Ticket.
   */
  Ticket() {

  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Ticket)) {
      return false;
    }
    Ticket other = (Ticket) obj;

    // if item is persisted use only the id for checking
    if (id != null && id != 0L) {
      return id.equals(other.id);
    }

    if (date == null) {
      if (other.date != null) {
        return false;
      }
    } else if (!date.equals(other.date)) {
      return false;
    }
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (initiator == null) {
      if (other.initiator != null) {
        return false;
      }
    } else if (!initiator.equals(other.initiator)) {
      return false;
    }
    if (participant == null) {
      if (other.participant != null) {
        return false;
      }
    } else if (!participant.equals(other.participant)) {
      return false;
    }
    if (procedure == null) {
      if (other.procedure != null) {
        return false;
      }
    } else if (!procedure.equals(other.procedure)) {
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
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((initiator == null) ? 0 : initiator.hashCode());
    result = prime * result + ((participant == null) ? 0 : participant.hashCode());
    result = prime * result + ((procedure == null) ? 0 : procedure.hashCode());
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
   * Getter for the date, when the action was executed.
   *
   * @return date, when the action was executed.
   */
  public Calendar getDate() {

    return date;
  }

  /**
   * Setter for the date, when the action was executed.
   *
   * @param date date, when the action was executed.
   */
  public void setDate(Calendar date) {

    this.date = date;
  }

  /**
   * Getter for the person having executed the action.
   *
   * @return initiator.
   */
  public Long getInitiator() {

    return initiator;
  }

  /**
   * Setter for the person having executed the action.
   *
   * @param initiator initiator.
   */
  public void setInitiator(Long initiator) {

    this.initiator = initiator;
  }

  /**
   * Getter for the concerned person.
   *
   * @return concerned person.
   */
  public Long getParticipant() {

    return participant;
  }

  /**
   * Setter for the concerned person.
   *
   * @param participant concerned person
   */
  public void setParticipant(Long participant) {

    this.participant = participant;
  }

  /**
   * Getter for procedure.
   *
   * @return the procedure
   */
  public Procedure getProcedure() {

    return procedure;
  }

  /**
   * Setter of procedure.
   *
   * @param procedure the procedure to set
   */
  public void setProcedure(Procedure procedure) {

    this.procedure = procedure;
  }

  /**
   * Sets the identifier.
   *
   * @param id identifier
   */
  void setId(Long id) {

    this.id = id;
  }


}
