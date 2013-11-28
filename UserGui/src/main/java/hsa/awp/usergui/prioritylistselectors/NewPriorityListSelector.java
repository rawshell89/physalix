package hsa.awp.usergui.prioritylistselectors;

import hsa.awp.campaign.model.DrawProcedure;
import hsa.awp.campaign.model.PriorityList;
import hsa.awp.event.model.Category;
import hsa.awp.event.model.Event;
import hsa.awp.event.model.Subject;
import hsa.awp.gui.util.LoadableDetachedModel;
import hsa.awp.user.model.SingleUser;
import hsa.awp.usergui.controller.IUserGuiController;
import hsa.awp.usergui.util.DragAndDropableBox;
import hsa.awp.usergui.util.DragableElement;
import hsa.awp.usergui.util.DropAndSortableBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.context.SecurityContextHolder;

public class NewPriorityListSelector extends AbstractPriorityListSelector {
	/**
	 * 
	 */
	private Label messageEmpty = new Label("prioListSelector.messageEmpty",
			"Die maximale Anzahl der erlaubten Wunschlisten ist erreicht.");
	private Label messageTitle = new Label("prioListSelector.messageTitle",
			"Neue Wunschlisten");
	private Label messageSubtitle = new Label(
			"prioListSelector.messageSubtitle",
			"Diese Listen sind noch nicht gespeichert!");
	private static final long serialVersionUID = 1L;
	private IModel<List<Category>> categoryListModel;
	private List<DropAndSortableBox> dropBoxList;
	private SingleUser singleUser;
	@SpringBean(name = "usergui.controller")
	private IUserGuiController controller;
	private IModel<DrawProcedure> drawProcedureModel;
	private Form<String> form;
	private SubjectModel subjectModel;
	private ListView<Subject> subjectListView;
	private DragAndDropableBox eventsContainer;
	private Map<Long, List<Subject>> subjectCache = new HashMap<Long, List<Subject>>();
	private Map<Long, List<Event>> eventCache = new HashMap<Long, List<Event>>();

