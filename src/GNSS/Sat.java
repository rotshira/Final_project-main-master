package GNSS;

import Geometry.Point3D;


public class Sat {

    Point3D satPosInECEF;//Represents the position of the satellite in Earth-Centered, Earth-Fixed (ECEF) coordinates.
    Point3D satVelocity;//Represents the velocity of the satellite.
    double azimuth, elevetion;//azimuth-Represents the azimuth angle of the satellite. elevetion-Represents the elevation angle of the satellite.
    int satID;//Represents the identifier of the satellite.
    public int[] snr;
    public int SingleSNR;//Represents the SNR of a single satellite signal.


    public Sat(Point3D satPosInECEF, double azimuth, double elevetion, int satID) {
        this.satPosInECEF = satPosInECEF;
        this.azimuth = azimuth;
        this.elevetion = elevetion;
        this.satID = satID;
    }


    public int getSingleSNR() {
        return SingleSNR;
    }

    public void setSingleSNR(int singleSNR) {
        SingleSNR = singleSNR;
    }

    public Sat(double azimuth, double elevetion, int satID)
    {
        this.azimuth = azimuth;
        this.elevetion = elevetion;
        this.satID = satID;
    }

    public Sat(double azimuth, double elevetion, int satID, int SNR)
    {
        this.azimuth = azimuth;
        this.elevetion = elevetion;
        this.satID = satID;
        this.SingleSNR = SNR;
    }

    //Calculates the distance between the satellite and a given position.
    public double distanceFromSatToPos(Point3D pos) {
        if (this.satPosInECEF != null) {
            return this.satPosInECEF.distance(pos);
        } else {
            // Handle the case where satPosInECEF is null
            return Double.MAX_VALUE;  // Or another value indicating an error
        }
    }

    //Computes the azimuth and elevation angles from the satellite to a given position.
    //Not really Computes
    public void computeAnglesfromSatToPos(Point3D pos)
    {
        this.azimuth = 0;
        this.elevetion = 0;
    }

    public Point3D getSatPosInECEF() {
        return satPosInECEF;
    }

    public void setSatPosInECEF(Point3D satPosInECEF) {
        this.satPosInECEF = satPosInECEF;
    }

    public double getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }

    public double getElevetion() {
        return elevetion;
    }

    public void setElevetion(double elevetion) {
        this.elevetion = elevetion;
    }

    public int getSatID() {
        return satID;
    }

    public void setSatID(int satID) {
        this.satID = satID;
    }

    public int[] getSnr() {
        return snr;
    }

    public void setSnr(int[] snr) {
        this.snr = snr;
    }

    public double getXposECEF(){return this.satPosInECEF.getX();}

    public double getYposECEF(){return this.satPosInECEF.getY();}

    public double getZposECEF(){return this.satPosInECEF.getZ();}

}
