package dataStructres;

/**
 * Created with IntelliJ IDEA.
 * User: Roi           	int[] filteredCNo;

 * Date: 08/04/14
 * Time: 16:39
 * To change this template use File | Settings | File Templates.
 */

public class SVMachineLearningData {

    public static final int MaxCn0Value =  50;



    double GPSTime;
    double azimuth;
    int[] filteredCNo;
    int[] CNo;
    int[] OldCNo; //  Highest and lowest  Cn0 values of the previous 5 seconds.
    double elevation;
    double Hdop;
    int GNSS_Type; // -1 for GPS. +1 for GLONASS
    double ComputedRange;   //as was computed from the XYZ of sv and xyz of reciever.
    double PseudoRange;  //as was produced by Sirf message no. 28
    double ClockBias;
    double DeltaReciverPosition;//the current location -  previous (1 sec) location. this is for tracking jumps in the computed location which may indicate...
    double Clock_drift;
    Boolean LOS; // 1 is for LOS, -1 is for NLOS


    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    public Boolean getLOS() {
        return LOS;
    }

    public void setLOS(Boolean LOS) {
        this.LOS = LOS;
    }



    public double getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }

    public double getGPSTime() {
        return GPSTime;
    }

    public void setGPSTime(double GPSTime) {
        this.GPSTime = GPSTime;
    }
    public int[] getCNo() {
        return CNo;
    }

    public void setCNo(int[] CNo) {
        this.CNo = CNo;
    }


    public int[] getFilteredCNo() {
        return filteredCNo;
    }

    public void setFilteredCNo(int[] filteredCNo) {
        this.filteredCNo = filteredCNo;
    }

    public int[] getOldCNo() {
        return OldCNo;
    }

    public void setOldCNo(int[] oldCNo) {
        OldCNo = oldCNo;
    }


    public double getHdop() {
        return Hdop;
    }

    public void setHdop(double hdop) {
        Hdop = hdop;
    }



    public SVMachineLearningData(double azimuth, double elevation, int[] CNo, int[] filteredCNo, int[] oldCNo, int GNSS_Type, double computedRange, double pseudoRange, double clockBias, double deltaReciverPosition, double clock_drift, double GPStime) {

        this.GPSTime = GPStime;
        this.azimuth = azimuth;
        this.elevation = elevation;
        this.CNo = CNo;
        this.filteredCNo = filteredCNo;
        OldCNo = oldCNo;
        this.GNSS_Type = GNSS_Type;
        ComputedRange = computedRange;
        PseudoRange = pseudoRange;
        ClockBias = clockBias;
        DeltaReciverPosition = deltaReciverPosition;
        Clock_drift = clock_drift;

    }

    public SVMachineLearningData(SirfSVMeasurement SV_Data)
    {
       this.azimuth = SV_Data.getAzimuth();

       this.elevation = SV_Data.getElevation();
       this.CNo = SV_Data.getCNo();
       this.filteredCNo = SV_Data.getFilteredCNo();
       this.PseudoRange = SV_Data.getPseudorange();
       this.Clock_drift = SV_Data.getClockDrift();
       this.ClockBias = SV_Data.getClockDrift();
        this.GPSTime = SV_Data.getGpsSoftwareTime();
       this.ComputedRange = SV_Data.getComputedPSrange();
       this.setOldCNo(SV_Data.getOldCNo());
        //this.PrintOldCn0();
        //need to add the following
        // DeltaReciverpos

    }

    public void PrintOldCn0()
    {
        for(int i=0; i<this.OldCNo.length; i++)
            System.out.println("Old Cn0 number "+i+" is "+this.OldCNo[i]);
    }

    public void PrintData()
    {
        System.out.println("-------- New SV------- ");
        System.out.println(" GPS TIme is"+ this.getGPSTime() );
        System.out.println("GNSS Type " + this.getGNSS_Type());
        System.out.println("Azimuth is " + this.azimuth);
        System.out.println("  elev is " + this.elevation);
        System.out.println("cr freq is " + this.PseudoRange);
        for (int i=0; i<10; i++)
            System.out.println(i+" Cno "+this.CNo[i]+". Filtered Cn0 "+this.filteredCNo[i]+". History Cn0 "+this.getOldCNo()[i]);

        System.out.println();
    }

    public double getDeltaReciverPosition() {
        return DeltaReciverPosition;
    }

    public void setDeltaReciverPosition(double deltaReciverPosition) {
        DeltaReciverPosition = deltaReciverPosition;
    }



    public double getClock_drift() {
        return Clock_drift;
    }

    public void setClock_drift(double clock_drift) {
        Clock_drift = clock_drift;
    }
    public int getGNSS_Type() {
        return GNSS_Type;
    }

    public void setGNSS_Type(int PRN) {
        if(PRN>40)
        this.GNSS_Type = 1;
        else
            this.GNSS_Type = -1;

    }

    public double getPseudoRange() {
        return PseudoRange;
    }

    public void setPseudoRange(double pseudoRange) {
        PseudoRange = pseudoRange;
    }

    public double getComputedRange() {
        return ComputedRange;
    }

    public void setComputedRange(double computedRange) {
        ComputedRange = computedRange;
    }


    public double getClockBias() {
        return ClockBias;
    }

    public void setClockBias(double clockBias) {
        ClockBias = clockBias;
    }



    /*only sclae the folowing data:
    azimuth
    elevation
     all of the Cn0;*/

   public void ScaleSimpleData()
   {
       this.azimuth = this.azimuth/360;
       this.elevation = this.elevation/90;
       for(int i=0; i<this.CNo.length; i++)
       {
           CNo[i] = CNo[i]/MaxCn0Value;
           filteredCNo[i] = filteredCNo[i]/MaxCn0Value;
           OldCNo[i] = OldCNo[i]/MaxCn0Value;
       }
   }
    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

}
