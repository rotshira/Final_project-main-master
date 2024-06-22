package ParticleFilter;

import Algorithm.LosAlgorithm;
import GNSS.Sat;
import Geometry.Building;
import Geometry.BuildingsFactory;
import Geometry.Point3D;
import Geometry.Wall;
import Parsing.nmea.NMEAProtocolParser;
import Utils.GeoUtils;
import Utils.KML_Generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class test_our_function {
    public static void main(String[] args) {
        //manual1();
        //manual2();
        //test_make_kml_from_point_to_sat();
         //ourChckForOutOfRegion();
        //ourChckForisPoint2D_inBuilding();
//        Point3D pivot = new Point3D(670053, 3551100, 1);
//        System.out.println(Point3D.convertUTMToLatLon(pivot,"36N"));
        //ourChckFor_LosData_los();
//        ourtestforEvaluateWeightsNoHistory();
//        ourtestforComputeWeightsNoHistory();
          OurTest_SetAfterResample();



    }

    public static String arrayToString(Boolean[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
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
        for (Point3D p :path){
            System.out.println("the main point is: "+ p);
        }

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
    public static void ourTestforShift(){//לא סיימנו צריל לבדוק קודם את EvaluateWeightsNoHistory
        String walls_file = "Esri_v0.4.kml";


        // Buildings bs;
        List<Sat> allSats;



        List<Point3D> path;
        Particles ParticleList;
        Point3D pivot, pivot2;
        int CurrentGeneration;
        String Simulation_route_kml_path = "Simulaton__route_May_2016.kml";



        List<ActionFunction>  Actions;
        List<Building> bs = null;
        try {
            bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Number of buildings is " + bs.size());
        path = UtilsAlgorithms.createPath();

        allSats = UtilsAlgorithms.createSatDataList();
        String Simulation_route_3D_kml_path = "Simulaton__route_May_2016.kml";

        String Particle_path3 = "KaminData\\Simulaton_routeTest_initial.kml";
        String Particle_path = "KaminData\\Simulaton_routeTest_FInal";


        KML_Generator.Generate_kml_from_List(path, Simulation_route_3D_kml_path);

        ParticleList = new Particles();
        pivot = new Point3D(670053, 3551100, 1);
        pivot2 =  new Point3D(pivot);
        pivot2.offset(100, 100, 0);
        LosData losData = new LosData( bs, path, allSats);


        ParticleList.initParticles(pivot, pivot2);
        KML_Generator.Generate_kml_from_ParticleList(ParticleList, Particle_path3,10);

        NMEAProtocolParser parser = new NMEAProtocolParser();


        Actions = new ArrayList<ActionFunction>();
        List<Point3D> PointList = null;
        CurrentGeneration = 0;
        Random R1= new Random();
        System.out.println(path.size());
        for(int i=0;i<path.size()-1; i++)
        {
            ActionFunction tmp = new ActionFunction(path.get(i), path.get(i+1), 0 , 0,0);

            Actions.add(tmp);
        }
        List<Point3D> ans = new ArrayList<Point3D>();
        for(int i=1;i<path.size()-1; i++)
        {

            System.out.println("compute for timestamp "+i);
            ParticleList.MoveParticleWithError(Actions.get(i));

            ParticleList.OutFfRegion(bs, pivot, pivot2);

            ParticleList.MessureSignalFromSats( bs,  allSats);

            ParticleList.MoveParticleWithError(Actions.get(i));

            ParticleList.ComputeWeightsNoHistory(losData.getSatData(i));
            //ParticleList.ComputeWeights(losData.getSatData(i)); // compute weights with hisotry
            ParticleList.Resample();


            Point3D tmp = ParticleList.GetParticleWithMaxWeight();
            ans.add(tmp);
            String Particle_path2=Particle_path+i+".kml";

            KML_Generator.Generate_kml_from_ParticleList(ParticleList, Particle_path2,10);

            ParticleList.ComputeAndPrintErrors(path.get(i));

        }

        KML_Generator.Generate_kml_from_List(ans,"checkAns.kml");

    }
    public static void ourtestforEvaluateWeightsNoHistory(){
        //הפונקציה EvaluateWeightsNoHistory עובדת כמו שצריך
        Particle realpoint = new Particle(670103.5, 3551179.5,1.0);
        String walls_file = "Esri_v0.4.kml";
        List<Building> bs = null;
        try {
            bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Point3D> path;
        path = UtilsAlgorithms.createPath();
        List<Sat> allSats;
        allSats = UtilsAlgorithms.createSatDataList();




        Particles ParticleList = new Particles();
        Point3D pivot = new Point3D(670053, 3551100, 1);
        Point3D pivot2 =  new Point3D(pivot);
        pivot2.offset(100, 100, 0);
        LosData losData = new LosData( bs, path, allSats);
        ParticleList.initParticles(pivot, pivot2);
        realpoint.MessureSesnor(bs,allSats);
        Boolean[] b = realpoint.getLOS();
        System.out.println("the real los: " + arrayToString(b));
        int i = 0;

        for (Particle p : ParticleList.getParticleList()){
            p.OutOfRegion(bs,pivot,pivot2);
            System.out.println("Point "+i +" ,OutOfRegion: " + p.OutOfRegion);
            p.MessureSesnor(bs,allSats);
            System.out.println("Point "+i +" ,MessureSesnor: " + arrayToString(p.getLOS()));
            p.EvaluateWeightsNoHistory(b);
            System.out.println("Point "+i +" ,NumberOfMatchedSats: " + p.getNumberOfMatchedSats() );


            i++;
        }
    }
    public static void ourtestforComputeWeightsNoHistory() {
        //הפונקציה ComputeWeightsNoHistory עובדת כמו שצריך

        Particle realpoint = new Particle(670103.5, 3551179.5, 1.0);
        String walls_file = "Esri_v0.4.kml";
        List<Building> bs = null;
        try {
            bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Point3D> path;
        path = UtilsAlgorithms.createPath();
        List<Sat> allSats;
        allSats = UtilsAlgorithms.createSatDataList();


        Particles ParticleList = new Particles();
        Point3D pivot = new Point3D(670053, 3551100, 1);
        Point3D pivot2 = new Point3D(pivot);
        pivot2.offset(100, 100, 0);
        LosData losData = new LosData(bs, path, allSats);
        ParticleList.initParticles(pivot, pivot2);
        realpoint.MessureSesnor(bs, allSats);
        Boolean[] b = realpoint.getLOS();
        System.out.println("the real los: " + arrayToString(b));
        int i = 0;
        for (Particle p : ParticleList.getParticleList()){

            System.out.println("Point "+i +" ,NumberOfMatchedSats: " + p.getNumberOfMatchedSats());
            i++;
        }
        ParticleList.MessureSignalFromSats( bs,  allSats);
        ParticleList.ComputeWeightsNoHistory(b);
        i=0;
        for (Particle p : ParticleList.getParticleList()) {
            System.out.println("Point " + i + " ,NumberOfMatchedSats: " + p.getNumberOfMatchedSats());
            i++;
        }
    }


    public static void OurTest_SetAfterResample() {
        // Initialize ParticleList with dummy particles
        Particles ParticleList = new Particles();
        Point3D pivot = new Point3D(670053, 3551100, 1);
        Point3D pivot2 = new Point3D(pivot);
        pivot2.offset(3, 3, 0);
        ParticleList.initParticles(pivot, pivot2);

        // Create a NewList with new positions
        List<Point3D> NewList = new ArrayList<>();
        for (int i = 0; i < ParticleList.getParticleList().size(); i++) {
            NewList.add(new Point3D(i + 10, i + 10, i + 10));
        }

        // Perform SetAfterResample
        ParticleList.SetAfterResample(NewList);

        // Verify the results
        System.out.println("Particles after resampling:");
        for (int i = 0; i < ParticleList.getParticleList().size(); i++) {
            Point3D pos = ParticleList.getParticleList().get(i).getLocation();
            double weight = ParticleList.getParticleList().get(i).getWeight();

            // Print the results for manual inspection
            System.out.println("Particle " + i + ": Position = " + pos + ", Weight = " + weight);
        }
    }




}
