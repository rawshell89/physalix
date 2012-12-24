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

package hsa.awp.event.model;

import hsa.awp.common.AbstractMandatorableDomainObject;
import hsa.awp.common.exception.ProgrammingErrorException;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Persistent object for Categories.
 *
 * @author klassm
 */
@Entity
@Table(name = "`category`", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "mandatorId"})
})
public class Category extends AbstractMandatorableDomainObject<Long> {
  /**
   * Version UID which is used for serialization.
   */
  private static final long serialVersionUID = -3655365762586158618L;

  /**
   * Identifier of Category.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id = 0L;

  /**
   * Name of the category. Must be unique!
   */
  @Column(name = "name")
  private String name;

  /**
   * Reference to all subjects in the category.
   */

  @Column(name = "subjects")
  @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
  @JoinTable
  private Set<Subject> subjects;

  /**
   * Creates a new {@link Category} and initializes it with appropriate values (e.g. inserts a HashSet at appropriate places, so
   * that the Sets can be used without risking a {@link NullPointerException}.
   *
   * @param name name ob the {@link Category}
   * @return new domain object.
   */
  public static Category getInstance(String name, Long mandatorId) {

    if (name == null) {
      throw new IllegalArgumentException("no name given");
    }

    Category category = new Category(mandatorId);
    category.setSubjects(new HashSet<Subject>());
    category.setName(name);

    return category;
  }

  /**
   * Constructor for creating a Category.
   */
  protected Category(Long mandatorId) {
    super(mandatorId);
  }

  Category() {
  }

  /**
   * Constructs a new {@link Category} by its unique name.
   *
   * @param name name of the Category to create.
   */
  public Category(String name, Long mandatorId) {

    super(mandatorId);

    if (name == null) {
      throw new IllegalArgumentException("no name given");
    }

    setSubjects(new HashSet<Subject>());
    setName(name);
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Category)) {
      return false;
    }
    Category other = (Category) obj;

    // if item is persisted use only the id for checking
    if (id != null && id != 0L) {
      return id.equals(other.id);
    }

    if (!super.equals(other)) {
      return false;
    }

    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    // if item is persisted use only the id for calculating hash code
    if (id != null && id != 0L) {
      return id.hashCode();
    }

    final int prime = 31;
    int result = 1;
    result = prime * result + (super.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public String toString() {

    return "CATEGORY [ID]: " + id + "; [Name]: " + name + "; [Subjects]: " + subjects;
  }

  /**
   * Getter for the identifier.
   *
   * @return identifier.
   */
  public Long getId() {

    return id;
  }

  /**
   * This method adds the given {@link Subject} to the {@link Category}. It initializes the bidirectional connection between
   * {@link Category} and {@link Subject} too.
   *
   * @param subject the {@link Subject} to add to this {@link Category}.
   */
  public void addSubject(Subject subject) {

    subject.setCategory(this);
    subjects.add(subject);
  }

  /**
   * Getter for the name.
   *
   * @return name as String.
   */
  public String getName() {

    return name;
  }

  /**
   * Setter for the name. Must be unique!
   *
   * @param name new name.
   */
  public void setName(String name) {

    this.name = name;
  }

  /**
   * Getter for all subjects in the category.
   *
   * @return subjects contained by the category.
   */
  public Set<Subject> getSubjects() {

    return subjects;
  }

  /**
   * Sets a new subject collection.
   *
   * @param subjects Collection of subjects to set.
   */
  void setSubjects(Set<Subject> subjects) {

    this.subjects = subjects;
  }

  /**
   * Removes the given {@link Subject} from this {@link Category}. This operation destroys the association between the given
   * Subject and this Category. You should merge them after this method.
   *
   * @param subject the {@link Subject} to be removed.
   * @return <code>true</code> if the {@link Subject} was removed successfully.
   * @throws ProgrammingErrorException if the subject can not be removed.
   */
  public boolean removeSubject(Subject subject) {
    // if there is already a category associated we have to remove this connection.
    if (!subjects.remove(subject) && subject.getCategory() != null) {
      throw new ProgrammingErrorException("hashcode method of Subject is incorrect!");
    } else {
      subject.setCategory(null);
      return true;
    }
  }

  /**
   * Sets the identifier. Be careful with that one!
   *
   * @param id new identifier.
   */
  void setId(Long id) {

    this.id = id;
  }

}
