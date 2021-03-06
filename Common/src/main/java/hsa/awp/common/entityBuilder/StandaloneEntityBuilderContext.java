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

import hsa.awp.common.exception.ProgrammingErrorException;

import java.lang.reflect.Constructor;

public class StandaloneEntityBuilderContext implements EntityBuilderContext {
  public static final StandaloneEntityBuilderContext INSTANCE = new StandaloneEntityBuilderContext();

  @Override
  public <T extends EntityBuilder> T get(Class<T> builderClass) {

    try {
      Constructor<T> constructor = builderClass.getConstructor();
      constructor.setAccessible(true);

      return constructor.newInstance();
    } catch (Exception e) {
      throw new ProgrammingErrorException(e);
    }
  }
}
