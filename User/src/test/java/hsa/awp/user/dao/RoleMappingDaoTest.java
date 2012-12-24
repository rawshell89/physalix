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

import hsa.awp.common.test.GenericDaoTest;
import hsa.awp.common.test.IObjectFactory;
import hsa.awp.user.model.Mandator;
import hsa.awp.user.model.Role;
import hsa.awp.user.model.RoleMapping;
import hsa.awp.user.model.SingleUser;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class RoleMappingDaoTest extends GenericDaoTest<RoleMapping, RoleMappingDao, Long> {


  public RoleMappingDaoTest() {
    super();
    super.setDao(new RoleMappingDao());

    super.setIObjectFactory(new IObjectFactory<RoleMapping, Long>() {
      private int counter = 0;

      @Override
      public RoleMapping getInstance() {

        return RoleMapping.getInstance(Role.REGISTERED);
      }
    });
  }

  private RoleMapping getInstance(Role role) {

    RoleMapping roleMapping = getInstance();
    roleMapping.setRole(role);

    return roleMapping;
  }


  @Test
  public void testFindByRole() {

    generateAndPersistObjects(4);

    RoleMapping sysAdminMapping = getInstance(Role.SYSADMIN);
    this.startTransaction();
    getDao().persist(sysAdminMapping);
    this.commit();

    assertEquals(5, getDao().findAll().size());
    assertEquals(1, getDao().findByRole(Role.SYSADMIN).size());
    assertEquals(sysAdminMapping, getDao().findByRole(Role.SYSADMIN).get(0));


  }

  @Test
  public void testFindByUser() {

    SingleUserDbDao dao = initDao(new SingleUserDbDao());

    List<RoleMapping> mappingList = generateAndPersistObjects(4);

    this.startTransaction();
    SingleUser user = dao.persist(SingleUser.getInstance("test"));
    mappingList.get(0).setSingleUser(user);

    getDao().merge(mappingList.get(0));
    this.commit();

    assertEquals(1, getDao().findByUser(user).size());
    assertEquals(user, getDao().findByUser(user).get(0).getSingleUser());
  }


  @Test
  public void testFindByExample() {

    SingleUserDbDao dao = initDao(new SingleUserDbDao());
    MandatorDao mandatorDao = initDao(new MandatorDao());


    List<RoleMapping> mappingList = generateAndPersistObjects(4);

    this.startTransaction();
    Mandator mandator = mandatorDao.persist(Mandator.getInstance(Mandator.allMandator));
    SingleUser user = dao.persist(SingleUser.getInstance("test2"));
    mappingList.get(0).setSingleUser(user);
    mappingList.get(0).setRole(Role.SYSADMIN);
    mappingList.get(0).getMandators().add(mandator);

    getDao().merge(mappingList.get(0));
    this.commit();

    assertEquals(mappingList.get(0), getDao().findByExample(user, Mandator.allMandator, Role.SYSADMIN));
    assertEquals(null, getDao().findByExample(user, "nonExistingMandator", Role.SYSADMIN));
  }

  @Override
  @Test
  public void testMerge() {

    RoleMapping mapping = getInstance();

    this.startTransaction();
    getDao().persist(mapping);
    this.commit();

    Long id = mapping.getId();

    this.startTransaction();
    mapping.setRole(Role.APPADMIN);
    this.commit();
    RoleMapping mergedMapping = getDao().merge(mapping);

    assertEquals(Role.APPADMIN, mergedMapping.getRole());

  }

}
