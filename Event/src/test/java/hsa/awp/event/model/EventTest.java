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

import org.junit.Test;

import java.util.HashSet;

import static hsa.awp.common.test.TestUtil.checkEquality;
import static hsa.awp.common.test.TestUtil.checkNoEquality;

/**
 * Unit test for Event.
 *
 * @author johannes
 */
public class EventTest {
  /**
   * Test method for hashCode and equals methods of {@link Event}. This test checks if any change of parameters has an
   * effect on this methods. When the item is persisted the check has only to include the id.
   */
  @Test
  public void testHashCodeAndEquals() {

    Event a = Event.getInstance(0, 0L);
    Event b = Event.getInstance(0, 0L);
    checkEquality(a, b);

    HashSet<Long> crs = new HashSet<Long>();
    crs.add(6L);
    a.setConfirmedRegistrations(crs);
    checkEquality(a, b);

    a.setEventId(456);
    checkNoEquality(a, b);
    b.setEventId(456);
    checkEquality(a, b);

    HashSet<Exam> exams = new HashSet<Exam>();
    exams.add(Exam.getInstance(0L));
    a.setExams(exams);
    checkEquality(a, b);

    a.setMaxParticipants(60);
    checkNoEquality(a, b);
    b.setMaxParticipants(60);
    checkEquality(a, b);

    Subject subject = Subject.getInstance(0L);
    a.setSubject(subject);
    checkNoEquality(a, b);
    b.setSubject(subject);
    checkEquality(a, b);

    HashSet<Long> teachers = new HashSet<Long>();
    teachers.add(6L);
    a.setTeachers(teachers);
    checkEquality(a, b);

    Term term = Term.getInstance(0L);
    a.setTerm(term);
    checkNoEquality(a, b);
    b.setTerm(term);
    checkEquality(a, b);

    Timetable timetable = Timetable.getInstance(0L);
    a.setTimetable(timetable);
    checkNoEquality(a, b);
    b.setTimetable(timetable);
    checkEquality(a, b);

    // persist item
    a.setId(8L);
    checkNoEquality(a, b);
    b.setId(8L);
    checkEquality(a, b);

    a.setConfirmedRegistrations(new HashSet<Long>());
    a.setEventId(54);
    a.setExams(new HashSet<Exam>());
    a.setMaxParticipants(40);
    b.setSubject(Subject.getInstance(0L));
    b.setTeachers(new HashSet<Long>());
    b.setTerm(Term.getInstance(0L));
    b.setTimetable(Timetable.getInstance(0L));
    checkEquality(a, b);
  }
}
