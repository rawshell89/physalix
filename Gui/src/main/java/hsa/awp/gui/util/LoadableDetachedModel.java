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

package hsa.awp.gui.util;

import org.apache.wicket.model.IModel;

/**
 * Class representing for which it is possible to specify a load method. In difference to the {@link LoadableDetachedModel} of
 * wicket it is possible to call a detach() method allowing it to reload the modelObject. Using the {@link LoadableDetachedModel}
 * the modelObject will be reloaded only one time - at the first request needing it.
 *
 * @param <T> type of model to store.
 * @author klassm
 */
public abstract class LoadableDetachedModel<T> implements IModel<T>, ILoadableDetachedModel<T> {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = 1520665972469427440L;

  /**
   * Internal modelObject to store.
   */
  private transient T modelObject;

  @Override
  public void detach() {

    modelObject = null;
  }

  @Override
  public T getObject() {

    if (modelObject == null) {
      modelObject = load();
    }
    return modelObject;
  }

  @Override
  public void setObject(T object) {

    modelObject = object;
  }

  /**
   * Method that loads the modelObject. This can be used for specifying database access methods.
   *
   * @return new modelObject to store.
   */
  protected abstract T load();
}
