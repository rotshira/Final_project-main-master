package dataStructres;

import Geometry.Point3D;

/**
 * Created by Roi on 1/31/2015.
 */
public class STMSVMeasurement extends NMEASVMeasurement {

    /*
The below equation fits GPS soley
Correct PseudoRange (before clock bias computation) is :PR = rawPR - deltaP_ATM + deltaP_sv
 */
    private double rawPR;

    /*
    for GLONASS sats, the equation becomes PR = rawPR - deltaP_ATM + deltaP_sv + deltaP_glo
     */
    private double deltaP_glo;
    private double correctedPR;

    public boolean isGoodSVforPseudoRangeComputation() {
        return this.isGoodSVforPseudoRangeComputation;
    }

    boolean isGoodSVforPseudoRangeComputation;
    private double PRwithDeltaT;
    double deltaP_ATM;
    double deltaP_SV;
    protected Point3D ECEFpos;
    protected Point3D EcefVel;
    double frequncy;
    boolean lockSignal;
    boolean navigationData;

    public STMSVMeasurement(int prn, int el, int az, int snr, double xPos, double yPos, double zPos, double rawPR, double deltaP_ATM, double deltaP_SV) {
        super(prn, el, az, snr);
        this.ECEFpos = new Point3D(xPos, yPos, zPos);
        this.rawPR = rawPR;
        this.deltaP_ATM = deltaP_ATM;
        this.deltaP_SV = deltaP_SV;
        setCorrectedPR();
    }

    public STMSVMeasurement(int prn, double rawPR, double freq, boolean lockSignal, int cn0, double trackedTime, boolean navigationData, double ecefPosX, double ecefPosY, double ecefPosZ, double ecefVelX, double eceFVelY, double eceFVelz, double deltaPsv, double deltaPatm)
    {
        //
        super(prn, cn0);

        this.rawPR =rawPR;
        this.deltaP_SV = deltaPsv;
        this.deltaP_ATM = deltaPatm;
        this.ECEFpos = new Point3D(ecefPosX, ecefPosY, ecefPosZ);
        this.EcefVel = new Point3D(ecefVelX, eceFVelY, eceFVelz);
        this.frequncy = freq;
        this.lockSignal = lockSignal;
        this.navigationData = navigationData;
        setCorrectedPR();


    }

    public static STMSVMeasurement nullMeas = new STMSVMeasurement(-1, -1, -1,false, -1, -1, false, -1, -1, -1, -1, -1, -1, -1, -1);


    public double getCorrectedPR() {

        return this.correctedPR;
    }



    private void setCorrectedPR() //todo ROi add glonass correction.
    {

            this.correctedPR = this.rawPR - this.deltaP_ATM + this.deltaP_SV;
            if(this.isNavigationData())
            isGoodSVforPseudoRangeComputation = true;


        else
            isGoodSVforPseudoRangeComputation = false;

       // else if(super.prn>=40)
        //    this.correctedPR = this.rawPR -this.deltaP_ATM + this.deltaP_SV
    }

    public void setPseudoRangeWithDeltaT(double pseudoRangeWithDeltaT)
    {
        this.PRwithDeltaT = pseudoRangeWithDeltaT;
    }

    public double getPRwithDeltaT()
    {
        return this.PRwithDeltaT;
    }


    public Point3D getECEFpos() {
        return ECEFpos;
    }

    public double getEcefXpos() {return ECEFpos.getX();}

    public double getEcefYpos() {return ECEFpos.getY();}

    public double getEcefZpos() {return ECEFpos.getZ();
    }

    public double getEcefXvel() {return EcefVel.getX();
    }

    public double getEcefYvel() {return EcefVel.getY();}

    public double getEcefZvel() {return EcefVel.getZ();}


    public Point3D getEcefVel() {
        return EcefVel;
    }

    public double getFrequncy() {
        return frequncy;
    }

    public boolean isLockSignal() {
        return lockSignal;
    }

    public boolean isNavigationData() {
        return navigationData;
    }

    public double getDeltaATM() {
        return this.deltaP_ATM;
    }
}
