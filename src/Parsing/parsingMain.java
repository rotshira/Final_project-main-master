package Parsing;

import Algorithm.LosAlgorithm;
import Algorithm.PseudoRangeComp;
import GNSS.Sat;
import Geometry.Building;
import Geometry.BuildingsFactory;
import Geometry.Point3D;
import Parsing.sirf.SirfCsvWriter;
import Parsing.sirf.SirfMLCsvWriter;
import Parsing.sirf.SirfProtocolParser;
import Parsing.stm.STMProtocolParser;
import dataStructres.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Roi on 22/02/2015.
 */
public class parsingMain {

    public static void main(String[] args) throws Exception {

     // MovingRecording();
      //  SirfParseForGpsSwitch();
      // SirfParsingML();
      //  TestLosNlosAlgorithm();
        //PseudoRangeCompute();
    /*    try {
            TestLosNlosAlgorithm();
        } catch (Exception e) {
            e.printStackTrace();

        }


*/

    String SirfFIle = "pointA_11_AM.gps";
        String OutputFile = "ParsedPointA_11_AM";
          ParseSirfFileToCsv(SirfFIle, OutputFile);
    }

    private static void ParseSirfFileToCsv(String SirfFilePath, String OutputFile)
    {
        SirfProtocolParser parser = new SirfProtocolParser();
        try {
            List<SirfPeriodicMeasurement> sirfMeas = parser.parseFile(SirfFilePath);
            System.out.println(sirfMeas.size());
            //SirfMLCsvWriter.printToFile4GpsSwitchProject(sirfMeas, OutputFile);
            SirfCsvWriter.printToFile(sirfMeas, OutputFile);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void SirfParseForGpsSwitch()
    {

        SirfProtocolParser parser = new SirfProtocolParser();
        String SirfFilePath = "GPSSwitch3AntennaBuilding5.gps";
        String OutputFile  = "ParsedExpriment9_6_2015";
        try {
            List<SirfPeriodicMeasurement> sirfMeas = parser.parseFile(SirfFilePath);
            System.out.println(sirfMeas.size());
            SirfMLCsvWriter.printToFile4GpsSwitchProject(sirfMeas, OutputFile);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void MovingRecording() {

        SirfProtocolParser parser = new SirfProtocolParser();
        List<Integer> Stamps=computeIndex();
        Point3D[] receiverPosition = new Point3D[4];
        receiverPosition[0] = new Point3D(670114.15, 3551135.3, 1.8); //according to Boaz file bursa-a-d.kml point a
        receiverPosition[1] = new Point3D(670126.5, 3551136.25, 1.8); //according to Boaz file bursa-a-d.kml point b
        receiverPosition[2] = new Point3D(670123.4, 3551171.47, 1.8); //according to Boaz file bursa-a-d.kml point c
        receiverPosition[3] =  new Point3D(670111.6, 3551170.62, 1.8); //according to Boaz file bursa-a-d.km point d
        String routePath = "routeABCDFabricated.kml";
        Integer[] Times={0,11,16,50,50,58,59,94,94,106,106,142,142,151,151,186};
        Integer[] timeSum={11, 34, 8, 35, 12, 36,11, 37};
        BuildingsFactory fact = new BuildingsFactory();
        String buildingFilePath = "EsriBuildingsBursaNoindentWithBoazBuilding.kml";

        List<Building> buildings1 = null;
        try {
            buildings1 = fact.generateUTMBuildingListfromKMLfile(buildingFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Point3D> route = squarePathReconstruction(receiverPosition, timeSum);
        String OutputFile = "routeABCD_Parsed";
        String SirfFilePath = "route_abcd_twice.gps";
        try {
            List<SirfPeriodicMeasurement> sirfMeas = parser.parseFile(SirfFilePath);
            System.out.println("Number of Instncec : "+ sirfMeas.size());

            sirfMeas.get(0).computeCorrectPseudoRangeForAllSats();
            sirfMeas.get(1).computeCorrectPseudoRangeForAllSats();
            sirfMeas.get(1).setExtremeSnrValuesForAllSats();
            for (int i = 2; i < sirfMeas.size(); i++) {
                sirfMeas.get(i).computeCorrectPseudoRangeForAllSats();
                sirfMeas.get(i).computePseudoRangeResidualsForAllSats();
                sirfMeas.get(i).computePreviousValues(sirfMeas.get(i - 1), sirfMeas.get(i - 2));

            }
            for(int i=0; i<sirfMeas.size();i++)
                System.out.println(i+ ")"+ sirfMeas.get(i).getCourse());
            int j=0;
            System.out.println("ROute is "+ route.size());
            System.out.println("Stamps is "+ Stamps.size());
            System.out.println("Meas is "+ sirfMeas.size());
            int i=0;
            for( j=0; j< route.size(); j++)
            {


                parser.ComputeLosNlosForSingleTimeStamp(sirfMeas.get(Stamps.get(j)),buildings1, route.get(j));
            }
            System.out.println("Start writing file number ");
            SirfMLCsvWriter.printToFileSpecificValues(sirfMeas, OutputFile, 6);
            System.out.println("Finshed writing file number ");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static List<Integer> computeIndex() {
        Integer[] Times={0,11,16,50,50,58,59,94,94,106,106,142,142,151,151,186};
        Integer[] timeSum={11, 34, 8, 35, 12, 36,11, 37};
        int buffer=6;
        int tmp;
       List<Integer> Stamps = new ArrayList<>();
        for(int i=0; i<184; i++)
        {

                if(i<11)
                    tmp=i+buffer;
                else
                tmp=i+3+buffer;
                Stamps.add(tmp);
        }
        return Stamps;



    }

    private static void TestLosNlosAlgorithm() throws Exception {

        String BuildingPath = "SingleWall2.kml";
        System.out.println("The program begins");
        BuildingsFactory fact = new BuildingsFactory();
        List<Building> buildings1 = null;

        buildings1 = fact.generateUTMBuildingListfromKMLfile(BuildingPath);
        System.out.println("Number of Buildings is " + buildings1.size());

        Point3D tmpPointInUTM =  new Point3D(670123.4, 3551171.47, 4);
        Sat tmpSat = new Sat(248, 79, 1);
        String KmlFilePath ="test.kml";
        SirfSVMeasurement sirfMeas= new SirfSVMeasurement();

//        KMLgenerator.generateSatLinesFromSat(tmpSat, tmpPointInUTM, KmlFilePath);

            boolean los = LosAlgorithm.ComputeLos(tmpPointInUTM, buildings1, tmpSat);
            System.out.println("Azimut:" + tmpSat.getAzimuth() + ". Elev:" + tmpSat.getElevetion() + " status of computation is " + los);

            //tmpSat.setElevetion(tmpSat.getElevetion()+5);



    }


    public static void SirfParsingML()  {

        String[] SirfFilePath={"POINT_A_STATIONARY.txt","POINT_B_STATIONARY.txt","POINT_C_STATIONARY.txt","POINT_D_STATIONARY.txt"};

       /* String SirfFilePathA = ;
        String SirfFilePathC = "POINT_C_STATIONARY.txt";
        String SirfFilePathD = "POINT_D_STATIONARY.txt";
        String SirfFileRouteABCD  = "route_abcd_twice.txt";
*/


        String[] OutputFile ={"PointA_FilterNoFirst30Good","PointB_FilterNoFirst30Good","PointC_FilterNoFirst30Good","PointD_FilterNoFirst30Good "};
       // String[] OutputFile ={"PointA_Filter","PointB_Filter","PointC_Filter","PointD_Filter "};


        String outputFileRouteSirf = "route_abcd_twice_ML_ClassificationWrong.txt";

        String buildingFilePath = "EsriBuildingsBursaNoindentWithBoazBuilding.kml";
        System.out.println("The program begins");

        Point3D[] receiverPosition = new Point3D[4];
        receiverPosition[0] = new Point3D(670114.15, 3551135.3, 1.8); //according to Boaz file bursa-a-d.kml point a
        receiverPosition[1] = new Point3D(670126.5, 3551136.25, 1.8); //according to Boaz file bursa-a-d.kml point b
        receiverPosition[2] = new Point3D(670123.4, 3551171.47, 1.8); //according to Boaz file bursa-a-d.kml point c
        receiverPosition[3] =  new Point3D(670111.6, 3551170.62, 1.8); //according to Boaz file bursa-a-d.km point d

        BuildingsFactory fact = new BuildingsFactory();

        List<Building> buildings1 = null;
        try {
            buildings1 = fact.generateUTMBuildingListfromKMLfile(buildingFilePath);
            System.out.println(buildings1.size());
            String KmlFilePath = "pointC.kml";
            SirfProtocolParser parser = new SirfProtocolParser();
            for(int cnt=0; cnt<4; cnt++) {
                List<SirfPeriodicMeasurement> sirfMeas = parser.parseFile(SirfFilePath[cnt]);
              //  KMLgenerator.generateSatLinesFromSirfSvMesserment(sirfMeas, receiverPosition[2], KmlFilePath);


                sirfMeas.get(0).computeCorrectPseudoRangeForAllSats();
                sirfMeas.get(1).computeCorrectPseudoRangeForAllSats();
                sirfMeas.get(1).setExtremeSnrValuesForAllSats();
                for (int i = 2; i < sirfMeas.size(); i++) {
                    sirfMeas.get(i).computeCorrectPseudoRangeForAllSats();
                    sirfMeas.get(i).computePseudoRangeResidualsForAllSats();
                    sirfMeas.get(i).computePreviousValues(sirfMeas.get(i - 1), sirfMeas.get(i - 2));

                }
                parser.ComputeLosNLOSFromStaticPoint(sirfMeas, buildings1, receiverPosition[cnt]);
                System.out.println("Start writing file number "+ cnt);
                SirfMLCsvWriter.printToFileSpecificValues(sirfMeas, OutputFile[cnt], 30);
                System.out.println("Finshed writing file number "+ cnt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    public static List<Point3D> squarePathReconstruction(Point3D[] square, Integer[] timeSum)

    {

        List<Point3D> Points = new ArrayList<>();
        List<Point3D> tmpList;
        Integer CurrentLenghtInSec;
        for(int i=0; i<timeSum.length; i++)
        {
            CurrentLenghtInSec = timeSum[i];
            tmpList = GeneratePointsFromLine(square[i%4], square[(i+1)%4], CurrentLenghtInSec);
            Points.addAll(tmpList);

        }
        return Points;
    }

   public static double calcAngle(Point3D p1,Point3D p2) {
        return Math.atan2(p2.getY()-p1.getY(),p2.getX()-p1.getX())*180.0/Math.PI;
    }
    private static List<Point3D> GeneratePointsFromLine(Point3D p1, Point3D p2, Integer currentLenghtInSec) {

        double dist = p1.distance2D(p2);
        double angle = p1.angle2D(p2);
        double angle2=calcAngle(p1,p2);
        double quants = dist/currentLenghtInSec;
        List<Point3D> ans= new ArrayList<>();
        for(int i=0; i<currentLenghtInSec; i++)
        {

            double dx=i*quants*Math.cos(Math.toRadians(angle2));
            double dy = i*quants*Math.sin(Math.toRadians(angle2));
            Point3D tmp = new Point3D(p1.getX()+dx, p1.getY()+dy, p1.getZ());
            ans.add(tmp);
        }
        return ans;
    }

    public static void PseudoRangeCompute()
    {
        String StmFilePath = "route_ABCD_STM_1Hz_twice.txt";
        STMProtocolParser stmParser = new STMProtocolParser();
        List<STMPeriodMeasurment> stmMeas = null;
        try {
            stmMeas = stmParser.parse(StmFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //todo roi: learn this code.
      stmMeas = stmParser.ClearBadSattelitesOnlyGps(stmMeas);

        for (STMPeriodMeasurment meas : stmMeas) {
            Map<Integer, STMSVMeasurement> mappedSvMeasurements = meas.getMappedSvMeasurements();
            for (Integer key : mappedSvMeasurements.keySet()) {
                STMSVMeasurement stmSvMeasurement = mappedSvMeasurements.get(key);

            }
        }

        System.out.println(" number of meas is " + stmMeas.size());

            PseudoRangeComp.computePseudoRangeFromStmMessurment(stmMeas.get(1));



        System.out.println(" end of main");




    }

    private static void main2() throws IOException, ParseException {
        //String sirfFilePath  = "POINT_A_STATIONARY.txt";
        String nmeaDilePath = "";
        String StmFilePath = "route_ABCD_STM_1Hz_twice.txt";

        // String sirfCsvFilePath  = "PointA_ParsedCsv.csv";
        String nmeaCsvFilePath = "";
        String StmCsvFilePath = "route_ABCD_STM_1Hz_twice";


        STMProtocolParser stmParser = new STMProtocolParser();
        List<STMPeriodMeasurment> stmMeas = stmParser.parse(StmFilePath);
        //todo roi: learn this code.
        for (STMPeriodMeasurment meas : stmMeas){
            Map<Integer, STMSVMeasurement> mappedSvMeasurements =meas.getMappedSvMeasurements();
            for (Integer key : mappedSvMeasurements.keySet()){
                STMSVMeasurement stmSvMeasurement =mappedSvMeasurements.get(key);
                double correctedPR = stmSvMeasurement.getCorrectedPR();
                // System.out.println(key + "\t " + correctedPR);
            }
        }
      //  STMcsvWriter.printStmToFile(stmMeas, StmCsvFilePath );
        // NMEAProtocolParser nmeaParser  = new NMEAProtocolParser();
        // List<NMEAPeriodicMeasurement>  nmeaMeas =  nmeaParser.parse(nmeaDilePath);

        //  SirfProtocolParser sirfParser = new SirfProtocolParser();
        //  List<SirfPeriodicMeasurement> sirfMeas  = sirfParser.parseFile(sirfFilePath);
        //  SirfCsvWriter.printToFile(sirfMeas, sirfCsvFilePath);
        System.out.println("Parsed O.k");


    }
}
