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

package hsa.awp.common.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Session;
import javax.net.SocketFactory;
import java.util.Properties;

/**
 * This class holds the configuration for {@link Mail} objects.
 *
 * @author johannes
 */
public class MailSource {
  /**
   * {@link Logger} for this class.
   */
  private Logger log = LoggerFactory.getLogger(getClass());

  /**
   * Host which is used for sending a {@link IMail}.
   */
  private String host;

  /**
   * Protocol used for transport.
   */
  private String protocol;

  /**
   * Port for connection with host.
   */
  private int port;

  /**
   * {@link SocketFactory} for connection with host.
   */
  private String socketFactory;

  /**
   * User name for connection authentication at host.
   */
  private String user;

  /**
   * Password for the specified user.
   */
  private String password;

  /**
   * Specifies if the host needs an authentication for establishing connections.
   */
  private boolean auth;

  /**
   * Returns host.
   *
   * @return the host
   */
  public String getHost() {

    return host;
  }

  /**
   * Setter for host.
   *
   * @param host the host to set
   */
  public void setHost(String host) {

    this.host = host;
  }

  /**
   * Returns password.
   *
   * @return the password
   */
  public String getPassword() {

    return password;
  }

  /**
   * Setter for password.
   *
   * @param password the password to set
   */
  public void setPassword(String password) {

    this.password = password;
  }

  /**
   * Returns port.
   *
   * @return the port
   */
  public int getPort() {

    return port;
  }

  /**
   * Setter for port.
   *
   * @param port the port to set
   */
  public void setPort(int port) {

    this.port = port;
  }

  /**
   * Returns protocol.
   *
   * @return the protocol
   */
  public String getProtocol() {

    return protocol;
  }

  /**
   * Setter for protocol.
   *
   * @param protocol the protocol to set
   */
  public void setProtocol(String protocol) {

    this.protocol = protocol;
  }

  /**
   * Returns a {@link Session} which can be used for sending e-mails.
   *
   * @return a fully configured {@link Session}
   */
  public Session getSession() {

    Properties props = getSessionProperties();
    SMTPAuthentication authentication = new SMTPAuthentication(user, password);

    Session session = Session.getDefaultInstance(props, authentication);

    if (log.isDebugEnabled()) {
      log.debug("Created new mail session [" + session + "]");
    }

    return session;
  }

  /**
   * Returns the configuration for the Java Mail API. The configuration contains the parameters as listed below. If a
   * socketFactory is specified this parameter will be added too.
   * <p/>
   * <pre>
   * mail.transport.protocol
   * mail.<i>protocol</i>.host
   * mail.<i>protocol</i>.port
   * mail.<i>protocol</i>.user
   * mail.<i>protocol</i>.socketFactory.class
   * </pre>
   *
   * @return a {@link Properties} object which contains the configuration parameters.
   */
  private Properties getSessionProperties() {

    Properties props = new Properties();

    props.put("mail.transport.protocol", protocol);
    props.put("mail." + protocol + ".host", host);
    props.put("mail." + protocol + ".port", Integer.toString(port));
    props.put("mail." + protocol + ".user", user);

    if (!socketFactory.isEmpty()) {
      props.put("mail." + protocol + ".socketFactory.class", socketFactory);
    }

    return props;
  }

  /**
   * Returns socketFactory.
   *
   * @return the socketFactory
   */
  public String getSocketFactory() {

    return socketFactory;
  }

  /**
   * Setter for socketFactory.
   *
   * @param socketFactory the socketFactory to set
   */
  public void setSocketFactory(String socketFactory) {

    this.socketFactory = socketFactory;
  }

  /**
   * Returns user.
   *
   * @return the user
   */
  public String getUser() {

    return user;
  }

  /**
   * Setter for user.
   *
   * @param user the user to set
   */
  public void setUser(String user) {

    this.user = user;
  }

  /**
   * Returns auth.
   *
   * @return the auth
   */
  public boolean isAuth() {

    return auth;
  }

  /**
   * Setter for auth.
   *
   * @param auth the auth to set
   */
  public void setAuth(boolean auth) {

    this.auth = auth;
  }
}
