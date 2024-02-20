package Utils;

import Geometry.Line3D;
import Geometry.Point3D;
import Geometry.Wall;

public class GeomUtils {
    public GeomUtils() {
    }

    public static boolean intersectRayWithPlane(Line3D ray, Wall wall) {
        Point3D[] PointArray = wall.getPoint3dArray();
        Point3D dS21 = PointArray[1].sub2PointsReturnNewPoint(PointArray[0]);
        Point3D dS31 = PointArray[2].sub2PointsReturnNewPoint(PointArray[0]);
        Point3D n = dS21.cross(dS31);
        Point3D dR = ray.getP1().sub2PointsReturnNewPoint(ray.getP2());
        double ndotdR = n.dotProduct(dR);
        if (Math.abs(ndotdR) < 9.999999974752427E-7) {
            return false;
        } else {
            double t = -n.dotProduct(ray.getP1().sub2PointsReturnNewPoint(PointArray[0])) / ndotdR;
            Point3D M = ray.getP1().add2PointsReturnNewPoint(dR.scale(t));
            Point3D dMS1 = M.sub2PointsReturnNewPoint(PointArray[0]);
            double u = dMS1.dotProduct(dS21);
            double v = dMS1.dotProduct(dS31);
            return u >= 0.0 && u <= dS21.dotProduct(dS21) && v >= 0.0 && v <= dS31.dotProduct(dS31);
        }
    }

    public static double ConvertSatAzimutInDegreesTOfitUnitCircle(double azimuth) {
        if (azimuth < 0.0) {
            azimuth += 360.0;
        }

        double newAzimut = 450.0 - azimuth;
        if (newAzimut >= 360.0) {
            newAzimut -= 360.0;
        }

        return newAzimut;
    }
}