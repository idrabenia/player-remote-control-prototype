/*
 * aTunes 1.14.0 code adapted by Jajuk team
 * 
 * Original copyright notice bellow : 
 * 
 * Copyright (C) 2006-2009 Alex Aranda, Sylvain Gaudard, Thomas Beckers and contributors
 *
 * See http://www.atunes.org/wiki/index.php?title=Contributing for information about contributors
 *
 * http://www.atunes.org
 * http://sourceforge.net/projects/atunes
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package ext.services.network;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import org.apache.commons.codec.binary.Base64;

/**
 * .
 */
public class Proxy extends java.net.Proxy {
  /** Generated serialVersionUID. */
  private static final long serialVersionUID = 7495084217081194366L;
  private final String url;
  private final int port;
  private final String user;
  private final String password;

  /**
   * Instantiates a new proxy.
   * 
   * @param type 
   * @param url 
   * @param port 
   * @param user 
   * @param password 
   * 
   * @throws UnknownHostException the unknown host exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Proxy(Type type, String url, int port, String user, String password)
      throws UnknownHostException, IOException {
    super(type, new Socket(url, port).getRemoteSocketAddress());
    this.url = url;
    this.port = port;
    this.user = user;
    this.password = password;
  }

  /**
   * Gets the connection.
   * 
   * @param u 
   * 
   * @return the connection
   * 
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public URLConnection getConnection(URL u) throws IOException {
    URLConnection con = u.openConnection(this);
    String encodedUserPwd = new String(Base64.encodeBase64((user + ':' + password).getBytes()));
    con.setRequestProperty("Proxy-Authorization", "Basic " + encodedUserPwd);
    return con;
  }

  /**
   * Gets the password.
   * 
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Gets the url.
   * 
   * @return the url
   */
  public String getUrl() {
    return url;
  }

  /**
   * Gets the port.
   * 
   * @return the port
   */
  public int getPort() {
    return port;
  }

  /**
   * Gets the user.
   * 
   * @return the user
   */
  public String getUser() {
    return user;
  }
}
