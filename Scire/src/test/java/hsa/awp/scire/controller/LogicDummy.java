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

import hsa.awp.campaign.model.FifoProcedure;
import hsa.awp.campaign.model.Procedure;
import hsa.awp.scire.procedureLogic.AbstractProcedureLogic;
import hsa.awp.scire.procedureLogic.IProcedureLogic;

public class LogicDummy extends AbstractProcedureLogic<FifoProcedure> implements IProcedureLogic<FifoProcedure> {
  int afterActiveCount = 0;

  int beforeActiveCount = 0;

  int whileActiveCount = 0;

  private FifoProcedure procedure;

  public LogicDummy() {

    super(FifoProcedure.class);
  }

  @Override
  public void afterActive() {

    afterActiveCount++;
  }

  @Override
  public void beforeActive() {

    beforeActiveCount++;
  }

  @Override
  public FifoProcedure getProcedure() {

    return procedure;
  }

  @Override
  public void setProcedure(Procedure procedure) {

    try {
      this.procedure = (FifoProcedure) procedure;
    } catch (ClassCastException e) {
      throw new IllegalArgumentException(FifoProcedure.class + " expected. Got " + procedure.getClass());
    }
  }

  @Override
  public void whileActive() {

    whileActiveCount++;
  }

  /**
   * Getter for afterActiveCount.
   *
   * @return the afterActiveCount
   */
  public int getAfterActiveCount() {

    return afterActiveCount;
  }

  /**
   * Getter for beforeActiveCount.
   *
   * @return the beforeActiveCount
   */
  public int getBeforeActiveCount() {

    return beforeActiveCount;
  }

  /**
   * Getter for whileActiveCount.
   *
   * @return the whileActiveCount
   */
  public int getWhileActiveCount() {

    return whileActiveCount;
  }
}
