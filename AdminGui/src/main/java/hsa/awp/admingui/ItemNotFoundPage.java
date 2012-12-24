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

package hsa.awp.admingui;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Page that can be called when an item was not found.
 *
 * @param <T> type of element that was not found.
 * @author klassm
 */
public abstract class ItemNotFoundPage<T> extends Panel {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = 7732601578649053826L;

  /**
   * Item that was not found.
   */
  private T item;

  /**
   * Creates a new {@link ItemNotFoundPage}.
   *
   * @param id   wicket id.
   * @param item item that was not found.
   */
  public ItemNotFoundPage(String id, T item) {

    super(id);

    this.item = item;

    Label label = new Label("item", renderObject());
    add(label);
  }

  /**
   * Method to set the text that shall be displayed when an item was not found.
   *
   * @return error text.
   */
  public abstract String renderObject();

  /**
   * Getter for the item that was not found.
   *
   * @return item that was not found.
   */
  protected T getItem() {

    return item;
  }
}
