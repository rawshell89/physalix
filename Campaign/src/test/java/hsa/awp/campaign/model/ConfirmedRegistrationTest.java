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
 * Test class for model object {@link ConfirmedRegistration}.
 *
 * @author johannes
 */
public class ConfirmedRegistrationTest {
  /**
   * Test method for hashCode and equals methods of {@link ConfirmedRegistration}. This test checks if any change of parameters
   * has an effect on this methods. When the item is persisted the check has only to include the id.
   */
  @Test
  public void testHashCodeAndEquals() {

    Calendar temp = Calendar.getInstance();
    ConfirmedRegistration a = ConfirmedRegistration.getInstance(1L, 0L);
    ConfirmedRegistration b = ConfirmedRegistration.getInstance(1L, 0L);

    temp.add(Calendar.HOUR, 1);
    a.setDate(temp);
    checkNoEquality(a, b);
    b.setDate(temp);
    checkEquality(a, b);

    a.setEventId(2L);
    checkNoEquality(a, b);
    b.setEventId(2L);
    checkEquality(a, b);

    a.setExamOnly(true);
    checkNoEquality(a, b);
    b.setExamOnly(true);
    checkEquality(a, b);

    a.setInitiator(5L);
    checkNoEquality(a, b);
    b.setInitiator(5L);
    checkEquality(a, b);

    a.setParticipant(6L);
    checkNoEquality(a, b);
    b.setParticipant(6L);
    checkEquality(a, b);

    Procedure proc = Procedure.getInstance(0L);
    a.setProcedure(proc);
    checkNoEquality(a, b);
    b.setProcedure(proc);
    checkEquality(a, b);

    // persist item
    a.setId(3L);
    checkNoEquality(a, b);
    b.setId(3L);
    checkEquality(a, b);

    a.setExamOnly(false);
    a.setInitiator(123L);
    b.setEventId(4564);
    b.setParticipant(543L);
    checkEquality(a, b);
  }
}
