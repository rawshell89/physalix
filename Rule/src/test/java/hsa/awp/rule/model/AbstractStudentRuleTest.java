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

package hsa.awp.rule.model;

import hsa.awp.user.model.Student;
import hsa.awp.user.model.StudyCourse;
import org.junit.Before;

/**
 * This class holds a test instance of {@link Student}. All subclasses have to call {@link #setUp()} to instantiate
 * all provided objects.
 *
 * @author johannes
 */
public abstract class AbstractStudentRuleTest extends AbstractRuleTest {
  /**
   * The {@link Student} used for checking. Participates {@link #course}.
   */
  public Student student;

  /**
   * The {@link StudyCourse} used for checking. The provided {@link Student} participates this study course.
   */
  public StudyCourse course;

  /**
   * Initializes the test instances.
   */
  @Before
  public void setUp() {

    super.setUp();
    student = Student.getInstance();

    course = StudyCourse.getInstance("course");
    course.setId(7L);

    student.setStudyCourse(course);
  }
}
