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
 * The UTM (Universal Transverse Mercator) projective system in
 * the GRS84 frame ().
 */
public class UTM extends Coordinates {
  /**
   * Mercator zone (from 1 to 60). France is covered by zones 30 to 32.
   */
  private int zone;
  /**
   * east coordinate in UTM frame (in meters)
   */
  private double x;
  /**
   * north coordinate in UTM frame (in meters)
   */
  private double y;
  /**
   * altitude (in meters)
   */
  private double z;
  /**
   * whether north or south hemisphere
   */
  private boolean north;

  /**
   * graphic text field to receive zone information

  private boolean edited = false;

  /**
   * reference ellipsoid
   */
  protected Ellipsoid ellipsoid = null; 

  /**
   * WGS84 => reference ellipsoid translation
   */
  protected double translation[] = { 0.0, 0.0, 0.0 };

  /**
   * initializes a default Lambert UTM coordinate: (0, 0, 0) zone 30, north
   */
  public UTM() {
    north = true;
    zone = 30;
    x = y = z = 0;
    ellipsoid = Ellipsoid.GRS80;
  }

  /**
   * initializes a UTM coordinate
   *
   * @param zone UTM zone (from 1 to 60)
   * @param x UTM east coordinate in meters
   * @param y UTM north coordinate in meters
   * @param x UTM altitude in meters
   * @param north true if in north hemisphere, false in south hemisphere
   */
  public UTM(int zone, double x, double y, double z, boolean north) {
    this.zone = zone;
    this.x = x;
    this.y = y;
    this.z = z;
    this.north = north;
    ellipsoid = Ellipsoid.GRS80;
  }
  
  public void translate(double xx, double yy, double zz){
	  x += xx;
	  y += yy;
	  z += zz;
  }


  /**
   * read data from graphic widget if needed
   */


  /**
   * returns this coordinate as a string
   * 
   * @return string formated as "UTM <1-60> <N|S> east north altitude"
   */
  public String toString() {
    String res = getName() + " " + String.valueOf(zone) + " ";
    if(north)
      res += "N ";
    else
      res += "S ";
    return res + Coordinates.lengthToString(x) + " " +
      Coordinates.lengthToString(y) + " " + Coordinates.altitudeToString(z);
  }


  /**
   * creates a new UTM coordinate object initialized at the same location
   * than the input WGS84 coordinate
   *
   * @param from WGS84 coordinate to translate
   * @return UTM coordinate object
   */
  public Coordinates create(WGS84 from) {
    // WGS84 geographic => WGS84 cartesian
    Cartesian wgs = new Cartesian(from.longitude(), from.latitude(), from.h(),
				  Ellipsoid.GRS80);
    // WGS84 => reference ellipsoid similarity
    wgs.translate(translation[0], translation[1], translation[2]);

    // reference ellipsoid cartesian => reference ellipsoid geographic
    Geographic ref = new Geographic(wgs, ellipsoid);

    // reference ellipsoid geographic => UTM projection 
    MTProjection proj = new MTProjection(ref, ellipsoid.a, ellipsoid.e);

    return new UTM(proj.zone(), proj.east(), proj.north(), /*ref.h()*/from.h(),
		   proj.isNorth());
  }



  /**
   * creates a new WGS84 coordinates object initialized at the same location
   * than this UTM coordinate.
   *
   * @return WGS84 coordinates object
   */
  public WGS84 toWGS84() {

    /*
     * UTM projection => reference ellipsoid geographic
     */
    MTProjection proj = new MTProjection(x, y, zone, north);
    Geographic geo = new Geographic(proj, ellipsoid.a, ellipsoid.e, z);

    // reference ellipsoid geographic => reference ellipsoid cartesian
    Cartesian utm = new Cartesian(geo, ellipsoid);

    // reference ellipsoid => WGS84 ellipsoide similarity
    utm.translate(-translation[0], -translation[1], -translation[2]);

    // WGS84 cartesian => WGS84 geographic
    Geographic wgs = new Geographic(utm, Ellipsoid.GRS80);

    return new WGS84(wgs.lon(), wgs.lat(), /*wgs.h()*/z);
  }


  /**
   * Parse input string and creates a new UTM Coordinate.
   *
   * @param strings array of 6 strings which are the "UTM" constant, zone
   * number (from 1 to 60), N or S letter, east coordinate, north coordinate,
   * and altitude.
   * @return new UTM coordinate
   */
  public Coordinates create(String strings[]) throws InvalidCoordinate {
    if(strings.length == 1)
      return new UTM();
    if(strings.length != 6)
      throw new InvalidCoordinate();
    int zone;
    boolean north;
    double x, y, z;
    try {
      zone = Integer.parseInt(strings[1].trim());
      north = strings[2].trim().toUpperCase().equals("N");
      x = Coordinates.parseLength(strings[3]);
      y = Coordinates.parseLength(strings[4]);
      z = Coordinates.parseAltitude(strings[5]);
    }
    catch(NumberFormatException e) {
      throw new InvalidCoordinate();
    }
    return new UTM(zone, x, y, z, north);
  }


  /**
   * Creates UTM coordinate graphic representation
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
  public double getEast() {return this.x;}
  public double getNorth() {return this.y;}
  public double getZ() {return this.z;}
  public int getZone() {return this.zone;}
}


