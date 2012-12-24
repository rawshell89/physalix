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

package hsa.awp.campaign.model;

import org.junit.Test;

import java.util.Calendar;

import static hsa.awp.common.test.TestUtil.checkEquality;
import static hsa.awp.common.test.TestUtil.checkNoEquality;

/**
 * Test class for model object {@link Ticket}.
 *
 * @author johannes
 */
public class TicketTest {
  /**
   * Test method for hashCode and equals methods of {@link Ticket}. This test checks if any change of parameters has an effect on
   * this methods. When the item is persisted the check has only to include the id.
   */
  @Test
  public void testHashCodeAndEquals() {

    Calendar temp = Calendar.getInstance();
    Ticket a = new Ticket();
    Ticket b = new Ticket();
    a.setDate(temp);
    b.setDate(temp);

    temp = Calendar.getInstance();
    temp.add(Calendar.HOUR, 1);
    a.setDate(temp);
    checkNoEquality(a, b);
    b.setDate(temp);
    checkEquality(a, b);

    a.setInitiator(5L);
    checkNoEquality(a, b);
    b.setInitiator(5L);
    checkEquality(a, b);

    a.setParticipant(9L);
    checkNoEquality(a, b);
    b.setParticipant(9L);
    checkEquality(a, b);

    Procedure proc = Procedure.getInstance(0L);
    a.setProcedure(proc);
    checkNoEquality(a, b);
    b.setProcedure(proc);
    checkEquality(a, b);
    proc.setName("test");
    checkEquality(a, b);

    // persist item
    a.setId(7L);
    checkNoEquality(a, b);
    b.setId(7L);
    checkEquality(a, b);

    a.setDate(Calendar.getInstance());
    a.setInitiator(545L);
    b.setParticipant(759L);
    b.setProcedure(Procedure.getInstance(0L));
    checkEquality(a, b);
  }
}
