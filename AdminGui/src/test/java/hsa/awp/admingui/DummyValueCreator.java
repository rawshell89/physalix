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

package hsa.awp.admingui;

import hsa.awp.campaign.facade.ICampaignFacade;
import hsa.awp.campaign.model.*;
import hsa.awp.event.facade.IEventFacade;
import hsa.awp.event.model.Category;
import hsa.awp.event.model.Event;
import hsa.awp.event.model.Subject;
import hsa.awp.user.facade.IUserFacade;
import hsa.awp.user.model.Role;
import hsa.awp.user.model.RoleMapping;
import hsa.awp.user.model.SingleUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;

/**
 * Test class inserting dummy values into a specified database. Also works as integration test for testing the relations between the
 * components. INFO: To change the database (default inMemory) goto to Test spring configuration file and change the
 * persistenceUnitName.
 *
 * @author klassm
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/spring/TestValueCreator.xml")
public class DummyValueCreator {
  /**
   * Facade for accessing the Event component.
   */
  @Resource(name = "event.facade")
  private IEventFacade eventFacade;

  /**
   * Facade for accessing the Campaign component.
   */
  @Resource(name = "campaign.facade")
  private ICampaignFacade campaignFacade;

  /**
   * Facade for accessing the SingleUser component.
   */
  @Resource(name = "user.facade")
  private IUserFacade userFacade;

  /**
   * Logger used for Logging.
   */
  private Logger logger = LoggerFactory.getLogger(DummyValueCreator.class);

  /**
   * Inserts the test data.
   */
  @Test
  @Transactional
  public void insertData() {

    logger.debug("start test insertData()");

    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("admin", "password"));

    Campaign c = Campaign.getInstance(0L);
    c.setName("CampaignName");
    campaignFacade.saveCampaign(c);
    logger.debug("saved campaign " + c);

    DrawProcedure d1 = DrawProcedure.getInstance(0L);
    d1.getDrawDate().roll(Calendar.DATE, 1);
    d1.getEndDate().roll(Calendar.DATE, 2);
    d1.setName("1. Losung");
    campaignFacade.saveDrawProcedure(d1);
    logger.debug("saved drawProcedure " + d1);

    DrawProcedure d2 = DrawProcedure.getInstance(0L);
    d2.getStartDate().roll(Calendar.DATE, 2);
    d2.getEndDate().roll(Calendar.DATE, 4);
    d2.getDrawDate().roll(Calendar.DATE, 3);
    d2.setName("2. Losung");
    campaignFacade.saveDrawProcedure(d2);
    logger.debug("saved drawProcedure " + d2);

    FifoProcedure f1 = FifoProcedure.getInstance(0L);
    Calendar f1Start = Calendar.getInstance();
    f1Start.roll(Calendar.DATE, 4);
    Calendar f1End = Calendar.getInstance();
    f1End.roll(Calendar.DATE, 5);
    f1.setInterval(f1Start, f1End);
    f1.setName("Restfifo");
    campaignFacade.saveFifoProcedure(f1);
    logger.debug("saved fifiProcedure " + f1);

    c.addProcedure(f1);
    c.addProcedure(d1);
    c.addProcedure(d2);
    campaignFacade.updateCampaign(c);

    Category c1 = Category.getInstance("Testkategorie1", 0L);
    Category c2 = Category.getInstance("Testkategorie2", 0L);
    eventFacade.saveCategory(c1);
    eventFacade.saveCategory(c2);

    Subject s1 = Subject.getInstance(0L);
    s1.setName("TestFach");
    eventFacade.saveSubject(s1);
    c1.addSubject(s1);
    eventFacade.updateSubject(s1);
    eventFacade.updateCategory(c1);

    SingleUser t1 = SingleUser.getInstance("meixner");
    t1.setName("Gerhard Meixner");
    RoleMapping roleMapping = RoleMapping.getInstance(Role.REGISTERED);
    userFacade.saveRoleMapping(roleMapping);
    t1.getRolemappings().add(roleMapping);
    userFacade.saveSingleUser(t1);

    Event e1 = Event.getInstance(1, 0L);
    e1.setMaxParticipants(20);
    e1.setSubject(s1);
    e1.getTeachers().add(t1.getId());
    e1.setSubject(s1);
    eventFacade.saveEvent(e1);

    Event e2 = Event.getInstance(2, 0L);
    e2.setMaxParticipants(20);
    e2.setSubject(s1);
    e2.getTeachers().add(t1.getId());
    e2.setSubject(s1);
    eventFacade.saveEvent(e2);

    Event e3 = Event.getInstance(3, 0L);
    e3.setMaxParticipants(30);
    e3.setSubject(s1);
    e3.getTeachers().add(t1.getId());
    e3.setSubject(s1);
    eventFacade.saveEvent(e3);

    s1.addEvent(e1);
    s1.addEvent(e2);
    s1.addEvent(e3);
    eventFacade.updateSubject(s1);

    c.getEventIds().add(e1.getId());
    campaignFacade.updateCampaign(c);

    PriorityList p1 = PriorityList.getInstance(0L);
    p1.setDate(Calendar.getInstance());
    campaignFacade.savePriorityList(p1);

    PriorityListItem pi1 = PriorityListItem.getInstance(p1, e1.getId(), 1, 0L);
    PriorityListItem pi2 = PriorityListItem.getInstance(p1, e2.getId(), 2, 0L);
    PriorityListItem pi3 = PriorityListItem.getInstance(p1, e3.getId(), 3, 0L);
    campaignFacade.savePriorityListItem(pi1);
    campaignFacade.savePriorityListItem(pi2);
    campaignFacade.savePriorityListItem(pi3);

    p1.getItems().add(pi1);
    p1.getItems().add(pi2);
    p1.getItems().add(pi3);
    campaignFacade.updatePriorityList(p1);

    d1.getPriorityLists().add(p1);
    campaignFacade.updatePriorityList(p1);
  }
}
