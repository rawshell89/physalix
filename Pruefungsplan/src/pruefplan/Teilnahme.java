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

import java.io.*;
import java.util.Map;


public class Teilnahme {
  /**
   * Reads the file for participation and blacklists subjects for a parallel test with a subject.
   *
   * @param faecher
   * @param path
   */
  public static void readTeilnahmeliste(Map<Integer, Fach> faecher,
                                        String path) {

    File f = new File(path);

    try {
      BufferedReader bf = new BufferedReader(new FileReader(f));
      String in;
      while ((in = bf.readLine()) != null) {
        in = in.trim();

        String[] val = in.split("\\s");

        try {
          Fach current = faecher.get(Integer.valueOf(val[0]));
          if (current == null) {
            continue;
          }

          for (String v : val) {
            current.addNotParallelPruef(faecher.get(Integer
                .valueOf(v)));
          }
        } catch (NumberFormatException e) {
          continue;
        }
      }
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("file not found: " + path);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
