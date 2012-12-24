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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.io.Serializable;

public abstract class AbstractModifyingPanel<ITEM extends Serializable> extends Panel {

  public AbstractModifyingPanel(String id, ITEM item, String img, String desc, boolean confirm) {
    super(id);

    Link<ITEM> modifyingLink = new Link<ITEM>("modifyingLink", new Model<ITEM>(item)) {
      @Override
      public void onClick() {
        modifyItem(this.getModelObject());
      }
    };
    if (confirm) {
      modifyingLink.add(new JavascriptEventConfirmation("onclick", getConfirmationMessage()));
    }
    modifyingLink.add(new Label("linkDesc", new Model<String>(desc)));

    StaticImage image = new StaticImage("linkImg", new Model(img));
    image.add(new AttributeModifier("alt", new Model<String>(desc)));
    modifyingLink.add(image);

    add(modifyingLink);
  }

  protected String getConfirmationMessage() {
    return "Sind Sie sicher?";
  }

  public abstract void modifyItem(ITEM item);


  private class StaticImage extends WebComponent {

    public StaticImage(String id, IModel model) {
      super(id, model);
    }

    protected void onComponentTag(ComponentTag tag) {
      super.onComponentTag(tag);
      checkComponentTag(tag, "img");
      tag.put("src", getDefaultModelObjectAsString());
    }

  }
}

