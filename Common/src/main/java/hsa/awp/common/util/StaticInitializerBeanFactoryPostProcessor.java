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

package hsa.awp.common.util;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Map;

/**
 * This {@link BeanFactoryPostProcessor} can initialize static variables in Spring Beans.
 *
 * @author johannes
 */
public class StaticInitializerBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
  /**
   * The map of all classes containing static variables to initialize.
   */
  private Map<?, ?> classes;

  /**
   * The {@link BeanWrapperImpl} which is used to convert the parameters if necessary.
   */
  private BeanWrapperImpl bri;

  /**
   * Initializes a new {@link StaticInitializerBeanFactoryPostProcessor} and instantiates a new {@link BeanWrapperImpl}.
   */
  public StaticInitializerBeanFactoryPostProcessor() {

    bri = new BeanWrapperImpl();
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) {

    for (Iterator<?> classIterator = classes.keySet().iterator(); classIterator.hasNext(); ) {
      String className = (String) classIterator.next();
      // System.out.println("Class " + className + ":");
      Map<?, ?> vars = (Map<?, ?>) classes.get(className);
      Class<?> c = null;
      try {
        c = Class.forName(className);
      } catch (ClassNotFoundException e) {
        throw new StaticInitializerBeansException("Class not found for " + className, e);
      }
      Method[] methods = c.getMethods();
      for (Iterator<?> fieldIterator = vars.keySet().iterator(); fieldIterator.hasNext(); ) {
        String fieldName = (String) fieldIterator.next();
        Object value = vars.get(fieldName);
        Method method = findStaticSetter(methods, fieldName);
        if (method == null) {
          throw new StaticInitializerBeansException("No static setter method found for class " + className + ", field "
              + fieldName);
        }
        // System.out.println("\tFound method " + method.getName() + " for field " + fieldName + ", value " + value);
        Object newValue = bri.convertIfNecessary(value, getPropertyType(method));
        try {
          method.invoke(null, new Object[]{newValue});
        } catch (Exception e) {
          throw new StaticInitializerBeansException("Invocation of method " + method.getName() + " on class " + className
              + " with value " + value + " failed.", e);
        }
      }
    }
  }

  /**
   * Look for a static setter method for field named fieldName in Method[]. Return null if none found.
   *
   * @param methods   {@link Method} array where to look for the setter
   * @param fieldName the name of the field
   * @return the static setter {@link Method} object
   */
  private Method findStaticSetter(Method[] methods, String fieldName) {

    String methodName = setterName(fieldName);
    for (int i = 0; i < methods.length; i++) {
      if (methods[i].getName().equals(methodName) && Modifier.isStatic(methods[i].getModifiers())) {
        return methods[i];
      }
    }
    return null;
  }

  /**
   * Returns the standard setter name for the given field name.
   *
   * @param fieldName the name of the field
   * @return the name of the setter
   */
  private String setterName(String fieldName) {

    String nameToUse = null;
    if (fieldName.length() == 1) {
      if (Character.isLowerCase(fieldName.charAt(0))) {
        nameToUse = fieldName.toUpperCase();
      } else {
        nameToUse = fieldName;
      }
    } else {
      if (Character.isLowerCase(fieldName.charAt(0)) && Character.isLowerCase(fieldName.charAt(1))) {
        nameToUse = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
      } else {
        nameToUse = fieldName;
      }
    }
    return "set" + nameToUse;
  }

  /**
   * Finds the parameter types from the first argument of the given method.
   *
   * @param setter the method to find out the parameter types
   * @return the parameter type of the first argument
   */
  private Class<?> getPropertyType(Method setter) {

    Class<?>[] params = setter.getParameterTypes();
    if (params.length != 1) {
      throw new StaticInitializerBeansException("bad write method arg count: " + setter);
    }
    return params[0];
  }

  /**
   * The {@link Map} of all classes containing static variables to initialize. Every entry in this map represents one class. Every
   * class entry consists of a map. Here are the variable names mapped to the values.
   *
   * @param classes the map of the classes to initialize
   */
  public void setClasses(Map<?, ?> classes) {

    this.classes = classes;
  }
}

/**
 * This exception is thrown when an exception occurs in the {@link StaticInitializerBeanFactoryPostProcessor}.
 *
 * @author johannes
 */
class StaticInitializerBeansException extends BeansException {
  /**
   * serial version UID.
   */
  private static final long serialVersionUID = -6821119492403266859L;

  /**
   * Constructs a new {@link StaticInitializerBeansException} with the given message.
   *
   * @param message the message
   */
  StaticInitializerBeansException(String message) {

    super(message);
  }

  /**
   * Constructs a new {@link StaticInitializerBeansException} with the given message and cause.
   *
   * @param message the message
   * @param cause   the cause of this exception
   */
  StaticInitializerBeansException(String message, Throwable cause) {

    super(message, cause);
  }
}
