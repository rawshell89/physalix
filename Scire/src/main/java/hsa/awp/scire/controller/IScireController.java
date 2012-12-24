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

package hsa.awp.scire.controller;

import hsa.awp.campaign.model.Campaign;
import hsa.awp.campaign.model.Procedure;
import hsa.awp.common.exception.NoMatchingElementException;
import hsa.awp.scire.procedureLogic.IProcedureLogic;

import java.util.Collection;
import java.util.Set;

public interface IScireController {
  /**
   * Looks for all active {@link Campaign}s and returns a {@link Collection} of them.
   *
   * @return {@link Collection} of {@link Campaign}s.
   */
  Collection<Campaign> findActiveCampaigns();

  /**
   * Looks for an active logic of a given {@link Campaign}.
   *
   * @param campaign {@link Campaign} to look for.
   * @return associated logic
   * @throws NoMatchingElementException if no matching logic was found.
   * @throws IllegalArgumentException   if no valid {@link Campaign} was given.
   */
  IProcedureLogic<? extends Procedure> findActiveLogicByCampaign(Campaign campaign);

  /**
   * Looks for a {@link IProcedureLogic} by its associated {@link Procedure}.
   *
   * @param id id of the {@link Procedure}.
   * @return associated {@link IProcedureLogic} or null if {@link IProcedureLogic} was not found.
   * @throws IllegalArgumentException if no id was given
   */
  IProcedureLogic<? extends Procedure> findActiveLogicByProcedure(Long id);

  /**
   * Looks for a {@link IProcedureLogic} by its associated {@link Procedure}.
   *
   * @param proc {@link Procedure} to look for.
   * @return associated {@link IProcedureLogic}.
   */
  IProcedureLogic<? extends Procedure> findActiveLogicByProcedure(Procedure proc);

  /**
   * Getter for the running {@link IProcedureLogic}s.
   *
   * @return copy of the internal {@link Set} of running {@link IProcedureLogic}s.
   */
  Set<IProcedureLogic<? extends Procedure>> getRunningProcedures();

  /**
   * Returns the current timer interval.
   *
   * @return check interval.
   */
  int getTimerInterval();

  /**
   * Getter for the state of the update Timer for the {@link Procedure} states.
   *
   * @return true if the timer is running.
   */
  boolean isCheckingForProcedureStates();

  /**
   * Sets a given Set of logic types. This will be used for finding out the appropriate logic for a {@link Procedure}.
   *
   * @param types logic type class Set.
   */
  void setProcedureLogicTypeList(Set<Class<? extends IProcedureLogic<?>>> types);

  /**
   * Sets the timer interval. Time span must be greater than zero.
   *
   * @param interval interval of the update checking in milliseconds.
   */
  void setTimerInterval(int interval);

  /**
   * Starts the timer if it is currently running.
   */
  void startTimer();

  /**
   * Stops the timer if it is currently running.
   */
  void stopTimer();
}
