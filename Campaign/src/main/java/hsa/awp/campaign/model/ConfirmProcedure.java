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

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Persistent object for {@link ConfirmProcedure}. This procedure is used to represent the livecycle of confirming the registrations
 * which are done in former procedures.
 *
 * @author johannes
 */
@Entity
@Table(name = "`confirmprocedure`")
public class ConfirmProcedure extends Procedure {
  /**
   * Version UID which is used for serialization.
   */
  private static final long serialVersionUID = -6391240383886332584L;

  /**
   * Set of {@link Ticket}s which have to be confirmed.
   */
  @OneToMany(targetEntity = Ticket.class)
  private Set<Ticket> tickets;

  /**
   * Creates a fully initialized {@link ConfirmProcedure} without risking a {@link NullPointerException} when operating on
   * {@link Set} references.
   *
   * @return the initialized {@link ConfirmProcedure}
   */
  public static ConfirmProcedure getInstance(Long mandatorId) {

    ConfirmProcedure confirmProcedure = new ConfirmProcedure();

    confirmProcedure.setInterval(Calendar.getInstance(), Calendar.getInstance());
    confirmProcedure.setTickets(new HashSet<Ticket>());
    confirmProcedure.setMandatorId(mandatorId);

    return confirmProcedure;
  }

  /**
   * Default empty constructor.
   */
  ConfirmProcedure() {

  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof ConfirmProcedure)) {
      return false;
    }
//        ConfirmProcedure other = (ConfirmProcedure) obj;
//
//        // equality is already checked with super.equals()
//        if (getId() != null && getId() != 0L) {
//            return true;
//        }

    return true;
  }

  @Override
  public int hashCode() {
    // if item is persisted use only the id for calculating hash code
    if (getId() != null && getId() != 0L) {
      return super.hashCode();
    }

//        final int prime = 31;
    int result = super.hashCode();
//        result = prime * result + ((tickets == null) ? 0 : tickets.hashCode());
    return result;
  }

  /**
   * Adds a {@link Ticket} to the {@link Set} of {@link Ticket}s which have to be confirmed. Indicates whether the Ticket was
   * added or rejected through the return value.
   *
   * @param ticket the new {@link Ticket} to be confirmed.
   * @return <code>true</code> if the given {@link Ticket} was added. <code>false</code> if it's already added.
   */
  public synchronized boolean addTicket(Ticket ticket) {

    return this.tickets.add(ticket);
  }

  // TODO Jojo : Method naming.

  /**
   * Confirms a {@link Ticket}. This operation will remove the given {@link Ticket} from the list.
   *
   * @param ticket the {@link Ticket} to be confirmed
   * @return the confirmed {@link Ticket}
   */
  public synchronized Ticket confirmTicket(Ticket ticket) {

    this.tickets.remove(ticket);

    return ticket;
  }

  /**
   * Returns tickets.
   *
   * @return the tickets
   */
  public Set<Ticket> getTickets() {

    return tickets;
  }

  /**
   * Setter for tickets.
   *
   * @param tickets the tickets to set
   */
  void setTickets(Set<Ticket> tickets) {

    this.tickets = tickets;
  }
}
