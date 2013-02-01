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
package org.jajuk.ui.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import org.jajuk.base.File;
import org.jajuk.base.FileManager;
import org.jajuk.services.dj.Ambience;
import org.jajuk.services.dj.AmbienceManager;
import org.jajuk.services.players.QueueModel;
import org.jajuk.util.Conf;
import org.jajuk.util.Const;
import org.jajuk.util.IconLoader;
import org.jajuk.util.JajukIcons;
import org.jajuk.util.Messages;
import org.jajuk.util.UtilFeatures;
import org.jajuk.util.error.JajukException;
import org.jajuk.util.log.Log;

/**
 * .
 */
public class BestOfAction extends JajukAction {
  /** Generated serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new best of action.
   */
  BestOfAction() {
    super(Messages.getString("JajukWindow.7"), IconLoader.getIcon(JajukIcons.BESTOF), true);
    setShortDescription(Messages.getString("JajukWindow.24"));
  }

  /* (non-Javadoc)
   * @see org.jajuk.ui.actions.JajukAction#perform(java.awt.event.ActionEvent)
   */
  @Override
  public void perform(ActionEvent evt) throws JajukException {
    new Thread("BestOfAction") {
      @Override
      public void run() {
        try {
          Ambience ambience = AmbienceManager.getInstance().getSelectedAmbience();
          List<File> alToPlay = UtilFeatures.filterByAmbience(FileManager.getInstance()
              .getGlobalBestofPlaylist(), ambience);
          // For perfs (mainly playlist editor view refresh), we set a ceil for
          // tracks
          // number
          if (alToPlay.size() > 0 && alToPlay.size() > Const.NB_TRACKS_ON_ACTION) {
            alToPlay = alToPlay.subList(0, Const.NB_TRACKS_ON_ACTION);
          }
          QueueModel.push(UtilFeatures.createStackItems(alToPlay,
              Conf.getBoolean(Const.CONF_STATE_REPEAT), false), false);
        } catch (Exception e) {
          Log.error(e);
        }
      }
    }.start();
  }
}
