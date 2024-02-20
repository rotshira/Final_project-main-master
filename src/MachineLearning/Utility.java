package MachineLearning;

import Algorithm.LosAlgorithm;
import GNSS.Sat;
import Geometry.Building;
import Geometry.BuildingsFactory;
import Geometry.Point3D;
import Parsing.SVM.SVMCSVWriter;
import Parsing.sirf.SirfProtocolParser;
import dataStructres.SVMachineLearningData;
import dataStructres.SirfPeriodicMeasurement;
import dataStructres.SirfSVMeasurement;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Roi
 * Date: 23/03/14
 * Time: 17:16
 * To change this template use File | Settings | File Templates.
 */
public class Utility {
    static boolean UserIsStationary = true;
    static boolean ComputeLosViaCamera = false;
    static String sirfFilePath = "POINT_A_STATIONARY.txt";
    static String SVM_File = "Point_A_SVM.csv";


    public static final int FirstElementInList = 10;

    public static void main(String[] args) {

        Point3D userStationaryPosUtmCord = new Point3D(0, 0, 0);
        List<SirfPeriodicMeasurement> me = null;
        try {
            me = SirfProtocolParser.parseFile(sirfFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Integer> parsedPrnsSorted = SirfProtocolParser.getParsedPrnsSorted();
        try {
            List<SirfPeriodicMeasurement> me2 = SirfProtocolParser.GetHistoryValuesforSVM2(me, parsedPrnsSorted);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //      char ch = (char) System.in.read();

        List<SVMachineLearningData> SVM_Data = SetSVMDatafromList(me, parsedPrnsSorted);
        for (int i = 0; i < SVM_Data.size(); i++)
            SVM_Data.get(i).PrintData();

        System.out.println("End of SVM database creation");
        try {
            SVMCSVWriter.printToFile(SVM_Data, SVM_File);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Get the KML 3d buildings
        String walls_file = "";
        List<Building> bs = null;
        try {
            bs = BuildingsFactory.generateUTMBuildingListfromKMLfile(walls_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Number of buildings is " + bs.size());

        List<Sat> allSats = new ArrayList<Sat>();
        int i = 0;
        //   this itarate for each second of the
        //Only good for known stationary user position
        if (UserIsStationary && !ComputeLosViaCamera) {
            for (SirfPeriodicMeasurement SirfTimeStamp : me) {

                //  Point3D UserLoc = new Point3D(SirfTimeStamp.getLat(), SirfTimeStamp.getLon(), 1.8);
                //THis for iterate for each PRN and update the location (Azimuth, Elevation) of each Sattelite in a list (allsats)  .
                for (Integer PRN : parsedPrnsSorted) {


                    SirfSVMeasurement SV = SirfTimeStamp.getSatellites().get(PRN);
                    Sat tmp = new Sat(SV.getAzimuth(), SV.getElevation(), PRN);
                    Boolean Los = LosAlgorithm.ComputeLos(userStationaryPosUtmCord, bs,tmp);
                    SirfTimeStamp.getSatellites().get(PRN).setLOS(Los);
                    // System.out.println("For timestamp 0 PRN " + PRN + " is labled as " + Los);

                }
            }
        }// end of if(UserIsstatronary)

        if(ComputeLosViaCamera)
        {

        }

        System.out.println("end of main");
        //  Parsing.SirfCsvWriter.printToFile(me, "c://Parsing//ggg.csv");


    }






    public static List<SVMachineLearningData> SetSVMDatafromList(List<SirfPeriodicMeasurement> me, List<Integer> ParsedPrn)
    {
        List<SVMachineLearningData> SVM_Data = new ArrayList<SVMachineLearningData>();
        double Hdop;
        SVMachineLearningData tmp;
        for(int i = FirstElementInList; i<me.size(); i++)

        {

            Hdop = me.get(i).getHdop();
            for( Integer PRN: ParsedPrn)
            {
                SirfSVMeasurement SV = me.get(i).getSatellites().get(PRN);
                if(SV!=null&&SV.IsMaxOldCn0GreaterThanZero())
                {
                    tmp = new SVMachineLearningData(SV);
                    tmp.setHdop(Hdop);
                    tmp.setGNSS_Type(PRN);
                    SVM_Data.add(tmp);
                }
            }
        }
        System.out.println("Done creating an SVM database");
        return SVM_Data;
    } //end of   SetSVMDatafromList
}