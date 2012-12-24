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

package hsa.awp.common.test;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * This class simulates the {@link OpenEntityManagerInViewFilter} in integration tests. The main purpose is to hold an
 * {@link EntityManager} open over the complete test method, not only the transaction.
 *
 * @author johannes
 */
public abstract class OpenEntityManagerInTest {
  /**
   * {@link Logger} for this class.
   */
  private Logger log = LoggerFactory.getLogger(getClass());

  /**
   * {@link EntityManagerFactory} used to create an {@link EntityManager}.
   */
  @Resource(name = "entityManagerFactory")
  private EntityManagerFactory emf;

  /**
   * {@link EntityManager} which is created for every test case.
   */
  private EntityManager em;

  /**
   * Is <code>true</code>, if a new {@link EntityManager} is created when executing a test case.
   */
  private boolean create;

  /**
   * Closes the previously created {@link EntityManager}.
   */
  @After
  public final void closeEntityManager() {

    if (create) {
      log.trace("Unbinding EntityManager from thread '{}'", Thread.currentThread().getName());
      EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.unbindResource(emf);

      log.debug("Closing EntityManager");
      EntityManagerFactoryUtils.closeEntityManager(emHolder.getEntityManager());
    } else {
      log.debug("Don't close EntityManager because of @Transactional annotation");
    }
  }

  /**
   * Opens an {@link EntityManager} per test.
   */
  @Before
  public final void openEntityManager() {

    create = !TransactionSynchronizationManager.hasResource(emf);

    if (create) {
      log.debug("Opening EntityManager");
      em = emf.createEntityManager();

      log.trace("Binding EntityManager to thread '{}'", Thread.currentThread().getName());
      TransactionSynchronizationManager.bindResource(emf, new EntityManagerHolder(em));
    } else {
      log.debug("Don't open EntityManager because of @Transactional annotation");
    }
  }
}
