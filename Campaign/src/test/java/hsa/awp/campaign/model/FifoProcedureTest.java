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
 * Test class for model object {@link FifoProcedure}.
 *
 * @author johannes
 */
public class FifoProcedureTest {
  /**
   * Test method for hashCode and equals methods of {@link FifoProcedure}. This test checks if any change of parameters has an
   * effect on this methods. When the item is persisted the check has only to include the id.
   */
  @Test
  public void testHashCodeAndEquals() {

    Calendar temp = Calendar.getInstance();
    FifoProcedure a = FifoProcedure.getInstance(0L);
    FifoProcedure b = FifoProcedure.getInstance(0L);
    temp.add(Calendar.HOUR, 1);
    a.setEndDate(temp);
    b.setEndDate(temp);

    temp = Calendar.getInstance();
    temp.add(Calendar.HOUR, -1);
    a.setStartDate(temp);
    checkNoEquality(a, b);
    b.setStartDate(temp);
    checkEquality(a, b);

    temp = Calendar.getInstance();
    temp.add(Calendar.HOUR, 2);
    a.setEndDate(temp);
    checkNoEquality(a, b);
    b.setEndDate(temp);
    checkEquality(a, b);

    a.setName("test");
    checkNoEquality(a, b);
    b.setName("test");
    checkEquality(a, b);

    Campaign camp = Campaign.getInstance(0L);
    a.setCampaign(camp);
    checkNoEquality(a, b);
    b.setCampaign(camp);
    checkEquality(a, b);

    // persist item
    a.setId(8L);
    checkNoEquality(a, b);
    b.setId(8L);
    checkEquality(a, b);

    a.setName("temp");
    checkEquality(a, b);
    a.setId(3L);
    checkNoEquality(a, b);
  }
}
