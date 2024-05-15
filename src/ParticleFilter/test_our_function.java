package ParticleFilter;

import Algorithm.LosAlgorithm;
import GNSS.Sat;
import Geometry.Building;
import Geometry.BuildingsFactory;
import Geometry.Point3D;
import Geometry.Wall;

import java.util.ArrayList;
import java.util.List;

public class test_our_function {
    public static void main(String[] args) {
        manual1();

    }
    public  static void manual1() {


        Point3D p1 = new Point3D(-122.083,37.422,100);
        Point3D p2 = new Point3D(-122.083,37.423,100);
        Point3D p3 = new Point3D(-122.082,37.423,100);
        Point3D p4 = new Point3D(-122.082,37.422,100);
        Point3D p5 = new Point3D(-122.083,37.422,100);

        List<Point3D> points  = new ArrayList<Point3D> ();;
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        points.add(p5);

        Building wall = new Building(points);
        //System.out.println("bs: "+wall.getBuildindVertices());
        Point3D p = new Point3D(-122.084,37.421,0);

        // Coordinates of the points
        double lon1 = -122.084;
        double lat1 = 37.421;
        double lon2 = -122.0825;
        double lat2 = 37.4225;

// Calculate differences in longitude and latitude
        double dLon = Math.toRadians(lon2 - lon1);
        double dLat = Math.toRadians(lat2 - lat1);

// Convert latitude to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

// Calculate azimuth (horizontal angle)
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
        double azimuth = Math.toDegrees(Math.atan2(y, x));

// Calculate elevation (vertical angle)
        double z = Math.sqrt(Math.pow(lon2 - lon1, 2) + Math.pow(lat2 - lat1, 2));
        double elevation = Math.toDegrees(Math.atan2(z, Math.abs(lat2 - lat1)));

// Output the calculated values
        System.out.println("Azimuth: " + azimuth + " degrees");
        System.out.println("Elevation: " + elevation + " degrees");
        Sat s = new Sat(azimuth,elevation,0);
        double isLOS = LosAlgorithm.ourComputeLos(p, wall, s);
        System.out.println("Point: " + p + ", Satellite: " + s.getSatID() + ", LOS: " + isLOS);

    }

    public  static void manual2() {


    }
}
