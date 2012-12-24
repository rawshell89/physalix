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

package hsa.awp.common.logging;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

import java.util.Calendar;

/**
 * Logging interceptor for logging a method call with all passed parameters. If an Exception is thrown this Exception will be logged
 * too. After completing the invocation a message will be logged.
 *
 * @author johannes
 */
public class DebugInterceptor implements MethodInterceptor {
  /**
   * Switch for in target context logging.
   */
  private boolean logInTargetContext = true;

  /**
   * Switch for parameter logging.
   */
  private boolean logParamters = true;

  /**
   * Switch for return value logging.
   */
  private boolean logReturnValue = true;

  /**
   * Switch for exception logging.
   */
  private boolean logException = true;

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {

    Object target = invocation.getThis();
    Logger log = getLogger(target);

    // Log method entering with all parameters
    if (log.isTraceEnabled()) {
      logBefore(log, target.getClass().getSimpleName(), invocation.getMethod().getName(), invocation.getArguments());
    }

    try {
      // proceed invocation
      Object ret = invocation.proceed();

      // log return value of invocation if activated
      if (log.isTraceEnabled() && logReturnValue) {
        logReturnValue(log, ret);
      }

      return ret;
    } catch (Throwable t) {
      // log exception if activated
      if (log.isTraceEnabled() && logException) {
        logException(log, invocation.getMethod().getName(), t);
      }
      throw t;
    } finally {
      // leaving method
      if (log.isTraceEnabled()) {
        log.trace("Leaving method '" + target.getClass().getSimpleName() + "." + invocation.getMethod().getName() + "'");
      }
    }
  }

  /**
   * Returns the {@link Logger} to be used for logging.
   *
   * @param target the target object of this method call.
   * @return the {@link Logger} to be used.
   * @see #setLogInTargetContext(boolean)
   */
  private Logger getLogger(Object target) {

    if (logInTargetContext) {
      return LoggerFactory.getLogger(AopUtils.getTargetClass(target));
    } else {
      return LoggerFactory.getLogger(getClass());
    }
  }

  /**
   * Logs the beginning of a method to the given {@link Logger}. If {@link #logParamters} is true the parameters will be logged
   * too.
   *
   * @param log    the {@link Logger} instance to be used for logging.
   * @param clazz  the name of the target class
   * @param method the name of the method.
   * @param params array of parameters passed to the target method.
   */
  private void logBefore(Logger log, String clazz, String method, Object[] params) {

    StringBuilder sb = new StringBuilder();

    sb.append("Entering '" + clazz + "." + method + "'");

    // only log parameters if activated
    if (logParamters) {
      sb.append(" with " + params.length + " parameters:");
      for (int i = 0; i < params.length; i++) {
        Object o = params[i];

        String type;
        if (o != null) {
          type = o.getClass().getName();
        } else {
          type = "null";
        }

        if (o instanceof Calendar) {
          o = ((Calendar) o).getTime();
        }

        sb.append("\n   [" + i + "](" + type + ") = " + o);
      }
    }

    log.trace(sb.toString());
  }

  /**
   * Logs a thrown exception to the given {@link Logger}.
   *
   * @param log    the {@link Logger} instance to be used for logging.
   * @param method the name of the method.
   * @param t      the thrown Exception
   */
  private void logException(Logger log, String method, Throwable t) {

    log.trace("'" + t.getClass().getSimpleName() + "' was thrown in '" + method + "'", t);
  }

  /**
   * Logs the return value to the given {@link Logger}. If the given return value is null, the value will be logged too.
   *
   * @param log the {@link Logger} instance to be used for logging.
   * @param ret the return value of the invoked method.
   */
  private void logReturnValue(Logger log, Object ret) {

    if (ret != null) {
      if (ret instanceof Calendar) {
        ret = ((Calendar) ret).getTime();
      }
      log.trace("Return value : (" + ret.getClass().getName() + ") = " + ret);
    } else {
      log.trace("Return value : null");
    }
  }

  /**
   * Specifies if exceptions should be logged.
   *
   * @param logException <code>true</code> if exceptions should be logged.
   */
  public void setLogException(boolean logException) {

    this.logException = logException;
  }

  /**
   * Specifies if the logging should be done in target context. The {@link Logger} will be initialized with the
   *
   * @param logInTargetContext <code>true</code> if logging should be done in target context.
   */
  public void setLogInTargetContext(boolean logInTargetContext) {

    this.logInTargetContext = logInTargetContext;
  }

  /**
   * Specifies if parameters of method calls should be logged.
   *
   * @param logParamters <code>true</code> if the parameters should be logged.
   */
  public void setLogParamters(boolean logParamters) {

    this.logParamters = logParamters;
  }

  /**
   * Specifies if the return value should be logged.
   *
   * @param logReturnValue <code>true</code> if the return value should be logged.
   */
  public void setLogReturnValue(boolean logReturnValue) {

    this.logReturnValue = logReturnValue;
  }
}
