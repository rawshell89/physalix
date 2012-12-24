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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility class for Java Generic operations.
 *
 * @author johannes
 */
public final class GenericUtil {
  /**
   * Searches in the class mark-up of the given item if the given type is a type parameter of a super class of an implemented
   * interface. Example given:<br>
   * <br>
   * You are searching for the parameter Foo in class Item. The parameters would be:<br>
   * item = Item.class; type = Foo.class;<br>
   * <i>Note that T can be Object too</i>
   * <p/>
   * <pre>
   * class Item extends/implements Bar&lt;Foo&gt;
   *
   * class Foo extends/implements T
   * </pre>
   *
   * @param <T>  the super class or interface of the type parameter which is searched for
   * @param item the class mark-up to search in
   * @param type the class mark-up to search for
   * @return the found type or <code>null</code> if nothing is found
   */
  @SuppressWarnings("unchecked")
  public static <T> Class<? extends T> searchForTypeArgument(Class<?> item, Class<? extends T> type) {
//        Type[] temp = item.getGenericInterfaces();
//        List<Type> typesOld = Arrays.asList(temp);
//        typesOld.add(item.getGenericSuperclass());

    List<Type> types = new LinkedList<Type>();
    types.addAll(Arrays.asList(item.getGenericInterfaces()));
    types.add(item.getGenericSuperclass());

    for (Type t : types) {
      if (t instanceof ParameterizedType) {
        ParameterizedType p = (ParameterizedType) t;

        for (Type t2 : p.getActualTypeArguments()) {
          try {
            Class<? extends T> clazz = (Class<? extends T>) t2;
            if (clazz.equals(type)) {
              return clazz;
            }
          } catch (ClassCastException e) {
            continue;
          }
        }
      }
    }

    return null;
  }

  /**
   * Hide constructor.
   */
  private GenericUtil() {

  }
}
