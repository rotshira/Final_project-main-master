/*
 * fr.geo.convert package
 * A geographic coordinates converter.
 * Copyright (C) 2002 Johan Montagnat
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
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * Johan Montagnat
 * johan@creatis.insa-lyon.fr
 */

package GeoConvertUtils;


/**
 * Base class for projected coordinates of a point:
 * (East, North) pair expressed in meters.
 */
public class Projection {
  /**
   * X coordinate (east from origin) in meters 
   */
  protected double east;

  /**
   * Y coordinate (north from origin) in meters 
   */
  protected double north;

  /**
   * Creates point (0, 0)
   */
  public Projection() {
    east = 0.0;
    north = 0.0;
  }

  /**
   * initializes new projection coordinates
   *
   * @param east east from origin in meters 
   * @param north north from origin in meters 
   */
  public Projection(double east, double north) {
    this.east = east;
    this.north = north;
  }

  /**
   * returns east coordinate (in meters)
   */
  public double east() {
    return east;
  }

  /**
   * returns north coordinate (in meters)
   */
  public double north() {
    return north;
  }
}
