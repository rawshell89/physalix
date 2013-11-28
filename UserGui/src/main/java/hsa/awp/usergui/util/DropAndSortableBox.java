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

package hsa.awp.usergui.util;

import hsa.awp.event.model.Event;
import hsa.awp.usergui.prioritylistselectors.AbstractPriorityListSelector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Panel which displays a sortable list of events.
 * 
 * @author basti
 */
public class DropAndSortableBox extends Panel {
	/**
	 * unique serialization id.
	 */
	private static final long serialVersionUID = 1929005508618747799L;

	/**
	 * Elements which the panel contains.
	 */
	private DragableElement[] elements;

	/**
	 * Targets representing each slot of the priolist.
	 */
	private List<DraggablePrioListTarget> draggablePrioListTargets;

	/**
	 * ListView which displays all targets.
	 */
	private ListView<DraggablePrioListTarget> itemContainers;

	/**
	 * MarkupBox which contains all elements, in order to support ajax updating.
	 */
	private MarkupContainer markupBox;

	/**
	 * Constructor which generates an empty priolist, with maxItems
	 * priolistItems.
	 * 
	 * @param id
	 *            wicket id
	 * @param maxItems
	 *            number of items the priolist should hold
	 */
	public DropAndSortableBox(String id, int maxItems) {

		this(id, null, maxItems, true);
	}

	/**
	 * Constructor which generates a priolist with the given elements.
	 * 
	 * @param id
	 *            wicket id
	 * @param events
	 *            eventlist with events to be displayed. list.size() should not
	 *            be grater than maxItems.
	 * @param maxItems
	 *            number of items the priolist should hold
	 * @param isActive
	 *            true if draggabiliy is given
	 */
	public DropAndSortableBox(String id, List<Event> events, int maxItems,
			boolean isActive) {

		super(id);

		markupBox = new WebMarkupContainer("DropAndSortableBox.box");
		markupBox.setOutputMarkupId(true);

		elements = new DragableElement[maxItems];

		if (events != null) {
			if (events.size() > maxItems) {
				throw new IllegalArgumentException(
						"list bigger than allowed items count");
			}
			for (Event event : events) {
				elements[events.indexOf(event)] = new DragableElement(
						"DropAndSortableBox.element", event, isActive);
			}
		}

		draggablePrioListTargets = new ArrayList<DraggablePrioListTarget>(
				maxItems);

		for (int i = 0; i < maxItems; i++) {
			draggablePrioListTargets.add(new DraggablePrioListTarget(
					"DropAndSortableBox.droptarget", DropAndSortableBox.this,
					i, elements[i], isActive));
		}

		itemContainers = new ListView<DraggablePrioListTarget>(
				"DropAndSortableBox.list", draggablePrioListTargets) {
			/**
       *
       */
			private static final long serialVersionUID = -3426894828457312814L;

			@Override
			protected void populateItem(ListItem<DraggablePrioListTarget> item) {

				draggablePrioListTargets.get(item.getIndex())
						.setOutputMarkupId(true);
				item.add(draggablePrioListTargets.get(item.getIndex()));
			}
		};

		markupBox.add(itemContainers);
		add(markupBox);
	}

	/**
	 * Generates List of all Events, which are currently in the box.
	 * 
	 * @return list of events
	 */
	public List<Event> getEventList() {

		List<Event> list = new LinkedList<Event>();

		for (DragableElement e : elements) {
			if (e != null) {
				list.add(e.getEvent());
			}
		}

		return list;
	}

	/**
	 * Handles the addition of an element into this container.
	 * 
	 * @param element
	 *            element which is dropped.
	 * @param prioList
	 *            list in which it was dropped.
	 * @param target
	 *            {@link AjaxRequestTarget} for the ajax update
	 */
	public void itemDropped(DragableElement element,
			DraggablePrioListTarget prioList, AjaxRequestTarget target) {

		int index = draggablePrioListTargets.indexOf(prioList);
		if (elements[index] == null) { /* dropped on empty slot */
			if (index == 0) {
				elements[index] = element; /*
											 * dropped in empty slot && dropped
											 * in first slot. Just add element
											 */
			} else if (elements[index - 1] != null) { /*
													 * dropped in empty slot &&
													 * previous slot is
													 * occupied. add element
													 */
				elements[index] = element;
			} else { /*
					 * dropped in empty slot && previous slot is empty. move
					 * element up.
					 */
				for (int i = 0; i < elements.length; i++) {
					if (elements[i] == null) {
						elements[i] = element;
						break;
					}
				}
			}
		} else { /* dropped in occupied slot */
			for (int i = elements.length - 1; i > index; i--) { /*
																 * move items
																 * down (last
																 * item is lost)
																 * until the
																 * index where
																 * the element
																 * was dropped
																 */
				if (i == elements.length - 1 && elements[i] != null) { /*
																		 * last
																		 * element
																		 * have
																		 * to be
																		 * moved
																		 * back
																		 * to
																		 * item
																		 * list
																		 */
					AbstractPriorityListSelector pls = findParent(AbstractPriorityListSelector.class);
					pls.addElementToSourceBox(elements[i]);
					pls.updateLists(target);
				}
				elements[i] = elements[i - 1];
			}
			elements[index] = element; /*
										 * set element to position where it was
										 * dropped
										 */
		}

		updateAll(target);
	}

	/**
	 * updates all components in this component with target.
	 * 
	 * @param target
	 *            {@link AjaxRequestTarget} for the ajax update
	 */
	private void updateAll(AjaxRequestTarget target) {

		for (DraggablePrioListTarget list : draggablePrioListTargets) {
			updateCell(list, target);
			list.update(target);
			target.addComponent(list);
		}

		target.addComponent(markupBox);
	}

	/**
	 * updates only one priolist item.
	 * 
	 * @param list
	 *            item to be updated
	 * @param target
	 *            {@link AjaxRequestTarget} for the ajax update
	 */
	private void updateCell(DraggablePrioListTarget list,
			AjaxRequestTarget target) {

		int index = draggablePrioListTargets.indexOf(list);

		if (elements.length > index) {
			if (list.getElement() != elements[index]) {
				list.setElement(elements[index]);
			}
		}
	}

	/**
	 * Debug method for displaying all elements in this container.
	 */
	@SuppressWarnings("unused")
	private void printElements() {

		System.out.print("[ ");
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] != null) {
				System.out.print(elements[i].getEvent().getSubject().getName()
						+ " | ");
			} else {
				System.out.print("null | ");
			}
		}

		System.out.println(" ]");
	}

	/**
	 * handles process when an element is moved out of this container.
	 * 
	 * @param element
	 *            element which is removed
	 * @param target
	 *            {@link AjaxRequestTarget} for the ajax update
	 */
	public void removeItem(DragableElement element, AjaxRequestTarget target) {

		boolean deleted = false;

		for (int i = 0; i < elements.length; i++) {
			if (deleted) {
				elements[i - 1] = elements[i]; /* move all following items up */
				elements[i] = null;
			} else if (elements[i] != null
					&& elements[i].getEvent().equals(element.getEvent())) {
				elements[i] = null; /* delete item */
				deleted = true;
			}
		}

		updateAll(target); /* update component */
	}
}
