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

package hsa.awp.usergui;

import hsa.awp.campaign.model.DrawProcedure;
import hsa.awp.campaign.model.Procedure;
import hsa.awp.usergui.controller.IUserGuiController;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ProcedureDetailView extends Panel {

  /**
   * {@link hsa.awp.usergui.controller.IUserGuiController}.
   */
  @SpringBean(name = "usergui.controller")
  private IUserGuiController controller;

  public ProcedureDetailView(String id, Procedure procedure) {
    super(id);
    createProcedureBar(procedure);
  }

  private void createProcedureBar(Procedure procedure) {
    DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    WebMarkupContainer container = new WebMarkupContainer("box");

    container.add(new Label("procedureDetail.detailInfo", "Phase: " + procedure.getName()));
    container.add(new Label("procedureDetail.timeSpan", "vom "
        + df.format(procedure.getStartDate().getTime())
        + " bis "
        + (procedure instanceof DrawProcedure ? df.format(((DrawProcedure) procedure).getDrawDate().getTime()) : df
        .format((procedure.getEndDate().getTime())))));
    if (procedure instanceof DrawProcedure && controller.isAlreadyDrawn((DrawProcedure) procedure)) {
      container.add(new AttributeAppender("class", new Model<String>("disabled"), ";"));
      container.setEnabled(false);
    }

    float progress = calculateProgress(procedure);

    Label progressLabel = new Label("procedureDetail.progress");
    progressLabel.add(new AttributeAppender("style", new Model<String>("width: " + (int) progress + "%"), ";"));
    container.add(progressLabel);
    add(container);
  }

  private float calculateProgress(Procedure procedure) {
    float progress = 100 * (System.currentTimeMillis() - procedure.getStartDate().getTimeInMillis())
        / (procedure.getEndDate().getTimeInMillis() - procedure.getStartDate().getTimeInMillis());
    if (progress > 100) {
      progress = 100;
    }
    if (progress < 0) {
      progress = 0;
    }
    return progress;
  }
}
