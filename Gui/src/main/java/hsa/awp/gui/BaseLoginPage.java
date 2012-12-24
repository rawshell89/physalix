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

package hsa.awp.gui;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;

/**
 * A common login page to be used by all Gui projects.
 */
public abstract class BaseLoginPage extends WebPage {
  /**
   * generated UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * TextField to enter the user name.
   */
  private TextField<String> userName = new TextField<String>("userName", new Model<String>());

  /**
   * TextField to enter the password.
   */
  private PasswordTextField password = new PasswordTextField("password", new Model<String>());

  private FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");

  /**
   * Constructor that is invoked when page is invoked without a session.
   *
   * @param parameters Page parameters
   */
  public BaseLoginPage(final PageParameters parameters) {
    // Add the simplest type of label
    add(CSSPackageResource.getHeaderContribution(BaseLoginPage.class, "style.css"));

    final Form<Object> form = new Form<Object>("form");

    // add validators
    userName.setRequired(true);
    password.setRequired(true);

    if (parameters.containsKey("login")) {
      feedbackPanel.error("Benutzername oder Passwort ist falsch!");
    }

    form.add(userName);
    form.add(password);

    add(feedbackPanel);
  }
}
