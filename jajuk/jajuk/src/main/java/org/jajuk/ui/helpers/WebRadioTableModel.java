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
package org.jajuk.ui.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jajuk.base.Item;
import org.jajuk.services.webradio.WebRadio;
import org.jajuk.services.webradio.WebRadioManager;
import org.jajuk.services.webradio.WebRadioOrigin;
import org.jajuk.ui.widgets.IconLabel;
import org.jajuk.util.Conf;
import org.jajuk.util.Const;
import org.jajuk.util.Filter;
import org.jajuk.util.Messages;

/**
 * Table model used for web radios.
 */
public class WebRadioTableModel extends JajukTableModel {
  /** Generated serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
  * Model constructor.
  * 
  */
  public WebRadioTableModel() {
    super(9);
    setEditable(Conf.getBoolean(Const.CONF_WEBRADIO_TABLE_EDITION));
    // Columns names
    // First column is play icon, need to set a space character
    // for proper display in some look and feel
    vColNames.add(" ");
    idList.add(Const.XML_PLAY);
    vColNames.add(Messages.getHumanPropertyName(Const.XML_NAME));
    idList.add(Const.XML_NAME);
    vColNames.add(Messages.getHumanPropertyName(Const.XML_DESC));
    idList.add(Const.XML_DESC);
    vColNames.add(Messages.getHumanPropertyName(Const.XML_URL));
    idList.add(Const.XML_URL);
    vColNames.add(Messages.getHumanPropertyName(Const.XML_KEYWORDS));
    idList.add(Const.XML_KEYWORDS);
    vColNames.add(Messages.getHumanPropertyName(Const.XML_GENRE));
    idList.add(Const.XML_GENRE);
    vColNames.add(Messages.getString("WebRadioView.2"));
    idList.add(Const.XML_ORIGIN);
    vColNames.add(Messages.getHumanPropertyName(Const.XML_BITRATE));
    idList.add(Const.XML_BITRATE);
    vColNames.add(Messages.getHumanPropertyName(Const.XML_FREQUENCY));
    idList.add(Const.XML_FREQUENCY);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jajuk.ui.helpers.JajukTableModel#populateModel(java.lang.String, java.lang.String,
   * java.util.List)
   */
  @Override
  public void populateModel(String sPropertyName, String sPattern, List<String> columnsToShow) {
    // This should be monitor file manager to avoid NPE when changing items
    List<WebRadio> alToShow = new ArrayList<WebRadio>(WebRadioManager.getInstance().getWebRadios());
    oItems = new Item[iRowNum];
    // Filter radios if required
    Filter filter = new Filter(sPropertyName, sPattern, true, Conf.getBoolean(Const.CONF_REGEXP));
    alToShow = Filter.filterItems(alToShow, filter, WebRadio.class);
    Iterator<WebRadio> it = alToShow.iterator();
    int iColNum = iNumberStandardCols;
    iRowNum = alToShow.size();
    it = alToShow.iterator();
    oValues = new Object[iRowNum][iColNum];
    oItems = new Item[iRowNum];
    bCellEditable = new boolean[iRowNum][iColNum];
    for (int iRow = 0; it.hasNext(); iRow++) {
      WebRadio radio = it.next();
      setItemAt(iRow, radio);
      // Id
      oItems[iRow] = radio;
      // Play
      IconLabel il = getIcon(false);
      oValues[iRow][0] = il;
      bCellEditable[iRow][0] = false;
      // Radio name
      oValues[iRow][1] = radio.getName();
      bCellEditable[iRow][1] = true;
      // Radio description
      oValues[iRow][2] = radio.getDescription();
      bCellEditable[iRow][2] = true;
      // Url
      oValues[iRow][3] = radio.getUrl();
      bCellEditable[iRow][3] = true;
      // Keywords
      oValues[iRow][4] = radio.getKeywords();
      bCellEditable[iRow][4] = true;
      // Genre
      oValues[iRow][5] = radio.getGenre();
      // Genre can be edited only on custom radios
      bCellEditable[iRow][5] = (radio.getOrigin() == WebRadioOrigin.CUSTOM);
      // Origin
      oValues[iRow][6] = radio.getOrigin().name();
      bCellEditable[iRow][6] = false;
      // Bitrate
      oValues[iRow][7] = radio.getLongValue(Const.XML_BITRATE);
      bCellEditable[iRow][7] = true;
      // Frequency
      oValues[iRow][8] = radio.getLongValue(Const.XML_FREQUENCY);
      bCellEditable[iRow][8] = true;
    }
  }
}
