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

import hsa.awp.campaign.model.Procedure;

/**
 * Interface for all Procedure logics.
 *
 * @author klassm
 */
public interface IProcedureLogic<T extends Procedure> {
  /**
   * This method is called one time when the {@link Procedure} is not active any more.
   */
  void afterActive();

  /**
   * This method is called when the {@link Procedure} becomes active the first time.
   */
  void beforeActive();

  /**
   * Getter for the associated {@link Procedure} of a ProcedureLogic.
   *
   * @return associated {@link Procedure}.
   */
  T getProcedure();

  /**
   * Setter for the associated {@link Procedure} of a ProcedureLogic.
   *
   * @param procedure associated {@link Procedure}.
   */
  void setProcedure(Procedure procedure);

  /**
   * Method that is called in periodic time intervals while the associated {@link Procedure} is active.
   */
  void whileActive();
}
