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

import hsa.awp.campaign.dao.*;
import hsa.awp.campaign.model.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;

import java.util.ArrayList;

public class CampaignFacadeTestFactory { //implements FactoryBean<CampaignFacade>{
  private CampaignFacade instance;


  public CampaignFacade getObject() {

    if (instance == null) {
      instance = new CampaignFacade();

      Mockery mockery = new JUnit4Mockery();

      final ICampaignDao campaignDao = mockery.mock(ICampaignDao.class);
      final IDrawProcedureDao drawProcedureDao = mockery.mock(IDrawProcedureDao.class);
      final IFifoProcedureDao fifoProcedureDao = mockery.mock(IFifoProcedureDao.class);
      final IConfirmProcedureDao confirmProcedureDao = mockery.mock(IConfirmProcedureDao.class);
      final IConfirmedRegistrationDao confirmedRegistrationDao = mockery.mock(IConfirmedRegistrationDao.class);

      mockery.checking(new Expectations() {
        {
          allowing(campaignDao).findAll();
          will(returnValue(new ArrayList<Campaign>()));
          allowing(campaignDao).remove(Campaign.getInstance(0L));
          allowing(campaignDao);
          will(returnValue(Campaign.getInstance(0L)));

          allowing(drawProcedureDao).findAll();
          will(returnValue(new ArrayList<DrawProcedure>()));
          allowing(drawProcedureDao).remove(DrawProcedure.getInstance(0L));
          allowing(drawProcedureDao);
          will(returnValue(DrawProcedure.getInstance(0L)));

          allowing(fifoProcedureDao).findAll();
          will(returnValue(new ArrayList<FifoProcedure>()));
          allowing(fifoProcedureDao).remove(FifoProcedure.getInstance(0L));
          allowing(fifoProcedureDao);
          will(returnValue(FifoProcedure.getInstance(0L)));

          allowing(confirmProcedureDao).findAll();
          will(returnValue(new ArrayList<ConfirmProcedure>()));
          allowing(confirmProcedureDao).remove(ConfirmProcedure.getInstance(0L));
          allowing(confirmProcedureDao);
          will(returnValue(ConfirmProcedure.getInstance(0L)));

          allowing(confirmedRegistrationDao).remove(ConfirmedRegistration.getInstance(123L, 0L));
          allowing(confirmedRegistrationDao);
          will(returnValue(ConfirmedRegistration.getInstance(123L, 0L)));
        }
      });

      instance.setCampaignDao(campaignDao);
      instance.setConfirmedRegistrationDao(confirmedRegistrationDao);
      instance.setDrawProcedureDao(drawProcedureDao);
      instance.setFifoProcedureDao(fifoProcedureDao);
      instance.setConfirmProcedureDao(confirmProcedureDao);
    }
    return instance;
  }

  public Class<?> getObjectType() {

    return CampaignFacade.class;
  }

  public boolean isSingleton() {

    return true;
  }
}
