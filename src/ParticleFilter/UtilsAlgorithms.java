package ParticleFilter;


import GNSS.Sat;

import Geometry.Point3D;
import dataStructres.NMEAPeriodicMeasurement;
import dataStructres.NMEASVMeasurement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Roi
 * Date: 12/05/14
 * Time: 15:30
 * To change this template use File | Settings | File Templates.
 */
public class UtilsAlgorithms {

    public static int SEED_RND = 54;
    public static final double Knots2m_s = 0.514444;
    public static final int SNRthreshold = 38;
    public static final double Weight_pow =10.2  ;
    public static final double AbsValue= 2.5;
    public static Random _rnd = new Random(SEED_RND);
    public static double nextRnd(double min, double max) {
        double d = _rnd.nextDouble();
        double dx = max-min;
        return min+dx*d;
    }
    public static double getVelocityGauusianError() {
        return VelocityGauusianError;
    }
    public static void setSEEDRND(int seed) {SEED_RND=seed; _rnd = new Random(seed);}
    public static void setVelocityGauusianError(double velocityGauusianError) {
        VelocityGauusianError = velocityGauusianError;
    }



    static double convertKnots2m_s(double speed)
    {
        return speed*Knots2m_s;
    }





    public static List<Sat> GetUpdateSatList(NMEAPeriodicMeasurement meas)
    {
        List<Sat> allsats = new ArrayList<>();
        List<Integer> allPRNs = meas.getAllPRNs();
        Map<Integer,? extends NMEASVMeasurement> mappedSvMeasurements = meas.getMappedSvMeasurements();
        for (Integer prn : allPRNs) {
            NMEASVMeasurement SVmeas = mappedSvMeasurements.get(prn);
            if (SVmeas != null) {
                Sat tmp = new Sat(SVmeas.getAz(), SVmeas.getEl(), SVmeas.getPrn(), SVmeas.getSnr());
                allsats.add(tmp);
            }
        }
        return allsats;
    }



    public  static double VelocityGauusianError=1;

    public static double getVelocityHeadingError() {
        return VelocityHeadingError;
    }


    public static void setVelocityHeadingError(double velocityHeadingError) {
        VelocityHeadingError = velocityHeadingError;
    }

    public static List<Point3D> sample(List<Point3D> route, double factor) {
        List<Point3D> ans = new ArrayList<Point3D>();
        Point3D p0,p1=null;
        for(int i=0;i<route.size()-1;i++) {
            p0 = route.get(i);
            p1 = route.get(i + 1);
            double dist = p0.distance(p1);
            Point3D curr = new Point3D(p0);
            double steps = dist/factor;
            double dx = p1.getX()-p0.getX(), dy = p1.getY()-p0.getY(), dz = p1.getZ()-p0.getZ();
            double sx = dx/steps, sy = dy/steps, sz = dz/steps;
            for(double s=0;s<steps;s++) {
                ans.add(new Point3D(curr));
                curr.offset(sx, sy, sz);
            }
        }
        ans.add(new Point3D(p1));
        return ans;
    }

