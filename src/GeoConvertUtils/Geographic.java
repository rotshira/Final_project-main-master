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
 * Geographic coordinates of a point:
 * (longitude, latitude, ellipoidal elevation) triplet.
 * Angles are expressed in radians, elevation in meters.
 * The longitude is the angle between the Greenwich meridian and the plane
 * in which lies the point. The latitude is the angle between the equatorial
 * plane and the normal to the ellipsoid that goes through the point. The
 * ellipsoidal elevation is measure on the normal to the ellipsoid and differs
 * from the altitude up to several tens of meters.
 */
public class Geographic {
  /**
   * precision in iterative schema
   */
  public static final double epsilon = 1e-11;
  /**
   * longitude (in radian)
   */
  private double lg;
  /**
   * latitude (in radian)
   */
  private double lt;
  /**
   * ellipsoidal elevation (in meters)
   */
  private double h;

  /**
   * initializes from a coordinates triplet
   *
   * @param lg longitude in radian
   * @param lt latitude in radian
   * @param h ellipsoidal elevation in meters
   */
  public Geographic(double lt, double lg, double h) {
	  if (lt > Math.PI * 2 || lg > Math.PI * 2){
		  try {
			throw new Exception();
		} catch (Exception e) {
			System.err.println("enter coords in radians!");
			e.printStackTrace();
		}
	  }
    this.lg = lg;
    this.lt = lt;
    this.h = h;
  }
  
  public static Geographic createGeographic(double lt, double ln, double h){
	  return new Geographic(Math.toRadians(lt), Math.toRadians(ln), h);
  }


  /**
   * initalizes from cartesian coordinates
   *
   * @param X 1st coordinate in meters
   * @param Y 2nd coordinate in meters
   * @param Z 3rd coordinate in meters
   * @param ell reference ellipsoid
   */
  public Geographic(double X, double Y, double Z, Ellipsoid ell) {
    double norm = Math.sqrt(X*X + Y*Y);
    lg = 2.0 * Math.atan(Y / (X + norm));
    lt = Math.atan(Z / (norm * (1.0 - (ell.a*ell.e2 /
				       Math.sqrt(X*X+Y*Y+Z*Z)))));
    double delta = 1.0;
    while(delta > epsilon) {
      double s2 = Math.sin(lt);
      s2 *= s2;
      double l = Math.atan((Z / norm) /
			   (1.0 - (ell.a * ell.e2 * Math.cos(lt) /
				   (norm * Math.sqrt(1.0 - ell.e2 * s2))))
	         );
      delta = Math.abs(l - lt);
      lt = l;
    }
    double s2 = Math.sin(lt);
    s2 *= s2;
    h = norm / Math.cos(lt) - ell.a / Math.sqrt(1.0 - ell.e2 * s2);
  }


  /**
   * initalizes from cartesian coordinates
   *
   * @param coord cartesian coordinates triplet
   * @param ell reference ellipsoid
   */
  public Geographic(Cartesian coord, Ellipsoid ell) {
    this(coord.X(), coord.Y(), coord.Z(), ell);
  }


  /**
   * initalizes from projected coordinates (conic projection)
   *
   * @param coord projected coordinates pair
   * @param Xs false east (coordinate system origin) in meters
   * @param Ys false north (coordinate system origin) in meters
   * @param c projection constant
   * @param n projection exponent
   * @param lg0 longitude of origin wrt to the Greenwich meridian (in radian)
   * @param e reference ellipsoid excentricity
   * @param z altitude in meters 
   */
  public Geographic(ConicProjection coord, double Xs, double Ys,
		    double c, double n, double e, double lg0, double z) {
    double dx = coord.east() - Xs;
    double dy = Ys - coord.north();
    double R = Math.sqrt(dx*dx + dy*dy);
    double gamma = Math.atan(dx / dy);
    double l = -1.0/n * Math.log(Math.abs(R / c));
    l = Math.exp(l);
    lg = lg0 + gamma / n;
    lt = 2.0 * Math.atan(l) - Math.PI/2.0;
    double delta = 1.0;
    while(delta > epsilon) {
      double eslt = e * Math.sin(lt);
      double nlt = 2.0 * Math.atan(Math.pow((1.0 + eslt)/(1.0 - eslt), e/2.0)
				   * l) - Math.PI/2.0;
      delta = Math.abs(nlt - lt);
      lt = nlt;
    }
    h = z;
  }


