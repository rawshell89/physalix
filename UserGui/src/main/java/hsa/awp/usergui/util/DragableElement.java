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
import hsa.awp.usergui.EventDetailPanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.wicketstuff.scriptaculous.dragdrop.DraggableBehavior;

import static hsa.awp.event.util.EventFormattingUtils.formatDetailInformation;
import static hsa.awp.event.util.EventFormattingUtils.formatEventId;

/**
 * DragableElement which can be dragged and dropped.
 *
 * @author basti
 */
public class DragableElement extends Panel {
  /**
   * generated UID.
   */
  private static final long serialVersionUID = 2369244246280704603L;

  /**
   * DraggableBehavior which supplies the dragability.
   */
  private DraggableBehavior dragBehavior;

  /**
   * Event which this element displays.
   */
  private Event event;

  /**
   * Constructor for the DragableElement.
   *
   * @param id    Wicket id
   * @param event event to display
   */
  public DragableElement(String id, Event event) {

    this(id, event, true);
  }

  /**
   * Constructor for the DragableElement.
   *
   * @param id       Wicket id
   * @param event    event to display
   * @param isActive true if draggabiliy is given
   */
  public DragableElement(String id, final Event event, boolean isActive) {

    super(id);
    this.event = event;
    this.setOutputMarkupId(true);
    WebMarkupContainer box = new WebMarkupContainer("prioListDragableElement.element");

    String eventString = formatEventId(event) + " " + event.getSubject().getName();
    box.add(new Label("prioListDragableElement.title", eventString));
    box.add(new Label("prioListDragableElement.info", formatDetailInformation(event)));

    final ModalWindow detailWindow = new ModalWindow("prioListDragableElement.detailWindow");
    detailWindow.setContent(new AjaxLazyLoadPanel(detailWindow.getContentId()) {
      /**
       *
       */
      private static final long serialVersionUID = -822132746613326567L;

      @Override
      public Component getLazyLoadComponent(String markupId) {

        return new EventDetailPanel(markupId, event);
      }
    });
    detailWindow.setTitle(new Model<String>("Veranstaltungsdetails"));
    detailWindow.setInitialWidth(450);

    box.add(detailWindow);

    box.add(new AjaxFallbackLink<Object>("prioListDragableElement.infoLink") {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 543607735730300949L;

      @Override
      public void onClick(AjaxRequestTarget target) {

        detailWindow.show(target);
      }
    });

    if (isActive) {
      dragBehavior = new DraggableBehavior();
      dragBehavior.setRevert(true);
      box.add(dragBehavior);
      box.add(new AttributeAppender("class", new Model<String>("draggable"), " "));
    }
    add(box);
  }

  /**
   * Getter for Event.
   *
   * @return the event
   */
  public Event getEvent() {

    return event;
  }

  /**
   * setter for Event.
   *
   * @param event the event to set
   */
  public void setEvent(Event event) {

    this.event = event;
  }
}
