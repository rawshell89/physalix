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

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.wicketstuff.scriptaculous.ScriptaculousAjaxBehavior;
import org.wicketstuff.scriptaculous.effect.Effect;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Extended version of {@link AbstractListSelectorPanel}. The select list as well as the item list will be sort by a given
 * {@link Comparator}. In addition to a name, a prefix and a description can be given.
 *
 * @param <T> type of Element to add to the lists.
 * @author klassm
 */
public abstract class AbstractSortedListSelectorPanel<T> extends Panel {
  /**
   * unique identifier.
   */
  private static final long serialVersionUID = -5838359556070311937L;

  /**
   * Elements to select from.
   */
  private List<T> list;

  private ListView<T> selectedObjects;

  private ListView<T> selectableObjects;

  /**
   * Selected elements.
   */
  private List<T> selected;

  /**
   * Container which contains all objects which have to be updated.
   */
  private final MarkupContainer container = new WebMarkupContainer("sortedListSelector.container");

  /**
   * Creates a new {@link AbstractSortedListSelectorPanel}.
   *
   * @param id       wicket id
   * @param list     available elements.
   * @param selected selected elements.
   */
  public AbstractSortedListSelectorPanel(String id, final List<T> list, final List<T> selected) {

    super(id);

    if (list == null) {
      throw new IllegalArgumentException("no list of available elements given");
    }

    if (selected == null) {
      throw new IllegalArgumentException("no list of selected elements given");
    }

    add(ScriptaculousAjaxBehavior.newJavascriptBindingBehavior());

    setList(list);
    setSelected(selected);

    container.setOutputMarkupId(true);
    add(container);

    selectableObjects = new ListView<T>("sortedListSelector.selectableObjects", list) {
      /**
       * generated UID.
       */
      private static final long serialVersionUID = -636057392092428374L;

      @Override
      protected void populateItem(final ListItem<T> item) {

        AjaxFallbackLink<T> link = new AjaxFallbackLink<T>("sortedListSelector.selectableObjectLink") {
          /**
           * generated UID.
           */
          private static final long serialVersionUID = 9106963174127725034L;

          @Override
          public void onClick(AjaxRequestTarget target) {

            selectItem(item.getModelObject(), target, this);
          }
        };

        String name = renderName(item.getModelObject());
        String prefix = renderPrefix(item.getModelObject());
        String description = renderDescription(item.getModelObject());

        Label lName = new Label("sortedListSelector.selectableObjectName", name);
        Label lPrefix = new Label("sortedListSelector.selectableObjectPrefix", prefix);
        Label lDescription = new Label("sortedListSelector.selectableObjectDescription", description);

        if (prefix == null) {
          lPrefix.setVisible(false);
        }

        if (description == null) {
          lDescription.setVisible(false);
        }

        link.add(lName);
        link.add(lPrefix);
        link.add(lDescription);

        item.add(link);
      }
    };

    selectedObjects = new ListView<T>("sortedListSelector.selectedObjects", selected) {
      /**
       * generated UID.
       */
      private static final long serialVersionUID = -636057392092428374L;

      @Override
      protected void populateItem(final ListItem<T> item) {

        AjaxFallbackLink<T> link = new AjaxFallbackLink<T>("sortedListSelector.selectedObjectLink") {
          /**
           * generated UID.
           */
          private static final long serialVersionUID = 9106963174127725035L;

          @Override
          public void onClick(AjaxRequestTarget target) {

            unselectItem(item.getModelObject(), target, this);
          }
        };

        String name = renderName(item.getModelObject());
        String prefix = renderPrefix(item.getModelObject());
        String description = renderDescription(item.getModelObject());

        Label lName = new Label("sortedListSelector.selectedObjectName", name);
        Label lPrefix = new Label("sortedListSelector.selectedObjectPrefix", prefix);
        Label lDescription = new Label("sortedListSelector.selectedObjectDescription", description);

        if (prefix == null) {
          lPrefix.setVisible(false);
        }

        if (description == null) {
          lDescription.setVisible(false);
        }

        link.add(lName);
        link.add(lPrefix);
        link.add(lDescription);

        item.add(link);
      }
    };

    container.add(selectableObjects);
    container.add(selectedObjects);

    container.add(new AjaxFallbackLink<T>("sortedListSelector.selectAll") {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 1L;

      @Override
      public void onClick(AjaxRequestTarget target) {

        selectAll(target);
      }
    });

    container.add(new AjaxFallbackLink<T>("sortedListSelector.unselectAll") {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = -2663064005509476047L;

      @Override
      public void onClick(AjaxRequestTarget target) {

        unselectAll(target);
      }
    });
  }

