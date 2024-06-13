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
        //test_make_kml_from_point_to_sat();
         //ourChckForOutOfRegion();
        //ourChckForisPoint2D_inBuilding();
//        Point3D pivot = new Point3D(670053, 3551100, 1);
//        System.out.println(Point3D.convertUTMToLatLon(pivot,"36N"));
//        ourChckFor_LosData_los();
        OurtestMoveParticleWithError();
//        for (int i = 0; i < 5; i++) {
//            System.out.println("Test run: " + (i + 1));
//            OurtestMoveParticleWithError();
//            System.out.println("----------------------------");
//        }



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


    public static void test_make_kml_from_point_to_sat() {
        System.out.println("start...");
        // Path to the KML file containing route points
        String routeFilePath = "routeABCDFabricated.kml";

        // Parse path points and convert to Cartesian coordinates
        List<Point3D> path = BuildingsFactory.parseKML(routeFilePath);
        System.out.println("The number of points in path: " + path.size());

        // Extract four points of the rectangle
        List<Point3D> rectanglePoints = new ArrayList<>();
        if (path.size() >= 4) {
            rectanglePoints.add(path.get(0));
            rectanglePoints.add(path.get(path.size() / 4));
            rectanglePoints.add(path.get(path.size() / 2));
            rectanglePoints.add(path.get(3 * path.size() / 4));
        } else {
            System.out.println("Not enough points in the path to form a rectangle.");
            return;
        }

        // Create a list of 2 satellites
        List<Sat> allSats = UtilsAlgorithms.createSatDataList();
        List<Sat> Sats = new ArrayList<>();
        for (int i = 0; i < 2 && i < allSats.size(); i++)
        {
            Sats.add(allSats.get(i));
        }
        System.out.println("Using 2 satellites for calculations.");

        // Generate KML for the four points and 2 satellites
        String kml = KML_Generator.OurBuildKml(rectanglePoints, Sats);
        String filePath = "output.kml";

        try {
            KML_Generator.OurWriteKmlToFile(kml, filePath);
            System.out.println("KML file created at: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("end");
}

public static void ourChckForOutOfRegion(){
        Particle p1 = new Particle( 34.802110,  32.083923,0);
        List<Building> bs = null;
        String walls_file = "Esri_v0.4.kml";
        try {
            bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Point3D pivot1 = new Point3D( 34.801856,  32.083395, 1);
        Point3D pivot2 = new Point3D( 34.802890,  32.084246, 1);
        boolean ans = p1.OutOfRegion(bs,pivot1,pivot2);
        System.out.println(ans);

    }
    public static void ourChckForisPoint2D_inBuilding(){
        Particle p1 = new Particle( 34.802110,  32.083923,0);
        List<Building> bs = null;
        int i = 0;
        boolean contain;
        String walls_file = "yahlom.kml";
        try {
            bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Building tmp : bs)
        {
            i++;
            contain=tmp.isPoint2D_inBuilding(p1.pos);
            if(contain==true)
            {
                p1.OutOfRegion=true;
                System.out.println("building "+ i +" is contain ");
            }
            else {
                System.out.println("building "+ i +" is not contain ");

            }
        }
    }
    public static void ourChckFor_LosData_los(){
        List<Building> bs = null;
        String walls_file = "Esri_v0.4.kml";
        try {
            bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Sat s = new Sat(180,45,0);
        List<Sat> all_sat = new ArrayList<>();
        all_sat.add(s);


        Particle p_inside_the_building = new Particle(670081,3551156,1);
        Particle p_outside_the_building = new Particle(670053,3551100,1);
        Point3D pivot = new Point3D(670053, 3551100, 1);
        Point3D pivot2 =  new Point3D(pivot);
        pivot2.offset(100, 100, 0);

//        System.out.println(p_inside_the_building.OutOfRegion(bs, pivot, pivot2));


        Particle.PrintArr(p_inside_the_building.LOS);
        p_inside_the_building.MessureSesnor(bs,all_sat);
        Particle.PrintArr(p_inside_the_building.LOS);
    }

    public static void OurtestMoveParticleWithError() {
        Particles ParticleList;
        List<ActionFunction> Actions;
        Point3D p1, p2;
        ParticleList = new Particles();
        p1 = new Point3D(670053, 3551100, 0);
        p2 = new Point3D(670053, 3551100, 0);

        // Initialize particles in the ParticleList
        Point3D pivot1 = new Point3D(670053, 3551100, 0);
        Point3D pivot2 = new Point3D(pivot1);
        ParticleList.initParticles(pivot1, pivot2);

        Actions = new ArrayList<ActionFunction>();
        ActionFunction tmp = new ActionFunction(p1, p2, 0, 0, 0);
        Actions.add(tmp);

        System.out.println("Before moveWithError : ");
        ParticleList.ourPrint3DPoints();
        // Now move particles with the action
        ParticleList.MoveParticleWithError(Actions.get(0));
        System.out.println("\n");
        System.out.println("After moveWithError : ");
        ParticleList.ourPrint3DPoints();
        System.out.println("\n");
    }
}
