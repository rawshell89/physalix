package hsa.awp.usergui.util.DragAndDrop;

import hsa.awp.event.model.Event;
import hsa.awp.usergui.prioritylistselectors.NewPriorityListSelector;
import hsa.awp.usergui.util.DragAndDropableBox;
import hsa.awp.usergui.util.DragableElement;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.SimpleAttributeModifier;

public class DragAndSortableBoxWRules extends AbstractDragAndDrop {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long subjectId = -1;

	public DragAndSortableBoxWRules(String id, int maxItems) {
		super(id, maxItems);
	}

	public DragAndSortableBoxWRules(String id, List<Event> events,
			int maxItems, boolean isActive) {
		super(id, events, maxItems, isActive);
	}

	@Override
	public boolean isAddingAllowed(DragableElement element,
			AjaxRequestTarget target) {
		long elementSubjectId = element.getEvent().getSubject().getId();
		if (subjectId == -1 || subjectId == elementSubjectId) {
			NewPriorityListSelector selector = findParent(NewPriorityListSelector.class);
			if (selector != null) {
				List<DragAndSortableBoxWRules> lists = selector.getLists();
				List<Long> alreadySettedLists = selector
						.getSubjectIdsFromSavedLists();
				if (checkListsForItem(alreadySettedLists, lists, element,
						target)) {
					subjectId = elementSubjectId;
					DragAndDropableBox box = element.findParent(DragAndDropableBox.class);
					if(box != null)
						box.add(new SimpleAttributeModifier("style", getColor()));
					return true;
				}
				changeDialogContentAndShow(target, "Es ist nur eine Liste pro Fach erlaubt. Bitte überprüfen Sie ihre Eingaben!");
				return false;
			}
		}
		changeDialogContentAndShow(target, "Es ist nur ein Fach pro Liste erlaubt.");
		return false;
	}

	@Override
	public boolean removeItem(DragableElement element, AjaxRequestTarget target) {
		boolean lastElement = false;
		boolean deleted = false;
		DragableElement[] elements = getElements();
		for (int i = 0; i < elements.length; i++) {
			if (deleted) {
				elements[i - 1] = elements[i]; /* move all following items up */
				elements[i] = null;
			} else if (elements[i] != null
					&& elements[i].getEvent().equals(element.getEvent())) {
				elements[i] = null; /* delete item */
				deleted = true;
				if (isLastItemRemoved(elements)) {
					subjectId = -1;
					lastElement = true;
				}
			}
		}
		updateAll(target); /* update component */
		return lastElement;
	}

	private boolean isLastItemRemoved(DragableElement[] elements) {
		for (int i = 0; i < elements.length; i++) {
			if (elements[i] != null)
				return false;
		}
		return true;
	}

	private boolean checkListsForItem(List<Long> savedLists,
			List<DragAndSortableBoxWRules> drags, DragableElement element,
			AjaxRequestTarget target) {
		long subjectId = element.getEvent().getSubject().getId();
		for (Long id : savedLists) {
			if (subjectId == id)
				return false;
		}
		for (DragAndSortableBoxWRules drag : drags) {
			if (drag != this) {
				if (drag.subjectId == subjectId) {
					DragableElement[] elements = drag.getElements();
					int moreThanOne = 0;
					boolean containsEvent = false;
					for (int i = 0; i < elements.length; i++) {
						if (elements[i] != null) {
							moreThanOne++;
							if (elements[i].getEvent() == element.getEvent())
								containsEvent = true;
						}
					}
					if (moreThanOne == 1 && containsEvent) {
						drag.removeItem(element, target);
						return true;
					}
					return false;
				}
			}
		}
		return true;
	}
	
	public long getSubjectId(){
		return subjectId;
	}

}
