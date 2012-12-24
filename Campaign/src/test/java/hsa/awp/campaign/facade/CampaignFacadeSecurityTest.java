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

package hsa.awp.campaign.facade;

import hsa.awp.campaign.model.*;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
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
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * This unit test tests spring security of EventFacade. For every secured method
 * there are 3 tests. One with no credentials, one with existing credentials but
 * insufficient rights and finally a test with sufficient rights.
 *
 * @author alex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:config/spring/campaign_security_test.xml"})
@Ignore // temporary ignored because method security does not work with ldap yet (alex)
public class CampaignFacadeSecurityTest extends AbstractJUnit4SpringContextTests {
  private Authentication adminAuthentication;

  @Resource(name = "campaignFacade")
  private ICampaignFacade campaignFacade;
  private Authentication facultyAuthentication;
  private Mockery mockery = new JUnit4Mockery();
  private Authentication secretaryAuthentication;
  private SecurityContext securityContext;

  private Authentication studentAuthentication;

  /**
   * This constructor sets up the mocks and facade.
   */
  public CampaignFacadeSecurityTest() {

    securityContext = SecurityContextHolder.getContext();
    secretaryAuthentication = new UsernamePasswordAuthenticationToken(
        "secretary", "password");
    adminAuthentication = new UsernamePasswordAuthenticationToken("admin",
        "password");
    facultyAuthentication = new UsernamePasswordAuthenticationToken(
        "faculty", "password");
    studentAuthentication = new UsernamePasswordAuthenticationToken(
        "student", "password");
  }

  @After
  public void tearDown() {

    securityContext.setAuthentication(null);
  }

