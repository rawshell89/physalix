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

import hsa.awp.usergui.registrationmanagement.RegistrationManagementPanel;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Homepage.
 */
public class BasePage extends WebPage {
  /**
   * generated UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constructor that is invoked when page is invoked without a session.
   *
   * @param parameters Page parameters
   */
  public BasePage() {
    // Add the simplest type of label
    add(CSSPackageResource.getHeaderContribution(BasePage.class, "style.css"));

    add(new Link<FlatListPanel>("Anmelden") {
      /**
       * Generated serialization id.
       */
      private static final long serialVersionUID = 3620334367568581472L;

      @Override
      public void onClick() {

        setResponsePage(new OnePanelPage(new CampaignListPanel("p1")));
      }
    });

    add(new Link<RegistrationManagementPanel>("registrationsManagement") {
      /**
       * Generated serialization id.
       */
      private static final long serialVersionUID = 3620334367568581472L;

      @Override
      public void onClick() {

        setResponsePage(new OnePanelPage(new RegistrationManagementPanel("p1")));
      }
    });

    add(new NavigationLoginPanel("login"));

    DateFormat df = new SimpleDateFormat("HH:mm:ss");

    add(new Label("timestamp", df.format(new Date())));
  }
}
