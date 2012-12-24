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

package hsa.awp.admingui.report.view;

import hsa.awp.admingui.util.AbstractSortedListSelectorPanel;
import hsa.awp.event.model.Event;

import java.util.Comparator;
import java.util.List;

import static hsa.awp.event.util.EventFormattingUtils.formatIdSubjectNameAndDetailInformation;

/**
 * ListSelector for eventlists.
 *
 * @author basti
 */
public class EventSortedListSelectorPanel extends AbstractSortedListSelectorPanel<Event> {
  /**
   * generated uid.
   */
  private static final long serialVersionUID = 4163462540821155718L;

  /**
   * Comperator for events.
   */
  private transient Comparator<Event> comparator;

  /**
   * Constructor {@link AbstractSortedListSelectorPanel}.
   *
   * @param id       wicket:id
   * @param list     source list.
   * @param selected selected items.
   */
  public EventSortedListSelectorPanel(String id, List<Event> list, List<Event> selected) {

    super(id, list, selected);
  }

  @Override
  protected Comparator<Event> getComparator() {

    if (comparator == null) {
      comparator = new Comparator<Event>() {
        @Override
        public int compare(Event o1, Event o2) {

          String subjectName1 = o1.getSubject().getName();
          String subjectName2 = o2.getSubject().getName();

          if (!(subjectName1.equals(subjectName2))) {
            return subjectName1.compareTo(subjectName2);
          }
          return ((Integer) o1.getEventId()).compareTo(o2.getEventId());
        }
      };
    }
    return comparator;
  }

  @Override
  protected String renderName(Event event) {

    return formatIdSubjectNameAndDetailInformation(event);
  }
}