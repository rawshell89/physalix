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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Persistent object for {@link DrawProcedure}. {@link ConfirmedRegistration}s will be created by using a random algorithm.
 *
 * @author klassm
 */
@Entity
@Table(name = "`drawprocedure`")
public class DrawProcedure extends Procedure {
  /**
   * Version UID which is used for serialization.
   */
  private static final long serialVersionUID = 8334787389084038885L;

  /**
   * Date, when the drawing procedure starts.
   */
  @Column(nullable = false)
  private Calendar drawDate;

  /**
   * Maximum number of items that can be placed in a {@link PriorityList}.
   */
  private int maximumPriorityListItems = 0;

  /**
   * Maximum number of priority lists.
   */
  private int maximumPriorityLists = 0;

  /**
   * Associated {@link PriorityList}s.
   */
  @OneToMany(targetEntity = PriorityList.class)
  private Set<PriorityList> priorityLists;

  /**
   * Creates a new {@link DrawProcedure} and initializes it with appropriate values (e.g. inserts a HashSet at appropriate places,
   * so that the Sets can be used without risking a {@link NullPointerException}.
   *
   * @return new domain object.
   */
  public static DrawProcedure getInstance(Long mandatorId) {

    DrawProcedure drawProcedure = new DrawProcedure();
    drawProcedure.setDrawDate(Calendar.getInstance());
    drawProcedure.setInterval(Calendar.getInstance(), Calendar.getInstance());
    drawProcedure.setPriorityLists(new HashSet<PriorityList>());
    drawProcedure.setMandatorId(mandatorId);

    return drawProcedure;
  }

  /**
   * Constructs a new {@link DrawProcedure}.
   */
  DrawProcedure() {

  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof DrawProcedure)) {
      return false;
    }
    DrawProcedure other = (DrawProcedure) obj;

    // equality is already checked with super.equals()
    if (getId() != null && getId() != 0L) {
      return true;
    }

    if (drawDate == null) {
      if (other.drawDate != null) {
        return false;
      }
    } else if (!drawDate.equals(other.drawDate)) {
      return false;
    }
    if (maximumPriorityListItems != other.maximumPriorityListItems) {
      return false;
    }
    if (maximumPriorityLists != other.maximumPriorityLists) {
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
    result = prime * result + ((drawDate == null) ? 0 : drawDate.hashCode());
    result = prime * result + maximumPriorityListItems;
    result = prime * result + maximumPriorityLists;
    return result;
  }

  /**
   * Adds the given {@link PriorityList} to this {@link Procedure}. This method initializes the bidirectional connection between
   * {@link DrawProcedure} and {@link PriorityList} by invoking {@link PriorityList#setDrawProcedure(DrawProcedure)}.
   *
   * @param list the {@link PriorityList} to add.
   */
  public void addPriorityList(PriorityList list) {

    list.setDrawProcedure(this);
    priorityLists.add(list);
  }

  /**
   * Getter for the draw date.
   *
   * @return draw date.
   */
  public Calendar getDrawDate() {

    return drawDate;
  }

  /**
   * Setter for the draw date.
   *
   * @param drawDate new draw date.
   */
  public void setDrawDate(Calendar drawDate) {

    this.drawDate = drawDate;
  }

  public int getMaximumPriorityListItems() {

    return maximumPriorityListItems;
  }

  public void setMaximumPriorityListItems(int maximumPriorityListItems) {

    this.maximumPriorityListItems = maximumPriorityListItems;
  }

  public int getMaximumPriorityLists() {

    return maximumPriorityLists;
  }

  public void setMaximumPriorityLists(int maximumPriorityLists) {

    this.maximumPriorityLists = maximumPriorityLists;
  }

  /**
   * Getter for priorityLists.
   *
   * @return the priorityLists
   */
  public Set<PriorityList> getPriorityLists() {

    return priorityLists;
  }

  /**
   * Setter for priorityLists.
   *
   * @param priorityLists the priorityLists to set
   */
  void setPriorityLists(Set<PriorityList> priorityLists) {

    this.priorityLists = priorityLists;
  }
}
