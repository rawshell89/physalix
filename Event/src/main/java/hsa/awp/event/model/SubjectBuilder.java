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

package hsa.awp.event.model;

import hsa.awp.common.entityBuilder.EntityBuilder;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class SubjectBuilder extends EntityBuilder<Subject> {
  private CategoryBuilder categoryBuilder;
  private Category category;

  private String description = "I am a dummy description";

  private Set<Event> events = new HashSet<Event>();

  private String link = "I am a dummy link";

  private String name = "DummyName " + System.currentTimeMillis();

  public SubjectBuilder() {

    super(Subject.class);
  }

  public SubjectBuilder withCategory(Category category) {

    this.category = category;
    return this;
  }

  public SubjectBuilder withEvents(Set<Event> events) {

    this.events = events;
    return this;
  }

  public SubjectBuilder withName(String name) {

    this.name = name;
    return this;
  }
}
