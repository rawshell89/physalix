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

package hsa.awp.admingui.edit.event.tabs;

import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.admingui.util.UserSelectPanel;
import hsa.awp.event.model.Event;
import hsa.awp.event.model.Exam;
import hsa.awp.gui.util.LoadableDetachedModel;
import hsa.awp.user.model.SingleUser;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.LinkedList;
import java.util.List;

/**
 * tab for Teacher actions.
 *
 * @author Basti
 */
public class TabTeacher extends Panel {
  /**
   * generated UID.
   */
  private static final long serialVersionUID = -3428052872202333030L;

  /**
   * Container for list which displays all {@link SingleUser} associated as teachers with the {@link Event}.
   */
  private WebMarkupContainer teacherListContainer;

  /**
   * Container for the {@link UserSelectPanel}.
   */
  private WebMarkupContainer teacherEditContainer;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * event whose Teacher is edited.
   */
  private Event event;

  /**
   * Tab for Teacher.
   *
   * @param id  wicket id
   * @param evt event whose Teacher
   */
  public TabTeacher(String id, Event evt) {

    super(id);

    this.event = evt;

    teacherEditContainer = new WebMarkupContainer("event.tabTeacher.TeacherEditContainer");
    teacherEditContainer.setOutputMarkupId(true);


    TeacherSelectPanel teacherSelectPanel = new TeacherSelectPanel("event.tabTeacher.TeacherSelectPanel");

    teacherEditContainer.add(teacherSelectPanel);
    teacherSelectPanel.setVisible(false);
    add(teacherEditContainer);

    teacherListContainer = new WebMarkupContainer("event.tabTeacher.TeacherListContainer");
    teacherListContainer.setOutputMarkupId(true);
    add(teacherListContainer);

    teacherListContainer.add(new AjaxFallbackLink<Exam>("event.tabTeacher.createNewTeacherLink") {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 1L;

      @Override
      public void onClick(AjaxRequestTarget target) {

        target.addComponent(teacherEditContainer);
        teacherEditContainer.removeAll();
        teacherEditContainer.add(new TeacherSelectPanel("event.tabTeacher.TeacherSelectPanel"));
      }
    });

    final LoadableDetachedModel<List<SingleUser>> teachers = new LoadableDetachedModel<List<SingleUser>>() {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 7140862438222206869L;

      @Override
      protected List<SingleUser> load() {

        event = controller.getEventById(event.getId());
        List<SingleUser> teacherList = new LinkedList<SingleUser>();

        for (Long id : event.getTeachers()) {
          teacherList.add(controller.getUserById(id));
        }

        return teacherList;
      }
    };

    ListView<SingleUser> singleUserList = new ListView<SingleUser>("event.tabTeacher.TeacherList", teachers) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 999046116701991766L;

      @Override
      protected void populateItem(final ListItem<SingleUser> item) {

        final SingleUser singleUser = item.getModelObject();

        item.add(new Label("event.tabTeacher.string", singleUser.getName()));

        item.add(new AjaxFallbackLink<SingleUser>("event.tabTeacher.delete") {
          /**
           * unique serialization id.
           */
          private static final long serialVersionUID = 1L;

          @Override
          public void onClick(AjaxRequestTarget target) {

            target.addComponent(teacherListContainer);
            controller.deleteLecture(singleUser, event);
          }
        });
      }
    };
    teacherListContainer.add(singleUserList);
    teacherListContainer.setOutputMarkupId(true);
  }

  /**
   * TeacherSelectPanel which extend UserSelectPanel for the teacher tab.
   *
   * @author Basti
   */
  private class TeacherSelectPanel extends UserSelectPanel {
    /**
     * generated UID.
     */
    private static final long serialVersionUID = -5660087861205877151L;

    /**
     * Constructor with wicket id.
     *
     * @param id wicket id
     */
    public TeacherSelectPanel(String id) {

      super(id);
    }

    @Override
    protected boolean onSendSubmit(SingleUser singleUser) {

      controller.writeLecture(singleUser, event);

      return true;
    }


  }
}
