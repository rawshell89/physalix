package hsa.awp.usergui.prioritylistselectors;

import hsa.awp.campaign.model.DrawProcedure;
import hsa.awp.campaign.model.PriorityList;
import hsa.awp.campaign.model.PriorityListItem;
import hsa.awp.event.model.Category;
import hsa.awp.event.model.Event;
import hsa.awp.event.model.Subject;
import hsa.awp.gui.util.LoadableDetachedModel;
import hsa.awp.user.model.SingleUser;
import hsa.awp.usergui.OnePanelPage;
import hsa.awp.usergui.controller.IUserGuiController;
import hsa.awp.usergui.registrationmanagement.DrawRegistrationManagementPanel;
import hsa.awp.usergui.util.DragAndDropableBox;
import hsa.awp.usergui.util.DragableElement;
import hsa.awp.usergui.util.DragAndDrop.AbstractDragAndDrop;
import hsa.awp.usergui.util.DragAndDrop.DragAndSortableBoxWRules;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
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
	private List<DragAndSortableBoxWRules> dropBoxList;
	private SingleUser singleUser;
	@SpringBean(name = "usergui.controller")
	private IUserGuiController controller;
	private IModel<DrawProcedure> drawProcedureModel;
	private Form<String> form;
	private SubjectModel subjectModel;
	private static Button submitButton;
	private DropDownChoice<Subject> subjectList;
	private FeedbackPanel feedbackPanel = new FeedbackPanel("prio.feedback");
	private DragAndDropableBox eventsContainer;
	private Map<Long, List<Subject>> subjectCache = new HashMap<Long, List<Subject>>();
	private Map<Long, List<Event>> eventCache = new HashMap<Long, List<Event>>();
	private MarkupContainer box;
	private MarkupContainer updateContainer;

	public NewPriorityListSelector(String id, final long procId) {
		super(id);
		this.setOutputMarkupId(true);
		box = new WebMarkupContainer("prioListSelector.box");
		updateContainer = new WebMarkupContainer("subject.updateContainer");
		updateContainer.setOutputMarkupId(true);
		box.setOutputMarkupId(true);
		feedbackPanel.setOutputMarkupId(true);
		subjectModel = new SubjectModel();
		form = new Form<String>("prioListSelector.form");
		form.setOutputMarkupId(true);
		singleUser = controller.getUserById(SecurityContextHolder.getContext()
				.getAuthentication().getName());
		eventsContainer = new DragAndDropableBox(
				"prioListSelector.selectableObjects");
		eventsContainer.setOutputMarkupId(true);
		
		subjectList = new DropDownChoice<Subject>("prioListSelector.subjects", new Model<Subject>(),
				subjectModel, new ChoiceRenderer<Subject>(){

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					
					public String getDisplayValue(Subject sub){
						return sub.getName();
					}
			
		});
		
		subjectList.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Subject sub = subjectList.getModelObject();
				eventsContainer.removeAllElements();
				if(sub != null){
					eventsContainer.setComponentId(sub.getId());
					addEventsToContainer(sub.getId());
				}
				target.addComponent(eventsContainer);
			}

			private void addEventsToContainer(long id) {
				if (eventCache.get(id) == null) {
					List<Event> eventList = controller
							.findEventsBySubjectId(id);
					if (eventList != null) {
						eventCache.put(id, eventList);
						List<Event> filteredList = filterEventListForSourcebox(eventCache
								.get(id));
						eventsContainer.addAllEvents(filteredList);
					}
				} else {
					List<Event> filteredList = filterEventListForSourcebox(eventCache
							.get(id));
					eventsContainer.addAllEvents(filteredList);
				}
			}

		});
		
		updateContainer.add(subjectList);
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
		final DropDownChoice<Category> categoryList = new DropDownChoice<Category>("prioListSelector.categories", new Model<Category>(), categoryListModel, new ChoiceRenderer<Category>() {
		
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public String getDisplayValue(Category cat){
				return cat.getName();
			}
			
		});
		categoryList.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 8894656604521410486L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				Category cat = categoryList.getModelObject();
				subjectModel.reset();
				if(cat != null)
					subjectModel.setId(cat.getId());
				eventsContainer.removeAllElements();
				target.addComponent(updateContainer);
				target.addComponent(eventsContainer);
			}
		});
		
		dropBoxList = new ArrayList<DragAndSortableBoxWRules>(drawProcedureModel
				.getObject().getMaximumPriorityLists());
		
		
		submitButton = new Button("prioListSelector.submit") {
		      /**
		       * generated UID.
		       */
		      private static final long serialVersionUID = -1440808750941977688L;

		      @Override
		      public void onSubmit() {

		        Set<List<Event>> lists = new HashSet<List<Event>>();

		        for (AbstractDragAndDrop box : dropBoxList) {
		          if (box.getEventList().size() > 0) {
		            lists.add(box.getEventList());
		          }
		        }

		        if (lists.size() != 0) {
		          drawProcedureModel.detach();
		          try {
		            DrawProcedure drawProcedure = drawProcedureModel.getObject();
		            controller.createPrioList(SecurityContextHolder.getContext().getAuthentication().getName(),
		                SecurityContextHolder.getContext().getAuthentication().getName(), lists, drawProcedure
		                .getCampaign());
		            setResponsePage(new OnePanelPage(new NewPriorityListSelector(OnePanelPage.getPanelIdOne(), drawProcedure.getId())));
		          } catch (IllegalArgumentException e) {
		            moveElementsBackToSource();
		            feedbackPanel.error("Bitte Eingaben \u00dcberpr\u00fcfen.");
		          } catch (IllegalStateException e) {
		            feedbackPanel.error("Leider zu spät, die Verlosung hat schon stattgefunden.");
		          }
		        } else {
		          feedbackPanel.error("Wunschlisten d\u00dcrfen nicht leer sein!");
		        }
		      }
		    };
		
		
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
				
				i = i - j;
				
				submitButton.setVisible(i > 0);
		        messageEmpty.setVisible(!(i > 0));
		        messageTitle.setVisible((i > 0));
		        messageSubtitle.setVisible((i > 0));

				return i;
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
				DragAndSortableBoxWRules list = new DragAndSortableBoxWRules(
						"prioListSelector.prioList",
						drawProcedure.getMaximumPriorityListItems());
				list.setOutputMarkupId(true);
				item.add(new Label("prioListSelector.listName",
						"Wunschliste Fach "
								+ (item.getIteration() + 1 + controller
										.findPriorityListsByUserAndProcedure(
												singleUser.getId(),
												drawProcedure).size())));
				dropBoxList.add(list);
				item.add(list);
			}
		};

		priolists.setOutputMarkupId(true);
		
		IModel<List<PriorityList>> prioListsModel = new LoadableDetachableModel<List<PriorityList>>() {
		      /**
		       * generated UID.
		       */
		      private static final long serialVersionUID = 8833064897441919997L;

		      @Override
		      protected List<PriorityList> load() {

		        List<PriorityList> list = controller.findPriorityListsByUserAndProcedure(singleUser.getId(), drawProcedureModel
		            .getObject());

		        return list;
		      }
		};
		
		DrawRegistrationManagementPanel drawRegistrationManagementPanel = new DrawRegistrationManagementPanel(
		        "prioListSelector.managementPanel", prioListsModel);

		form.add(drawRegistrationManagementPanel);
		form.add(priolists);
		form.add(messageEmpty);
		form.add(messageSubtitle);
		form.add(messageTitle);
		form.add(categoryList);
		form.add(updateContainer);
		form.add(eventsContainer);
		form.add(submitButton);
		//add(form);
		
		 DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		 box.add(new Label("prioListSelector.heading", drawProcedureModel.getObject().getCampaign().getName()
		        + ": Phase: "
		        + drawProcedureModel.getObject().getName()
		        + " vom "
		        + df.format(drawProcedureModel.getObject().getStartDate().getTime())
		        + " bis "
		        + (drawProcedureModel.getObject() instanceof DrawProcedure ? df.format(drawProcedureModel.getObject().getDrawDate()
		        .getTime()) : df.format((drawProcedureModel.getObject().getEndDate().getTime())))));

		 box.add(feedbackPanel);
		 box.add(form);
		 add(box);

	}

	public List<Event> filterEventListForSourcebox(List<Event> events) {

		List<Event> eventBlackList = new LinkedList<Event>();
		/*
		 * check if event is in an uncommited priolist
		 */
		if (dropBoxList != null && dropBoxList.size() > 0) {
			for (DragAndSortableBoxWRules box : dropBoxList) {
				eventBlackList.addAll(box.getEventList());
			}
		}
		return controller.filterEventList(events, singleUser,
				drawProcedureModel.getObject(), eventBlackList);
	}

	public void moveElementsBackToSource() {

		for (DragAndSortableBoxWRules dropBox : dropBoxList) {
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
	
	public long getDropBoxElementId(DragableElement element){
		return element.getEvent().getSubject().getId();
	};

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
		
		public void reset(){
			this.id = -1;
		}

	}

	@Override
	public DragAndDropableBox getSourceBox() {
		return eventsContainer;
	}
	
	public List<DragAndSortableBoxWRules> getLists(){
		return dropBoxList;
	}
	
	public List<Long> getSubjectIdsFromSavedLists(){
		List<PriorityList> prioLists = controller.findPriorityListsByUserAndProcedure(singleUser.getId(), drawProcedureModel.getObject());
		List<Long> ids = new ArrayList<Long>();
		for(PriorityList pl : prioLists){
			Set<PriorityListItem> items = pl.getItems();
			if(items.size() > 0){
				long eventId = pl.getItem(1).getEvent();
				ids.add(controller.getEventById(eventId).getSubject().getId());
			}
		}
		return ids;
	}
}
