package Jamming;

import Geometry.Point2D;
import Geometry.Point3D;
import Utils.GeoUtils;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roi on 9/23/2016.
 */
public class TestMain {
    static final long UTC_Start_Time_First_Jam_Record = 1857319;
    static final long UTC_End_Time_First_Jam_Record = 1857689;
    public static void main(String[] args) throws IOException {


        testCord();
        testCord2();
        final File folder = new File("src/Jamming/gps_log_1");
        parse2018(0,100, folder);
//        Parse("UTC_Start_Time_First_Jam_Record, UTC_End_Time_First_Jam_Record");
//        Parse3(0,0 ,"src/Jamming/gps_log_15/nmea_2018_03_25_13_23_39_log.csv", "src/Jamming/gps_log_15/nmea_2018_03_25_13_23_39_log.csv","15");

    }

    private static void testCord() {


        System.out.println("Data for Ariel Experiment");
        double minLat =32.10236;
        double minLon = 35.206606;

        Point3D tmp = GeoUtils.convertLATLONtoUTM(new Point3D(minLat,minLon, 0));
        System.out.println(tmp);

        double maxLat = 32.104638;
        double maxLong =35.211922;
        Point3D tmp2 = GeoUtils.convertLATLONtoUTM(new Point3D(maxLat,maxLong, 0));
        System.out.println(tmp2);
        System.out.println(tmp.getX()- tmp2.getX());
        System.out.println(tmp.getY()-tmp2.getY());
        System.out.println();
//

    }
    private static void testCord2() {

        System.out.println("Data for South experiment");
        double minLat = 31.161083;
        double minLon = 34.530456;

        Point3D tmp = GeoUtils.convertLATLONtoUTM(new Point3D(minLat,minLon, 0));
        System.out.println(tmp);

        double maxLat = 31.164944;
        double maxLong =34.536903
                ;
        Point3D tmp2 = GeoUtils.convertLATLONtoUTM(new Point3D(maxLat,maxLong, 0));
        System.out.println(tmp2);
        System.out.println(tmp.getX()- tmp2.getX());
        System.out.println(tmp.getY()-tmp2.getY());



    }

    public static List<RealClient> getClientsAtTimestamp(List<RealClientList> list, long UtcTime)
    {
        List<RealClient> newList = new ArrayList<RealClient>();
        for(RealClientList client : list)
        {
            RealClient tmp = client.getClinetTimeStampByUTC_time(UtcTime);
            if(tmp!=null)
                newList.add(tmp);
        }
        if(newList.size()>0)
            return newList;
        return null;
    }

    public static List<RealClientList> Parse(long initialTIme, long FinishTime, String fileName)  throws IOException {

        System.out.println("Parsing..");
        List<RealClientList> listClient = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        int flag=0;
        String tmpDeviceId="";
        line = br.readLine();
        RealClientList tmpList= new RealClientList();
        while ((line = br.readLine()) != null) {
            String[] array = line.split(",");
            int j=0;
            String deviceIdString= (array[j++]);
            int deviceId = Integer.parseInt(array[j++]);
            double time = Double.parseDouble(array[j++]);
            double lat = Double.parseDouble(array[j++]);
            double lon = Double.parseDouble(array[j++]);
            double cog = Double.parseDouble(array[j++]);
            double sog = Double.parseDouble(array[j++]);
            double maxSnrGPS = Double.parseDouble(array[j++]);
            int numberOfGPS_sats = Integer.parseInt(array[j++]);
            double maxSnrGLNS = Double.parseDouble(array[j++]);
            int numberOfGLNS_sats = Integer.parseInt(array[j++]);
            time = time/1000;
            if(time>=initialTIme && time<=FinishTime)
            {
                if(!deviceIdString.equals(tmpDeviceId) && tmpList.empty==1) {
                    tmpList.setDeviceID(deviceIdString);
                  //  System.out.println(deviceId);
                    tmpDeviceId = deviceIdString;
                }
                if(!tmpDeviceId.equals(deviceIdString))
                {
                    listClient.add(tmpList);
                    System.out.println(tmpDeviceId+ " "+deviceIdString+"veververrverf ");
                    tmpList = new RealClientList(deviceIdString);
                    tmpDeviceId = deviceIdString;

                }
                RealClient tmp = new RealClient(time, lat, lon, cog, sog, maxSnrGPS,numberOfGPS_sats, maxSnrGLNS, numberOfGLNS_sats,deviceId);
                System.out.printf("dexp: %.0f", time );
                System.out.println(" . Device ID "+ tmpDeviceId +"lat : " + lat+ ". Long : "+lon );


                tmpList.Clients.add(tmp);


//                }

            }



        }
        listClient.add(tmpList);
        System.out.println("Done Parsing.");
        System.out.println("Number of instance "+listClient.size());
        for(int i=0;i<listClient.size();i++)
        {
            System.out.print(i+" device num "+"  "+listClient.get(i).deviceIdString+" "  +listClient.get(i).Clients.size());
            int j = listClient.get(i).Clients.size();
            System.out.println(" Start time:"+listClient.get(i).Clients.get(0).time+ "End time "+listClient.get(i).Clients.get(j-1).time);
            //System.out.println();
        }
        return listClient;
    }