  /**
   * Setter for the list of available items.
   *
   * @param list available items.
   */
  public void setList(List<T> list) {

    this.list = list;
    Collections.sort(list, getComparator());
  }

  /**
   * Getter for the {@link Comparator} determining the order of both select and element list.
   *
   * @return {@link Comparator}.
   */
  protected abstract Comparator<T> getComparator();

  /**
   * Setter for the list of selected items.
   *
   * @param selected selected items
   */
  public void setSelected(List<T> selected) {

    this.selected = selected;
    Collections.sort(selected, getComparator());
  }

  /**
   * Selects an object.
   *
   * @param item      item to be selected.
   * @param target    target to update component
   * @param component component to highlight
   */
  private void selectItem(T item, AjaxRequestTarget target, Component component) {

    if (!vetoSelect(item)) {
      selected.add(item);
      list.remove(item);
      target.addComponent(container);

      Collections.sort(selected, getComparator());
      Collections.sort(list, getComparator());
    } else {
      Effect.Highlight effect = new Effect.Highlight(component);
      effect.setEndColor("ffffff");
      effect.setStartColor("ff0000");
      effect.setRestoreColor("ffffff");

      target.appendJavascript(effect.toJavascript());
    }
  }

  /**
   * Method called whenever an object is selected.
   *
   * @param object object to veto
   * @return true if the select operation shall not be executed.
   */
  protected boolean vetoSelect(T object) {

    return false;
  }

  /**
   * Unselect an object.
   *
   * @param item   item to be unselected.
   * @param target target to update component * @param component component to highlight
   */
  private void unselectItem(T item, AjaxRequestTarget target, Component component) {

    if (!vetoUnselect(item)) {
      list.add(item);
      selected.remove(item);
      target.addComponent(container);

      Collections.sort(selected, getComparator());
      Collections.sort(list, getComparator());
    } else {
      Effect.Highlight effect = new Effect.Highlight(component);
      effect.setEndColor("ffffff");
      effect.setStartColor("ff0000");
      effect.setRestoreColor("ffffff");

      target.appendJavascript(effect.toJavascript());
    }
  }

  /**
   * Method called whenever an object is unselected.
   *
   * @param object object to veto
   * @return true if the unselect operation shall not be executed.
   */
  protected boolean vetoUnselect(T object) {

    return false;
  }

  /**
   * Renders the name of an object.
   *
   * @param object object to render
   * @return name as {@link String}.
   */
  protected abstract String renderName(T object);

  /**
   * Renders the prefix of an object.
   *
   * @param object object to render
   * @return prefix as {@link String}.
   */
  protected String renderPrefix(T object) {

    return "";
  }

  /**
   * Renders the description of an object.
   *
   * @param object object to render
   * @return description as {@link String}.
   */
  protected String renderDescription(T object) {

    return "";
  }

  /**
   * Selects all selectable objects.
   *
   * @param target target to update component.
   */
  private void selectAll(AjaxRequestTarget target) {

    List<T> tempList = new LinkedList<T>();

    for (T item : list) {
      if (!vetoSelect(item)) {
        tempList.add(item);
      }
    }
    for (T item : tempList) {
      selectItem(item, target, selectableObjects);
    }
  }

  /**
   * Unselects all selected objects.
   *
   * @param target target to update component.
   */
  private void unselectAll(AjaxRequestTarget target) {

    List<T> tempList = new LinkedList<T>();

    for (T item : selected) {
      if (!vetoUnselect(item)) {
        tempList.add(item);
      }
    }
    for (T item : tempList) {
      unselectItem(item, target, selectedObjects);
    }
  }

  /**
   * Getter for the available items.
   *
   * @return available items.
   */
  public List<T> getList() {

    return list;
  }

  /**
   * Getter for the selected items.
   *
   * @return selected items.
   */
  public List<T> getSelected() {

    return selected;
  }


}
