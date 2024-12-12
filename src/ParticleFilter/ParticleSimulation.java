package ParticleFilter;
import Algorithm.LosAlgorithm;
import GNSS.Sat;
import Geometry.Building;
import Geometry.BuildingsFactory;
import Geometry.Point2D;
import Geometry.Point3D;
import ML_Los_Nlos_Classifier.ClassifierTrainer;
import ML_Los_Nlos_Classifier.LOSPredictor;
import Parsing.nmea.NMEAProtocolParser;
import Utils.KML_Generator;
import dataStructres.NMEAPeriodicMeasurement;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.Inflater;
import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.SerializationHelper;
/**
 * Created by Roi on 5/23/2016.
 */
public class ParticleSimulation {


    Classifier classifier = (Classifier) SerializationHelper.read("los_nlos_model.model");
    LOSPredictor predictor = new LOSPredictor(classifier);

	public ParticleSimulation() throws Exception {
	}

	public static void main(String[] args) throws Exception {
        //simulationMain2();

        simulatePoint(21, 55, 56.33, 'N', 24, 55, 10.2, 'E', 5.0);
        createSquarePath();
        Point3D pointA = new Point3D(21.0, 24.0, 0.0); // Latitude, Longitude, Altitude
        Point3D pointB = new Point3D(21.0, 25.0, 0.0);
        Point3D pointC = new Point3D(22.0, 25.0, 0.0);
        Point3D pointD = new Point3D(22.0, 24.0, 0.0);
        createRectanglePathWithTacks(pointA,pointB,pointC,pointD);

        Point3D point = new Point3D(21.9323, 24.9189, 100.0); // Latitude, Longitude, Altitude
        simulatePoint(point);

//        simulationMainWithML();
        //testParse();

        //NMEAParser();
           //NEMAFIlePF();
         //NEMAFIlePF2();
           // NEMAFIlePF3();


           //test3();
        //oursimulationMain();




    }

    private static void createRectanglePathWithTacks(Point3D A, Point3D B,Point3D C,Point3D D) {
        // Step 1: Define the four corners of the rectangle
        Point3D pointA = A; // Latitude, Longitude, Altitude
        Point3D pointB = B;
        Point3D pointC = C;
        Point3D pointD = D;

        // Step 2: Generate intermediate points along each side of the rectangle
        List<Point3D> rectanglePoints = new ArrayList<>();
        int steps = 50; // Number of tacks per side

        // Add points between A and B
        rectanglePoints.addAll(interpolatePoints(pointA, pointB, steps));

        // Add points between B and C
        rectanglePoints.addAll(interpolatePoints(pointB, pointC, steps));

        // Add points between C and D
        rectanglePoints.addAll(interpolatePoints(pointC, pointD, steps));

        // Add points between D and A
        rectanglePoints.addAll(interpolatePoints(pointD, pointA, steps));

        // Step 3: Specify the output file path
        String filePath = "rectangleWithTacks.kml";

        // Step 4: Generate the KML file
        try {
            KML_Generator.Generate_kml_from_List(rectanglePoints, filePath, true);
            System.out.println("KML file for rectangle with tacks generated successfully: " + filePath);
        } catch (Exception e) {
            System.err.println("Error generating KML file: " + e.getMessage());
        }
    }

    // Method to interpolate points between two coordinates
    private static List<Point3D> interpolatePoints(Point3D start, Point3D end, int steps) {
        List<Point3D> points = new ArrayList<>();
        for (int i = 0; i <= steps; i++) {
            double lat = start.getX() + i * (end.getX() - start.getX()) / steps;
            double lon = start.getY() + i * (end.getY() - start.getY()) / steps;
            double alt = start.getZ() + i * (end.getZ() - start.getZ()) / steps;
            points.add(new Point3D(lat, lon, alt));
        }
        return points;
    }


