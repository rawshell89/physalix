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

import hsa.awp.admingui.controller.IAdminGuiController;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class StudyCourseImportPanel extends Panel {

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  public StudyCourseImportPanel(String id) {
    super(id);

    final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");

    add(feedbackPanel);

    Form form = new Form("form");

    form.add(new Button("studycourseimport.submitButton", new Model<String>("Importieren")) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 1127918285241155983L;

      @Override
      public void onSubmit() {

        controller.readAllStudyCourses();

        feedbackPanel.info("Die Studiengänge wurden erfolgreich ausgelesen.");

      }
    });

    add(form);


  }
}
