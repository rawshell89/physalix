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

package hsa.awp.scire.procedureLogic.util;

import hsa.awp.campaign.model.ConfirmedRegistration;
import hsa.awp.campaign.model.DrawProcedure;
import hsa.awp.campaign.model.PriorityList;
import hsa.awp.user.model.SingleUser;

import java.util.LinkedList;
import java.util.List;

public class MailContent {
  private SingleUser user;

  private List<ConfirmedRegistration> registrations = new LinkedList<ConfirmedRegistration>();

  private List<PriorityList> prioLists = new LinkedList<PriorityList>();

  private DrawProcedure drawProcedure;

  public MailContent(SingleUser user) {

    this.user = user;
  }

  /**
   * @return the drawProcedure
   */
  public DrawProcedure getDrawProcedure() {

    return drawProcedure;
  }

  /**
   * @param drawProcedure the drawProcedure to set
   */
  public void setDrawProcedure(DrawProcedure drawProcedure) {

    this.drawProcedure = drawProcedure;
  }

  /**
   * @return the prioLists
   */
  public List<PriorityList> getPrioLists() {

    return prioLists;
  }

  /**
   * @param prioLists the prioLists to set
   */
  public void setPrioLists(List<PriorityList> prioLists) {

    this.prioLists = prioLists;
  }

  /**
   * @return the registrations
   */
  public List<ConfirmedRegistration> getRegistrations() {

    return registrations;
  }

  /**
   * @param registrations the registrations to set
   */
  public void setRegistrations(List<ConfirmedRegistration> registrations) {

    this.registrations = registrations;
  }

  /**
   * @return the user
   */
  public SingleUser getUser() {

    return user;
  }

  /**
   * @param user the user to set
   */
  public void setUser(SingleUser user) {

    this.user = user;
  }

  public boolean isDrawn() {

    return registrations.size() > 0;
  }


}
