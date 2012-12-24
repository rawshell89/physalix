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

package hsa.awp.user.facade;

import hsa.awp.user.dao.*;
import hsa.awp.user.model.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Facade for accessing all domain objects in the SingleUser Context.
 *
 * @author johannes
 */
public class UserFacade implements IUserFacade {
  /**
   * {@link Group} Data Access Object.
   */
  private IGroupDao groupDao;

  /**
   * {@link SingleUser} Data Access Object.
   */
  private ISingleUserDao singleUserDao;

  /**
   * {@link StudyCourse} Data Access Object.
   */
  private IStudyCourseDao studyCourseDao;

  /**
   * {@link User} Data Access Object.
   */
  private IUserDao userDao;

  private IMandatorDao mandatorDao;

  private IRoleMappingDao roleMappingDao;

  @Override
  @Transactional
  public List<Group> getAllGroups() {

    return groupDao.findAll();
  }

  @Override
  @Transactional
  public List<SingleUser> getAllSingleUsers() {

    return singleUserDao.findAll();
  }

  @Override
  public List<SingleUser> getAllSingleUsersByRole(Role role) {

    return singleUserDao.findUsersByRole(role);
  }

  @Override
  @Transactional
  public List<StudyCourse> getAllStudyCourses() {

    return studyCourseDao.findAll();
  }

  @Override
  public List<SingleUser> getAllTeachers() {

    throw new UnsupportedOperationException("NOT IMPLEMENTED YET... Ask Alex if you need this implementiation");
  }

  @Override
  @Transactional
  public List<User> getAllUsers() {

    return userDao.findAll();
  }

  @Override
  @Transactional
  public Group getGroupById(Long id) {

    return groupDao.findById(id);
  }

  @Override
  @Transactional
  public SingleUser getSingleUserById(Long id) {

    return singleUserDao.findById(id);
  }

  @Transactional
  @Override
  public SingleUser getSingleUserByLogin(String login) {

    return singleUserDao.findByUsername(login);
  }

  @Override
  @Transactional
  public StudyCourse getStudyCourseById(Long id) {

    return studyCourseDao.findById(id);
  }

  @Override
  @Transactional
  public StudyCourse getStudyCourseByName(String name) {

    return studyCourseDao.findByName(name);
  }

  @Override
  @Transactional
  public User getUserById(Long id) {

    return userDao.findById(id);
  }

  @Override
  @Transactional
  public void removeAllGroups() {

    groupDao.removeAll();
  }

  @Override
  @Transactional
  public void removeAllSingleUsers() {

    singleUserDao.removeAll();
  }

  @Override
  @Transactional
  public void removeAllStudyCourses() {

    studyCourseDao.removeAll();
  }

  @Override
  @Transactional
  public void removeGroup(Group g) {

    groupDao.remove(g);
  }

  @Override
  @Transactional
  public void removeSingleUser(SingleUser u) {

    singleUserDao.remove(u);
  }

  @Override
  @Transactional
  public void removeStudyCourse(StudyCourse s) {

    studyCourseDao.remove(s);
  }

  @Override
  @Transactional
  public void removeUser(User p) {

    userDao.remove(p);
  }

  @Override
  @Transactional
  public Group saveGroup(Group g) {

    return groupDao.persist(g);
  }

  @Override
  @Transactional
  public SingleUser saveSingleUser(SingleUser u) {

    return singleUserDao.persist(u);
  }

  @Override
  @Transactional
  public StudyCourse saveStudyCourse(StudyCourse s) {

    return studyCourseDao.persist(s);
  }

  @Override
  @Transactional
  public User saveUser(User p) {

    return userDao.persist(p);
  }

  @Override
  @Transactional
  public Group updateGroup(Group g) {

    return groupDao.merge(g);
  }

  @Override
  @Transactional
  public SingleUser updateSingleUser(SingleUser u) {

    if (u == null) {
      throw new IllegalArgumentException("user is null");
    }

    checkSingleUserRoles(u);

    return singleUserDao.merge(u);
  }

