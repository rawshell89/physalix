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

package hsa.awp.event.facade;

import hsa.awp.event.dao.ICategoryDao;
import hsa.awp.event.dao.IEventDao;
import hsa.awp.event.dao.ISubjectDao;
import hsa.awp.event.dao.ITermDao;
import hsa.awp.event.model.Category;
import hsa.awp.event.model.Event;
import hsa.awp.event.model.Subject;
import hsa.awp.event.model.Term;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * This unit test tests spring security of EventFacade. For every secured method there are 3 tests. One with no credentials, one
 * with existing credentials but insufficient rights and finally a test with sufficient rights.
 *
 * @author alex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/spring/TestEventFacadeSecurity.xml")
@Ignore // temporary ignored because method security does not work with ldap yet (alex)
public class TestEventFacadeSecurity {
  private Mockery mockery;

  @Resource(name = "event.facade")
  private IEventFacade facade;

  @Resource(name = "event.securityTest.mockFactory")
  private TestEventFacadeSecurityMockFactory mockFactory;

  private ICategoryDao categoryDao;

  private IEventDao eventDao;

  private ISubjectDao subjectDao;

  private ITermDao termDao;

  private SecurityContext securityContext;

  private Authentication secretaryAuthentication;

  private Authentication adminAuthentication;

  private Authentication facultyAuthentication;

  private Authentication studentAuthentication;

  @Before
  public void setUp() {

    securityContext = SecurityContextHolder.getContext();
    secretaryAuthentication = new UsernamePasswordAuthenticationToken("secretary", "password");
    adminAuthentication = new UsernamePasswordAuthenticationToken("admin", "password");
    facultyAuthentication = new UsernamePasswordAuthenticationToken("faculty", "password");
    studentAuthentication = new UsernamePasswordAuthenticationToken("student", "password");
    securityContext.setAuthentication(null);

    mockery = mockFactory.getMockery();

    categoryDao = mockFactory.getCategoryDao();
    eventDao = mockFactory.getEventDao();
    subjectDao = mockFactory.getSubjectDao();
    termDao = mockFactory.getTermDao();

    mockery.checking(new Expectations() {
      {
        allowing(categoryDao).findAll();
        will(returnValue(new ArrayList<Category>()));
        allowing(categoryDao).remove(Category.getInstance("", 0L));
        allowing(categoryDao);
        will(returnValue(Category.getInstance("", 0L)));

        allowing(eventDao).findAll();
        will(returnValue(new ArrayList<Event>()));
        allowing(eventDao).remove(Event.getInstance(0, 0L));
        allowing(eventDao);
        will(returnValue(Event.getInstance(0, 0L)));

        allowing(subjectDao).findAll();
        will(returnValue(new ArrayList<Subject>()));
        allowing(subjectDao).remove(Subject.getInstance(0L));
        allowing(subjectDao);
        will(returnValue(Subject.getInstance(0L)));

        allowing(termDao).findAll();
        will(returnValue(new ArrayList<Term>()));
        allowing(subjectDao).remove(Subject.getInstance(0L));
        allowing(termDao);
        will(returnValue(Term.getInstance(0L)));
      }
    });
  }

//    @Before
//    public void setUp() {
//
//        securityContext.setAuthentication(adminAuthentication);
//        facade.setCategoryDao(categoryDao);
//        facade.setEventDao(eventDao);
//        facade.setSubjectDao(subjectDao);
//        facade.setTermDao(termDao);
//        securityContext.setAuthentication(null);
//    }

  @After
  public void tearDown() {

    securityContext.setAuthentication(null);
  }

  @Test
  public void testGetAllCategoriesGood() {

    securityContext.setAuthentication(adminAuthentication);
    facade.getAllCategories();
    securityContext.setAuthentication(facultyAuthentication);
    facade.getAllCategories();
    securityContext.setAuthentication(secretaryAuthentication);
    facade.getAllCategories();
    securityContext.setAuthentication(studentAuthentication);
    facade.getAllCategories();
  }

