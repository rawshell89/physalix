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

package hsa.awp.user.dao;

import hsa.awp.common.naming.IAbstractDirectory;

import java.util.Properties;

/**
 * This class contains some dummy data to test the DAO layer.
 *
 * @author alex
 */
public final class DummyData {
  /**
   * Another user.
   */
  private static Properties secretary;

  /**
   * A student.
   */
  private static Properties student;

  /**
   * A teacher.
   */
  private static Properties teacher;

  /**
   * Returns rest properties for a teacher.
   *
   * @return Teacher properties.
   */
  public static Properties getTeacher() {

    if (teacher == null) {
      initDummyData();
    }
    return teacher;
  }

  /**
   * Initializes the dummy properties.
   */
  public static void initDummyData() {

    teacher = new Properties();
    teacher.put(IAbstractDirectory.MATRICULATIONNUMBER, "0");
    teacher.put(IAbstractDirectory.TERM, "0");
    teacher.put(IAbstractDirectory.FACULTY, "Informatik");
    teacher.put(IAbstractDirectory.LOGIN, "hans");
    teacher.put(IAbstractDirectory.EMAIL, "hans@physalix");
    teacher.put(IAbstractDirectory.NAME, "Hans Huber");
    teacher.put(IAbstractDirectory.ROLE, "Professoren");
    teacher.put(IAbstractDirectory.UUID, "1000");

    student = new Properties();
    student.put(IAbstractDirectory.MATRICULATIONNUMBER, "987654");
    student.put(IAbstractDirectory.TERM, "1");
    student.put(IAbstractDirectory.FACULTY, "Elektrotechnik");
    student.put(IAbstractDirectory.LOGIN, "peter");
    student.put(IAbstractDirectory.EMAIL, "peter@physalix");
    student.put(IAbstractDirectory.NAME, "Peter Mayer");
    student.put(IAbstractDirectory.ROLE, "Studenten");
    student.put(IAbstractDirectory.UUID, "2000");
    student.put(IAbstractDirectory.STUDYCOURSE, "Multimedia (Bachelor)");

    secretary = new Properties();
    secretary.put(IAbstractDirectory.MATRICULATIONNUMBER, "0");
    secretary.put(IAbstractDirectory.TERM, "");
    secretary.put(IAbstractDirectory.FACULTY, "Gestaltung");
    secretary.put(IAbstractDirectory.LOGIN, "steffi");
    secretary.put(IAbstractDirectory.EMAIL, "steffi@physalix");
    secretary.put(IAbstractDirectory.NAME, "Stefanie Müller");
    secretary.put(IAbstractDirectory.ROLE, "Angestellte");
    secretary.put(IAbstractDirectory.UUID, "3001");
  }

  /**
   * Returns a dummy user with the given id.
   *
   * @param id - id the dummy should have
   * @return A dummy user with the given id.
   */
  public static Properties getUser(long id) {

    Properties user = new Properties(getStudent());
    user.setProperty(IAbstractDirectory.UUID, Long.toString(id));
    user.setProperty(IAbstractDirectory.LOGIN, "DUMMY" + id);
    return user;
  }

  /**
   * Returns rest properties for a student.
   *
   * @return Student properties.
   */
  public static Properties getStudent() {

    if (student == null) {
      initDummyData();
    }
    return student;
  }

  /**
   * Returns rest properties for a secretary.
   *
   * @return Secretary properties.
   */
  public static Properties getSecretary() {

    if (secretary == null) {
      initDummyData();
    }
    return secretary;
  }

  /**
   * This class is just a data container, you should not be able to make instances of it.
   */
  private DummyData() {

  }
}
