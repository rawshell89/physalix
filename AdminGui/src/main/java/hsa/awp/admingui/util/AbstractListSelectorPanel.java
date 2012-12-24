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

package hsa.awp.admingui.util;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.List;

/**
 * Component for selecting List of items. The method renderObject has to be overwritten to supply the String copy of the item.
 *
 * @param <T>
 * @author Basti
 */
public abstract class AbstractListSelectorPanel<T> extends Panel {
  /**
   * generated UID.
   */
  private static final long serialVersionUID = -5838359556070311937L;

  /**
   * list with objects which can be selected.
   */
  private List<T> list;

  /**
   * list with selected objects.
   */
  private List<T> selected;

  /**
   * Constructor for AbstractListSelectorPanel which takes a list with items to be selected and a list with items which are
   * selected.
   *
   * @param id       wicket id
   * @param list     items which can be selected
   * @param selected selected items
   */
  public AbstractListSelectorPanel(String id, final List<T> list, final List<T> selected) {

    super(id);
    this.setList(list);
    this.setSelected(selected);
    final MarkupContainer container = new WebMarkupContainer("listSelector.container");
    container.setOutputMarkupId(true);
    add(container);

    final ListView<T> selectableObjects = new ListView<T>("listSelector.selectableObjects", list) {
      /**
       * generated UID.
       */
      private static final long serialVersionUID = -636057392092428374L;

      @Override
      protected void populateItem(final ListItem<T> item) {

        AjaxFallbackLink<T> link = new AjaxFallbackLink<T>("listSelector.selectableObjectLink") {
          /**
           * generated UID.
           */
          private static final long serialVersionUID = 9106963174127725034L;

          @Override
          public void onClick(AjaxRequestTarget target) {

            selected.add(item.getModelObject());
            list.remove(item.getModelObject());
            target.addComponent(container);
          }
        };
        link.add(new Label("listSelector.selectableObjectName", renderObject(item.getModelObject())));
        item.add(link);
      }
    };

    final ListView<T> selectedObjects = new ListView<T>("listSelector.selectedObjects", selected) {
      /**
       * generated UID.
       */
      private static final long serialVersionUID = -636057392092428374L;

      @Override
      protected void populateItem(final ListItem<T> item) {

        AjaxFallbackLink<T> link = new AjaxFallbackLink<T>("listSelector.selectedObjectLink") {
          /**
           * generated UID.
           */
          private static final long serialVersionUID = 9106963174127725035L;

          @Override
          public void onClick(AjaxRequestTarget target) {

            list.add(item.getModelObject());
            selected.remove(item.getModelObject());
            target.addComponent(container);
          }
        };
        link.add(new Label("listSelector.selectedObjectName", renderObject(item.getModelObject())));
        item.add(link);
        item.add(new AjaxFallbackLink<T>("listSelector.selectedObjectUp") {
          /**
           * generated UID.
           */
          private static final long serialVersionUID = -2669309594395586038L;

          @Override
          public void onClick(AjaxRequestTarget target) {

            int index = selected.indexOf(item.getModelObject());
            if (index > 0) {
              T saved = selected.get(index - 1);
              selected.set(index - 1, selected.get(index));
              selected.set(index, saved);
            }
            target.addComponent(container);
          }
        });

        item.add(new AjaxFallbackLink<T>("listSelector.selectedObjectDown") {
          /**
           * generated UID.
           */
          private static final long serialVersionUID = -2669309594395586039L;

          @Override
          public void onClick(AjaxRequestTarget target) {

            int index = selected.indexOf(item.getModelObject());
            if (index < selected.size() - 1) {
              T saved = selected.get(index + 1);
              selected.set(index + 1, selected.get(index));
              selected.set(index, saved);
            }
            target.addComponent(container);
          }
        });
      }
    };

    container.add(selectableObjects);
    container.add(selectedObjects);
  }

  /**
   * Method which handles the string representation.
   *
   * @param object Object which will be rendered.
   * @return String representation for GUI
   */
  public abstract String renderObject(T object);

  /**
   * getter for the list of the (unselected) objects.
   *
   * @return the list
   */
  public List<T> getList() {

    return list;
  }

  /**
   * Setter for unselected list.
   *
   * @param list the list to set
   */
  public void setList(List<T> list) {

    this.list = list;
  }

  /**
   * getter for the list with selected objects.
   *
   * @return the selected
   */
  public List<T> getSelected() {

    return selected;
  }

  /**
   * setter for selected list.
   *
   * @param selected the selected to set
   */
  public void setSelected(List<T> selected) {

    this.selected = selected;
  }
}
