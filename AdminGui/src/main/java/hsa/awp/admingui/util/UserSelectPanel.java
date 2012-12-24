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
import hsa.awp.gui.util.LoadableDetachedModel;
import hsa.awp.user.model.SingleUser;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Panel for selecting a {@link SingleUser}.
 *
 * @author klassm
 */
public abstract class UserSelectPanel extends Panel {
  /**
   * unique serialization id.
   */
  private static final long serialVersionUID = 7663664290867849746L;

  /**
   * GuiController which feeds the Gui with Data.
   */
  @SpringBean(name = "admingui.controller")
  private transient IAdminGuiController controller;

  /**
   * Button to start the search action.
   */
  private Button searchUserButton;

  /**
   * Button performing the submit action. This action can be specified in the onSendSubmit method.
   */
  private Button submitButton;

  /**
   * Panel for performing feedback to the user. This Panel will show only messages from the local form.
   */
  private FeedbackPanel userFindFeedbackPanel;

  /**
   * User that was found after hitting the findUser button.
   */
  private List<SingleUser> usersFound;

  /**
   * Name of the user that was found.
   */
  private RadioChoice<SingleUser> userFoundRadioChoice;

  /**
   * TextField for entering the username to search.
   */
  private TextField<String> usernameInput;

  /**
   * Constructor for creating a Panel to select a user.
   *
   * @param id wicket:id
   */
  public UserSelectPanel(String id) {

    super(id);

    Form<Object> form = new Form<Object>("userSelectPanel.form");
    add(form);

    usersFound = Collections.emptyList();

    userFindFeedbackPanel = new FeedbackPanel("userSelectPanel.feedbackPanel");
    userFindFeedbackPanel.setFilter(new ContainerFeedbackMessageFilter(form));
    form.add(userFindFeedbackPanel);

    usernameInput = new TextField<String>("userSelectPanel.username", new Model<String>());
    form.add(usernameInput);

    final LoadableDetachedModel<List<SingleUser>> userListModel = new LoadableDetachedModel<List<SingleUser>>() {
      @Override
      protected List<SingleUser> load() {
        return usersFound;
      }
    };

    userFoundRadioChoice = new RadioChoice<SingleUser>("userSelectPanel.userFound", new Model<SingleUser>(), userListModel, new IChoiceRenderer<SingleUser>() {
      @Override
      public Object getDisplayValue(SingleUser object) {
        return object.getName() + " (" + object.getUsername() + ")";
      }

      @Override
      public String getIdValue(SingleUser object, int index) {
        return String.valueOf(index);
      }
    });
    form.add(userFoundRadioChoice);

    submitButton = new Button("userSelectPanel.submitButton", new Model<String>(getSubmitButtonText())) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 1127918285241155983L;

      @Override
      public void onSubmit() {

        Session.get().getFeedbackMessages().clear();

        if (usersFound == null) {
          userFindFeedbackPanel.error("Kein Benutzer ausgew&uuml;hlt");
        }

        if (onSendSubmit(userFoundRadioChoice.getModelObject())) {
          usernameInput.clearInput();
          userFoundRadioChoice.clearInput();
        }

        if (getResponsePage() != null) {
          setResponsePage(getResponsePage());
        }

      }
    };
    form.add(submitButton);
    submitButton.setVisible(false);

    searchUserButton = new Button("userSelectPanel.searchuser", new Model<String>(getFindUserButtonText())) {
      /**
       * unique serialization id.
       */
      private static final long serialVersionUID = 18453249101L;

      @Override
      public void onSubmit() {

        userFoundRadioChoice.clearInput();
        submitButton.setVisible(false);

        String searchString = usernameInput.getModelObject();
        if (searchString == null) {
          return;
        }

        if (isSearchInvalid(searchString)) {
          userFindFeedbackPanel.error("Ungültige Suche");
          return;
        }

        usersFound = controller.searchForUser(searchString);

        if (usersFound == null) {
          Session.get().getFeedbackMessages().clear();
          userFindFeedbackPanel.error("Kein Benutzer gefunden");
        } else {

          Comparator<SingleUser> singleUserComparator = new Comparator<SingleUser>() {
            @Override
            public int compare(SingleUser singleUser, SingleUser singleUser1) {
              return singleUser.getName().compareTo(singleUser1.getName());
            }
          };

          Collections.sort(usersFound, singleUserComparator);

          userListModel.detach();
          submitButton.setVisible(true);
        }
      }
    };
    form.add(searchUserButton);
  }

  private boolean isSearchInvalid(String searchString) {
    while (searchString.contains("**")) {
      searchString = searchString.replace("**", "*");
    }
    return searchString.equals("") || searchString.equals("*") || (searchString.contains("*") && searchString.length() < 4);
  }

  protected Page getResponsePage() {
    return null;
  }

  /**
   * Text of the submit button text.
   *
   * @return text
   */
  protected String getSubmitButtonText() {
    // TODO Sprache
    return "Benutzer registrieren";
  }

  /**
   * This method will be called whenever the submit button is hit. Please put your submit action in this method.
   *
   * @param singleUser user to perform the action on.
   * @return false if you want to stop the action. The userName textField won't be emptied then. To continue return true.
   */
  protected abstract boolean onSendSubmit(SingleUser singleUser);

  /**
   * Text of the find user button.
   *
   * @return text.
   */
  protected String getFindUserButtonText() {
    // TODO Sprache
    return "Benutzer suchen";
  }

  /**
   * Method that can be overwritten in subclasses. It will be called whenever the find button has been hit.
   *
   * @param user user that was found as result of the search action.
   * @return true if you want to veto the search action. You might want to specify a message in the feedbackPanel. Please use
   *         getFeedbackPanel().* to do that.
   */
  protected boolean vetoFindSubmit(SingleUser user) {

    return false;
  }

  /**
   * Getter for the feedbackPanel. Please use this to display any error or success messages.
   *
   * @return feedbackPanel.
   */
  public FeedbackPanel getFeedbackPanel() {

    return userFindFeedbackPanel;
  }
}
