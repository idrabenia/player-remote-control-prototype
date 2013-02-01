/*
 *  Jajuk
 *  Copyright (C) The Jajuk Team
 *  http://jajuk.info
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *  
 */
package org.jajuk.services.webradio;

import javax.swing.ImageIcon;

import org.jajuk.base.PhysicalItem;
import org.jajuk.util.Const;
import org.jajuk.util.IconLoader;
import org.jajuk.util.JajukIcons;
import org.jajuk.util.Messages;

/**
 * A web Radio.
 */
public class WebRadio extends PhysicalItem implements Comparable<WebRadio> {
  /**
  * Instantiates a new web radio.
  * 
  * @param name 
  * @param url 
  */
  WebRadio(String sId, String sName) {
    super(sId, sName);
  }

  /**
   * Gets the url.
   * 
   * @return the url
   */
  public String getUrl() {
    return getStringValue(XML_URL);
  }

  /**
   * Gets the genre.
   * 
   * @return the url
   */
  public String getGenre() {
    return getStringValue(XML_GENRE);
  }

  /**
   * Gets the webradio description.
   * 
   * @return the descrption
   */
  public String getDescription() {
    return getStringValue(Const.XML_DESC);
  }

  /* (non-Javadoc)
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(WebRadio other) {
    // We must ensure that compareTo is consistent with equals because this kind of items are used in Tree Sets
    if (this.equals(other)) {
      return 0;
    }
    // make null url web radio to appear first (useful for the wizard)
    if (getUrl() == null && other.getUrl() != null) {
      return -1;
    }
    // ensure that even radios with the same name can't return a compareTo = 0 (only equal items can)
    StringBuilder sb = new StringBuilder(getName());
    sb.append(getID());
    StringBuilder sbOther = new StringBuilder(other.getName());
    sbOther.append(other.getID());
    return sb.toString().compareToIgnoreCase(sbOther.toString());
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return getProperties().toString();
  }

  /**
   * @return the origin
   */
  public WebRadioOrigin getOrigin() {
    return (WebRadioOrigin) getValue(XML_ORIGIN);
  }

  /**
   * @return the keywords
   */
  public String getKeywords() {
    return getStringValue(XML_KEYWORDS);
  }

  /* (non-Javadoc)
   * @see org.jajuk.base.Item#getDesc()
   */
  @Override
  public String getTitle() {
    return Messages.getString("WebRadioView.1");
  }

  /* (non-Javadoc)
   * @see org.jajuk.base.Item#getLabel()
   */
  @Override
  public String getXMLTag() {
    // Label not used as we don't persist webradio using collection code.   
    return null;
  }

  /* (non-Javadoc)
   * @see org.jajuk.base.Item#getIconRepresentation()
   */
  @Override
  public ImageIcon getIconRepresentation() {
    return IconLoader.getIcon(JajukIcons.WEBRADIO);
  }
}