    public static List<Sat> createSatDataList()
    {
        /// data as recorded from the Galaxy note on 27.8.2012 next to the kcg lab
        List<Sat> sat_data2;
        sat_data2=new ArrayList<Sat>();
        Sat tmp;
        tmp=new Sat(0,90,0);             //PRN 2
        sat_data2.add(tmp);
//        tmp=new Sat( 130, 45,1);      //PRN 4
//        sat_data2.add(tmp);
//
//        tmp=new Sat(163,9,2);//PRN 9
//        sat_data2.add(tmp);
//
//        tmp=new Sat(118,9,3) ;  //PRN 12
//        sat_data2.add(tmp);
//
//        tmp=new Sat(40,21,4); //PRN 14
//        sat_data2.add(tmp);
//
//        tmp=new Sat(148,26,5);                //PRN 15
//        sat_data2.add(tmp);
//
//        tmp=new Sat(6,67,6); //PRN  17
//        sat_data2.add(tmp);
//        tmp=new Sat(7,325,7);//PRN 25
//        sat_data2.add(tmp);
//
//        tmp=new Sat(8,304,8);//PRN 26         was 180
//        sat_data2.add(tmp);
//
//        tmp=new Sat(9,340,9);         //PRN 27
//        sat_data2.add(tmp);
//
//        tmp=new Sat(10,232,10); //PRN 28
//
//       sat_data2.add(tmp);
//

///// Measurements from the city of Ariel//
        tmp=new Sat(31, 10, 6); // PRN 30
        sat_data2.add(tmp);
        tmp=new Sat(85, 37, 7); // PRN 31
        sat_data2.add(tmp);
        tmp=new Sat(42, 6, 8); // PRN 32
        sat_data2.add(tmp);
        tmp=new Sat(127, 35, 9); // PRN 33
        sat_data2.add(tmp);
        tmp=new Sat(226, 50, 10); // PRN 34
        sat_data2.add(tmp);











// ////////////////////\
//        ////////////GLONASS sats  /////////////
//        //////////////////////////////////
//
//        tmp=new Sat(11,125,11);         //   PRN 73
//        sat_data2.add(tmp);
//        tmp=new Sat(12,96,12);          //PRN 74
//        sat_data2.add(tmp);
//        tmp=new Sat(13,348,13);                       //PRN 75
//        sat_data2.add(tmp);
//        tmp=new Sat(14,30,14);                   //PRN 84
//        sat_data2.add(tmp);
//
//        tmp=new Sat(15,209,15);                //PRN  85
//        sat_data2.add(tmp);
//        tmp=new Sat(16,210,16);                                //PRN 86
//        sat_data2.add(tmp);
//
//        ////////////////////////////////////\
//        ////////////IMAGINARY sats  /////////////
//        //////////////////////////////////
//        tmp=new Sat(17,271,17);
//        sat_data2.add(tmp);
//        tmp=new Sat(18,281,18);
//        sat_data2.add(tmp);
//             tmp=new Sat(19,291,19);
//sat_data2.add(tmp);
//
//
//tmp=new Sat(20,301,20);
//sat_data2.add(tmp);
//tmp=new Sat(21,311,21);
//sat_data2.add(tmp);
//tmp=new Sat(22,321,22);
//sat_data2.add(tmp);
//tmp=new Sat(23,331,23);
//sat_data2.add(tmp);
//tmp=new Sat(24,341,24);
//sat_data2.add(tmp);
//tmp=new Sat(25,351,25);
//sat_data2.add(tmp);
//tmp=new Sat(26,351,26);
//sat_data2.add(tmp);
//tmp=new Sat(27,341,27);
//sat_data2.add(tmp);
//tmp=new Sat(28,331,28);
//sat_data2.add(tmp);
//tmp=new Sat(29,321,29);
//sat_data2.add(tmp);
//tmp=new Sat(30,311,30);
//sat_data2.add(tmp);
//tmp=new Sat(31,301,31);
//sat_data2.add(tmp);
//tmp=new Sat(32,291,32);
//sat_data2.add(tmp);
//tmp=new Sat(33,281,33);
//sat_data2.add(tmp);
//tmp=new Sat(34,271,34);
//sat_data2.add(tmp);
//tmp=new Sat(35,261,35);
//sat_data2.add(tmp);
//tmp=new Sat(36,251,36);
//sat_data2.add(tmp);
//tmp=new Sat(37,241,37);
//sat_data2.add(tmp);
//tmp=new Sat(38,231,38);
//sat_data2.add(tmp);
//tmp=new Sat(39,221,39);
//sat_data2.add(tmp);
//tmp=new Sat(40,211,40);
//sat_data2.add(tmp);
//tmp=new Sat(41,201,41);
//sat_data2.add(tmp);
//
//
//tmp=new Sat(42,11,42);
//sat_data2.add(tmp);
//tmp=new Sat(43,21,43);
//sat_data2.add(tmp);
//tmp=new Sat(44,31,44);
//sat_data2.add(tmp);
//tmp=new Sat(45,41,45);
//sat_data2.add(tmp);
//tmp=new Sat(46,51,46);
//sat_data2.add(tmp);
//tmp=new Sat(47,61,47);
//sat_data2.add(tmp);
//tmp=new Sat(48,71,48);
//sat_data2.add(tmp);
//tmp=new Sat(49,81,49);
//sat_data2.add(tmp);
//tmp=new Sat(50,90,50);
//sat_data2.add(tmp);
//tmp=new Sat(51,101,51);
//sat_data2.add(tmp);
//tmp=new Sat(52,121,52);
//sat_data2.add(tmp);
//tmp=new Sat(53,131,53);
//sat_data2.add(tmp);
//tmp=new Sat(54,141,54);
//sat_data2.add(tmp);
//tmp=new Sat(55,151,55);
//sat_data2.add(tmp);
//tmp=new Sat(56,161,56);
//sat_data2.add(tmp);
//tmp=new Sat(57,171,57);
//sat_data2.add(tmp);
//tmp=new Sat(58,181,58);
//sat_data2.add(tmp);
//tmp=new Sat(59,191,59);
//sat_data2.add(tmp);

        return sat_data2;
    }