  /*
  * getAllCampaigns() Accepted roles: ROLE_STUDENT, ROLE_FACULTY,
  * ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test
  public void testGetAllCampaignsGood() {

    securityContext.setAuthentication(adminAuthentication);
    campaignFacade.getAllCampaigns();
    securityContext.setAuthentication(facultyAuthentication);
    campaignFacade.getAllCampaigns();
    securityContext.setAuthentication(secretaryAuthentication);
    campaignFacade.getAllCampaigns();
    securityContext.setAuthentication(studentAuthentication);
    campaignFacade.getAllCampaigns();
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testGetAllCampaignsNo() {

    securityContext.setAuthentication(null);
    campaignFacade.getAllCampaigns();
  }

  /*
  * getCampaignById(1L) Accepted roles: ROLE_STUDENT, ROLE_FACULTY,
  * ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test
  public void testGetCampaignByIdGood() {

    securityContext.setAuthentication(adminAuthentication);
    campaignFacade.getCampaignById(1L);
    securityContext.setAuthentication(facultyAuthentication);
    campaignFacade.getCampaignById(1L);
    securityContext.setAuthentication(secretaryAuthentication);
    campaignFacade.getCampaignById(1L);
    securityContext.setAuthentication(studentAuthentication);
    campaignFacade.getCampaignById(1L);
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testGetCampaignByIdNo() {

    campaignFacade.getCampaignById(1L);
  }

  /*
  * getConfirmProcedureById(1L) Accepted roles: ROLE_FACULTY,
  * ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test
  public void testGetConfirmProcedureByIdGood() {

    securityContext.setAuthentication(adminAuthentication);
    campaignFacade.getConfirmProcedureById(1L);
    securityContext.setAuthentication(facultyAuthentication);
    campaignFacade.getConfirmProcedureById(1L);
    securityContext.setAuthentication(secretaryAuthentication);
    campaignFacade.getConfirmProcedureById(1L);
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testGetConfirmProcedureByIdNo() {

    campaignFacade.getConfirmProcedureById(1L);
  }

  @Test(expected = AccessDeniedException.class)
  public void testGetConfirmProcedureInsufficient1() {

    securityContext.setAuthentication(studentAuthentication);
    campaignFacade.getConfirmProcedureById(1L);
  }

  /*
  * getConfirmedRegistrationById(1L) Accepted roles: ROLE_STUDENT, ROLE_FACULTY,
  * ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test
  public void testGetConfirmedRegistrationByIdGood() {

    securityContext.setAuthentication(adminAuthentication);
    campaignFacade.getConfirmedRegistrationById(1L);
    securityContext.setAuthentication(facultyAuthentication);
    campaignFacade.getConfirmedRegistrationById(1L);
    securityContext.setAuthentication(secretaryAuthentication);
    campaignFacade.getConfirmedRegistrationById(1L);
    securityContext.setAuthentication(studentAuthentication);
    campaignFacade.getConfirmedRegistrationById(1L);
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testGetConfirmedRegistrationByIdNo() {

    campaignFacade.getConfirmedRegistrationById(1L);
  }

  /*
  * getDrawProcedureById(1L) Accepted roles: ROLE_FACULTY,
  * ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test
  public void testGetDrawProcedureByIdGood() {

    securityContext.setAuthentication(adminAuthentication);
    campaignFacade.getDrawProcedureById(1L);
    securityContext.setAuthentication(facultyAuthentication);
    campaignFacade.getDrawProcedureById(1L);
    securityContext.setAuthentication(secretaryAuthentication);
    campaignFacade.getDrawProcedureById(1L);
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testGetDrawProcedureByIdNo() {

    campaignFacade.getDrawProcedureById(1L);
  }

  @Test(expected = AccessDeniedException.class)
  public void testGetDrawProcedureInsufficient1() {

    securityContext.setAuthentication(studentAuthentication);
    campaignFacade.getDrawProcedureById(1L);
  }

  /*
  * getFifoProcedureById(1L) Accepted roles: ROLE_FACULTY,
  * ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test
  public void testGetFifoProcedureByIdGood() {

    securityContext.setAuthentication(adminAuthentication);
    campaignFacade.getFifoProcedureById(1L);
    securityContext.setAuthentication(facultyAuthentication);
    campaignFacade.getFifoProcedureById(1L);
    securityContext.setAuthentication(secretaryAuthentication);
    campaignFacade.getFifoProcedureById(1L);
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testGetFifoProcedureByIdNo() {

    campaignFacade.getFifoProcedureById(1L);
  }

  @Test(expected = AccessDeniedException.class)
  public void testGetFifoProcedureInsufficient1() {

    securityContext.setAuthentication(studentAuthentication);
    campaignFacade.getFifoProcedureById(1L);
  }

  @Test
  public void testRemoveCampaignGood() {

    securityContext.setAuthentication(adminAuthentication);
    campaignFacade.removeCampaign(Campaign.getInstance(0L));
    securityContext.setAuthentication(facultyAuthentication);
    campaignFacade.removeCampaign(Campaign.getInstance(0L));
    securityContext.setAuthentication(secretaryAuthentication);
    campaignFacade.removeCampaign(Campaign.getInstance(0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testRemoveCampaignInsufficient1() {

    securityContext.setAuthentication(studentAuthentication);
    campaignFacade.removeCampaign(Campaign.getInstance(0L));
  }

  /*
  * mergeDrawProcedure(new DrawProcedure()) Accepted roles: ROLE_FACULTY,
  * ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testRemoveCampaignNo() {

    campaignFacade.removeCampaign(Campaign.getInstance(0L));
  }

  /*
  * removeConfirmProcedure(ConfirmProcedure.getInstance()) Accepted roles: ROLE_FACULTY,
  * ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test
  public void testRemoveConfirmProcedureGood() {

    securityContext.setAuthentication(adminAuthentication);
    campaignFacade.removeConfirmProcedure(ConfirmProcedure.getInstance(0L));
    securityContext.setAuthentication(facultyAuthentication);
    campaignFacade.removeConfirmProcedure(ConfirmProcedure.getInstance(0L));
    securityContext.setAuthentication(secretaryAuthentication);
    campaignFacade.removeConfirmProcedure(ConfirmProcedure.getInstance(0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testRemoveConfirmProcedureInsufficient1() {

    securityContext.setAuthentication(studentAuthentication);
    campaignFacade.removeConfirmProcedure(ConfirmProcedure.getInstance(0L));
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testRemoveConfirmProcedureNo() {

    campaignFacade.removeConfirmProcedure(ConfirmProcedure.getInstance(0L));
  }

  @Test
  public void testRemoveConfirmedRegistrationGood() {

    securityContext.setAuthentication(adminAuthentication);
    campaignFacade.removeConfirmedRegistration(ConfirmedRegistration.getInstance(123L, 0L));
    securityContext.setAuthentication(facultyAuthentication);
    campaignFacade.removeConfirmedRegistration(ConfirmedRegistration.getInstance(123L, 0L));
    securityContext.setAuthentication(secretaryAuthentication);
    campaignFacade.removeConfirmedRegistration(ConfirmedRegistration.getInstance(123L, 0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testRemoveConfirmedRegistrationInsufficient1() {

    securityContext.setAuthentication(studentAuthentication);
    campaignFacade.removeConfirmedRegistration(ConfirmedRegistration.getInstance(123L, 0L));
  }

  /*
  * mergeFifoProcedure(new FifoProcedure()) Accepted roles: ROLE_FACULTY,
  * ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testRemoveConfirmedRegistrationNo() {

    campaignFacade.removeConfirmedRegistration(ConfirmedRegistration.getInstance(123L, 0L));
  }

  @Test
  public void testRemoveDrawProcedureGood() {

    securityContext.setAuthentication(adminAuthentication);
    campaignFacade.removeDrawProcedure(DrawProcedure.getInstance(0L));
    securityContext.setAuthentication(facultyAuthentication);
    campaignFacade.removeDrawProcedure(DrawProcedure.getInstance(0L));
    securityContext.setAuthentication(secretaryAuthentication);
    campaignFacade.removeDrawProcedure(DrawProcedure.getInstance(0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testRemoveDrawProcedureInsufficient1() {

    securityContext.setAuthentication(studentAuthentication);
    campaignFacade.removeDrawProcedure(DrawProcedure.getInstance(0L));
  }

  /*
  * removeCampaign(Campaign.getInstance(0L)) Accepted roles: ROLE_FACULTY,
  * ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testRemoveDrawProcedureNo() {

    campaignFacade.removeDrawProcedure(DrawProcedure.getInstance(0L));
  }

  @Test
  public void testRemoveFifoProcedureGood() {

    securityContext.setAuthentication(adminAuthentication);
    campaignFacade.removeFifoProcedure(FifoProcedure.getInstance(0L));
    securityContext.setAuthentication(facultyAuthentication);
    campaignFacade.removeFifoProcedure(FifoProcedure.getInstance(0L));
    securityContext.setAuthentication(secretaryAuthentication);
    campaignFacade.removeFifoProcedure(FifoProcedure.getInstance(0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testRemoveFifoProcedureInsufficient1() {

    securityContext.setAuthentication(studentAuthentication);
    campaignFacade.removeFifoProcedure(FifoProcedure.getInstance(0L));
  }

  /*
  * removeConfirmedRegistration(ConfirmedRegistration.getInstance(123L)) Accepted roles: ROLE_FACULTY,
  * ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testRemoveFifoProcedureNo() {

    campaignFacade.removeFifoProcedure(FifoProcedure.getInstance(0L));
  }

  @Test
  public void testSaveCampaignGood() {

    securityContext.setAuthentication(adminAuthentication);
    campaignFacade.saveCampaign(Campaign.getInstance(0L));
    securityContext.setAuthentication(facultyAuthentication);
    campaignFacade.saveCampaign(Campaign.getInstance(0L));
    securityContext.setAuthentication(secretaryAuthentication);
    campaignFacade.saveCampaign(Campaign.getInstance(0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testSaveCampaignInsufficient1() {

    securityContext.setAuthentication(studentAuthentication);
    campaignFacade.saveCampaign(Campaign.getInstance(0L));
  }

  /*
  * mergeConfirmedRegistration(ConfirmedRegistration.getInstance(123L)) Accepted roles: ROLE_FACULTY,
  * ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testSaveCampaignNo() {

    campaignFacade.saveCampaign(Campaign.getInstance(0L));
  }

  /*
  * saveConfirmProcedure(ConfirmProcedure.getInstance()) Accepted roles: ROLE_FACULTY,
  * ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test
  public void testSaveConfirmProcedureGood() {

    securityContext.setAuthentication(adminAuthentication);
    campaignFacade.saveConfirmProcedure(ConfirmProcedure.getInstance(0L));
    securityContext.setAuthentication(facultyAuthentication);
    campaignFacade.saveConfirmProcedure(ConfirmProcedure.getInstance(0L));
    securityContext.setAuthentication(secretaryAuthentication);
    campaignFacade.saveConfirmProcedure(ConfirmProcedure.getInstance(0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testSaveConfirmProcedureInsufficient1() {

    securityContext.setAuthentication(studentAuthentication);
    campaignFacade.saveConfirmProcedure(ConfirmProcedure.getInstance(0L));
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testSaveConfirmProcedureNo() {

    campaignFacade.saveConfirmProcedure(ConfirmProcedure.getInstance(0L));
  }

  @Test
  public void testSaveDrawProcedureGood() {

    securityContext.setAuthentication(adminAuthentication);
    campaignFacade.saveDrawProcedure(DrawProcedure.getInstance(0L));
    securityContext.setAuthentication(facultyAuthentication);
    campaignFacade.saveDrawProcedure(DrawProcedure.getInstance(0L));
    securityContext.setAuthentication(secretaryAuthentication);
    campaignFacade.saveDrawProcedure(DrawProcedure.getInstance(0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testSaveDrawProcedureInsufficient1() {

    securityContext.setAuthentication(studentAuthentication);
    campaignFacade.saveDrawProcedure(DrawProcedure.getInstance(0L));
  }

  /*
  * removeDrawProcedure(DrawProcedure.getInstance()) Accepted roles: ROLE_FACULTY,
  * ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testSaveDrawProcedureNo() {

    campaignFacade.saveDrawProcedure(DrawProcedure.getInstance(0L));
  }

  @Test
  public void testSaveFifoProcedureGood() {

    securityContext.setAuthentication(adminAuthentication);
    campaignFacade.saveFifoProcedure(FifoProcedure.getInstance(0L));
    securityContext.setAuthentication(facultyAuthentication);
    campaignFacade.saveFifoProcedure(FifoProcedure.getInstance(0L));
    securityContext.setAuthentication(secretaryAuthentication);
    campaignFacade.saveFifoProcedure(FifoProcedure.getInstance(0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testSaveFifoProcedureInsufficient1() {

    securityContext.setAuthentication(studentAuthentication);
    campaignFacade.saveFifoProcedure(FifoProcedure.getInstance(0L));
  }

  /*
  * removeFifoProcedure(FifoProcedure.getInstance()) Accepted roles: ROLE_FACULTY,
  * ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testSaveFifoProcedureNo() {

    campaignFacade.saveFifoProcedure(FifoProcedure.getInstance(0L));
  }

  @Test
  public void testUpdateCampaignGood() {

    securityContext.setAuthentication(adminAuthentication);
    campaignFacade.updateCampaign(Campaign.getInstance(0L));
    securityContext.setAuthentication(facultyAuthentication);
    campaignFacade.updateCampaign(Campaign.getInstance(0L));
    securityContext.setAuthentication(secretaryAuthentication);
    campaignFacade.updateCampaign(Campaign.getInstance(0L));
  }

  /*
  * updateCampaign(Campaign.getInstance(0L)) Accepted roles: ROLE_FACULTY,
  * ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test(expected = AccessDeniedException.class)
  public void testUpdateCampaignInsufficient1() {

    securityContext.setAuthentication(studentAuthentication);
    campaignFacade.updateCampaign(Campaign.getInstance(0L));
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testUpdateCampaignNo() {

    campaignFacade.updateCampaign(Campaign.getInstance(0L));
  }

  /*
  * updateConfirmProcedure(ConfirmProcedure.getInstance()) Accepted roles: ROLE_FACULTY,
  * ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test
  public void testUpdateConfirmProcedureGood() {

    securityContext.setAuthentication(adminAuthentication);
    campaignFacade.updateConfirmProcedure(ConfirmProcedure.getInstance(0L));
    securityContext.setAuthentication(facultyAuthentication);
    campaignFacade.updateConfirmProcedure(ConfirmProcedure.getInstance(0L));
    securityContext.setAuthentication(secretaryAuthentication);
    campaignFacade.updateConfirmProcedure(ConfirmProcedure.getInstance(0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testUpdateConfirmProcedureInsufficient1() {

    securityContext.setAuthentication(studentAuthentication);
    campaignFacade.updateConfirmProcedure(ConfirmProcedure.getInstance(0L));
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testUpdateConfirmProcedureNo() {

    campaignFacade.updateConfirmProcedure(ConfirmProcedure.getInstance(0L));
  }

  @Test
  public void testUpdateConfirmedRegistrationGood() {

    securityContext.setAuthentication(adminAuthentication);
    campaignFacade.updateConfirmedRegistration(ConfirmedRegistration.getInstance(123L, 0L));
    securityContext.setAuthentication(facultyAuthentication);
    campaignFacade.updateConfirmedRegistration(ConfirmedRegistration.getInstance(123L, 0L));
    securityContext.setAuthentication(secretaryAuthentication);
    campaignFacade.updateConfirmedRegistration(ConfirmedRegistration.getInstance(123L, 0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testUpdateConfirmedRegistrationInsufficient1() {

    securityContext.setAuthentication(studentAuthentication);
    campaignFacade.updateConfirmedRegistration(ConfirmedRegistration.getInstance(123L, 0L));
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testUpdateConfirmedRegistrationNo() {

    campaignFacade.updateConfirmedRegistration(ConfirmedRegistration.getInstance(123L, 0L));
  }

  /*
  * persistFifoProcedure(FifoProcedure.getInstance()) Accepted roles: ROLE_FACULTY,
  * ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test
  public void testUpdateDrawProcedureGood() {

    securityContext.setAuthentication(adminAuthentication);
    campaignFacade.updateDrawProcedure(DrawProcedure.getInstance(0L));
    securityContext.setAuthentication(facultyAuthentication);
    campaignFacade.updateDrawProcedure(DrawProcedure.getInstance(0L));
    securityContext.setAuthentication(secretaryAuthentication);
    campaignFacade.updateDrawProcedure(DrawProcedure.getInstance(0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testUpdateDrawProcedureInsufficient1() {

    securityContext.setAuthentication(studentAuthentication);
    campaignFacade.updateDrawProcedure(DrawProcedure.getInstance(0L));
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testUpdateDrawProcedureNo() {

    campaignFacade.updateDrawProcedure(DrawProcedure.getInstance(0L));
  }

  /*
  * persistDrawProcedure(DrawProcedure.getInstance()) Accepted roles: ROLE_FACULTY,
  * ROLE_SECRETARY, ROLE_ADMIN
  */

  @Test
  public void testUpdateFifoProcedureGood() {

    securityContext.setAuthentication(adminAuthentication);
    campaignFacade.updateFifoProcedure(FifoProcedure.getInstance(0L));
    securityContext.setAuthentication(facultyAuthentication);
    campaignFacade.updateFifoProcedure(FifoProcedure.getInstance(0L));
    securityContext.setAuthentication(secretaryAuthentication);
    campaignFacade.updateFifoProcedure(FifoProcedure.getInstance(0L));
  }

  @Test(expected = AccessDeniedException.class)
  public void testUpdateFifoProcedureInsufficient1() {

    securityContext.setAuthentication(studentAuthentication);
    campaignFacade.updateFifoProcedure(FifoProcedure.getInstance(0L));
  }

  @Test(expected = AuthenticationCredentialsNotFoundException.class)
  public void testUpdateFifoProcedureNo() {

    campaignFacade.updateFifoProcedure(FifoProcedure.getInstance(0L));
  }
}
