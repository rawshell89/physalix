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

package hsa.awp.common.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Creates a {@link EntityManagerFactory} and returns {@link EntityManager}s.
 *
 * @author klassm
 */
public final class TPersistenceUtil {
  /**
   * {@link EntityManagerFactory} creating {@link EntityManager}s.
   */
  private static EntityManagerFactory entityManagerFactory;

  /**
   * Getter for an {@link EntityManager}.
   *
   * @return new {@link EntityManager}
   */
  public static EntityManager getEntityManager() {

    if (entityManagerFactory == null) {
      entityManagerFactory = Persistence.createEntityManagerFactory("InMemory");
    }
    return entityManagerFactory.createEntityManager();
  }

  /**
   * Getter for an {@link EntityManager}.
   *
   * @param unitName persistence unit name.
   * @return new {@link EntityManager}
   */
  public static EntityManager getEntityManager(String unitName) {

    if (entityManagerFactory == null) {
      entityManagerFactory = Persistence.createEntityManagerFactory(unitName);
    }
    return entityManagerFactory.createEntityManager();
  }

  /**
   * Private constructor for forbidding instantiation.
   */
  private TPersistenceUtil() {

  }
}