  /**
   * initalizes from projected coordinates (Mercator transverse projection)
   *
   * @param coord projected coordinates pair
   * @param e reference ellipsoid excentricity
   * @param a reference ellipsoid long axis
   * @param z altitude in meters 
   */
  public Geographic(MTProjection coord, double a, double e, double z) {
    double n = 0.9996 * a;
    double e2 = e * e;
    double e4 = e2 * e2;
    double e6 = e4 * e2;
    double e8 = e4 * e4;
    double C[] = {
      1.0 - e2/4.0 - 3.0*e4/64.0 - 5.0*e6/256.0 - 175.0*e8/16384.0,
      e2/8.0 + e4/48.0 + 7.0*e6/2048.0 + e8/61440.0,
      e4/768.0 + 3.0*e6/1280.0 + 559.0*e8/368640.0,
      17.0*e6/30720.0 + 283.0*e8/430080.0,
      4397.0*e8/41287680.0
    };
    double l = (coord.north() - coord.Ys()) / (n * C[0]);
    double ls = (coord.east() - coord.Xs()) / (n * C[0]);
    double l0 = l;
    double ls0 = ls;
    for(int k = 1; k < 5; k++) {
      double r = 2.0 * k * l0;
      double m = 2.0 * k * ls0;
      double em = Math.exp(m);
      double en = Math.exp(-m);
      double sr = Math.sin(r)/2.0 * (em + en);
      double sm = Math.cos(r)/2.0 * (em - en);
      l -= C[k] * sr;
      ls -= C[k] * sm;
    }
    lg = coord.lg0() + Math.atan(((Math.exp(ls) - Math.exp(-ls)) / 2.0) /
				 Math.cos(l));
    double phi = Math.asin(Math.sin(l) /
			   ((Math.exp(ls) + Math.exp(-ls)) / 2.0));
    l = Math.log(Math.tan(Math.PI/4.0 + phi/2.0));
    lt = 2.0 * Math.atan(Math.exp(l)) - Math.PI / 2.0;
    double lt0;
    do {
      lt0 = lt;
      double s = e * Math.sin(lt);
      lt = 2.0 * Math.atan(Math.pow((1.0 + s) / (1.0 - s), e/2.0) *
			   Math.exp(l)) - Math.PI / 2.0;
    }
    while(Math.abs(lt-lt0) >= epsilon);
    h = z;
  }

  /**
   * returns longitude in radian
   */
  public double lon() {
    return lg;
  }

  /**
   * returns latitude in radian
   */
  public double lat() {
    return lt;
  }

  /**
   * returns ellipsoidal elevation in meters
   */
  public double el() {
    return h;
  }
  
	public double distVincenty(Geographic c1) {
		  double a = 6378137, b = 6356752.314245,  f = 1/298.257223563;  // WGS-84 ellipsoid params
		  double L = ((this.lon()-c1.lon()));
		  double U1 = Math.atan((1-f) * Math.tan((c1.lat())));
		  double U2 = Math.atan((1-f) * Math.tan((this.lat())));
		  double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
		  double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);
		  double sinSigma;
			double cos2SigmaM;
			  double sigma;
			double cosSigma;

			  double cosSqAlpha;

		  
		  double lambda = L, lambdaP, iterLimit = 100;
		  do {
		    double sinLambda = Math.sin(lambda), cosLambda = Math.cos(lambda);
		    sinSigma = Math.sqrt((cosU2*sinLambda) * (cosU2*sinLambda) + 
		      (cosU1*sinU2-sinU1*cosU2*cosLambda) * (cosU1*sinU2-sinU1*cosU2*cosLambda));
		    if (sinSigma==0) return 0;  // co-incident points
		    cosSigma = sinU1*sinU2 + cosU1*cosU2*cosLambda;
		    sigma = Math.atan2(sinSigma, cosSigma);
		    double sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
		    cosSqAlpha = 1 - sinAlpha*sinAlpha;
		    cos2SigmaM = cosSigma - 2*sinU1*sinU2/cosSqAlpha;
		    if (Double.isNaN(cos2SigmaM)) cos2SigmaM = 0;  // equatorial line: cosSqAlpha=0 (�6)
		    double C = f/16*cosSqAlpha*(4+f*(4-3*cosSqAlpha));
		    lambdaP = lambda;
		    lambda = L + (1-C) * f * sinAlpha *
		      (sigma + C*sinSigma*(cos2SigmaM+C*cosSigma*(-1+2*cos2SigmaM*cos2SigmaM)));
		  } while (Math.abs(lambda-lambdaP) > 1e-12 && --iterLimit>0);

		  if (iterLimit==0) return Double.NaN;  // formula failed to converge
		double uSq = cosSqAlpha * (a*a - b*b) / (b*b);
		  double A = 1 + uSq/16384*(4096+uSq*(-768+uSq*(320-175*uSq)));
		  double B = uSq/1024 * (256+uSq*(-128+uSq*(74-47*uSq)));
		double deltaSigma = B*sinSigma*(cos2SigmaM+B/4*(cosSigma*(-1+2*cos2SigmaM*cos2SigmaM)-
		    B/6*cos2SigmaM*(-3+4*sinSigma*sinSigma)*(-3+4*cos2SigmaM*cos2SigmaM)));
		double s = b*A*(sigma-deltaSigma);
		  return s;
	}
	
	  /**
	   *
	   * @return UTM coordinate object
	   */
	  public UTM toUTM() {
		  UTM u = new UTM();
	    // WGS84 geographic => WGS84 cartesian
	    Cartesian wgs = new Cartesian(this.lon(), this.lat(), this.el(),
					  Ellipsoid.GRS80);
	    // WGS84 => reference ellipsoid similarity
	    wgs.translate(u.translation[0], u.translation[1], u.translation[2]);

	    // reference ellipsoid cartesian => reference ellipsoid geographic
	    Geographic ref = new Geographic(wgs, u.ellipsoid);

	    // reference ellipsoid geographic => UTM projection 
	    MTProjection proj = new MTProjection(ref, u.ellipsoid.a, u.ellipsoid.e);

	    return new UTM(proj.zone(), proj.east(), proj.north(), /*ref.h()*/this.el(),
			   proj.isNorth());
	  }


  
  
}


