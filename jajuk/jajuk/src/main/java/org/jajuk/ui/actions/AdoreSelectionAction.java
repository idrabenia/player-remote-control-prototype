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

import org.jajuk.base.Track;
import org.jajuk.base.TrackManager;
import org.jajuk.events.JajukEvent;
import org.jajuk.events.JajukEvents;
import org.jajuk.events.ObservationManager;
import org.jajuk.util.IconLoader;
import org.jajuk.util.JajukIcons;
import org.jajuk.util.Messages;
import org.jajuk.util.log.Log;

/**
 * .
 */
public class AdoreSelectionAction extends SelectionAction {
  /** Generated serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Set adore preference to a selection
   * <p>
   * Selection action
   * </p>.
   */
  AdoreSelectionAction() {
    super(Messages.getString("Preference.6"), IconLoader.getIcon(JajukIcons.PREFERENCE_ADORE), true);
    setShortDescription(Messages.getString("Preference.6"));
  }

  /* (non-Javadoc)
   * @see org.jajuk.ui.actions.SelectionAction#perform(java.awt.event.ActionEvent)
   */
  @Override
  public void perform(final ActionEvent e) throws Exception {
    new Thread("AdoreSelectionAction") {
      @Override
      public void run() {
        try {
          AdoreSelectionAction.super.perform(e);
          // Check selection is not void
          if (selection.size() == 0) {
            return;
          }
          // Extract tracks of each item
          List<Track> tracks = TrackManager.getInstance().getAssociatedTracks(selection, false);
          // Set the preference
          for (Track track : tracks) {
            track.setPreference(3l);
          }
          // Request a GUI refresh
          ObservationManager.notify(new JajukEvent(JajukEvents.RATE_CHANGED));
        } catch (Exception e) {
          Log.error(e);
        }
      }
    }.start();
  }
}
