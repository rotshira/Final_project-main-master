package ParticleFilter;

import Algorithm.LosAlgorithm;
import GNSS.Sat;
import Geometry.*;
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
//         ourChckForOutOfRegion();
//        ourChckForisPoint2D_inBuilding();
//        Point3D pivot = new Point3D(670053, 3551100, 1);
//        System.out.println(Point3D.convertUTMToLatLon(pivot,"36N"));
        //ourChckFor_LosData_los();
//        OurtestMoveParticleWithError();
//        ourtestforEvaluateWeightsNoHistory();
//        ourtestforComputeWeightsNoHistory();
//          OurTest_SetAfterResample();
//        Test_for_Resample();
//        OurTest_GetParticleWithMaxWeight();
//        OurTest_Actions();
//        OurTest_move();
//        testConvertToLAtAndLong();
//        convert();
//        test_MessureSesnor();
//        create_satellite_data();
//        test_compute_los();
//        test_lineIntersect();
//        testLine3D();
//        test_intersectionPoint();
        testIntersectionPoint3D();
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
        Particle p_in_bilding = new Particle(  670083,   3551156,0);
        Particle p_out_from_urea = new Particle(  670208,   3551084,0);
        Particle p_in_era_not_in_bilding = new Particle(  670103,   3551142,0);

        List<Building> bs = null;
        String walls_file = "Esri_v0.4.kml";
        try {
            bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Point3D pivot1 = new Point3D(670053, 3551100, 1);
        Point3D pivot2 =  new Point3D(pivot1);
        pivot2.offset(100, 100, 0);
        boolean ans1 = p_in_bilding.OutOfRegion(bs,pivot1,pivot2);
        boolean ans2 = p_out_from_urea.OutOfRegion(bs,pivot1,pivot2);
        boolean ans3 = p_in_era_not_in_bilding.OutOfRegion(bs,pivot1,pivot2);

        System.out.println("point in_bilding in utm: "+ p_in_bilding.pos.toString() +", in lat long: "+GeoUtils.convertUTMtoLATLON(p_in_bilding.pos,36)+", and the ans is: " +ans1);
        System.out.println("point out_from_urea in utm: "+ p_out_from_urea.pos.toString() +", in lat long: "+GeoUtils.convertUTMtoLATLON(p_out_from_urea.pos,36)+", and the ans is: " +ans2);
        System.out.println("point in_era_not_in_bilding in utm: "+ p_in_era_not_in_bilding.pos.toString() +", in lat long: "+GeoUtils.convertUTMtoLATLON(p_in_era_not_in_bilding.pos,36)+", and the ans is: " +ans3);

    }
    public static void ourChckForisPoint2D_inBuilding(){
        Particle p1 = new Particle( 670083.00,  3551156.00,0);
        List<Building> bs = null;
        int i = 0;
        boolean contain;
        String walls_file = "Esri_v0.4.kml";
        try {
            bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("roi convert: "+GeoUtils.convertUTMtoLATLON(p1.pos,36));
//        System.out.println("my convert: "+Point3D.convertUTMToLatLon(p1.pos,"36N"));
        for (Building tmp : bs)
        {
            i++;
            contain=tmp.isPoint2D_inBuilding(GeoUtils.convertUTMtoLATLON(p1.pos,36));
//            contain=tmp.isPoint2D_inBuilding(Point3D.convertUTMToLatLon(p1.pos,"36N"));
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
        ParticleList.Print3DPoints();

        // Now move particles with the action
        ParticleList.MoveParticleWithError(Actions.get(0));

        System.out.println("\n");
        System.out.println("After moveWithError : ");
        ParticleList.Print3DPoints();
        System.out.println("\n");
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
        pivot2.offset(100, 100, 0);
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


    public static void Test_for_Resample()
    {
        //עובד טוב לוקח רק חלקיקים עם משקלים גבוהים
        // Initialize ParticleList with particles
        Particles ParticleList = new Particles();
        Point3D pivot = new Point3D(670053, 3551100, 1);
        Point3D pivot2 = new Point3D(pivot);
        pivot2.offset(100, 100, 0);
        ParticleList.initParticles(pivot, pivot2);
        int i=0;

        // Introduce variation in the initial weights
        List<Particle> particles = ParticleList.getParticleList();
        for (Particle p: ParticleList.getParticleList()) {
            p.setWeight((i + 1) * 0.1); // Set varying weights for the first 10 particles
            i++;
        }

        // Print initial weights and total weight (limited to the first 10 particles)
        i=0;
        System.out.println("Initial Particles:");
        for (Particle p: ParticleList.getParticleList()) {
            System.out.printf("Particle %d: Position = %s, Initial Weight = %.10f\n", i + 1, p.getLocation().toString(), p.getWeight());
            i++;

        }

        // Normalize weights and print normalized weights (limited to the first 10 particles)
        double[] normalizedWeights = ParticleList.Normal_Weights();
        System.out.println("Normalized Weights:");
        i=0;
        for (Particle p: ParticleList.getParticleList()) {
            System.out.printf("Particle %d: Position = %s, Initial Weight = %.10f\n", i + 1, p.getLocation().toString(), p.getWeight());
            i++;

        }

        // Print sum of normalized weights for verification
        double sumOfWeights = 0;
        for (double weight : normalizedWeights) {
            sumOfWeights += weight;
        }
        System.out.printf("Sum of Normalized Weights: %.10f\n", sumOfWeights);

        // Perform resampling
        ParticleList.Resample();

        // Print particles after resampling and their weights (limited to the first 10 particles)
        i=0;
        System.out.println("Particles After Resample:");
        for (Particle p: ParticleList.getParticleList()) {
            System.out.printf("Particle %d: Position = %s.\n", i + 1, p.getLocation().toString());
            i++;

        }
    }
    public static void OurTest_GetParticleWithMaxWeight() {
        // Initialize ParticleList with particles
        Particles ParticleList = new Particles();
        Point3D pivot = new Point3D(670053, 3551100, 1);
        Point3D pivot2 = new Point3D(pivot);
        pivot2.offset(100, 100, 0);
        ParticleList.initParticles(pivot, pivot2);
        int i=0;

        // Introduce variation in the initial weights
        List<Particle> particles = ParticleList.getParticleList();
        for (Particle p: ParticleList.getParticleList()) {
            p.setWeight((i + 1) * 0.1); // Set varying weights for the first 10 particles
            i++;
        }
        //ParticleList.getParticleList().get(0).setWeight(100000);
        Point3D tmp = ParticleList.GetParticleWithMaxWeight();
        // Print initial weights and total weight (limited to the first 10 particles)
        i=0;
        System.out.println("Initial Particles:");
        for (Particle p: ParticleList.getParticleList()) {
            System.out.printf("Particle %d: Position = %s, Initial Weight = %.10f\n", i + 1, p.getLocation().toString(), p.getWeight());
            i++;

        }
        System.out.println("the partical With the Max Weigh is: "+tmp);
        //KML_Generator.Generate_kml_from_ParticleList(ParticleList, "fattest_point_or_weighted_point.kml",10);
//        tmp=GeoUtils.convertUTMtoLATLON(tmp, 36);
//        double lat=tmp.getX();
//        double lon=tmp.getY();
//        String line=Double.toString(lon)+" "+Double.toString(lat)+" "+tmp.getZ();
//        System.out.println(line);



    }
    public static void OurTest_Actions() {
        //הרשימה של הActions הם נכונים ועובדים טוב
        Point3D p0 = new Point3D(670103.5,3551179.5, 1.0);
        List<Point3D> path;
        path = UtilsAlgorithms.createPath();
        List<ActionFunction>  Actions = new ArrayList<ActionFunction>();

        for(int i=0;i<path.size()-1; i++)
        {
            ActionFunction tmp = new ActionFunction(path.get(i), path.get(i+1), 0 , 0,0);

            Actions.add(tmp);
        }
        //משווה בין נקודה במקום 0 אחרי Actions מספר 0 לבין נקודה במקום 1
//        System.out.println("p0 is: "+p0.toString());
//        System.out.println("the first point of path: "+path.get(0).toString());
//        System.out.println("the second point of path: "+path.get(1).toString());
//        p0.offset(Actions.get(0).PivotX,Actions.get(0).PivotY);
//        System.out.println("p0 after actions 0: "+p0.toString());
//
//
//
        //משווה בין הנקודה במקום i אחרי Actions מספר i לבין נקודה במקום i+1
//        boolean its_work = true;
//        for (int i = 0;i<path.size()-1;i++){
//            path.get(i).offset(Actions.get(i).PivotX,Actions.get(i).PivotY);
//            double RX = path.get(i).getX();
//            double RY = path.get(i).getY();
//            double NRX = path.get(i+1).getX();
//            double NRY = path.get(i+1).getY();
//            if (RX!=NRX || RY != NRY){
//                its_work = false;
//            }
//        }
//        System.out.println("its_work = " + its_work );
//
//
//
        //Actionsיוצר את המסלול מהנקודה הראשונה בעזרת ה
//        List<Point3D> my_path = new ArrayList<>();
//        Point3D temp_point = p0;
//        my_path.add(temp_point);
//        for (int i = 0; i < Actions.size(); i++) {
//            temp_point.offset(Actions.get(0).PivotX,Actions.get(0).PivotY);
//            my_path.add(temp_point);
//        }
//        System.out.println(my_path.size());
//        KML_Generator.Generate_kml_from_List(path, "my_path.kml");

    }
    public static void OurTest_move() {
        //A test to check whether a green nail is obtained
        String walls_file = "Esri_v0.4.kml";
        List<Sat> allSats;
        List<Point3D> path;
        path = UtilsAlgorithms.createPath();
        Particles ParticleList = new Particles();
        Point3D pivot, pivot2;
        String Simulation_route_3D_kml_path = "Simulation_route_May_2016.kml";
        String Particle_path3 = "KaminData/Simulation_routeTest_initial.kml";
        allSats = UtilsAlgorithms.createSatDataList();
        KML_Generator.Generate_kml_from_List(path, Simulation_route_3D_kml_path);
        List<Building> bs = null;
        try {
            bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LosData losData = new LosData( bs, path, allSats);
        pivot = new Point3D(670053, 3551100, 1);
        pivot2 = new Point3D(pivot);
        pivot2.offset(100, 100, 0);

        ParticleList.initParticles(pivot, pivot2);
        KML_Generator.Generate_kml_from_ParticleList(ParticleList, Particle_path3, 10);
        Particle real_Point_0 = new Particle( 670103.5,  3551179.5,  1.0);
        Point3D closest_Particle = new Point3D(670105.0,3551180.0,1.0);


        for(int i = 0; i< ParticleList.getParticleList().size(); i++)
        {
            ParticleList.getParticleList().get(i).pos.offset(real_Point_0.pos.getX() -closest_Particle.getX(),real_Point_0.pos.getY() -closest_Particle.getY(),0);
        }
        for(int i = 0; i< ParticleList.getParticleList().size(); i++)
        {
            if(ParticleList.getParticleList().get(i).pos.equals(real_Point_0.pos))
                System.out.println("equal");
        }
        ParticleList.OutFfRegion(bs, pivot, pivot2);
        ParticleList.MessureSignalFromSats( bs,  allSats);
        real_Point_0.MessureSesnor(bs, allSats);
        Boolean[] b = real_Point_0.getLOS();
        ParticleList.ComputeWeightsNoHistory(b);

        String updatedParticlePath = "KaminData/Simulation_routeTest_updated.kml";
        KML_Generator.Generate_kml_from_ParticleList(ParticleList, updatedParticlePath, allSats.size());
//        Point3D p = ParticleList.GetParticleWithMaxWeight();
//        System.out.println(p);
    }
    public static void testConvertToLAtAndLong(){
        //החישוב של רוי נכון !!
        Point3D pivot = new Point3D(670053, 3551100, 1);
        System.out.println("GeoUtils:");
        System.out.println(GeoUtils.convertUTMtoLATLON(pivot, 36));
        System.out.println("Point3D");
        System.out.println(Point3D.convertUTMToLatLon(pivot,"36N"));

    }
    public static void convert() {
        Point3D p = new Point3D(670053,3551100,1);
        List<Point3D> ans = new ArrayList<Point3D>();
        Point3D GeoUtils_p =GeoUtils.convertUTMtoLATLON(p,36);
        Point3D Point3D_p =  Point3D.convertUTMToLatLon(p,"36N");
        System.out.println(GeoUtils_p);
        System.out.println(Point3D_p);
        ans.add(GeoUtils_p);
//        ans.add(Point3D_p);
        KML_Generator.Generate_kml_from_List(ans,"Ans.kml",true);




    }
    public static void create_satellite_data(){
        String walls_file = "Esri_v0.4.kml";
        List<Sat> allSats;

//        List<Point3D> path;
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
//        path = UtilsAlgorithms.createPath();

        allSats = UtilsAlgorithms.createSatDataList();
        String Simulation_route_3D_kml_path = "Simulaton__route_May_2016.kml";

        String Particle_path = "KaminData\\Simulaton_routeTest_FInal";
        String Particle_path3 = "KaminData\\Simulaton_routeTest_initial.kml";

//        KML_Generator.Generate_kml_from_List(path, Simulation_route_3D_kml_path);


        ParticleList = new Particles();
        pivot = new Point3D(670053, 3551100, 1);
        pivot2 =  new Point3D(pivot);
        pivot2.offset(100, 100, 0);
//        LosData losData = new LosData( bs, path, allSats);


        ParticleList.initParticles(pivot, pivot2);
//        KML_Generator.Generate_kml_from_ParticleList(ParticleList, Particle_path3,allSats.size());




        Actions = new ArrayList<ActionFunction>();
//        System.out.println(path.size());
        ParticleList.OutFfRegion(bs, pivot, pivot2);
        ParticleList.MessureSignalFromSats( bs,  allSats);

        for(int i=0;i<ParticleList.getParticleList().size()-1; i++)
        {
            System.out.print(ParticleList.getParticleList().get(i).pos+": ");
            for (int j = 0; j < allSats.size(); j++) {
                System.out.print(ParticleList.getParticleList().get(i).LOS[j]+", ");
            }
            System.out.println();
        }
        System.out.println(ParticleList.getParticleList().size()-1);

    }
    public static void test_MessureSesnor() {
        Particle p = new Particle(670162,3551062,1);
        String walls_file = "Esri_v0.4.kml";
        List<Building> bs = null;
        List<Sat> allSats;
        try {
            bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        allSats = UtilsAlgorithms.createSatDataList();
        p.MessureSesnor(bs,allSats);
        int cnt_T=0;
        int cnt_F=0;
        for (int i = 0; i < allSats.size()-1; i++) {
//            System.out.println(p.LOS[i]);
            if (p.LOS[i]==true){
                cnt_T++;

            }
            else{
                cnt_F++;
            }

        }
        System.out.println(cnt_T);
        System.out.println(cnt_F);


    }
    public static void test_compute_los()
    {
        String walls_file = "Esri_v0.4.kml";
        List<Sat> allSats;
        List<Building> bs = null;
        try {
            bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        allSats = UtilsAlgorithms.createSatDataList();
        Particle p = new Particle( 670079.41,  3551156.63, 1.0);
//        p.MessureSesnor(bs,allSats);
//        for(int i=0 ; i<allSats.size()-1 ;i++){
//            System.out.println(p.getLOS()[i]);
//        }
        System.out.println(bs.get(0).getWalls().get(1).toString());

    }
    public static void test_lineIntersect()
    {
        // משוואה 1
        double x1 = 1.0, y1 = 1.0;
        double x2 = 4.0, y2 = 4.0;
        // 2 משוואה
        double x3 = 1.0, y3 = 4.0;
        double x4 = 4.0, y4 = 1.0;

        Point2D intersection = Line3D.lineIntersect(x1, y1, x2, y2, x3, y3, x4, y4);
        System.out.println(intersection);

        if (intersection != null) {
            System.out.println("passed");
        } else {
            System.out.println(" failed " + intersection);
        }

        double a1 = 1.0, b1 = 1.0;
        double a2 = 4.0, b2 = 4.0;

        // Line 2: Parallel to Line 1, shifted vertically
        double a3 = 1.0, b3 = 2.0;
        double a4 = 4.0, b4 = 5.0;

        Point2D noIntersection = Line3D.lineIntersect(a1, b1, a2, b2, a3, b3, a4, b4);

        if (noIntersection == null) {
            System.out.println(" Passed");
        } else {
            System.out.println(" Failed " + noIntersection);
        }

    }
    public static void testLine3D() {
        // Test parameters
        Point3D start = new Point3D(670053, 3551100, 1);
        double azimuth = 114.5;      // Azimuth in degrees
        double elevation = 3;    // Elevation in degrees
        int distance = 300;       // Distance between points

        // Create a new Line3D object using the constructor with azimuth, elevation, and distance
        Line3D test = new Line3D(start, azimuth, elevation, distance);
        System.out.println(test.getP1());
        System.out.println(test.getP2());

        // Calculate distance between the two points and check if it matches the expected value
        double calculatedDistance = test.getP1().distance3D(test.getP2());
        if (Math.abs(calculatedDistance - distance) > 1e-6) {
            System.out.println("Error on distance: expected " + distance + ", but got " + calculatedDistance);
        } else {
            System.out.println("Distance is correct: " + calculatedDistance);
        }

//         Calculate azimuth between the two points and check if it matches the expected value
        double calculatedAzimuth = test.getP1().azimuthBetweenPoints( test.getP2());
        if (Math.abs(calculatedAzimuth - azimuth) > 1e-6) {
            System.out.println("Error on azimuth: expected " + azimuth + ", but got " + calculatedAzimuth);
        } else {
            System.out.println("Azimuth is correct: " + calculatedAzimuth);
        }

        // Calculate elevation between the two points and check if it matches the expected value
        double calculatedElevation = test.getP1().elevationBetweenPoints( test.getP2());
        if (Math.abs(calculatedElevation - elevation) > 1e-6) {
            System.out.println("Error on elevation: expected " + elevation + ", but got " + calculatedElevation);
        } else {
            System.out.println("Elevation is correct: " + calculatedElevation);
        }


    }
    public static void test_intersectionPoint() {
        Point3D p1 = new Point3D(1,2,3);
        Point3D p2 = new Point3D(4,5,6);
        Point3D p3 = new Point3D(1,5,3);
        Point3D p4 = new Point3D(4,2,6);
        //Supposed to meet at (2.5,3.5,4.5) and indeed meet there
        Line3D l1 = new Line3D(p1,p2);
        Line3D l2 = new Line3D(p3,p4);

        Point2D ans1 = l1.intersectionPoint(l2);
        System.out.println(ans1);
        Point3D p5 = new Point3D(1,1,1);
        Point3D p6 = new Point3D(4,2,3);
        Point3D p7 = new Point3D(0,3,2);
        Point3D p8 = new Point3D(2,4,5);

        ////Shouldn't meet and indeed they don't
        Line3D l3 = new Line3D(p5,p6);
        Line3D l4 = new Line3D(p7,p8);

        Point2D ans2 = l3.intersectionPoint(l4);
        System.out.println(ans2);




    }
    public static void testIntersectionPoint3D() {
        String walls_file = "Esri_v0.4.kml";
        List<Building> bs = null;
        try {
            bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        double azimut = 50;
        double elovation = 40;

        Point3D point = new Point3D(670113  ,3551167,1);
        point= GeoUtils.convertUTMtoLATLON(point,36);
        System.out.println(point);


        Line3D line = new Line3D(point,azimut,elovation,300);

        for(int j=0;j<bs.size();j++){
            for(int i=0; i<4;i++){
                Point3D ans = bs.get(j).getWalls().get(i).intersectionPoint3D(line);
                System.out.println(ans);
                // System.out.println(bs.get(j).getWalls().get(i).getWallAsLine());
            }
        }






    }




}















