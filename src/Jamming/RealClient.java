package Jamming;

import Geometry.Point2D;
import Geometry.Point3D;
import dataStructres.SVMachineLearningData;

/**
 * Created by Roi_Yozevitch on 7/16/2017.
 */
public class RealClient {

    public  double gpsTime;
    public double accuarcy;
    public  int devID;
    private  String DeviceID;
    double lat;
    double xPos,yPos,zPos;
    double id;
    int numOfGpsSat;
    int numOfGlonassSat;
    double lon;
     double utmX;
     double utmY;
    double xScreenCord;
    double yScreenCord;
    double alt;
    boolean isFix;
    double maxSnrGPS;
    double maximumSNR;
    double COG, SOG;
    double time;
    double maxSnrGlonass;
    double noiseFigure;

    public RealClient(double systemTime, int numberOfGPS_sats, int numberOfGLNS_sats, double maxSnrGPS, double maxSnrGLNS, boolean isFix) {

        this.maxSnrGPS = maxSnrGPS;
        this.maxSnrGlonass = maxSnrGLNS;
        this.time = systemTime;
        this.numOfGpsSat = numberOfGPS_sats;
        this.numOfGlonassSat = numberOfGLNS_sats;
        this.isFix = false;
    }

    public RealClient(double systemTime, double gps_time, double lat, double lon, double alt, double sog, double cog, double accuarcy) {

        this.time = systemTime;
        this.gpsTime = gps_time;
        this.lat = lat;
        this.lon = lon;
        this.alt = alt;
        this.COG = cog;
        this.SOG = sog;
        this.accuarcy = accuarcy;
    }

    public Point2D getClientLocUTM()
    {
        return new Point2D(utmX, utmY);
    }

    public double getUtmX() {
        return utmX;
    }

    public double getUtmY() {
        return utmY;
    }

    public RealClient(double lat, double id, int numOfGpsSat, int numOfGlonassSat, double lon, double alt, double maxSnrGPS, double COG, double SOG, double time, double maxSnrGlonass) {
        this.lat = lat;
        this.id = id;
        this.numOfGpsSat = numOfGpsSat;
        this.numOfGlonassSat = numOfGlonassSat;
        this.lon = lon;
        this.alt = alt;
        this.maxSnrGPS = maxSnrGPS;
        this.COG = COG;
        this.SOG = SOG;
        this.time = time;
        this.maxSnrGlonass = maxSnrGlonass;

        convertLatLongToLocalCord();

    }

    public String toString()
    {
        return this.utmX+","+this.utmY+","+this.alt+","+this.getSOG()+","+this.getCOG()+","+this.time+"," + this.maxSnrGPS+","+this.maxSnrGlonass+","+this.numOfGpsSat+","+this.numOfGlonassSat;
    }
    public RealClient()
    {
        this.time=0;

    }

    public double getMaxSnrGlonass() {
        return maxSnrGlonass;
    }

    public int getNumOfGpsSat() {
        return numOfGpsSat;
    }

    public int getNumOfGlonassSat() {
        return numOfGlonassSat;
    }

    public double getMaxSnrGPS() {
        return maxSnrGPS;
    }

    public double getMaximumSNR() {
        return maximumSNR;
    }

    public double getCOG() {
        return COG;
    }

    public double getSOG() {
        return SOG;
    }

    public double getTime() {
        return time;
    }

    public RealClient(double time, double lat, double lon, double cog, double sog, double maxGPS, int n_gps, double maxGlns, int n_glns, int devID) {
        this.time= time;
        this.lat = lat;
        this.lon = lon;
        this.COG = cog;
        this.SOG = sog;
        this.maxSnrGPS = maxGPS;
        this.maxSnrGlonass = maxGlns;
        this.numOfGlonassSat = n_glns;
        this.numOfGpsSat = n_gps;
        //this.DeviceID = deviceId;
        this.devID = devID;
        convertLatLongToLocalCord();

    }


    public double getGpsTime() {
        return gpsTime;
    }

    public double getAccuarcy() {
        return accuarcy;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getAlt() {
        return alt;
    }

    private  String toCsvString(SVMachineLearningData meas ) {
            String res = "";
            //if (meas.getTime() == 0l){
            //	return res;
            //}
            res += this.time + ",";
            res += meas.getAzimuth() + ",";
            res += meas.getElevation() + ",";
            res += meas.getHdop() + ",";
            res += meas.getPseudoRange() + ",";
            res += meas.getComputedRange() + ",";
            res += meas.getClock_drift() + ",";
            res += meas.getClockBias() + ",";

            res += "\r\n";

            return res;

        }

    private void convertLatLongToLocalCord() {
        xPos = 0;
        yPos = 0;
//        Point3D tmp = GeoUtils.convertLATLONtoUTM(new Point3D(lat, lon, alt));
//        utmX = tmp.getX();
//        utmY = tmp.getY();
        zPos = this.alt;
    }

    public double getxScreenCord() {
        return xScreenCord;
    }

    public double getyScreenCord() {
        return yScreenCord;
    }

    public void setXY_screen_cords(double x, double y) {

        this.xScreenCord = x;
        this.yScreenCord = y;


    }

    public void merge(RealClient tmp) {


        this.gpsTime = tmp.getGpsTime();
        this.lat = tmp.getLat();
        this.lon = tmp.getLon();
        this.alt = tmp.getAlt();
        this.COG = tmp.getCOG();
        this.SOG = tmp.getSOG();
        this.accuarcy = tmp.getAccuarcy();
        this.isFix = true;

    }

    public void ComputeUtm() {

//        Point3D tmp = GeoUtils.convertLATLONtoUTM(new Point3D(this.lat,this.lon, this.alt));
//        this.utmX = tmp.getX();
//        this.utmY = tmp.getY();
//

    }

    public void printStats() {
        System.out.println(this.time+ " "+this.lat+ " "+this.lon);
    }
}
