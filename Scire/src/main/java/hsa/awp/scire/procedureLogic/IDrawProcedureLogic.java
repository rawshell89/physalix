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

package hsa.awp.scire.procedureLogic;

import hsa.awp.campaign.model.DrawProcedure;
import hsa.awp.campaign.model.PriorityList;
import hsa.awp.common.mail.IMailFactory;
import hsa.awp.common.mail.MailFactory;
import hsa.awp.event.model.Event;
import hsa.awp.scire.exception.DuplicatePriorityListElementException;
import hsa.awp.user.model.SingleUser;
import hsa.awp.user.model.User;

import java.util.Collection;
import java.util.Set;

/**
 * Interface for {@link DrawProcedureLogic}.
 *
 * @author klassm
 */
public interface IDrawProcedureLogic extends IProcedureLogic<DrawProcedure> {
  /**
   * Method that will return true if the {@link DrawProcedure} has already been drawn.
   *
   * @return true if already been drawn.
   */
  boolean isDrawn();

  /**
   * Registers a given user with a {@link Collection} of items.
   *
   * @param prioList Ordering will decide the priority (first item is priority 1).
   * @throws IllegalStateException    if the {@link DrawProcedure} has already been drawn.
   * @throws IllegalArgumentException if no lists were given (:= null) or if items.size() is 0 or participant or initiator is null.
   */
  void register(PriorityList prioList);

  /**
   * Registers a given user with a {@link Collection} of items.
   *
   * @param lists Set whose list items each represent a PriorityList. Each list consists of {@link Event}s, whose priority will be
   *              decided according to the list ordering (first item is priority 1)
   * @throws DuplicatePriorityListElementException
   *                                  if an element happens to be found in two Collections of Event.
   * @throws IllegalStateException    if the {@link DrawProcedure} has already been drawn.
   * @throws IllegalArgumentException if no lists were given or participant or initiator is null.
   */
  void register(Set<PriorityList> lists);

  /**
   * Registers a given user with a given {@link Event}. The registration will be only valid for the exam. This is NOT supposed to
   * be a normal participation.
   *
   * @param initiator   person who started the transaction
   * @param participant person who wants to join an event
   * @param event       {@link Event} the {@link User} shall be registered for.
   */
  void registerExamOnly(SingleUser initiator, User participant, Event event);

  /**
   * Setter for the {@link MailFactory}.
   *
   * @param mailFactory {@link MailFactory}.
   */
  void setMailFactory(IMailFactory mailFactory);
}
