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

import hsa.awp.campaign.model.FifoProcedure;
import hsa.awp.common.mail.IMailFactory;
import hsa.awp.common.mail.MailFactory;
import hsa.awp.event.model.Event;
import hsa.awp.user.model.SingleUser;
import hsa.awp.user.model.User;

public interface IFifoProcedureLogic extends IProcedureLogic<FifoProcedure> {
  /**
   * Registers a given user with a given {@link Event}. The registration will be only valid for the exam.
   *
   * @param initiator   person who started the transaction
   * @param participant person who wants to join an event
   * @param event       {@link Event} the {@link User} shall be registered for.
   * @param examOnly    true if the registration will only apply for the final exam.
   */
  void register(Event event, User participant, SingleUser initiator, boolean examOnly);

  /**
   * Setter for the {@link MailFactory}.
   *
   * @param mailFactory {@link MailFactory} to set.
   */
  void setMailFactory(IMailFactory mailFactory);
}
