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

package hsa.awp.event;

import hsa.awp.common.test.ITestDB;
import hsa.awp.event.dao.*;
import hsa.awp.event.model.Category;
import hsa.awp.event.model.Subject;

/**
 * @author bastigabriel
 */
public class EventTestDB implements ITestDB {
  private IEventDao evtDao = new EventDao();

  private ICategoryDao catDao = new CategoryDao();

  private ISubjectDao subDao = new SubjectDao();

  private IExamDao examDao = new ExamDao();

  private ITermDao termDao = new TermDao();

  private ITimetableDao timeDao = new TimetableDao();

  public EventTestDB() {

  }

  @Override
  public void clearDB() {

  }

  @Override
  public void initDB() {

    Category[] cat = new Category[5];
    Subject[] sub = new Subject[5];

    for (int i = 0; i < cat.length; i++) {
      cat[i] = Category.getInstance("Kategorie " + (i + 1), 0L);
      catDao.persist(cat[i]);
      for (int j = 0; j < sub.length; j++) {
        sub[j] = Subject.getInstance(0L);
        sub[j].setName("Fach " + (i + 1) + "." + (j + 1));
        cat[i].addSubject(sub[j]);

        subDao.persist(sub[j]);
      }

      catDao.merge(cat[i]);
    }
  }

  @Override
  public void resetDB() {

    clearDB();
    initDB();
  }
}