    private static void simulatePoint(int latDegrees, int latMinutes, double latSeconds, char latDirection,
                                      int lonDegrees, int lonMinutes, double lonSeconds, char lonDirection,
                                      double altitude) {
        // Step 1: Convert latitude and longitude to decimal degrees
        double latitude = convertToDecimalDegrees(latDegrees, latMinutes, latSeconds, latDirection);
        double longitude = convertToDecimalDegrees(lonDegrees, lonMinutes, lonSeconds, lonDirection);

        // Step 2: Create a Point3D object for the point
        Point3D point = new Point3D(latitude, longitude, altitude);

        // Step 3: Add the point to a list
        List<Point3D> pointList = new ArrayList<>();
        pointList.add(point);

        // Step 4: Specify the output file path
        String filePath = "outputAdiel.kml";

        // Step 5: Generate the KML file
        try {
            KML_Generator.Generate_kml_from_List(pointList, filePath, true);
            System.out.println("KML file generated successfully: " + filePath);
        } catch (Exception e) {
            System.err.println("Error generating KML file: " + e.getMessage());
        }
    }

    private static void simulatePoint(Point3D point) {
        // Step 1: Add the point to a list
        List<Point3D> pointList = new ArrayList<>();
        pointList.add(point);

        // Step 2: Specify the output file path
        String filePath = "outputRoey.kml";

        // Step 3: Generate the KML file
        try {
            KML_Generator.Generate_kml_from_List(pointList, filePath, true);
            System.out.println("KML file generated successfully: " + filePath);
        } catch (Exception e) {
            System.err.println("Error generating KML file: " + e.getMessage());
        }
    }


    private static void createSquarePath() {
        // Step 1: Define the four points of the square
        Point3D pointA = new Point3D(21.0, 24.0, 0.0); // Latitude, Longitude, Altitude
        Point3D pointB = new Point3D(21.0, 25.0, 0.0);
        Point3D pointC = new Point3D(22.0, 25.0, 0.0);
        Point3D pointD = new Point3D(22.0, 24.0, 0.0);

        // Step 2: Create a list to represent the path
        List<Point3D> squarePath = new ArrayList<>();
        squarePath.add(pointA); // Start at A
        squarePath.add(pointB); // Move to B
        squarePath.add(pointC); // Move to C
        squarePath.add(pointD); // Move to D
        squarePath.add(pointA); // Close the square back at A

        // Step 3: Specify the output file path
        String filePath = "squarePath.kml";

        // Step 4: Generate the KML file
        try {
            KML_Generator.Generate_kml_from_List(squarePath, filePath, true);
            System.out.println("KML file for square path generated successfully: " + filePath);
        } catch (Exception e) {
            System.err.println("Error generating KML file: " + e.getMessage());
        }
    }



