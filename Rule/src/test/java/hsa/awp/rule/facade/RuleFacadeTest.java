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

package hsa.awp.rule.facade;

import hsa.awp.campaign.rule.ICampaignRuleChecker;
import hsa.awp.rule.model.*;
import hsa.awp.user.model.StudyCourse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config/spring/RuleFacadeTest.xml")
public class RuleFacadeTest extends AbstractStudentRuleTest {
  @Resource(name = "rule.facade")
  private IRuleFacade facade;

  @Resource(name = "rule.facade")
  private ICampaignRuleChecker campaignRuleChecker;

  @Test
  @Transactional
  public void testAddRuleToRegistrationRuleSetExisting() {

    Long campaign = 6L;
    Long event = 5L;

    RegistrationRuleSet set = RegistrationRuleSet.getInstance(0L);
    set.setCampaign(campaign);
    set.setEvent(event);

    TermRule rule = TermRule.getInstance(0L);
    rule.setName("termRule");
    facade.saveRule(rule);

    facade.saveRegistrationRuleSet(set);
    RegistrationRuleSet merged = facade.addRuleToRegistrationRuleSet(campaign, event, 0L, rule);

    // validate
    assertEquals(set.getId(), merged.getId());
    assertEquals(1, merged.getRules().size());
    assertTrue(merged.getRules().contains(rule));
  }

  @Test
  @Transactional
  public void testAddRuleToRegistrationRuleSetNew() {

    Long campaign = 6L;
    Long event = 5L;

    TermRule rule = TermRule.getInstance(0L);
    rule.setName("termRule");
    facade.saveRule(rule);

    RegistrationRuleSet merged = facade.addRuleToRegistrationRuleSet(campaign, event, 0L, rule);

    // validate
    assertEquals(1, merged.getRules().size());
    assertTrue(merged.getRules().contains(rule));
  }

  @Test
  @Transactional
  public void testFindAllRules() {
    // create some rules
    List<Rule> rules = generateRules(10);
    for (Rule rule : rules) {
      // save the rule
      facade.saveRule(rule);
    }

    // try to find all rules again
    List<Rule> found = facade.findAllRules();
    assertEquals(10, found.size());
    assertTrue("has to contain all rules", rules.containsAll(found));
  }

  /**
   * Generates some dummy rules with default values.
   *
   * @param amount the amount of rules to generate
   * @return the generated rules as list
   */
  private List<Rule> generateRules(int amount) {

    List<Rule> rules = new LinkedList<Rule>();
    Rule rule;

    for (int i = 0; i < amount; i++) {
      if (i % 3 == 0) {
        rule = StudyCourseAndTermRule.getInstance(0L);
        rule.setName("SCATR-" + i);
      } else if (i % 2 == 0) {
        rule = TermRule.getInstance(0L);
        rule.setName("TR-" + i);
      } else {
        rule = StudyCourseRule.getInstance(0L);
        rule.setName("SCR-" + i);
      }
      rules.add(rule);
    }

    if (rules.size() != amount) {
      throw new IllegalStateException("could not generate the specified amount of rules.");
    }

    return rules;
  }

  /**
   * Test method for {@link IRuleFacade#getRuleById(Long)}. This test creates one rules and tries to find this rule by its id in
   * the database.
   */
  @Test
  @Transactional
  public void testGetById() {

    Rule rule = StudyCourseRule.getInstance(0L);
    rule.setName("RuleById");
    facade.saveRule(rule);

    Rule found = facade.findRuleById(rule.getId());

    assertEquals(rule.getId(), rule.getId());
    assertEquals(rule.getName(), found.getName());
  }

