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

package hsa.awp.common.naming;

import hsa.awp.common.exception.ProgrammingErrorException;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;

/**
 * This class provides some dummy user data for testing. The DummyDirectoryAdapter uses this data to simulate directoryLookups.
 *
 * @author alex
 */
public final class DummyUserData {

  /**
   * Dummy data.
   */
  private static Attributes student = create("stefan", "1000", "123456", "", "Stefan Student", "stefan@physalix", "Informatik", "Technische Informatik", "5", "Studenten");
  private static Attributes teacher = create("peter", "2000", "987654", "Prof. Dr.", "Peter Professor", "peter@physalix", "Elektrotechnik", "Elektrotechnik", "1", "Professoren");
  private static Attributes secretary= create("steffi", "3000", "456321", "", "Steffi Sekretaerin", "steffi@physalix", "Gestaltung", "Kommunikationsdesign", "10", "Angestellte");


  public static BasicAttributes create(String login, String uid, String matriculationnumber, String title, String fullName, String mail, String faculty, String studycourse, String term, String role) {
    BasicAttributes attr = new BasicAttributes();
    attr.put("matriculationnumber", matriculationnumber);
    attr.put("term", "0079$" + term);
    attr.put("studycourse", studycourse);
    attr.put("uid", login);
    attr.put("mail", mail);
    attr.put("name", fullName);
    attr.put("title", title);
    attr.put("role", role);
    attr.put("uidNumber", uid);
    attr.put("ou", faculty);

    return attr;
  }

  /**
   * Returns the login of a student.
   *
   * @return A student login.
   */
  public static String getStudentLogin() {
    return getStringAttributeOf("uid", student);
  }

  /**
   * Returns the login of a teacher.
   *
   * @return A teacher login.
   */
  public static String getTeacherLogin() {
    return getStringAttributeOf("uid", teacher);
  }

  /**
   * Returns the login of a secretary.
   *
   * @return A secretary login.
   */
  public static String getSecretaryLogin() {
    return getStringAttributeOf("uid", secretary);
  }

  /**
   * Returns the login of a secretary.
   *
   * @return A secretary login.
   */
  public static long getSecretaryId() {
    return getLongAttributeOf("uidNumber", secretary);
  }

  /**
   * Returns the login of a teacher.
   *
   * @return A teacher login.
   */
  public static long getTeacherId() {
    return getLongAttributeOf("uidNumber", teacher);
  }

  private static String getStringAttributeOf(String attributeName, Attributes of) {
    try {
      return (String) of.get(attributeName).get();
    } catch (NamingException e) {
      // this should never occur
      throw new ProgrammingErrorException(e);
    }
  }

  private static long getLongAttributeOf(String attributeName, Attributes of) {
    String value = getStringAttributeOf(attributeName, of);
    return Long.parseLong(value);
  }

  /**
   * Returns the login of a student.
   *
   * @return A student login.
   */
  public static long getStudentId() {
    return getLongAttributeOf("uidNumber", student);
  }

  /**
   * Returns the {@link Attributes} of a student.
   *
   * @return Attributes of a student.
   */
  public static Attributes getStudentAttributes() {
    return student;
  }

  /**
   * Returns the {@link Attributes} of a teacher.
   *
   * @return Attributes of a teacher.
   */
  public static Attributes getTeacherAttributes() {
    return teacher;
  }

  /**
   * Returns the {@link Attributes} of a secretary.
   *
   * @return Attributes of a secretary.
   */
  public static Attributes getSecretaryAttributes() {
    return secretary;
  }

  /**
   * we do not want to have instances of this class.
   */
  private DummyUserData() {

  }
}
