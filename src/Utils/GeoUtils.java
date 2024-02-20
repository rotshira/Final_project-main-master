package Utils;

import GeoConvertUtils.Coordinates;
import GeoConvertUtils.Geographic;
import GeoConvertUtils.UTM;
import Geometry.Point3D;

public class GeoUtils {
    public GeoUtils() {
    }

    public static Point3D convertECEFtoLATLON(Point3D p1) {
        double a = 6378137.0;
        double f = 0.0034;
        double b = 6356800.0;
        double e = Math.sqrt((Math.pow(a, 2.0) - Math.pow(b, 2.0)) / Math.pow(a, 2.0));
        double e2 = Math.sqrt((Math.pow(a, 2.0) - Math.pow(b, 2.0)) / Math.pow(b, 2.0));
        double x = p1.getX();
        double y = p1.getY();
        double z = p1.getZ();
        double[] lla = new double[]{0.0, 0.0, 0.0};
        double p = Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0));
        double theta = Math.atan(z * a / (p * b));
        double lon = Math.atan(y / x);
        double lat = Math.atan((z + Math.pow(e2, 2.0) * b * Math.pow(Math.sin(theta), 3.0)) / (p - Math.pow(e, 2.0) * a * Math.pow(Math.cos(theta), 3.0)));
        double N = a / Math.sqrt(1.0 - Math.pow(e, 2.0) * Math.pow(Math.sin(lat), 2.0));
        double m = p / Math.cos(lat);
        double height = m - N;
        lon = lon * 180.0 / Math.PI;
        lat = lat * 180.0 / Math.PI;
        lla[0] = lat;
        lla[1] = lon;
        lla[2] = height;
        return new Point3D(lat, lon, height);
    }

    public static Point3D convertLATLONtoECEF(Point3D p) {
        return null;
    }

    public static Point3D convertUTMtoLATLON(Point3D p, int zone) {
        Coordinates utm = new UTM(zone, p.getX(), p.getY(), p.getZ(), true);
        double x = utm.toWGS84().latitude() * 180.0 / Math.PI;
        double y = utm.toWGS84().longitude() * 180.0 / Math.PI;
        return new Point3D(x, y, p.getZ());
    }

    public static Point3D convertLATLONtoUTM(Point3D p) {
        Geographic g = Geographic.createGeographic(p.getX(), p.getY(), p.getZ());
        UTM utm = g.toUTM();
        double east = utm.getEast();
        double north = utm.getNorth();
        Point3D ans = new Point3D(east, north, p.getZ());
        return ans;
    }

    public double computeAzimuthECEF(Point3D pos, Point3D sat) {
        return 0.0;
    }

    public double computeElevetionECEF(Point3D pos, Point3D sat) {
        return 0.0;
    }

    public static Point3D convertAnyCordToLATLON(Point3D p) {
        return null;
    }
}