  /*
  * getAllCategories() Accepted roles: ROLE_STUDENT, ROLE_FACULTY, ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testGetAllCategoriesNo() {

    facade.getAllCategories();
  }

  @Test
  public void testGetAllEventsGood() {

    securityContext.setAuthentication(adminAuthentication);
    facade.getAllEvents();
    securityContext.setAuthentication(facultyAuthentication);
    facade.getAllEvents();
    securityContext.setAuthentication(secretaryAuthentication);
    facade.getAllEvents();
    securityContext.setAuthentication(studentAuthentication);
    facade.getAllEvents();
  }

  /*
  * getAllEvents() Accepted roles: ROLE_STUDENT, ROLE_FACULTY, ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testGetAllEventsNo() {

    facade.getAllEvents();
  }

  @Test
  public void testGetAllSubjectsGood() {

    securityContext.setAuthentication(adminAuthentication);
    facade.getAllSubjects();
    securityContext.setAuthentication(facultyAuthentication);
    facade.getAllSubjects();
    securityContext.setAuthentication(secretaryAuthentication);
    facade.getAllSubjects();
    securityContext.setAuthentication(studentAuthentication);
    facade.getAllSubjects();
  }

  /*
  * getAllSubjects() Accepted roles: ROLE_STUDENT, ROLE_FACULTY, ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testGetAllSubjectsNo() {

    facade.getAllSubjects();
  }

  @Test
  public void testGetCategoryByIdGood() {

    securityContext.setAuthentication(adminAuthentication);
    facade.getCategoryById(1L);
    securityContext.setAuthentication(facultyAuthentication);
    facade.getCategoryById(1L);
    securityContext.setAuthentication(secretaryAuthentication);
    facade.getCategoryById(1L);
    securityContext.setAuthentication(studentAuthentication);
    facade.getCategoryById(1L);
  }

  /*
  * getCategoryById() Accepted roles: ROLE_STUDENT, ROLE_FACULTY, ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testGetCategoryByIdNo() {

    facade.getCategoryById(1L);
  }

  @Test
  public void testGetCategoryByNameGood() {

    securityContext.setAuthentication(adminAuthentication);
    facade.getCategoryByName("TestName");
    securityContext.setAuthentication(facultyAuthentication);
    facade.getCategoryByName("TestName");
    securityContext.setAuthentication(secretaryAuthentication);
    facade.getCategoryByName("TestName");
    securityContext.setAuthentication(studentAuthentication);
    facade.getCategoryByName("TestName");
  }

  /*
  * getCategoryByName() Accepted roles: ROLE_STUDENT, ROLE_FACULTY, ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testGetCategoryByNameNo() {

    facade.getCategoryByName("TestName");
  }

  @Test
  public void testGetEventByIdGood() {

    securityContext.setAuthentication(adminAuthentication);
    facade.getEventById(1L);
    securityContext.setAuthentication(facultyAuthentication);
    facade.getEventById(1L);
    securityContext.setAuthentication(secretaryAuthentication);
    facade.getEventById(1L);
    securityContext.setAuthentication(studentAuthentication);
    facade.getEventById(1L);
  }

  /*
  * getEventById() Accepted roles: ROLE_STUDENT, ROLE_FACULTY, ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testGetEventByIdNo() {

    facade.getEventById(1L);
  }

  @Test
  public void testRemoveCategoryGood() {

    securityContext.setAuthentication(adminAuthentication);
    facade.removeCategory(Category.getInstance("", 0L));
    securityContext.setAuthentication(facultyAuthentication);
    facade.removeCategory(Category.getInstance("", 0L));
    securityContext.setAuthentication(secretaryAuthentication);
    facade.removeCategory(Category.getInstance("", 0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testRemoveCategoryInsufficient() {

    securityContext.setAuthentication(studentAuthentication);
    facade.removeCategory(Category.getInstance("", 0L));
  }

  /*
  * removeCategory() Accepted roles: ROLE_FACULTY, ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testRemoveCategoryNo() {

    facade.removeCategory(Category.getInstance("", 0L));
  }

  @Test
  public void testRemoveEventGood() {

    securityContext.setAuthentication(adminAuthentication);
    facade.removeEvent(Event.getInstance(0, 0L));
    securityContext.setAuthentication(facultyAuthentication);
    facade.removeEvent(Event.getInstance(0, 0L));
    securityContext.setAuthentication(secretaryAuthentication);
    facade.removeEvent(Event.getInstance(0, 0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testRemoveEventInsufficient() {

    securityContext.setAuthentication(studentAuthentication);
    facade.removeEvent(Event.getInstance(0, 0L));
  }

  /*
  * removeEvent() Accepted roles: ROLE_FACULTY, ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testRemoveEventNo() {

    facade.removeEvent(Event.getInstance(0, 0L));
  }

  @Test
  public void testRemoveSubjectGood() {

    securityContext.setAuthentication(adminAuthentication);
    facade.removeSubject(Subject.getInstance(0L));
    securityContext.setAuthentication(facultyAuthentication);
    facade.removeSubject(Subject.getInstance(0L));
    securityContext.setAuthentication(secretaryAuthentication);
    facade.removeSubject(Subject.getInstance(0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testRemoveSubjectInsufficient() {

    securityContext.setAuthentication(studentAuthentication);
    facade.removeSubject(Subject.getInstance(0L));
  }

  /*
  * removeSubject() Accepted roles: ROLE_FACULTY, ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testRemoveSubjectNo() {

    facade.removeSubject(Subject.getInstance(0L));
  }

  @Test
  public void testSaveCategoryGood() {

    securityContext.setAuthentication(adminAuthentication);
    facade.saveCategory(Category.getInstance("", 0L));
    securityContext.setAuthentication(facultyAuthentication);
    facade.saveCategory(Category.getInstance("", 0L));
    securityContext.setAuthentication(secretaryAuthentication);
    facade.saveCategory(Category.getInstance("", 0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testSaveCategoryInsufficient() {

    securityContext.setAuthentication(studentAuthentication);
    facade.saveCategory(Category.getInstance("", 0L));
  }

  /*
  * persistCategory() Accepted roles: ROLE_FACULTY, ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testSaveCategoryNo() {

    facade.saveCategory(Category.getInstance("", 0L));
  }

  @Test
  public void testSaveEventGood() {

    securityContext.setAuthentication(adminAuthentication);
    facade.saveEvent(Event.getInstance(0, 0L));
    securityContext.setAuthentication(facultyAuthentication);
    facade.saveEvent(Event.getInstance(0, 0L));
    securityContext.setAuthentication(secretaryAuthentication);
    facade.saveEvent(Event.getInstance(0, 0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testSaveEventInsufficient() {

    securityContext.setAuthentication(studentAuthentication);
    facade.saveEvent(Event.getInstance(0, 0L));
  }

  /*
  * persistEvent() Accepted roles: ROLE_FACULTY, ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testSaveEventNo() {

    facade.saveEvent(Event.getInstance(0, 0L));
  }

  @Test
  public void testSaveSubjectGood() {

    securityContext.setAuthentication(adminAuthentication);
    facade.saveSubject(Subject.getInstance(0L));
    securityContext.setAuthentication(facultyAuthentication);
    facade.saveSubject(Subject.getInstance(0L));
    securityContext.setAuthentication(secretaryAuthentication);
    facade.saveSubject(Subject.getInstance(0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testSaveSubjectInsufficient() {

    securityContext.setAuthentication(studentAuthentication);
    facade.saveSubject(Subject.getInstance(0L));
  }

  /*
  * persistSubject() Accepted roles: ROLE_FACULTY, ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testSaveSubjectNo() {

    facade.saveSubject(Subject.getInstance(0L));
  }

  @Test
  public void testSaveTermGood() {

    securityContext.setAuthentication(adminAuthentication);
    facade.saveTerm(Term.getInstance(0L));
    securityContext.setAuthentication(facultyAuthentication);
    facade.saveTerm(Term.getInstance(0L));
    securityContext.setAuthentication(secretaryAuthentication);
    facade.saveTerm(Term.getInstance(0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testSaveTermInsufficient() {

    securityContext.setAuthentication(studentAuthentication);
    facade.saveTerm(Term.getInstance(0L));
  }

  /*
  * persistTerm() Accepted roles: ROLE_FACULTY, ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testSaveTermNo() {

    facade.saveTerm(Term.getInstance(0L));
  }

  @Test
  public void testUpdateCategoryGood() {

    securityContext.setAuthentication(adminAuthentication);
    facade.updateCategory(Category.getInstance("", 0L));
    securityContext.setAuthentication(facultyAuthentication);
    facade.updateCategory(Category.getInstance("", 0L));
    securityContext.setAuthentication(secretaryAuthentication);
    facade.updateCategory(Category.getInstance("", 0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testUpdateCategoryInsufficient() {

    securityContext.setAuthentication(studentAuthentication);
    facade.updateCategory(Category.getInstance("", 0L));
  }

  /*
  * mergeCategory() Accepted roles: ROLE_FACULTY, ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testUpdateCategoryNo() {

    facade.updateCategory(Category.getInstance("", 0L));
  }

  @Test
  public void testUpdateEventGood() {

    securityContext.setAuthentication(adminAuthentication);
    facade.updateEvent(Event.getInstance(0, 0L));
    securityContext.setAuthentication(facultyAuthentication);
    facade.updateEvent(Event.getInstance(0, 0L));
    securityContext.setAuthentication(secretaryAuthentication);
    facade.updateEvent(Event.getInstance(0, 0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testUpdateEventInsufficient() {

    securityContext.setAuthentication(studentAuthentication);
    facade.updateEvent(Event.getInstance(0, 0L));
  }

  /*
  * mergeEvent() Accepted roles: ROLE_FACULTY, ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testUpdateEventNo() {

    facade.updateEvent(Event.getInstance(0, 0L));
  }

  @Test
  public void testUpdateSubjectGood() {

    securityContext.setAuthentication(adminAuthentication);
    facade.updateSubject(Subject.getInstance(0L));
    securityContext.setAuthentication(facultyAuthentication);
    facade.updateSubject(Subject.getInstance(0L));
    securityContext.setAuthentication(secretaryAuthentication);
    facade.updateSubject(Subject.getInstance(0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testUpdateSubjectInsufficient() {

    securityContext.setAuthentication(studentAuthentication);
    facade.updateSubject(Subject.getInstance(0L));
  }

  /*
  * mergeSubject() Accepted roles: ROLE_FACULTY, ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testUpdateSubjectNo() {

    facade.updateSubject(Subject.getInstance(0L));
  }

  // TODO Alex : are these tests really needed? There are no more dao setters in the facade interface.

//    /*
//     * setCategoryDao() Accepted roles: ROLE_ADMIN
//     */
//
//    @Test(expected = AuthenticationCredentialsNotFoundException.class)
//    public void testsetCategoryDaoNo() {
//
//        facade.setCategoryDao(null);
//    }
//    
//    @Test(expected = AccessDeniedException.class)
//    public void testsetCategoryDaoInsufficient1() {
//
//        securityContext.setAuthentication(studentAuthentication);
//        facade.setCategoryDao(null);
//    }
//    
//    @Test(expected = AccessDeniedException.class)
//    public void testsetCategoryDaoInsufficient2() {
//
//        securityContext.setAuthentication(facultyAuthentication);
//        facade.setCategoryDao(null);
//    }
//    
//    @Test(expected = AccessDeniedException.class)
//    public void testsetCategoryDaoInsufficient3() {
//
//        securityContext.setAuthentication(secretaryAuthentication);
//        facade.setCategoryDao(null);
//    }
//    
//    @Test
//    public void testsetCategoryDaoGood() {
//
//        securityContext.setAuthentication(adminAuthentication);
//        facade.setCategoryDao(null);
//    }
//    
//    /*
//     * setEventDao() Accepted roles: ROLE_ADMIN
//     */
//
//    @Test(expected = AuthenticationCredentialsNotFoundException.class)
//    public void testsetEventDaoNo() {
//
//        facade.setEventDao(null);
//    }
//    
//    @Test(expected = AccessDeniedException.class)
//    public void testsetEventDaoInsufficient1() {
//
//        securityContext.setAuthentication(studentAuthentication);
//        facade.setEventDao(null);
//    }
//    
//    @Test(expected = AccessDeniedException.class)
//    public void testsetEventDaoInsufficient2() {
//
//        securityContext.setAuthentication(facultyAuthentication);
//        facade.setEventDao(null);
//    }
//    
//    @Test(expected = AccessDeniedException.class)
//    public void testsetEventDaoInsufficient3() {
//
//        securityContext.setAuthentication(secretaryAuthentication);
//        facade.setEventDao(null);
//    }
//    
//    @Test
//    public void testsetEventDaoGood() {
//
//        securityContext.setAuthentication(adminAuthentication);
//        facade.setEventDao(null);
//    }
//    
//    /*
//     * setSubjectDao() Accepted roles: ROLE_ADMIN
//     */
//
//    @Test(expected = AuthenticationCredentialsNotFoundException.class)
//    public void testsetSubjectDaoNo() {
//
//        facade.setSubjectDao(null);
//    }
//    
//    @Test(expected = AccessDeniedException.class)
//    public void testsetSubjectDaoInsufficient1() {
//
//        securityContext.setAuthentication(studentAuthentication);
//        facade.setSubjectDao(null);
//    }
//    
//    @Test(expected = AccessDeniedException.class)
//    public void testsetSubjectDaoInsufficient2() {
//
//        securityContext.setAuthentication(facultyAuthentication);
//        facade.setSubjectDao(null);
//    }
//    
//    @Test(expected = AccessDeniedException.class)
//    public void testsetSubjectDaoInsufficient3() {
//
//        securityContext.setAuthentication(secretaryAuthentication);
//        facade.setSubjectDao(null);
//    }
//    
//    @Test
//    public void testsetSubjectDaoGood() {
//
//        securityContext.setAuthentication(adminAuthentication);
//        facade.setSubjectDao(null);
//    }
//    
//    /*
//     * setTermDao() Accepted roles: ROLE_ADMIN
//     */
//
//    @Test(expected = AuthenticationCredentialsNotFoundException.class)
//    public void testsetTermDaoNo() {
//
//        facade.setTermDao(null);
//    }
//    
//    @Test(expected = AccessDeniedException.class)
//    public void testsetTermDaoInsufficient1() {
//
//        securityContext.setAuthentication(studentAuthentication);
//        facade.setTermDao(null);
//    }
//    
//    @Test(expected = AccessDeniedException.class)
//    public void testsetTermDaoInsufficient2() {
//
//        securityContext.setAuthentication(facultyAuthentication);
//        facade.setTermDao(null);
//    }
//    
//    @Test(expected = AccessDeniedException.class)
//    public void testsetTermDaoInsufficient3() {
//
//        securityContext.setAuthentication(secretaryAuthentication);
//        facade.setTermDao(null);
//    }
//    
//    @Test
//    public void testsetTermDaoGood() {
//
//        securityContext.setAuthentication(adminAuthentication);
//        facade.setTermDao(null);
//    }
}