	public NewPriorityListSelector(String id, final long procId) {
		super(id);
		subjectModel = new SubjectModel();
		form = new Form<String>("prioListSelector.form");
		form.setOutputMarkupId(true);
		singleUser = controller.getUserById(SecurityContextHolder.getContext()
				.getAuthentication().getName());
		eventsContainer = new DragAndDropableBox(
				"prioListSelector.selectableObjects");
		eventsContainer.setOutputMarkupId(true);
		subjectListView = new ListView<Subject>("prioListSelector.subjects",
				subjectModel) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Subject> item) {
				final Subject sub = item.getModelObject();
				AjaxFallbackLink<Subject> link = new AjaxFallbackLink<Subject>(
						"prioListSelector.subjectLink") {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						eventsContainer.removeAllElements();
						addEventsToContainer(sub.getName(), sub.getId());
						target.addComponent(eventsContainer);
					}

					private void addEventsToContainer(String name, long id) {
						if (eventCache.get(id) == null) {
							List<Event> eventList = controller
									.findEventsBySubjectId(id);
							if (eventList != null) {
								eventCache.put(id, eventList);
								eventsContainer.addAllEvents(eventList);
							}
						} else {
							eventsContainer.addAllEvents(eventCache.get(id));
						}
					}

				};
				link.add(new Label("prioListSelector.subjectName", sub
						.getName()));
				item.add(link);
			}
		};
		drawProcedureModel = new LoadableDetachedModel<DrawProcedure>() {
			/**
		       *
		       */
			private static final long serialVersionUID = 1L;

			@Override
			protected DrawProcedure load() {

				return controller.getDrawProcedureById(procId);
			}
		};

		final IModel<List<Event>> eventlistModel = new LoadableDetachableModel<List<Event>>() {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Event> load() {

				List<Event> eventList = controller
						.convertToEventList(new ArrayList<Long>(
								drawProcedureModel.getObject().getCampaign()
										.getEventIds()));

				return filterEventListForSourcebox(eventList);
			}
		};

		categoryListModel = new LoadableDetachableModel<List<Category>>() {
			/**
				 * 
				 */
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Category> load() {

				Set<Category> categories = new TreeSet<Category>(
						new Comparator<Category>() {
							@Override
							public int compare(Category o1, Category o2) {

								return o1.getName().compareTo(o2.getName());
							}
						});

				eventlistModel.detach();
				for (Event e : eventlistModel.getObject()) {
					categories.add(e.getSubject().getCategory());
				}

				return new ArrayList<Category>(categories);
			}
		};
		ListView<Category> categoryList = new ListView<Category>(
				"prioListSelector.categories", categoryListModel) {

			/**
					 * 
					 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Category> item) {
				final Category cat = item.getModelObject();
				AjaxFallbackLink<Category> link = new AjaxFallbackLink<Category>(
						"prioListSelector.categoriesLink") {

					/**
							 * 
							 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						subjectModel.setId(cat.getId());
						target.addComponent(form);
					}
				};
				link.add(new Label("prioListSelector.categoryName", cat
						.getName()));
				item.add(link);
			}

		};
		dropBoxList = new ArrayList<DropAndSortableBox>(drawProcedureModel
				.getObject().getMaximumPriorityLists());
		IModel<Integer> prioListIterations = new LoadableDetachableModel<Integer>() {
			/**
		             *
		             */
			private static final long serialVersionUID = -2446977182797089682L;

			@Override
			protected Integer load() {

				DrawProcedure drawProcedure = drawProcedureModel.getObject();

				int i = drawProcedure.getMaximumPriorityLists();
				int j = controller.findPriorityListsByUserAndProcedure(
						singleUser.getId(), drawProcedure).size();

				return i - j;
			}
		};

		final Loop priolists = new Loop("prioListSelector.listsList",
				prioListIterations) {
			/**
		             *
		             */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(LoopItem item) {

				DrawProcedure drawProcedure = drawProcedureModel.getObject();
				DropAndSortableBox list = new DropAndSortableBox(
						"prioListSelector.prioList",
						drawProcedure.getMaximumPriorityListItems());
				list.setOutputMarkupId(true);
				item.add(new Label("prioListSelector.listName",
						"Wunschliste Kurs "
								+ (item.getIteration() + 1 + controller
										.findPriorityListsByUserAndProcedure(
												singleUser.getId(),
												drawProcedure).size())));
				dropBoxList.add(list);
				item.add(list);
			}
		};

		priolists.setOutputMarkupId(true);

		form.add(priolists);
		form.add(messageEmpty);
		form.add(messageSubtitle);
		form.add(messageTitle);
		form.add(categoryList);
		form.add(subjectListView);
		form.add(eventsContainer);
		add(form);

	}

	public List<Event> filterEventListForSourcebox(List<Event> events) {

		List<Event> eventBlackList = new LinkedList<Event>();
		/*
		 * check if event is in an uncommited priolist
		 */
		if (dropBoxList != null && dropBoxList.size() > 0) {
			for (DropAndSortableBox box : dropBoxList) {
				eventBlackList.addAll(box.getEventList());
			}
		}
		return controller.filterEventList(events, singleUser,
				drawProcedureModel.getObject(), eventBlackList);
	}

	public void moveElementsBackToSource() {

		for (DropAndSortableBox dropBox : dropBoxList) {
			for (Event event : dropBox.getEventList()) {
				eventsContainer.addElement(new DragableElement(
						DragAndDropableBox.DRAG_AND_DROPABLE_BOX_ITEM, event));
			}
		}
	}

	/**
	 * Add element to sourceBox.
	 * 
	 * @param element
	 *            element to be added.
	 */
	public void addElementToSourceBox(DragableElement element) {

		eventsContainer.addElement(element);
	}

	/**
	 * Updates the component after a priolist is deleted.
	 * 
	 * @param target
	 *            ajax target
	 * @param list
	 *            priolist which is deleted, needed to add the elements back to
	 *            source box.
	 */
	public void update(AjaxRequestTarget target, PriorityList list) {

		moveElementsBackToSource();
		eventsContainer.removeAllElements();

		categoryListModel.detach();

		target.addComponent(form);
	}

	/**
	 * Ajax update for the whole component.
	 * 
	 * @param target
	 *            ajaxrequesttarget
	 */
	public void updateLists(AjaxRequestTarget target) {

		target.addComponent(eventsContainer);
	}

	/**
	 * generated UID.
	 */

	private class SubjectModel extends LoadableDetachableModel<List<Subject>> {

		/**
	 * 
	 */
		private static final long serialVersionUID = 1L;
		private long id = -1;

		@Override
		protected List<Subject> load() {
			if (id != -1) {
				if (subjectCache.get(id) == null) {
					List<Subject> subs = controller
							.findAllSubjectsByCategoryId(id);
					if (subs != null) {
						subjectCache.put(id, subs);
						return subs;
					}
					return new ArrayList<Subject>();
				} else {
					return subjectCache.get(id);
				}
			}
			return new ArrayList<Subject>();
		}

		public void setId(long id) {
			this.id = id;
		}

	}

}
