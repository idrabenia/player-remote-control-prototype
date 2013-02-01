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

import java.awt.Component;
import java.util.List;

import org.jajuk.base.File;
import org.jajuk.base.Item;
import org.jajuk.base.Track;
import org.jajuk.services.players.QueueModel;
import org.jajuk.services.webradio.WebRadio;
import org.jajuk.ui.widgets.JajukTable;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

/**
 * Jajuk playing highlighter used in some tables to change the cell background
 * if the item at given row is playing.
 */
public class PlayHighlighterPredicate implements HighlightPredicate {
  private final JajukTableModel model;
  private final JajukTable jtable;

  /**
   * Instantiates a new play highlighter predicate.
   * 
   * @param model 
   */
  public PlayHighlighterPredicate(JajukTable jtable) {
    this.jtable = jtable;
    this.model = (JajukTableModel) jtable.getModel();
  }

  /* (non-Javadoc)
   * @see org.jdesktop.swingx.decorator.HighlightPredicate#isHighlighted(java.awt.Component, org.jdesktop.swingx.decorator.ComponentAdapter)
   */
  @Override
  public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
    if (QueueModel.isStopped()) {
      return false;
    }
    int convertedRow = jtable.convertRowIndexToModel(adapter.row);
    // For some reasons, we get OutOfBoundException here when the model is updated (note that 
    // however the model can only be accessed by the EDT), so we enforce a bound test :
    if (convertedRow >= model.getRowCount()) {
      return false;
    }
    if (model instanceof WebRadioTableModel) {
      WebRadio radio = (WebRadio) model.getItemAt(convertedRow);
      return QueueModel.isPlayingRadio() && QueueModel.getCurrentRadio().equals(radio);
    }
    Item item = model.getItemAt(convertedRow);
    if (item instanceof File && QueueModel.getPlayingFile() != null) {
      File file = (File) item;
      if (file.equals(QueueModel.getPlayingFile())) {
        return true;
      }
    } else if (item instanceof Track) {
      List<File> files = ((Track) item).getFiles();
      if (files.contains(QueueModel.getPlayingFile())) {
        return true;
      }
    }
    return false;
  }
}