  private void checkSingleUserRoles(SingleUser singleUser) {

    for (RoleMapping roleMapping : singleUser.getRolemappings()) {
      roleMapping = getRoleMappingById(roleMapping.getId());
      Role role = roleMapping.getRole();
      if (Role.SECRETARY.equals(role) || Role.APPADMIN.equals(role)) {
        if (roleMapping.getMandators().contains(getAllMandator())) {
          throw new IllegalStateException("Secretary or AppAdmin aren't allowed to be in allMandator");
        }
      }
    }
  }

  @Override
  @Transactional
  public StudyCourse updateStudyCourse(StudyCourse s) {

    return studyCourseDao.merge(s);
  }

  @Override
  @Transactional
  public User updateUser(User p) {

    return userDao.merge(p);
  }

  @Override
  @Transactional
  public List<SingleUser> searchForUser(String searchString) {
    return singleUserDao.searchForUser(searchString);
  }

  @Override
  @Transactional
  public void readAllStudyCourses() {
    singleUserDao.readAllStudyCourses();
  }

  @Override
  @Transactional
  public Mandator getAllMandator() {
    Mandator mandator = mandatorDao.findByName(Mandator.allMandator);

    if (mandator == null) {
      mandator = Mandator.getInstance(Mandator.allMandator);
      mandator = mandatorDao.persist(mandator);
    }

    return mandator;
  }

  @Override
  @Transactional
  public List<Mandator> getAllMandators() {
    return mandatorDao.findAll();
  }

  @Override
  @Transactional
  public Mandator getMandatorByName(String name) {
    return mandatorDao.findByName(name);
  }

  @Override
  @Transactional
  public Mandator getMandatorById(Long id) {
    return mandatorDao.findById(id);
  }

  @Override
  @Transactional
  public void removeMandator(Mandator mandator) {
    mandatorDao.remove(mandator);
  }

  @Override
  @Transactional
  public Mandator saveMandator(Mandator mandator) {
    return mandatorDao.persist(mandator);
  }

  @Override
  @Transactional
  public Mandator updateMandator(Mandator mandator) {
    return mandatorDao.merge(mandator);
  }

  @Override
  @Transactional
  public RoleMapping getRoleMappingById(Long id) {
    return roleMappingDao.findById(id);
  }

  @Override
  @Transactional
  public List<RoleMapping> getRoleMappingsByRole(Role role) {
    return roleMappingDao.findByRole(role);
  }

  @Override
  @Transactional
  public List<RoleMapping> getRoleMappingsByUser(SingleUser singleUser) {
    return roleMappingDao.findByUser(singleUser);
  }

  @Override
  @Transactional
  public void removeRoleMapping(RoleMapping roleMapping) {
    roleMappingDao.remove(roleMapping);
  }

  @Override
  @Transactional
  public RoleMapping saveRoleMapping(RoleMapping roleMapping) {
    return roleMappingDao.persist(roleMapping);
  }

  @Override
  @Transactional
  public RoleMapping updateRoleMapping(RoleMapping roleMapping) {
    return roleMappingDao.merge(roleMapping);
  }

  @Override
  @Transactional
  public RoleMapping getRoleMappingByExample(SingleUser user, String mandator, Role role) {
    return roleMappingDao.findByExample(user, mandator, role);
  }

  /**
   * Setter for groupDao.
   *
   * @param groupDao the groupDao to set
   */
  public void setGroupDao(IGroupDao groupDao) {

    this.groupDao = groupDao;
  }

  /**
   * Setter for the singleUserDao..
   *
   * @param singleUserDao the singleUserDao to set
   */
  public void setSingleUserDao(ISingleUserDao singleUserDao) {

    this.singleUserDao = singleUserDao;
  }

  /**
   * Setter for studyCourseDao.
   *
   * @param studyCourseDao the studyCourseDao to set
   */
  public void setStudyCourseDao(IStudyCourseDao studyCourseDao) {

    this.studyCourseDao = studyCourseDao;
  }

  /**
   * Setter for the userDao.
   *
   * @param userDao the userDao to set
   */
  public void setUserDao(IUserDao userDao) {

    this.userDao = userDao;
  }

  public void setMandatorDao(IMandatorDao mandatorDao) {
    this.mandatorDao = mandatorDao;
  }

  public void setRoleMappingDao(IRoleMappingDao roleMappingDao) {
    this.roleMappingDao = roleMappingDao;
  }

}