@Ignore
class TestEventFacadeSecurityMockFactory {
  private Mockery mockery = new JUnit4Mockery();

  private final ICategoryDao categoryDao;

  private final IEventDao eventDao;

  private final ISubjectDao subjectDao;

  private final ITermDao termDao;

  public TestEventFacadeSecurityMockFactory() {

    categoryDao = mockery.mock(ICategoryDao.class);
    eventDao = mockery.mock(IEventDao.class);
    subjectDao = mockery.mock(ISubjectDao.class);
    termDao = mockery.mock(ITermDao.class);
  }

  /**
   * Returns categoryDao.
   *
   * @return the categoryDao
   */
  public ICategoryDao getCategoryDao() {

    return categoryDao;
  }

  /**
   * Returns eventDao.
   *
   * @return the eventDao
   */
  public IEventDao getEventDao() {

    return eventDao;
  }

  /**
   * Returns mockery.
   *
   * @return the mockery
   */
  public Mockery getMockery() {

    return mockery;
  }

  /**
   * Returns subjectDao.
   *
   * @return the subjectDao
   */
  public ISubjectDao getSubjectDao() {

    return subjectDao;
  }

  /**
   * Returns termDao.
   *
   * @return the termDao
   */
  public ITermDao getTermDao() {

    return termDao;
  }
}