  @Test
  @Transactional
  public void testIsRegistrationAllowed() {
    // create some rules
    TermRule termRule = TermRule.getInstance(0L);
    termRule.setName("termRule");
    termRule.setMinTerm(3);

    StudyCourseRule courseRule = StudyCourseRule.getInstance(0L);
    courseRule.setName("courseRule");
    courseRule.setStudyCourse(course.getId());

    facade.saveRule(termRule);
    facade.saveRule(courseRule);

    // create RuleSet
    RegistrationRuleSet set = RegistrationRuleSet.getInstance(0L);
    set.setCampaign(campaign.getId());
    set.setEvent(event.getId());
    set.addRule(termRule);
    set.addRule(courseRule);
    facade.saveRegistrationRuleSet(set);

    // student in wrong term
    student.setTerm(2);
    assertFalse(campaignRuleChecker.isRegistrationAllowed(student, campaign, event));
    // student in valid term
    student.setTerm(3);
    assertTrue(campaignRuleChecker.isRegistrationAllowed(student, campaign, event));

    // student in different study course
    student.setStudyCourse(StudyCourse.getInstance("different"));
    assertFalse(campaignRuleChecker.isRegistrationAllowed(student, campaign, event));
  }

  @Test
  @Transactional
  public void testRemoveRule() {
    // create one rule
    Rule rule = TermRule.getInstance(0L);
    rule.setName("RemoveRule");
    facade.saveRule(rule);

    // assert that the rule is saved
    assertEquals(1, facade.findAllRules().size());
    assertEquals(rule, facade.findRuleById(rule.getId()));

    // remove rule
    facade.removeRule(rule);

    // assert that the rule is removed
    assertEquals(0, facade.findAllRules().size());
  }

  @Test
  @Transactional
  public void testRemoveRuleWithSet() {
    // create one rule
    TermRule rule = TermRule.getInstance(0L);
    rule.setName("RemoveRuleWithSet");
    facade.saveRule(rule);

    // create rule set
    RegistrationRuleSet set = RegistrationRuleSet.getInstance(0L);
    set.addRule(rule);
    facade.saveRegistrationRuleSet(set);

    // assert that the rule is saved
    assertEquals(1, facade.findAllRules().size());
    assertEquals(rule, facade.findRuleById(rule.getId()));
    assertTrue(set.getRules().contains(rule));

    // remove rule
    facade.removeRule(rule);

    assertEquals(0, set.getRules().size());
    assertFalse(set.getRules().contains(rule));


    // assert that the rule is removed
    assertEquals(0, facade.findAllRules().size());
  }

  @Transactional
  @Test
  public void testRemoveRules() {

    Long campaignId = 5L;
    Long eventId = 3L;

    TermRule rule1 = TermRule.getInstance(0L);
    rule1.setMaxTerm(5);
    rule1.setMinTerm(3);
    rule1.setName("rule1");
    facade.saveRule(rule1);

    facade.addRuleToRegistrationRuleSet(campaignId, eventId, 0L, rule1);

    TermRule rule2 = TermRule.getInstance(0L);
    rule2.setMaxTerm(5);
    rule2.setMinTerm(3);
    rule2.setName("rule2");
    facade.saveRule(rule2);

    facade.addRuleToRegistrationRuleSet(campaignId, eventId, 0L, rule2);

    Set<Rule> rules = facade.findRulesByCampaignAndEvent(campaignId, eventId);

    assertEquals(2, rules.size());
    assertTrue(rules.contains(rule1));
    assertTrue(rules.contains(rule2));

    facade.removeRulesConnection(campaignId, eventId);

    rules = facade.findRulesByCampaignAndEvent(campaignId, eventId);

    assertEquals(null, rules);
  }

  @Test
  @Transactional
  public void testSaveRule() {
    // create one rule
    Rule rule = TermRule.getInstance(0L);
    rule.setName("SaveRule");
    facade.saveRule(rule);

    // find rule again
    assertEquals(1, facade.findAllRules().size());
    assertEquals(rule, facade.findRuleById(rule.getId()));
  }

  @Test
  @Transactional
  public void testUpdateRule() {
    // create one rule
    TermRule rule = TermRule.getInstance(0L);
    rule.setName("UpdateRule");
    facade.saveRule(rule);

    // change some values
    rule.setMinTerm(10);
    rule.setMaxTerm(15);

    // update changes
    rule = facade.updateRule(rule);

    // assert changes
    TermRule found = (TermRule) facade.findRuleById(rule.getId());
    assertEquals(rule.getMinTerm(), found.getMinTerm());
    assertEquals(rule.getMaxTerm(), found.getMaxTerm());
  }
}
