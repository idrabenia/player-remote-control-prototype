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
package ext.services.lastfm;

import java.awt.Image;

import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.jajuk.util.Messages;

/**
 * The Class LastFmSimilarArtistsRunnable.
 */
public class LastFmSimilarArtistsRunnable implements Runnable {
  /** The listener. */
  ContextListener listener;
  /** The service. */
  private LastFmService service;
  /** The artist. */
  private String artist;
  /** The interrupted. */
  private volatile boolean interrupted;
  /** The id. */
  long id;

  /**
   * Instantiates a new audio scrobbler similar artists runnable.
   * 
   * @param listener the listener
   * @param service the service
   * @param artist the artist
   * @param id the id
   */
  public LastFmSimilarArtistsRunnable(ContextListener listener, LastFmService service,
      String artist, long id) {
    this.listener = listener;
    this.service = service;
    this.artist = artist;
    this.id = id;
  }

  /**
   * Interrupt.
   */
  public void interrupt() {
    interrupted = true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Runnable#run()
   */
  @Override
  public void run() {
    if (!interrupted && StringUtils.isNotBlank(artist)
        && !artist.equalsIgnoreCase(Messages.getString("unknown_artist"))) {
      SimilarArtistsInfo artists = service.getSimilarArtists(artist);
      if (!interrupted && artists != null) {
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            listener.notifyStartRetrievingArtistImages(id);
          }
        });
        final Image artistImage = service.getImage(artists);
        if (!interrupted && artistImage != null) {
          SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
              listener.notifyArtistImage(artistImage, id);
            }
          });
        }
        for (int i = 0; i < artists.getArtists().size(); i++) {
          final Image img;
          final ArtistInfo a = artists.getArtists().get(i);
          if (!interrupted) {
            img = service.getImage(a);
          } else {
            img = null;
          }
          if (!interrupted) {
            SwingUtilities.invokeLater(new Runnable() {
              @Override
              public void run() {
                listener.notifyFinishGetSimilarArtist(a, img, id);
              }
            });
          }
        }
      }
    }
  }
}
