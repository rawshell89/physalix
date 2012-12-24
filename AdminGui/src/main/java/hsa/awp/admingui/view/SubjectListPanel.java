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

package hsa.awp.admingui.view;

import hsa.awp.admingui.OnePanelPage;
import hsa.awp.admingui.controller.IAdminGuiController;
import hsa.awp.admingui.edit.SubjectPanel;
import hsa.awp.admingui.util.AccessUtil;
import hsa.awp.admingui.util.JavascriptEventConfirmation;
import hsa.awp.event.model.Subject;
import hsa.awp.gui.util.LoadableDetachedModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Panel for showing all submitted Subjects.
 *
 * @author klassm
 */
public class SubjectListPanel extends Panel {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = -2377409187084346646L;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  public SubjectListPanel(String id) {

    super(id);

    final LoadableDetachedModel<List<Subject>> subjectHolder = new LoadableDetachedModel<List<Subject>>() {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = -8459568897122279374L;

      @Override
      protected List<Subject> load() {

        List<Subject> list = controller.getSubjectsByMandator(getSession());

        Collections.sort(list, new Comparator<Subject>() {
          @Override
          public int compare(Subject o1, Subject o2) {

            return o1.getName().compareTo(o2.getName());
          }
        });

        return list;
      }
    };

    final WebMarkupContainer subjectContainer = new WebMarkupContainer("subjects.container");
    add(subjectContainer);
    subjectContainer.setOutputMarkupId(true);

    subjectContainer.add(new ListView<Subject>("subjects.list", subjectHolder) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 7159413372470842459L;

      @Override
      protected void populateItem(final ListItem<Subject> item) {

        item.add(new Label("subject.name", item.getModelObject().getName()));

        item.add(createEditLink(item));
        item.add(createDeleteLink(item));
      }

      private Link<Object> createEditLink(final ListItem<Subject> item) {
        Link<Object> link = new Link<Object>("subject.edit") {
          /**
           * unique serialization id.
           */
          private static final long serialVersionUID = -2885102825816855786L;

          @Override
          public void onClick() {

            setResponsePage(new OnePanelPage(new SubjectPanel(OnePanelPage.getPanelIdOne(), item.getModelObject())));
          }
        };
        AccessUtil.allowRender(link, "editSubjects");
        return link;
      }

      private Link<Subject> createDeleteLink(final ListItem<Subject> item) {
        Link<Subject> deleteLink = new AjaxFallbackLink<Subject>("subject.delete") {
          /**
           * unique serialization id.
           */
          private static final long serialVersionUID = 1L;

          @Override
          public void onClick(AjaxRequestTarget target) {

            controller.deleteSubject(item.getModelObject());
            subjectHolder.detach();
            target.addComponent(subjectContainer);
          }
        };
        deleteLink.add(new JavascriptEventConfirmation("onclick", "Sind Sie sicher?"));

        if (item.getModelObject().getEvents().size() > 0) {
          deleteLink.setVisible(false);
        }

        AccessUtil.allowRender(deleteLink, "deleteSubjects");
        return deleteLink;
      }
    });
  }


}
