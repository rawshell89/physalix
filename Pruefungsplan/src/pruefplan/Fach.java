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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Fach {
  private String dozent;
  private int fachnummer;
  private String fachname;
  private Collection<Fach> notParallelPruef;

  public static Map<Integer, Fach> readFile(String path) {

    File f = new File(path);
    Map<Integer, Fach> faecher = new HashMap<Integer, Fach>();

    try {
      BufferedReader bf = new BufferedReader(new FileReader(f));
      String in;
      while ((in = bf.readLine()) != null) {
        in = in.trim();

        Pattern p = Pattern.compile("[\\s]{2,}");
        Matcher m = p.matcher(in);

        StringBuffer res = new StringBuffer();

        while (m.find()) {
          m.appendReplacement(res, ";");
        }
        m.appendTail(res);

        String[] val = res.toString().split(";");

        if (val.length < 2) {
          continue;
        }

        int fachnummer = Integer.valueOf(val[0]);
        String fachname = val[1].trim();
        String dozent = val[2].trim();
        faecher.put(fachnummer, new Fach(fachname, dozent, fachnummer));
      }
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("file not found: " + path);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return faecher;
  }

  public Fach(String fachname, String dozent, int fachnummer) {

    this.dozent = dozent;
    this.fachnummer = fachnummer;
    this.fachname = fachname;

    notParallelPruef = new LinkedList<Fach>();
  }

  public boolean equals(Object o) {

    if (!(o instanceof Fach)) {
      return false;
    }
    Fach f = (Fach) o;

    return f.getDozent().equals(dozent) && f.getFachnummer() == fachnummer && f.getFachname().equals(fachname);
  }

  public String toString() {

    return fachnummer + " " + fachname + " (" + dozent + ")";
  }

  public void addNotParallelPruef(Fach f) {

    if (f != null && !(notParallelPruef.contains(f)) && f.getFachnummer() != fachnummer) {
      notParallelPruef.add(f);
    }
  }

  public int countBlacklist() {

    return notParallelPruef.size();
  }

  public String getDozent() {

    return dozent;
  }

  public String getFachname() {

    return fachname;
  }

  public int getFachnummer() {

    return fachnummer;
  }

  public Collection<Fach> getNotParallelPruef() {

    return Collections.unmodifiableCollection(notParallelPruef);
  }

  public boolean hasBlacklistedFach(Fach f) {

    if (notParallelPruef.contains(f)) {
      return true;
    }
    return false;
  }
}
