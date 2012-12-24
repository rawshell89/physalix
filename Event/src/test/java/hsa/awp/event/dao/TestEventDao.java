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

package hsa.awp.event.dao;

import hsa.awp.common.exception.NoMatchingElementException;
import hsa.awp.common.test.GenericDaoTest;
import hsa.awp.common.test.IObjectFactory;
import hsa.awp.event.model.Event;
import hsa.awp.event.model.Term;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link EventDao}.
 *
 * @author alex
 */
public class TestEventDao extends GenericDaoTest<Event, EventDao, Long> {
  /**
   * Initializes the {@link GenericDaoTest} with a newly created {@link EventDao} and an {@link IObjectFactory}.
   */
  public TestEventDao() {

    super();
    super.setDao(new EventDao());

    super.setIObjectFactory(new IObjectFactory<Event, Long>() {
      private int nr = 100;

      @Override
      public Event getInstance() {

        return Event.getInstance(nr++, 0L);
      }
    });
  }

  @Test
  public void testFindEventsByTeacher() {

    startTransaction();
    Event eventWithTeacher = Event.getInstance(1, 0L);
    eventWithTeacher.getTeachers().add(123L);
    eventWithTeacher = getDao().persist(eventWithTeacher);

    Event event = Event.getInstance(2, 0L);
    event = getDao().persist(event);
    commit();

    startTransaction();
    List<Event> events = getDao().findEventsByTeacher(123L);
    assertTrue(events.contains(eventWithTeacher));
    assertTrue(events.size() == 1);
  }

  @Test
  public void testFindEventsByTerm() {

    startTransaction();
    Term term = Term.getInstance(0L);
    term.setTermDesc("Hallo123");
    TermDao termDao = new TermDao();
    termDao = initDao(termDao);
    termDao.persist(term);

    Event eventWithTerm = Event.getInstance(1, 0L);
    eventWithTerm.setTerm(term);
    eventWithTerm = getDao().persist(eventWithTerm);

    Event event = Event.getInstance(2, 0L);
    event = getDao().persist(event);
    commit();

    startTransaction();
    List<Event> events = getDao().findEventsByTerm("Hallo123");
    assertTrue(events.contains(eventWithTerm));
  }

  @Test
  public void testFindEventsByTermAndMandator() {

    startTransaction();
    Term term = Term.getInstance(0L);
    term.setTermDesc("Hallo123");
    TermDao termDao = new TermDao();
    termDao = initDao(termDao);
    termDao.persist(term);

    Event eventWithTerm = Event.getInstance(1, 1L);
    eventWithTerm.setTerm(term);
    eventWithTerm = getDao().persist(eventWithTerm);

    Event event = Event.getInstance(2, 2L);
    event = getDao().persist(event);
    commit();

    startTransaction();
    List<Event> events = getDao().findEventsByTermAndMandator("Hallo123", 1L);
    assertTrue(events.contains(eventWithTerm));
  }

  /**
   * Creates and persists an event object. Then tries to find the event again by id.
   */
  @Test
  public void testFind() {

    startTransaction();
    Event c = new Event(2, 0L);
    c = getDao().persist(c);
    commit();

    startTransaction();
    Event d = getDao().findById(c.getId());
    assertTrue(d != null);
    commit();
  }

  /**
   * Tries to find an event object by passing <code>null</code> to the findById method.
   */
  @Test(expected = NoMatchingElementException.class)
  public void testFindNotKnown() {

    startTransaction();
    Event c = new Event(2, 0L);
    getDao().persist(c);
    commit();

    startTransaction();
    Event d = getDao().findById(33L);
    assertTrue(d != null);
    commit();
  }

  /**
   * Tries to find an event object with a non-existing id.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testFindNull() {

    startTransaction();
    Event c = new Event(2, 0L);
    getDao().persist(c);
    commit();

    startTransaction();
    Event d = getDao().findById(null);
    assertTrue(d != null);
    commit();
  }

  /**
   * {@link EventDao#merge(Event)} test method.
   */
  @Test
  public void testMerge() {

    List<Event> events = generateAndPersistObjects(2);
    events.get(0).setMaxParticipants(99);
    assertEquals(events.get(0), getDao().merge(events.get(0)));
    assertEquals(2, getDao().findAll().size());
    assertEquals(99, getDao().findById(events.get(0).getId()).getMaxParticipants());
    assertEquals(events.get(0), getDao().findById(events.get(0).getId()));
    assertEquals(events.get(1), getDao().findById(events.get(1).getId()));
  }
}