    public static double convertToDecimalDegrees(int degrees, int minutes, double seconds, char direction) {
        // Convert DMS to decimal degrees
        double decimalDegrees = degrees + (minutes / 60.0) + (seconds / 3600.0);

        // Apply the sign based on the direction
        switch (Character.toUpperCase(direction)) {
            case 'N': case 'E':
                return decimalDegrees;
            case 'S': case 'W':
                return -decimalDegrees;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction +
                        ". Must be one of N, S, E, or W.");
        }
    }

    private static void testParse() {

        List<String> testString= new ArrayList<>();
        testString.add("$GPRMC,061916.400,A,3205.04218,N,03448.14630,E,2.2,178.0,170616,,A*46");
        testString.add("$GPGGA,061916.400,3205.04218,N,03448.14630,E,1,15,0.7,043.75,M,17.1,M,,*60");
        testString.add("$GNGSA,A,3,30,15,17,28,05,13,20,07,,,,,1.4,0.7,1.2*24");
        testString.add("$GNGSA,A,3,85,75,65,66,72,74,76,,,,,,1.4,0.7,1.2*26");
        testString.add("$GNGSA,A,3,,,,,,,,,,,,,1.4,0.7,1.2*2D");
        testString.add("$GPGSV,3,1,10,05,28,241,31,07,19,082,30,13,57,320,41,15,23,316,37*77");
        testString.add("$GPGSV,3,2,10,17,41,143,44,19,21,171,37,20,17,289,29,28,67,023,36*77");
        testString.add("$GPGSV,3,3,10,30,42,064,33,33,24,246,22,,,,,,,,*79");
        testString.add("$GLGSV,2,1,08,74,30,041,33,66,16,329,29,76,14,278,24,75,47,332,38*68");
        testString.add("$GLGSV,2,2,08,65,51,275,35,84,26,068,23,85,25,124,36,72,35,202,33*60");
        testString.add("06/17/2016 09:19:16.341	(255)	Cannot process message $GNGSA,A,3,,,,,,,,,,,,,1.4,0.7,1.2*2D");
        NMEAProtocolParser parser = new NMEAProtocolParser();
        NMEAPeriodicMeasurement ans = parser.ParseStringList(testString);
        System.out.println("end");


    }

    private static void test3() {


        for(int j=1;j<6;j++)
            for(int i=0;i<400;i+=10)
                System.out.println("Power is : "+j+". Dist is : "+Math.sqrt(2)*i+". Receivd SNR :"+SenseEden.GetSnr(0,0,i,i,j));

    }



    private static void NEMAFIlePF2() {



        String walls_file = "BoazMapJune8.kml";
        Particles ParticleList;
        Point3D pivot, pivot2;

        List<ActionFunction>  Actions;
        List<Building> bs = null;
        try {
            bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Number of buildings is " + bs.size());
        System.out.println("Number of particles is: "+ Particles.NumberOfParticles);


        String Particle_path = "KaminData\\CombineHeadong";
        String Particle_path3 = "KaminData\\Simulaton_routeTest_initial.kml";

        List<Particles> PointInTime=  new ArrayList<>();
        String Particle_ans_path = "NMEA_SmartPhones_Recordings\\Constructed_ABCD_Route_9_8_2016_V.kml";
        String Particle_ans_path2 = "KaminData\\400ParticlesTry2.kml";


        ParticleList = new Particles();
        pivot = new Point3D(670053, 3551100, 1);
        pivot2 =  new Point3D(pivot);
        pivot2.offset(100, 100, 0);


           pivot = new Point3D(670003, 3551050, 1);
         pivot2 =  new Point3D(pivot);
        pivot2.offset(130, 130, 0);

        ParticleList.initParticlesWithHeading(pivot, pivot2);

        KML_Generator.Generate_kml_from_ParticleList(ParticleList, Particle_path3,5);


        NMEAProtocolParser parser = new NMEAProtocolParser();
           String NMEAFIlePath = "KaminData\\NMEAFiles\\routeABCDtwice11AM_NMEA.txt";
//         String NMEAFIlePath = "NmeaRecordings\\ABCD_Twice_2nd_Time.txt";
//        String NMEAFIlePath = "NmeaRecordings\\April2015.txt";

        // String NMEAFIlePath = "NmeaRecordings\\Diagonal_A_C_D_B_A_Twice.txt";



        //   String NMEAFIlePath = "NMEA_SmartPhones_Recordings\\2.txt";


        List<NMEAPeriodicMeasurement> NmeaList = null;
        try {
            NmeaList = parser.parse(NMEAFIlePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

          String Recons_Path= "NmeaRecordings\\ABCD_Twice2_17_6COG_SOG_rec.kml";
           String Recons_Path2= "NmeaRecordings\\ABCD_Twice2_17_6from_NMEA.kml";

           UtilsAlgorithms.GenerateKMLfromCOG_SOS(NmeaList, Recons_Path);
          UtilsAlgorithms.GenerateKMLfromNMEA_List(NmeaList, Recons_Path2);

        int NumberOfSamples = NmeaList.size();
        List<Sat> allSats;
        List<Point3D> PointsInTIme = new ArrayList<>();
        List<Particles> finalList= new ArrayList<>();
        finalList.add(ParticleList);

        List<Point3D> ans = new ArrayList<Point3D>();
        int j=0;

        for(int i=2;i<NumberOfSamples; i++)
        {

            System.out.println("Compute position for TimeStamp # "+i);
            double COG = NmeaList.get(i).getCOG();
            double SOG = NmeaList.get(i).getSOG();
            double dt = NmeaList.get(i).getUtcTime()-NmeaList.get(i-1).getUtcTime();
            if(dt>=1)
                dt=1;
              System.out.println("SOG: "+ SOG+"  .  COG: "+COG+"  dt:  "+dt );
             ParticleList.MoveParticlesBySOG_COGWithHeading(SOG, COG);
            ParticleList.MoveParticlesBySOG_COG(SOG, COG, dt);

            allSats = UtilsAlgorithms.GetUpdateSatList(NmeaList.get(i));

            ParticleList.OutFfRegion(bs, pivot, pivot2);

            ParticleList.MessureSignalFromSats( bs,  allSats);

              ParticleList.ComputeWeight4KaminV0(allSats);
            ParticleList.ComputeWeight4KaminV1(allSats);

                  ParticleList.printWeights();

            ParticleList.sort();

             for(int x=0;x<10; x++)
                PointsInTIme.add(ParticleList.getParticleList().get(x).pos);

            Point3D tmp = ParticleList.GetBestParticle();
//              Point3D tmp = ParticleList.GetOptimalLocation(7);
            ans.add(tmp);
             PointInTime.add(i-2, ParticleList);

            ParticleList.Resample();

//               System.out.println(ParticleList.getParticleList().get(k).pos);
               String Particle_path2=Particle_path+i+".kml";
              KML_Generator.Generate_kml_from_ParticleList(ParticleList, Particle_path2,12);
//              KML_Point3D_List_Generator.Generate_kml_from_List(PointList,Particle_path2);
//              ParticleList.ComputeAndPrintErrors(path.get(i));
//
        }

        //     KML_Generator.generateKMLfromParticles(PointsInTIme, Particle_ans_path2, 20);
        KML_Generator.Generate_kml_from_List(ans, Particle_ans_path, false);
        System.out.println("end of program");


    }


    private static void NEMAFIlePF3() {



        String walls_file = "BoazMapJune8.kml";
        Particles ParticleList;
        Point3D pivot, pivot2;

        List<ActionFunction>  Actions;
        List<Building> bs = null;
        try {
            bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Number of buildings is " + bs.size());
        System.out.println("Number of particles is: "+ Particles.NumberOfParticles);


        String Particle_path = "KaminData\\CombineHeadong";
        String Particle_path3 = "KaminData\\Simulaton_routeTest_initial.kml";

        List<Particles> PointInTime=  new ArrayList<>();
        String Particle_ans_path = "NMEA_SmartPhones_Recordings\\Constructed_ABCD_Route_Weguht0.kml";
        String Particle_ans_path2 = "KaminData\\400ParticlesTry2.kml";


        ParticleList = new Particles();
        pivot = new Point3D(670053, 3551100, 1);
        pivot2 =  new Point3D(pivot);
        pivot2.offset(100, 100, 0);


        //   pivot = new Point3D(670003, 3551050, 1);
        // pivot2 =  new Point3D(pivot);
        //pivot2.offset(130, 130, 0);

        ParticleList.initParticlesWithHeading(pivot, pivot2);

        KML_Generator.Generate_kml_from_ParticleList(ParticleList, Particle_path3,5);


        NMEAProtocolParser parser = new NMEAProtocolParser();
        //   String NMEAFIlePath = "KaminData\\NMEAFiles\\routeABCDtwice11AM_NMEA.txt";
        // String NMEAFIlePath = "NmeaRecordings\\ABCD_Twice_2nd_Time.txt";
        String NMEAFIlePath = "NmeaRecordings\\2.txt";

        // String NMEAFIlePath = "NmeaRecordings\\Diagonal_A_C_D_B_A_Twice.txt";



        //   String NMEAFIlePath = "NMEA_SmartPhones_Recordings\\2.txt";


        List<NMEAPeriodicMeasurement> NmeaList = null;
        try {
            NmeaList = parser.parse(NMEAFIlePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //  String Recons_Path= "NmeaRecordings\\ABCD_Twice2_17_6COG_SOG_rec.kml";
        //   String Recons_Path2= "NmeaRecordings\\ABCD_Twice2_17_6from_NMEA.kml";

        //   UtilsAlgorithms.GenerateKMLfromCOG_SOS(NmeaList, Recons_Path);
        //  UtilsAlgorithms.GenerateKMLfromNMEA_List(NmeaList, Recons_Path2);

        int NumberOfSamples = NmeaList.size();
        List<Sat> allSats;
        List<Point3D> PointsInTIme = new ArrayList<>();
        List<Particles> finalList= new ArrayList<>();
        finalList.add(ParticleList);

        List<Point3D> ans = new ArrayList<Point3D>();
        int j=0;

        for(int i=2;i<NumberOfSamples; i++)
        {

            System.out.println("Compute position for TimeStamp # "+i);
            double COG = NmeaList.get(i).getCOG();
            double SOG = NmeaList.get(i).getSOG();
            double dt = NmeaList.get(i).getUtcTime()-NmeaList.get(i-1).getUtcTime();
            if(dt>=1)
                dt=1;
            //  System.out.println("SOG: "+ SOG+"  .  COG: "+COG+"  dt:  "+dt );
            // ParticleList.MoveParticlesBySOG_COGWithHeading(SOG, COG);
            ParticleList.MoveParticlesBySOG_COG(SOG, COG, dt);

            allSats = UtilsAlgorithms.GetUpdateSatList(NmeaList.get(i));

            ParticleList.OutFfRegion(bs, pivot, pivot2);

            ParticleList.MessureSignalFromSats( bs,  allSats);

            //  ParticleList.ComputeWeight4KaminV0(allSats);
            ParticleList.ComputeWeight4KaminV1(allSats);

            //      ParticleList.printWeights();

            ParticleList.sort();

            // for(int x=0;x<10; x++)
            //    PointsInTIme.add(ParticleList.getParticleList().get(x).pos);

            Point3D tmp = ParticleList.GetBestParticle();
            //  Point3D tmp = ParticleList.GetOptimalLocation(7);
            ans.add(tmp);
            // PointInTime.add(i-2, ParticleList);

            ParticleList.Resample();

            //   System.out.println(ParticleList.getParticleList().get(k).pos);
            //   String Particle_path2=Particle_path+i+".kml";
            //  KML_Generator.Generate_kml_from_ParticleList(ParticleList, Particle_path2,12);
            //  KML_Point3D_List_Generator.Generate_kml_from_List(PointList,Particle_path2);
            //  ParticleList.ComputeAndPrintErrors(path.get(i));

        }

        //     KML_Generator.generateKMLfromParticles(PointsInTIme, Particle_ans_path2, 20);
        //NMEAPeriodicMeasurement parse(List<String> NmeaREcords)
        // {}

        KML_Generator.Generate_kml_from_List(ans, Particle_ans_path, false);
        System.out.println("end of program");


    }

    private static void test2() {

        List<Point3D> ans = new ArrayList<Point3D>();
        Point3D tmp2 = new Point3D(670119, 3551134.6, 1);
        ans.add(tmp2);

        for (int i = 0; i < 60; i++) {
            Point2D tmp = Particles.randomPivotPoint(1, 0);
            double x = tmp.getX();
            double y = tmp.getY();
            tmp2.offset(x,y);
            tmp2 = new Point3D(tmp2);

            ans.add(tmp2);
        }
        for (int i = 0; i < 30; i++) {

            Point2D tmp = Particles.randomPivotPoint(1, 270);
            double x = tmp.getX();
            double y = tmp.getY();
            tmp2.offset(x,y);
            tmp2 = new Point3D(tmp2);
            ans.add(tmp2);
        }

        for (int i = 0; i < 60; i++) {
            Point2D tmp = Particles.randomPivotPoint(1, 180);
            double x = tmp.getX();
            double y = tmp.getY();
            tmp2.offset(x,y);
            tmp2 = new Point3D(tmp2);
            ans.add(tmp2);

        }
        for (int i = 0; i < 30; i++) {
            Point2D tmp = Particles.randomPivotPoint(1, 90);
            double x = tmp.getX();
            double y = tmp.getY();
            tmp2.offset(x,y);
            tmp2 = new Point3D(tmp2);
            ans.add(tmp2);

        }


        String Particle_ans_path = "KaminData\\ans_RouteTest.kml";
        KML_Generator.Generate_kml_from_List(ans, Particle_ans_path, false);


    }

    private static void NEMAFIlePF() {

        String walls_file = "BoazMapJune8.kml";
        Particles ParticleList;
        Point3D pivot, pivot2;

        List<ActionFunction>  Actions;
        List<Building> bs = null;
        try {
            bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Number of buildings is " + bs.size());
        System.out.println("Number of particles is: "+ Particles.NumberOfParticles);


        String Particle_path = "KaminData\\BayesianWeight900Par";
        String Particle_path3 = "KaminData\\Simulaton_routeTest_initial.kml";


        String Particle_ans_path = "KaminData\\August3.kml";

        ParticleList = new Particles();
        pivot = new Point3D(670053, 3551100, 1);
        pivot2 =  new Point3D(pivot);
        pivot2.offset(100, 100, 0);


        //   pivot = new Point3D(670003, 3551050, 1);
        // pivot2 =  new Point3D(pivot);
        //pivot2.offset(130, 130, 0);

        ParticleList.initParticles(pivot, pivot2);
        KML_Generator.Generate_kml_from_ParticleList(ParticleList, Particle_path3,5);

        NMEAProtocolParser parser = new NMEAProtocolParser();
        //String NMEAFIlePath = "KaminData\\NMEAFiles\\routeABCDtwice11AM_NMEA.txt";
        //  String NMEAFIlePath = "KaminData\\NMEAFiles\\route_ABCD_STM_1Hz_twice.txt";

        String NMEAFIlePath = "NmeaRecordings\\ABCD_Twice2_17_6.txt";
        //  String NMEAFIlePath = "NmeaRecordings\\ABCD_Twice_2nd_Time.txt";

        List<NMEAPeriodicMeasurement> NmeaList = null;
        try {
            NmeaList = parser.parse(NMEAFIlePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }



        int NumberOfSamples = NmeaList.size();
        List<Sat> allSats;




        List<Point3D> ans = new ArrayList<Point3D>();

        for(int i=2;i<NumberOfSamples; i++)
        {

            System.out.println("compute for timestamp "+i);
            double COG = NmeaList.get(i).getCOG();
            double SOG = NmeaList.get(i).getSOG();
            double dt = NmeaList.get(i).getUtcTime()-NmeaList.get(i-1).getUtcTime();
            if(dt>=1)
                dt=1;
            //     ParticleList.MoveParticlesBySOG_COGWithHeading(SOG, COG);
            ParticleList.MoveParticlesBySOG_COG(SOG, COG, dt);

            allSats = UtilsAlgorithms.GetUpdateSatList(NmeaList.get(i));
            ParticleList.OutFfRegion(bs, pivot, pivot2);

            ParticleList.MessureSignalFromSats( bs,  allSats);


            ParticleList.ComputeWeight4KaminV1(allSats);
            //      ParticleList.printWeights();
            Point3D tmp = ParticleList.GetBestParticle();
            ans.add(tmp);
            ParticleList.Resample();


        }

        KML_Generator.Generate_kml_from_List(ans, Particle_ans_path);
        System.out.println("end of program");


    }



    private static void NMEAParser() {

        NMEAProtocolParser parser = new NMEAProtocolParser();
        String NMEAFIlePath = "KaminData\\NMEAFiles\\routeABCDtwice11AM_NMEA.txt";


        List<NMEAPeriodicMeasurement> NmeaList = null;
        try {
            NmeaList = parser.parse(NMEAFIlePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Point3D> pointList = new ArrayList<>();
        for(int i=0; i<NmeaList.size();i++)
        {
            Point3D tmp= new Point3D(NmeaList.get(i).getLat(),NmeaList.get(i).getLon(),NmeaList.get(i).getAlt());
            // tmp = GeoUtils.convertLATLONtoUTM(tmp);
            pointList.add(tmp);

            List<Sat> allSat = UtilsAlgorithms.GetUpdateSatList(NmeaList.get(i));
            for(int j=0; j< allSat.size(); j++)

            {
                System.out.println("id: "+allSat.get(j).getSatID()+" Az: "+allSat.get(j).getAzimuth()+" El: "+allSat.get(j).getElevetion()+" SNR: "+allSat.get(j).getSingleSNR());
            }
            //   System.out.println("Timestamp "+ i);
            System.out.println();
            System.out.println();
            System.out.println("Time : "+NmeaList.get(i).getTime()+" cog: "+NmeaList.get(i).getCOG()+" Sog : "+NmeaList.get(i).getSOG());
        }

        String Path = "NMEA2ABCD_route.kml";
        KML_Generator.Generate_kml_from_List(pointList,Path);
        System.out.println("End of program");
    }

    public  static void simulationMain2() {
        //The filename of the buildings
        String walls_file = "Esri_v0.4.kml";
        //Variable for satellites
        List<Sat> allSats;
        //Variable for path
        List<Point3D> path;
        //Variable for Particles
        Particles ParticleList;
        //Variable for pivots
        Point3D pivot, pivot2;
        int CurrentGeneration;
        String Simulation_route_kml_path = "Simulaton__route_May_2016.kml";

        //Variable for Actions
        List<ActionFunction>  Actions;
        //Variable for Building
        List<Building> bs = null;
        try {
            //generate the buildings from their kml file (returned in utm)
            bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Number of buildings is " + bs.size());
        //Creating the initial path
        path = UtilsAlgorithms.createPath();
        //Creating satellites
        allSats = UtilsAlgorithms.createSatDataList();
        //A name string for the kml file that represented the original path
        String Simulation_route_3D_kml_path = "Simulaton__route_May_2016.kml";
        //A name string for the kml file that will represent each of the rounds of the Particles
        String Particle_path = "KaminData\\Simulaton_routeTest_FInal";
        //A name string for the kml file that will represent the initialization of the Particles
        String Particle_path3 = "KaminData\\Simulaton_routeTest_initial.kml";
        //Creating kml for the particles in their initialized form
        KML_Generator.Generate_kml_from_List(path, Simulation_route_3D_kml_path);

        //Initialization of the ParticleList
        ParticleList = new Particles();
        //The pivot represents the lower left point (in utm units) of the ParticleList
        pivot = new Point3D(670053, 3551100, 1);
        //pivot2 gets the values of pivot
        pivot2 =  new Point3D(pivot);
        //pivot2 shifts 100 units to the right and 100 relative units up
        pivot2.offset(100, 100, 0);
        LosData losData = new LosData( bs, path, allSats);

        //Particle initialization
        ParticleList.initParticles(pivot, pivot2);
        //Generate kml of the initialized particles
        KML_Generator.Generate_kml_from_ParticleList(ParticleList, Particle_path3,allSats.size());



        //Initialize Actions
        Actions = new ArrayList<ActionFunction>();
        System.out.println(path.size());
        for(int i=0;i<path.size()-1; i++)
        {
            ActionFunction tmp = new ActionFunction(path.get(i), path.get(i+1), 0 , 0,0);
            Actions.add(tmp);
        }
        //Initialize a list of points called ans that will hold the answer of the algorithm
        List<Point3D> ans = new ArrayList<Point3D>();
        for(int i=1;i<path.size()-1; i++)
        {

            System.out.println("compute for timestamp "+i);
            //Calculation of the OutFfRegion value for all particles
            ParticleList.OutFfRegion(bs, pivot, pivot2);
            //calculate the value of the LOS array for all particles
            ParticleList.MessureSignalFromSats( bs,  allSats);
            //Moving all the particles according to the appropriate Action
            ParticleList.MoveParticleWithError(Actions.get(i));
            //Calculate the weight for all the particles
            ParticleList.ComputeWeightsNoHistory(losData.getSatData(i));
            //resampling the particles
            ParticleList.Resample();

            //Choosing the best particle
            Point3D tmp = ParticleList.GetParticleWithMaxWeight();
            //Added it to the ans list
            ans.add(tmp);
            //Adjusting Particle_path2 to the current rotation
            String Particle_path2=Particle_path+i+".kml";
            //Generate the kml for the particles in the current rotation
            KML_Generator.Generate_kml_from_ParticleList(ParticleList, Particle_path2,allSats.size());
            //Calculate and print the error in the current rotation
            ParticleList.ComputeAndPrintErrors(path.get(i));

        }
        //Generate the kml of the answer from the ans list
        KML_Generator.Generate_kml_from_List(ans,"checkAns.kml");

    }
    public static void simulationMainWithML() throws Exception {
        // Path to KML and other required files
        String walls_file = "Esri_v0.4.kml";
        List<Sat> allSats;
        List<Point3D> path;
        Particles ParticleList;
        Point3D pivot, pivot2;

        List<ActionFunction> Actions;
        List<Building> bs = null;

        try {
            // Load buildings from KML file
            bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        path = UtilsAlgorithms.createPath();
        allSats = UtilsAlgorithms.createSatDataList();

        String Simulation_route_3D_kml_path = "Simulaton__route_May_2016.kml";
        String Particle_path = "KaminData\\Simulaton_routeTest_ML";
        String Particle_path3 = "KaminData\\Simulaton_routeTest_initial_ML.kml";

        // Generate initial KML file for route
        KML_Generator.Generate_kml_from_List(path, Simulation_route_3D_kml_path);

        ParticleList = new Particles();
        pivot = new Point3D(670053, 3551100, 1);
        pivot2 = new Point3D(pivot);
        pivot2.offset(100, 100, 0);

        LosData losData = new LosData(bs, path, allSats);
        ParticleList.initParticles(pivot, pivot2);
        KML_Generator.Generate_kml_from_ParticleList(ParticleList, Particle_path3, allSats.size());

        Actions = new ArrayList<>();

        for (int i = 0; i < path.size() - 1; i++) {
            ActionFunction tmp = new ActionFunction(path.get(i), path.get(i + 1), 0, 0, 0);
            Actions.add(tmp);
        }

        List<Point3D> ans = new ArrayList<>();
        Classifier classifier = null;

        try {
            classifier = (Classifier) SerializationHelper.read("los_nlos_model.model");
        } catch (Exception e) {
            e.printStackTrace();
            return; // Exit if the classifier couldn't be loaded
        }

        // Initialize the LOSPredictor with the classifier
        LOSPredictor predictor = new LOSPredictor(classifier);

        double overallErrorSum = 0;
        int iterations = 0;
        ParticleList.MessureSignalFromSats(bs,allSats);
        while (true) {
            ans.clear();  // Clear previous results for new iteration
            overallErrorSum = 0;
            double currentErrorSum = 0;  // Sum of errors for this iteration
            int errorCount = 0;  // Number of error computations

            for (int i = 1; i < path.size() - 1; i++) {
                System.out.println("Compute for timestamp " + i);
                ParticleList.OutFfRegion(bs, pivot, pivot2);
                // Load dataset
                // Generate dynamic dataset for each point in the path
                BufferedReader reader = new BufferedReader(new FileReader("src/ML_Los_Nlos_Classifier/satellite_data.arff"));

                Instances dataset = new Instances(reader);
                reader.close();
                dataset.setClassIndex(dataset.numAttributes() - 1);
                boolean[] losResults = predictor.predictLOS(allSats,ParticleList);  // Predict LOS/NLOS
                ParticleList.MessureSignalFromSatsWithML(bs, allSats, classifier, losResults, dataset);

                ParticleList.MoveParticleWithError(Actions.get(i));
                ParticleList.ComputeWeightsNoHistory(losData.getSatData(i));
                ParticleList.Resample();

                Point3D tmp = ParticleList.GetParticleWithMaxWeight();
                ans.add(tmp);

                // Compute error and accumulate
                double error = ParticleList.ComputeAndPrintErrors(path.get(i));
                overallErrorSum+=error;

            }

            double overallAverageError = overallErrorSum / (path.size() - 1);
            System.out.println("Average Error for this iteration: " + overallAverageError);
            if (overallAverageError <= 8) {
                break;
            }

        }

        // Generate final KML file after error condition is met
        KML_Generator.Generate_kml_from_List(ans, "checkAns_ML.kml");
    }

    public  static void oursimulationMain() {
// Path to the KML file containing building data
        String walls_file = "src/ParticleFilterSimulation/EsriBuildingsBursaNoindentWithBoazBuilding.kml";
        // Path to the KML file containing route points
        String routeFilePath = "routeABCDFabricated.kml";

        // Create a list of satellites
        List<Sat> allSats = UtilsAlgorithms.createSatDataList();
        System.out.println("the number of sats is: "+allSats.size());

        List<Point3D> path;
        path = BuildingsFactory.parseKML(routeFilePath);
        //..System.out.println(path);

        List<Building> bs = null;
        try {
            bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("the number of Building is: "+bs.size());


        System.out.println("start...");
        for (Point3D point : path) {
            // For each point, check LOS/NLOS with each satellite
            for (Sat sat : allSats) {
                boolean isLOS = LosAlgorithm.ComputeLos(point, bs, sat);
                System.out.println("Point: " + point + ", Satellite: " + sat.getElevetion() + ", LOS: " + isLOS);

            }
        }
        System.out.println("end");

    }









}