    public static RealClientList Parse3(long initialTIme, long FinishTime, String gnssFile, String LocationFile, String deviceName) throws IOException {

        //the location file contains less data. The gnss status is always bigger
        //the gnss status is like this : system_time,#gps,#glonass,maxGPS_Cn0,maxGLONASS_Cn0,isGPSfix
        //the location file is like this : system_time,gps_time,latitude,longtide,altitude,sog,cog,accuracy

        System.out.println("GNSS FIle Parsing..");
        RealClientList listClient = new RealClientList();
        listClient.setDeviceID(deviceName);
        BufferedReader br = new BufferedReader(new FileReader(gnssFile));
        String line;
        int flag = 0;
        String tmpDeviceId = "";
        line = br.readLine();
        //  RealClientList tmpList= new RealClientList();
        while ((line = br.readLine()) != null) {
            String[] array = line.split(",");
            int j=0;

            double SystemTime = Math.round(Double.parseDouble(array[j++])/100.);
            int numberOfGPS_sats = Integer.parseInt(array[j++]);
            int numberOfGLNS_sats = Integer.parseInt(array[j++]);
            double maxSnrGPS = Double.parseDouble(array[j++]);
            double maxSnrGLNS = Double.parseDouble(array[j++]);
            boolean isFix = Boolean.parseBoolean(array[j++]);
            RealClient tmp = new RealClient(SystemTime,numberOfGPS_sats,numberOfGLNS_sats, maxSnrGPS, maxSnrGLNS, isFix);
            listClient.Clients.add(tmp);

       }
        System.out.println();
        System.out.println("Done Parsing 1");
        System.out.println("Parsing Location File...");
        br = new BufferedReader(new FileReader(LocationFile));
        line = br.readLine();
        //  RealClientList tmpList= new RealClientList();
        RealClientList tmpList = new RealClientList();
        int index=0;
        while ((line = br.readLine()) != null) {
            //the location file is like this : system_time,gps_time,latitude,longtide,altitude,sog,cog,accuracy

            String[] array = line.split(",");
            int j=0;

            double SystemTime = Math.round(Double.parseDouble(array[j++])/100.);
            double GPS_time = Math.round(Double.parseDouble(array[j++])/100.);
            double lat = Double.parseDouble(array[j++]);
            double lon = Double.parseDouble(array[j++]);
            double alt = Double.parseDouble(array[j++]);
            double sog = Double.parseDouble(array[j++]);
            double cog = Double.parseDouble(array[j++]);
            double accuarcy = Double.parseDouble(array[j++]);
            RealClient tmp2 = new RealClient(SystemTime, GPS_time, lat, lon, alt, sog, cog, accuarcy);
            tmpList.Clients.add(tmp2);



        }
        tmpList.printStat();
        listClient.printStat();
        listClient.mergeLists2(tmpList);
        listClient.printStat();
        double current_UTC_time =  15219736087.0;
        int j=0;

        j=0;
        RealClient tmp = listClient.getNextFix(current_UTC_time);
        System.out.println("");

        System.out.println("end parsing");
        return listClient;
    }


