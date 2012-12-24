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

package hsa.awp.gui;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.List;


public abstract class AbstractListSelectorPanel<T> extends Panel {
  /**
   * generated UID
   */
  private static final long serialVersionUID = -5838359556070311937L;

  private List<T> list;

  public AbstractListSelectorPanel(String id, List<T> list) {

    super(id);
    this.setList(list);

    add(new ListView<T>("listSelector.selectableObjects", list) {
      /**
       * generated UID
       */
      private static final long serialVersionUID = -636057392092428374L;

      @Override
      protected void populateItem(ListItem<T> item) {

        add(new Label("listSelector.selectableObjectName", renderObject(item.getModelObject())));
      }
    });

    add(new Button("listSelector.right"));
    add(new Button("listSelector.left"));

    add(new ListView<T>("listSelector.selectedObjects") {
      /**
       * generated UID
       */
      private static final long serialVersionUID = -636057392092428374L;

      @Override
      protected void populateItem(ListItem<T> item) {

        add(new Label("listSelector.selectedObjectName", renderObject(item.getModelObject())));
      }
    });
  }

  public abstract String renderObject(T object);

  /**
   * @return the list
   */
  public List<T> getList() {

    return list;
  }

  /**
   * @param list the list to set
   */
  public void setList(List<T> list) {

    this.list = list;
  }
}