    public static double VelocityHeadingError=0.5;

    public static List<Point3D> createPath() {
        Point3D p1 = new Point3D(670103.5, 3551179.5, 1);
        Point3D p2 = new Point3D(670128.5, 3551179.5, 1);
        Point3D p3 = new Point3D(670128.5, 3551124.5, 1);
        Point3D p4 = new Point3D(670103.5, 3551124.5, 1);
        List<Point3D> route = new ArrayList<Point3D>();
        route.add(p1);
        route.add(p2);
        route.add(p3);
        route.add(p4);
        route.add(p1);
        List<Point3D> path = sample(route, 1); // the factor is represet of the velocity
        return path;
    }


    public static Boolean[] GetCurrentLosState(NMEAPeriodicMeasurement SatData, List<Sat> Allsat) {

        int satID;
        Boolean[] LosVector = new Boolean[Allsat.size()];
        for (int i = 0; i < Allsat.size(); i++) {

            satID = Allsat.get(i).getSatID();
            NMEASVMeasurement tmpSat = SatData.getSvMeasurement(satID);
            tmpSat.ComputeNaiveLOSWithTHreshold(SNRthreshold, 3);
         //   LosVector[i] = tmpSat.getLOS();


        }

        return LosVector;
    }

    public static ActionFunction getActionFromNMEA(NMEAPeriodicMeasurement nmeaPeriodicMeasurement) {

        double COG = nmeaPeriodicMeasurement.getSOG();
        double SOG = nmeaPeriodicMeasurement.getCOG();
        ActionFunction action = new ActionFunction(0,0,0);
        return action;
    }

    public static void PrintSatList(List<Sat> allSat) {
        for(int i=0; i< allSat.size(); i++)
            System.out.println("PRN: "+allSat.get(i).getSatID()+". SNR: "+allSat.get(i).getSingleSNR()+". Az: "+allSat.get(i).getAzimuth()+". El: "+allSat.get(i).getElevetion());
        System.out.println();
        System.out.println();
    }


    public static void GenerateKMLfromCOG_SOS(List<NMEAPeriodicMeasurement> nmeaList, String recons_path) {
        List<Point3D> points = new ArrayList<>();

        Point3D tmp = new Point3D(670109, 3551135, 1.5);
        points.add(tmp);
        for(int i=2; i<nmeaList.size(); i++)
        {
            double COG = nmeaList.get(i).getCOG();
            double SOG = nmeaList.get(i).getSOG();
            SOG = UtilsAlgorithms.convertKnots2m_s(SOG);
            double dt = nmeaList.get(i).getUtcTime()  - nmeaList.get(i-1).getUtcTime();
            if(dt>1)
                dt=1;
            System.out.println("dT is: "+dt+" .SOG is :"+ SOG);
            double dx = SOG*dt*Math.sin(Math.toRadians(COG));
            double dy = SOG*dt*Math.cos(Math.toRadians(COG));
            tmp.offset(dx, dy,0);
            tmp = new Point3D(tmp);
            points.add(tmp);

        }
//        KML_Generator.Generate_kml_from_List(points, recons_path, false);



    }

    public static void GenerateKMLfromNMEA_List(List<NMEAPeriodicMeasurement> nmeaList, String recons_path2) {
        List<Point3D> ans = new ArrayList<>();
        for (int i=1; i< nmeaList.size(); i++)
        {
            double lat = nmeaList.get(i).getLat();
            double lon = nmeaList.get(i).getLon();
            Point3D tmp = new Point3D(lat, lon, 1);
            ans.add(tmp);
        }
//        KML_Generator.Generate_kml_from_List(ans, recons_path2, true);
    }
}
