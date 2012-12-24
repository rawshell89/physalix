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

package pruefplan;

import java.util.*;


public class Pruefungsplan {
  private Map<Integer, Fach> faecher;
  private Collection<Collection<Fach>> belegung;

  public Pruefungsplan(Map<Integer, Fach> faecher) {

    this.faecher = faecher;
    belegung = new LinkedList<Collection<Fach>>();
    parse();
  }

  private void parse() {

    Collection<Fach> val = faecher.values();
    Fach[] valFach = new Fach[val.size()];
    val.toArray(valFach);
    Arrays.sort(valFach, new Comparator<Fach>() {
      @Override
      public int compare(Fach o1, Fach o2) {

        if (o1.countBlacklist() > o2.countBlacklist()) {
          return -1;
        } else if (o1.countBlacklist() == o2.countBlacklist()) {
          return 0;
        } else {
          return 1;
        }
      }
    });


    for (Fach current : valFach) {
      boolean foundSlot = false;
      for (Collection<Fach> slot : belegung) {
        if (!hasFach(slot, current)) {
          slot.add(current);
          foundSlot = true;
          break;
        }
      }
      if (!foundSlot) {
        Collection<Fach> slot = new LinkedList<Fach>();
        slot.add(current);
        belegung.add(slot);
      }
    }
  }

  private boolean hasFach(Collection<Fach> slot, Fach fach) {

    for (Fach item : slot) {
      if (item.getFachnummer() == fach.getFachnummer() || item.hasBlacklistedFach(fach)) {
        return true;
      } else {
//				System.out.println(item.getFachnummer() + " " + fach.getFachnummer());
      }
    }
    return false;
  }

  public String toString() {

    StringBuffer sb = new StringBuffer();

    int i = 0;
    for (Collection<Fach> slot : belegung) {
      sb.append("-------------------\n");
      sb.append("Slot ");
      sb.append(++i);
      sb.append('\n');
      sb.append("-------------------\n");

      for (Fach item : slot) {
        sb.append(item);
        sb.append('\n');
      }
      sb.append('\n');
    }
    return sb.toString();
  }
}
