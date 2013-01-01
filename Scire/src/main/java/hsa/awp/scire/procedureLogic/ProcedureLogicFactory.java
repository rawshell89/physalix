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

package hsa.awp.scire.procedureLogic;

import hsa.awp.campaign.facade.ICampaignFacade;
import hsa.awp.campaign.rule.ICampaignRuleChecker;
import hsa.awp.common.exception.ProgrammingErrorException;
import hsa.awp.common.mail.IMailFactory;
import hsa.awp.common.services.TemplateService;
import hsa.awp.event.facade.IEventFacade;
import hsa.awp.scire.procedureLogic.util.XmlDrawLogUtil;
import hsa.awp.user.facade.IUserFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcedureLogicFactory implements IProcedureLogicFactory {
  /**
   * Standard logger.
   */
  private static Logger logger = LoggerFactory.getLogger(ProcedureLogicFactory.class);

  /**
   * Facade for accessing the Campaign component.
   */
  private ICampaignFacade campaignFacade;

  /**
   * RuleChecker for validating rules.
   */
  private ICampaignRuleChecker campaignRuleChecker;

  /**
   * Facade for accessing the Event component.
   */
  private IEventFacade eventFacade;

  /**
   * Factory for sending mails.
   */
  private IMailFactory mailFactory;

  /**
   * Facade for accessing the SingleUser component.
   */
  private IUserFacade userFacade;

  private TemplateService templateService;

  private XmlDrawLogUtil xmlDrawLogUtil;

  @Override
  public IProcedureLogic<?> getInstance(Class<?> logicType) {

    logger.info("creating instance of {}", logicType.getSimpleName());

    AbstractProcedureLogic<?> logic = null;
    try {
      logic = (AbstractProcedureLogic<?>) logicType.newInstance();
    } catch (InstantiationException e) {
      throw new ProgrammingErrorException(e);
    } catch (IllegalAccessException e) {
      throw new ProgrammingErrorException(e);
    }

    // initialize the generated logic
    logic.setCampaignFacade(campaignFacade);
    logic.setEventFacade(eventFacade);
    logic.setMailFactory(mailFactory);
    logic.setUserFacade(userFacade);
    logic.setCampaignRuleChecker(campaignRuleChecker);
    logic.setTemplateService(templateService);

    if (logic instanceof DrawProcedureLogic) {
      ((DrawProcedureLogic) logic).setXmlDrawLogUtil(xmlDrawLogUtil);
    }

    return logic;
  }

  /**
   * Setter for campaignFacade.
   *
   * @param campaignFacade the campaignFacade to set
   */
  public void setCampaignFacade(ICampaignFacade campaignFacade) {

    this.campaignFacade = campaignFacade;
  }

  /**
   * Setter for campaignRuleChecker.
   *
   * @param campaignRuleChecker the campaignRuleChecker to set
   */
  public void setCampaignRuleChecker(ICampaignRuleChecker campaignRuleChecker) {

    this.campaignRuleChecker = campaignRuleChecker;
  }

  /**
   * Setter for eventFacade.
   *
   * @param eventFacade the eventFacade to set
   */
  public void setEventFacade(IEventFacade eventFacade) {

    this.eventFacade = eventFacade;
  }

  /**
   * Setter for mailFactory.
   *
   * @param mailFactory the mailFactory to set
   */
  public void setMailFactory(IMailFactory mailFactory) {

    this.mailFactory = mailFactory;
  }

  /**
   * Setter for userFacade.
   *
   * @param userFacade the userFacade to set
   */
  public void setUserFacade(IUserFacade userFacade) {

    this.userFacade = userFacade;
  }

  public void setTemplateService(TemplateService templateService) {
    this.templateService = templateService;
  }

  public void setXmlDrawLogUtil(XmlDrawLogUtil xmlDrawLogUtil) {
    this.xmlDrawLogUtil = xmlDrawLogUtil;
  }
}
