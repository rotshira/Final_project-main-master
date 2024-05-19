package ParticleFilter;

import Algorithm.LosAlgorithm;
import GNSS.Sat;
import Geometry.Building;
import Geometry.BuildingsFactory;
import Geometry.Point3D;
import Geometry.Wall;
import Utils.GeoUtils;
import Utils.KML_Generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class test_our_function {
    public static void main(String[] args) {
        //manual1();
        //manual2();
        test_make_kml_from_point_to_sat();

    }
    public  static void manual1() {
//        Just some point in the world and some wall in the world


        Point3D p1 = new Point3D(-122.083,37.422,0);
        Point3D p2 = new Point3D(-122.083,37.423,0);
        Point3D p3 = new Point3D(122.083,37.423,100);
        Point3D p4 = new Point3D(-122.083,37.422,100);

        List<Point3D> points  = new ArrayList<Point3D> ();;
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);

        Building wall = new Building(points);
        //System.out.println("bs: "+wall.getBuildindVertices());
        Point3D p = new Point3D(-122.085,37.4225,0);


        Sat s = new Sat(90,20,0);
        double isLOS = LosAlgorithm.ourComputeLos(p, wall, s);
        System.out.println("Point: " + p + ", Satellite: " + s.getSatID() + ", LOS: " + isLOS);

    }

    public  static void manual2() {
        String coordinatesList = "34.80226630887114,32.08426135651141,48.28 34.80226616040002,32.08429526325999,48.28 34.80231289876025,32.0842891892574,48.28 34.80238603364894,32.08430231689511,48.28 34.80244077477171,32.08433125760252,48.28 34.80250415724996,32.08437032571983,48.28 34.80249816031171,32.08453046420132,48.28 34.80273157818588,32.08470813515388,48.28 34.80274391388972,32.08481810218262,48.28 34.80270008430984,32.08488559730709,48.28 34.80262774950721,32.08495579756881,48.28 34.80255780428582,32.08498822046331,48.28 34.80242954257724,32.0849856493122,48.28 34.80239257893553,32.08496064269558,48.28 34.80227963988838,32.08484539655034,48.28 34.80220624045449,32.08467490605859,48.28 34.8021223254139,32.08448697763125,48.28 34.80203799586338,32.08439373554077,48.28 34.80194415100154,32.08432066321479,48.28 34.80192108275702,32.08426711392532,48.28 34.80226630887114,32.08426135651141,48.28";

        // Split the coordinates into individual triplets
        String[] triplets = coordinatesList.split("\\s+");

        // List to store the points
        List<Point3D> points = new ArrayList<>();

        // Iterate through the triplets and extract every third triplet to create points
        for (int i = 0; i < triplets.length; i += 3) {
            String[] coordinates = triplets[i].split(",");
            double x = Double.parseDouble(coordinates[0]);
            double y = Double.parseDouble(coordinates[1]);
            double z = Double.parseDouble(coordinates[2]);

            // Create a point and add it to the list
            Point3D point = new Point3D(x, y, z);
            points.add(point);
        }
        Building wall = new Building(points);
        //System.out.println("bs: "+wall.getBuildindVertices());
        Point3D p = new Point3D(34.80274391388972,32.08431668076913,0);


        Sat s = new Sat(310,89.80,0);
        double isLOS = LosAlgorithm.ourComputeLos(p, wall, s);
        System.out.println("Point: " + p + ", Satellite: " + s.getSatID() + ", LOS: " + isLOS);
    }


    public static void test_make_kml_from_point_to_sat(){
        System.out.println("start...");
       // Path to the KML file containing route points
        String routeFilePath = "routeABCDFabricated.kml";

        // Create a list of satellites
        List<Sat> allSats = UtilsAlgorithms.createSatDataList();
        System.out.println("the number of sats is: " + allSats.size());

        // Parse path points and convert to Cartesian coordinates
        List<Point3D> path = BuildingsFactory.parseKML(routeFilePath);
        System.out.println("the number of points in path: " + path.size());

        String kml = KML_Generator.OurBuildKml(path, allSats);
        String filePath = "output.kml";

        try {
            KML_Generator.OurWriteKmlToFile(kml, filePath);
            System.out.println("KML file created at: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("end");

    }
}