        public static void parse2018(long initialTime, long FInishTime, final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                System.out.println(" this is folder");
            } else {
                System.out.println(fileEntry.getName());
            }
        }

    }


    public static List<RealClientList> Parse2(long initialTIme, long FinishTime, String fileName)  throws IOException {

        System.out.println("Parsing..");
        List<RealClientList> listClient = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        int flag=0;
        String tmpDeviceId="";
        line = br.readLine();
      //  RealClientList tmpList= new RealClientList();
        while ((line = br.readLine()) != null) {
            String[] array = line.split(",");
            int j=0;
            String deviceIdString= (array[j++]);
            int deviceId = Integer.parseInt(array[j++]);
            double time = Double.parseDouble(array[j++]);
            double lat = Double.parseDouble(array[j++]);
            double lon = Double.parseDouble(array[j++]);
            double cog = Double.parseDouble(array[j++]);
            double sog = Double.parseDouble(array[j++]);
            double maxSnrGPS = Double.parseDouble(array[j++]);
            int numberOfGPS_sats = Integer.parseInt(array[j++]);
            double maxSnrGLNS = Double.parseDouble(array[j++]);
            int numberOfGLNS_sats = Integer.parseInt(array[j++]);
            time = time/1000;
            if(time>=initialTIme && time<=FinishTime)
            {
                RealClient tmp = new RealClient(time, lat, lon, cog, sog, maxSnrGPS,numberOfGPS_sats, maxSnrGLNS, numberOfGLNS_sats,deviceId);
                boolean newDevice=true;
                for(int i=0;i<listClient.size();i++)
                {
                    if(deviceIdString.equals(listClient.get(i).deviceIdString)) {
                        listClient.get(i).Clients.add((tmp));
                        newDevice = false;
                        break;
                    }

                }
                if(newDevice)
                {
                    RealClientList tmpList= new RealClientList();
                    tmpList.setDeviceID(deviceIdString);
                    tmpList.Clients.add(tmp);
                    listClient.add(tmpList);
                }

            }

        }
        System.out.println("Done Parsing.");
        System.out.println("Number of instance "+listClient.size());
        for(int i=0;i<listClient.size();i++)
        {
            System.out.println(i+" device num "+"  "+listClient.get(i).deviceIdString+" "  +listClient.get(i).Clients.size());
            int j = listClient.get(i).Clients.size();
            System.out.printf("Size is "+j);
            System.out.println(" Start time:"+listClient.get(i).Clients.get(0).time+ "End time "+listClient.get(i).Clients.get(j-1).time);
            //System.out.println();
        }


        return listClient;
    }





    public static void main2(String[] args) {

        Point2D jammerloc = new Point2D(50,50);
        JammerParticle realJammer  = new JammerParticle(50, jammerloc, 10);

        JamParticles JammerParticles = new JamParticles();
        JammerParticles.NaiveInit(0,0,200,200); // only one Tx Power. A stationary single Jammer.

     //   JammerParticles.getJamList().get(0).setJamLoc(new Point2D(50,51));
        JammerParticles.FindClosestJammer(realJammer);

        ClientList receiverList = new ClientList();
        receiverList.Init(0,0,200,200);

       // receiverList.PrintClinets();

        for(int i=0; i<40; i++)
        {
            receiverList.movebyCOGSOG();
            JammerParticles.FindClosestJammer(realJammer);
            receiverList.senseNoise(realJammer);
            JammerParticles.evalWeights(receiverList);
           // JammerParticles.getJamList().sort(JammerParticle::compareTo);
           // JammerParticles.PrintAll(realJammer);
            JammerParticles.PrintwithDistMargins(realJammer, 0, 20);
            JammerParticles.Resample();
           // JammerParticles.PrintResults(realJammer);



        }



    }
}
