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

package hsa.awp.common.entityBuilder;

import hsa.awp.common.IGenericDomainModel;
import hsa.awp.common.exception.ProgrammingErrorException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class EntityBuilder<ENTITY extends IGenericDomainModel<Long>> {
  private Class<ENTITY> entityClass;

  private BuildListener[] listeners = new BuildListener[0];

  public EntityBuilder(Class<ENTITY> entityClass) {

    this.entityClass = entityClass;

    initializeRecursiveBuilders();
  }

  @SuppressWarnings("unchecked")
  private void initializeRecursiveBuilders() {

    Class<? extends EntityBuilder> builderClass = this.getClass();

    try {
      for (Field field : builderClass.getDeclaredFields()) {
        field.setAccessible(true);

        if (isBuilderField(field)) {
          Class<? extends EntityBuilder> fieldClass = (Class<? extends EntityBuilder>) field.getType();

          Constructor<? extends EntityBuilder> constructor = fieldClass.getConstructor();
          constructor.setAccessible(true);

          EntityBuilder entityBuilder = constructor.newInstance();
          entityBuilder.setListeners(listeners);

          field.set(this, entityBuilder);
        }
      }
    } catch (Exception e) {
      throw new ProgrammingErrorException(e);
    }
  }

  private boolean isBuilderField(Field field) {

    return field.getName().endsWith("Builder") && EntityBuilder.class.isAssignableFrom(field.getType());
  }

  public void setListeners(BuildListener... listeners) {

    this.listeners = listeners;
  }

  public ENTITY build() {

    try {
      Constructor<ENTITY> constructor = entityClass.getDeclaredConstructor();
      constructor.setAccessible(true);

      ENTITY entity = constructor.newInstance();

      for (Field field : entityClass.getDeclaredFields()) {
        if (field.getName().equals("id")) {
          continue;
        }

        if (Modifier.isStatic(field.getModifiers())) {
          continue;
        }
        field.setAccessible(true);
        field.set(entity, getValue(field.getName(), this.getClass()));
      }

      notifyListeners(entity);
      return entity;
    } catch (Exception e) {
      throw new ProgrammingErrorException(e);
    }
  }

  private Object getValue(String fieldName, Class<?> cls) throws IllegalAccessException {

    Field valField = null;
    Field builderField = null;

    for (Field field : cls.getDeclaredFields()) {
      field.setAccessible(true);

      if (field.getName().equals(fieldName)) {
        valField = field;
      } else if ((fieldName + "Builder").equals(field.getName())) {
        builderField = field;
      }

      if (valField != null && builderField != null) {
        break;
      }
    }

    if (valField != null && (valField.get(this) == null && builderField == null || valField.get(this) != null)) {
      return valField.get(this);
    } else if (builderField != null) {
      EntityBuilder<?> builder = (EntityBuilder<?>) builderField.get(this);
      return builder.build();
    }

    Class<?> superClass = cls.getSuperclass();
    if (superClass != null) {
      return getValue(fieldName, superClass);
    }

    throw new ProgrammingErrorException("no field for " + fieldName + " found");
  }

  private void notifyListeners(ENTITY entity) {

    for (BuildListener listener : listeners) {
      listener.builtObject(entity);
    }
  }
}
