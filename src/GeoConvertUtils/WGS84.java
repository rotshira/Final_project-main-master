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
 * The WGS84 international system is a geographic coordinate system
 * that is used by GPS and coverts the whole earth. It is used as
 * a central system for all coordinate conversions.
 */
public class WGS84 extends Coordinates {
  /**
   * longitude (in radian)
   */
  private double longitude;
  /**
   * latitude (in radian)
   */
  private double latitude;
  /**
   * ellipsoidal elevation (in meters)
   */
  private double h;

  /**
   * longitude graphic widget
   */
  private JAngle tlg = null;
  /**
   * latitude graphic widget
   */
  private JAngle tlt = null;
  /**
   * ellipsoidal elevation graphic widget
   */

  /**
   * whether the graphic widget may have been edited
   */
  private boolean edited = false;

  /**
   * initializes a new WGS84 coordinate at (0, 0, 0)
   */
  public WGS84() {
    this.latitude = 0.0;
    this.longitude = 0.0;
    this.h = 0.0;
  }

  /**
   * initializes a new WGS84 coordinate
   *
   * @param longitude longitude in radian
   * @param latitude latitude in radian
   * @param h ellipsoidal elevation in meters
   */
  public WGS84(double longitude, double latitude, double h) {
    this.longitude = longitude;
    this.latitude = latitude;
    this.h = h;
  }

  /**
   * returns longitude angle in radian
   */
  public double longitude() {
    return longitude;
  }

  /**
   * returns latitude angle in radian
   */
  public double latitude() {
    return latitude;
  }

  /**
   * returns ellipsoidal elevation in meters
   */
  public double h() {
    return h;
  }

  /**
   * return the new WGS84 coordinate object given as a parameter
   *
   * @param from WGS84 coordinate to translate
   * @return a copy of <code>from</code> parameter
   */
  public Coordinates create(WGS84 from) {
    return new WGS84(from.longitude(), from.latitude(), from.h());
  }


  /**
   * read data from graphic widget if needed
   */



  /**
   * returns this coordinate as a string
   * 
   * @return string formated as "WGS84 longitude latitude elevation"
   */
  public String toString() {

    return getName() + " " + Math.toDegrees(Double.parseDouble(Coordinates.angleToString(longitude))) + " " +
    Math.toDegrees(Double.parseDouble(Coordinates.angleToString(latitude))) + " " +
      Coordinates.altitudeToString(h);
  }


  /**
   * returns a copy of this WGS84 coordinate
   *
   * @return new WGS84 coordinate identical to <code>this</code>
   */
  public WGS84 toWGS84() {

    return new WGS84(longitude, latitude, h);
  }



  /**
   * Parse input string and creates a new WGS84 Coordinate.
   *
   * @param strings array of 4 strings which are the "WGS84" constant,
   * longitude, latitude, and elevation.
   * @return new WGS84 coordinate
   */
  public Coordinates create(String strings[]) throws InvalidCoordinate {
    if(strings.length == 1)
      return new WGS84();
    if(strings.length != 4)
      throw new InvalidCoordinate();
    double lt, lg, h;
    try {
      lg = Coordinates.parseAngle(strings[1]);
      lt = Coordinates.parseAngle(strings[2]);
      h = Coordinates.parseAltitude(strings[3]);
    }
    catch(NumberFormatException e) {
      throw new InvalidCoordinate();
    }
    return new WGS84(lg, lt, h);
  }


  /**
   * Creates WGS84 coordinate graphic representation
   * 
   * @param panel parent window to create wigets in
   */


  /**
   * toggle the editable property of the graphic widgets
   *
   * @param edit wheter to toggle on or off
   */
  public void setEditable(boolean edit) {
  }
}


