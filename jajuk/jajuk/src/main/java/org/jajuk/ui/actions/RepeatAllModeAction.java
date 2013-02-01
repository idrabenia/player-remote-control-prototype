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

import org.jajuk.events.JajukEvent;
import org.jajuk.events.JajukEvents;
import org.jajuk.events.ObservationManager;
import org.jajuk.services.players.QueueModel;
import org.jajuk.util.Conf;
import org.jajuk.util.Const;
import org.jajuk.util.IconLoader;
import org.jajuk.util.JajukIcons;
import org.jajuk.util.Messages;
import org.jajuk.util.UtilGUI;

/**
 * .
 */
public class RepeatAllModeAction extends JajukAction {
  /** Generated serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new repeat all mode action.
   */
  RepeatAllModeAction() {
    super(Messages.getString("JajukJMenuBar.30"), IconLoader.getIcon(JajukIcons.REPEAT_ALL), true);
    setShortDescription(Messages.getString("CommandJPanel.26"));
  }

  /**
   * Invoked when an action occurs.
   * 
   * @param evt 
   */
  @Override
  public void perform(ActionEvent evt) {
    boolean b = Conf.getBoolean(Const.CONF_STATE_REPEAT_ALL);
    UtilGUI.setRepeatAllGui(!b);
    QueueModel.setRepeatModeToAll(!b);
    // computes planned tracks
    QueueModel.computesPlanned(false);
    // Refresh Queue View
    ObservationManager.notify(new JajukEvent(JajukEvents.QUEUE_NEED_REFRESH));
  }
}
