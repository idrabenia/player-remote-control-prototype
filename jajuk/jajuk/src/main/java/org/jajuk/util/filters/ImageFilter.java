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
package org.jajuk.util.filters;

import org.jajuk.util.JajukFileFilter;

/**
 * Image filter.
 */
public final class ImageFilter extends JajukFileFilter {
  /** Self instance. */
  private static ImageFilter self = new ImageFilter();

  /**
   * Gets the instance.
   * 
   * @return singleton
   */
  public static ImageFilter getInstance() {
    return ImageFilter.self;
  }

  /**
   * Singleton constructor (protected for testing purposes).
   */
  private ImageFilter() {
    super(new String[] { "jpg", "jpeg", "gif", "png" });
  }
}
