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

package hsa.awp.rule.model;

import hsa.awp.campaign.model.Campaign;
import hsa.awp.event.model.Event;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;

@Entity
@Table(name = "`registrationruleset`")
public class RegistrationRuleSet extends RuleSet {
  /**
   * serial version UID used for serialization.
   */
  private static final long serialVersionUID = -5584697747700842472L;

  /**
   * The referenced id of {@link Event}.
   */
  private Long event;

  /**
   * The referenced id of {@link Campaign}.
   */
  private Long campaign;

  public static RegistrationRuleSet getInstance(Long mandatorId) {

    RegistrationRuleSet set = new RegistrationRuleSet();
    set.setRules(new HashSet<Rule>());
    set.setMandatorId(mandatorId);

    return set;
  }

  /**
   * Default constructor.
   */
  RegistrationRuleSet() {

  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (!(obj instanceof RegistrationRuleSet)) {
      return false;
    }
    RegistrationRuleSet other = (RegistrationRuleSet) obj;

    // equality is already checked with super.equals()
    if (getId() != null && getId() != 0L) {
      return true;
    }

    if (campaign == null) {
      if (other.campaign != null) {
        return false;
      }
    } else if (!campaign.equals(other.campaign)) {
      return false;
    }
    if (event == null) {
      if (other.event != null) {
        return false;
      }
    } else if (!event.equals(other.event)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    // if item is persisted use only the id for calculating hash code
    if (getId() != null && getId() != 0L) {
      return super.hashCode();
    }

    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((campaign == null) ? 0 : campaign.hashCode());
    result = prime * result + ((event == null) ? 0 : event.hashCode());
    return result;
  }

  /**
   * Getter for the referenced {@link Campaign} id.
   *
   * @return the id of the referenced {@link Campaign}.
   */
  public Long getCampaign() {

    return campaign;
  }

  /**
   * Sets the referenced {@link Campaign} id.
   *
   * @param campaign the new {@link Campaign} id.
   */
  public void setCampaign(Long campaign) {

    this.campaign = campaign;
  }

  /**
   * Getter for the referenced {@link Event} id.
   *
   * @return the id of the referenced {@link Event}.
   */
  public Long getEvent() {

    return event;
  }

  /**
   * Sets the referenced {@link Event} id.
   *
   * @param event the new {@link Event} id.
   */
  public void setEvent(Long event) {

    this.event = event;
  }
}
