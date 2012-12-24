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
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Utility class for standard actions on an {@link EntityManager}.
 *
 * @author johannes
 */
public final class EntityManagerUtil {
  /**
   * Logger to log actions.
   */
  private static Logger log = LoggerFactory.getLogger(EntityManagerUtil.class);

  /**
   * This {@link EntityManagerFactory} is the key for the {@link ThreadLocal} variable.
   */
  private static EntityManagerFactory emf;

  /**
   * Finds the current {@link EntityManager} which is bound to the current Thread. If there is no {@link EntityManager} mapped,
   * <code>null</code> will be returned.
   *
   * @return the bound {@link EntityManager} or <code>null</code> if no one can be found.
   */
  public static EntityManager getEntityManagerFromThread() {

    if (TransactionSynchronizationManager.hasResource(emf)) {
      EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.getResource(emf);
      return emHolder.getEntityManager();
    } else {
      return null;
    }
  }

  /**
   * Setter for the {@link EntityManagerFactory} used for finding a pre-bound {@link EntityManager} in the current
   * {@link ThreadLocal}s.
   *
   * @param entityManagerFactory the {@link EntityManagerFactory} to use as key
   */
  public static void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {

    log.debug("Receiving entity manager factory instance : {}", entityManagerFactory);
    emf = entityManagerFactory;
  }

  /**
   * Private constructors for utility classes.
   */
  private EntityManagerUtil() {

  }
}
