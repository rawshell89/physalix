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

package hsa.awp.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.TimerTask;

/**
 * This {@link ITimerTaskFactory} wraps a {@link TimerTask} into the open entity manager per request pattern.
 *
 * @author johannes
 */
public class OpenEntityManagerTimerTaskFactory implements ITimerTaskFactory {
  /**
   * {@link Logger} for this class.
   */
  private Logger log = LoggerFactory.getLogger(getClass());

  /**
   * {@link EntityManagerFactory} used to create an {@link EntityManager}.
   */
  private EntityManagerFactory emf;

  /**
   * {@link EntityManager} which is created for every execution.
   */
  private EntityManager em;

  @Override
  public TimerTask getTask(final Runnable task) {

    return new TimerTask() {
      @Override
      public void run() {

        openEntityManager();
        task.run();
        closeEntityManager();
      }
    };
  }

  /**
   * Closes the previously created {@link EntityManager}.
   */
  private void closeEntityManager() {

    log.trace("Unbinding EntityManager from thread '{}'", Thread.currentThread().getName());
    EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.unbindResource(emf);

    log.debug("Closing EntityManager");
    EntityManagerFactoryUtils.closeEntityManager(emHolder.getEntityManager());
  }

  /**
   * Opens an {@link EntityManager} per execution.
   */
  private void openEntityManager() {

    log.debug("Opening EntityManager");
    em = emf.createEntityManager();

    log.trace("Binding EntityManager to thread '{}'", Thread.currentThread().getName());
    TransactionSynchronizationManager.bindResource(emf, new EntityManagerHolder(em));
  }

  /**
   * Sets the {@link EntityManagerFactory} used for creating {@link EntityManager}s.
   *
   * @param emf the {@link EntityManagerFactory}
   */
  public void setEntityManagerFactory(EntityManagerFactory emf) {

    this.emf = emf;
  }
}
