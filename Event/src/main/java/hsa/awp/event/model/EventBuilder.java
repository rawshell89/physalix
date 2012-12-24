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
public class EventBuilder extends EntityBuilder<Event> {
  private Set<Long> confirmedRegistrations = new HashSet<Long>();

  private int eventId = 42;

  private Set<Exam> exams = new HashSet<Exam>();

  private int maxParticipants = 10;

  private SubjectBuilder subjectBuilder;
  private Subject subject;

  private Set<Long> teachers = new HashSet<Long>();

  private Term term;

  private Timetable timetable;

  public EventBuilder() {

    super(Event.class);
  }

  public EventBuilder withEventId(int eventId) {

    this.eventId = eventId;
    return this;
  }

  public EventBuilder withSubject(Subject subject) {

    this.subject = subject;
    return this;
  }
}
