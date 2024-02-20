package dataStructres;

import GNSS.Sat;
import Geometry.Point3D;

import java.util.Arrays;


public class SirfSVMeasurement {
	
	double xPos;
	double yPos;
	double zPos;
    public static  double c= 299792458; // m/s



    boolean OKSVM;
    double carrierPhase;
    double carrierFreq;
    double pseudorange;
    double DeltapseudoRange;
    double gpsSoftwareTime;
    int[] filteredCNo;
    int[] CNo;
    private    int[] OldCNo; //  Highest and lowest  Cn0 values of the previous 5 seconds.
    double ComputedPSrange;
	double xV, yV, zV, clockDrift, ionosphericDelay,
		deltaRangeInterval, 
		meanDeltaRangeTime;
	double azimuth, elevation;
    int state;
	double clockBias;
    public double LOSLiklihood;
    private double pseudoRangeWithDeltaT;
    private double previousCarrierPhase;
    private Double prevoiusCorrectedPseudoRangeNoVelocityShift=null;
    private double PsResiduals;
    public int MaxCn0InSatHistory;
    public int MinCn0InSatHistory;
    private Double prevoiusDeltaCorrectedPrNoVelocityShift;
    private Integer PrevoiusMaxCn02seconds=null;
    private double deltaCorrectedPrNovelocityShift;

    public void setCarrierFreqDelta(double carrierFreqDelta) {
        this.carrierFreqDelta = carrierFreqDelta;
    }

    private double carrierFreqDelta;

    public void setDeltaIonosphericDelay(double deltaIonosphericDelay) {
        this.deltaIonosphericDelay = deltaIonosphericDelay;
    }

    private double deltaIonosphericDelay;

    public double getPreviousCarrierPhase() {
        return previousCarrierPhase;
    }

    public void setPreviousCarrierPhase(double previousCarrierPhase) {
        this.previousCarrierPhase = previousCarrierPhase;
    }


    public int getMaxCn0InSatHistory() {
        return MaxCn0InSatHistory;
    }

    public void setMaxCn0InSatHistory(int maxCn0InSatHistory) {
        MaxCn0InSatHistory = maxCn0InSatHistory;
    }

    public int getMinCn0InSatHistory() {
        return MinCn0InSatHistory;
    }

    public void setExtremeSNRValues(int MaxCn0inSatHistory, int MinCn0InSatHistory)
    {
        int maxCn0 = this.getMaxCn0();
        int MinCn0 = this.getMinCn0();
        if(MaxCn0inSatHistory>maxCn0)
            this.setMaxCn0InSatHistory(MaxCn0inSatHistory);
        else
            this.setMaxCn0InSatHistory(maxCn0);
        if(MinCn0InSatHistory<MinCn0)
            this.setMinCn0InSatHistory(MinCn0InSatHistory);
        else
            this.setMinCn0InSatHistory(MinCn0);

    }

    public void setMinCn0InSatHistory(int minCn0InSatHistory) {
        MinCn0InSatHistory = minCn0InSatHistory;
    }


    public double getCorrectPsResiduals() {
        return PsResiduals;
    }

    public void setExtremeValuesToCurrentValues()
    {
        this.setMinCn0InSatHistory(this.getMinCn0());
        this.setMaxCn0InSatHistory(this.getMaxCn0());
    }

    public Double getPrevoiusDeltaCorrectedPrNoVelocityShift() {
        return prevoiusDeltaCorrectedPrNoVelocityShift;
    }



    public void setPrevoiusMaxCn02seconds(Integer prevoiusMaxCn02seconds) {PrevoiusMaxCn02seconds = prevoiusMaxCn02seconds;}

    public double getDeltaCorrectedPrNovelocityShift() {
        return deltaCorrectedPrNovelocityShift;
    }

    public double getCorrectPseudoRangeNoVelocitySHift() {
        return correctPseudoRangeNoVelocitySHift;
    }

    public void setCorrectPseudoRangeNoVelocitySHift(double correctPseudoRangeNoVelocitySHift) {
        this.correctPseudoRangeNoVelocitySHift = correctPseudoRangeNoVelocitySHift;
    }

    private double correctPseudoRangeNoVelocitySHift;


    public boolean isOKSVM() {
        return OKSVM;
    }

    public void setOKSVM(boolean OKSVM) {
        this.OKSVM = OKSVM;
    }


  /*
    Getters and Setters:
    */

    public void PrintOldCn()
    {
        if(this.OldCNo!=null)
        for(int i=0; i<10; i++)
            System.out.println("Old Cn0 number "+i+" is "+this.OldCNo[i]);
        else
            System.out.println("For this instance, there is no OldCn0");
    }


    public int[] getOldCNo() {
        return OldCNo;
    }

    public void setOldCNo(int[] old) {
       // OldCNo = new int[10];
        this.OldCNo = old;
        this.OKSVM=true;
    }

    public double getComputedPSrange() {
        return ComputedPSrange;
    }

    public void setComputedPSrange(double computedPSrange) {
        ComputedPSrange = computedPSrange;
    }

    public void computeRange(Point3D Pos)   // compute range between 2 points ad PS resuidals
    {
        Point3D loc = new Point3D(this.getxPos(), this.getyPos(), this.getzPos());
        this.ComputedPSrange = Pos.distance(loc);
        this.DeltapseudoRange = this.pseudorange - this.ComputedPSrange;

    }
    public void PrintLOc()
    {
       // System.out.println("PRN Number "+  );
        System.out.println("Azimutut " +this.getAzimuth()+ ".  Elevation is "+this.getElevation());
    }
    public Boolean getLOS() {
        return LOS;
    }

    public void setLOS(Boolean LOS) {
        this.LOS = LOS; //if los this is ture
    }

    Boolean LOS;  //LOS is True  - NLOS is false



    public void ComputeNaiveLOSWithTHreshold(int thresh, double var)
    {
        this.LOS = false;

            if (this.getMaxCn0() >= thresh)
            {
                this.LOS = true;

            }

        double d= this.getMaxCn0() - thresh;
        if(d<-var) LOSLiklihood = 0;
        else {
            if (d > var) LOSLiklihood = 1;
            else LOSLiklihood = 0.5 + (d / (var*2));
        }
    }
	
	public SirfSVMeasurement(){
		super();
		CNo = new int[10];
        OldCNo = new int[10];
        this.OKSVM=false;
		filteredCNo = new int[10];
	}
	
	/**
	 * @return the xPos
	 */
	public double getxPos() {
		return xPos;
	}
	/**
	 * @return the yPos
	 */
	public double getyPos() {
		return yPos;
	}

    public int getMaxCn0()
    {
        int result= CNo[0];
        for(int i=1;i<10; i++)
       {
        if(CNo[i]>result)
            result = CNo[i];

       }
        return result;
    }

    public boolean IsMaxOldCn0GreaterThanZero()
    {

        int result= this.OldCNo[0];
        for(int i=1;i<10; i++)
        {
            if(OldCNo[i]>OldCNo[i-1])
                result = OldCNo[i];

        }
        if (result>0)
        return true;
        else
            return false;
    }

    public int getMaxFilterCn0()
    {

        int result= filteredCNo[0];
        for(int i=1;i<10; i++)
        {
            if(filteredCNo[i]>result)
                result = filteredCNo[i];

        }
        return result;
    }

    public int getMinCn0()
    {

        int result= CNo[0];
        for(int i=1;i<10; i++)
        {
            if(CNo[i]<result)
                result = CNo[i];

        }
        return result;
    }
    public int getMinFilterCn0()
    {

        int result= filteredCNo[0];
        for(int i=1;i<10; i++)
        {
            if(filteredCNo[i]<result)
                result = filteredCNo[i];

        }
        return result;
    }
	/*
	* *
	 * @return the zPos
	 */
	public double getzPos() {
		return zPos;
	}
	/**s
	 * @return the xV
	 */
	public double getxV() {
		return xV;
	}
	/**
	 * @return the yV
	 */
	public double getyV() {
		return yV;
	}
	/**
	 * @return the zV
	 */
	public double getzV() {
		return zV;
	}
	/**
	 * @return the clockBias
	 */
	public double getClockBias() {
		return clockBias;
	}
	/**
	 * @return the clockDrift
	 */
	public double getClockDrift() {
		return clockDrift;
	}
	/**
	 * @return the ionosphericDelay
	 */
	public double getIonosphericDelay() {
		return ionosphericDelay;
	}
	/**
	 * @return the pseudorange
	 */
	public double getPseudorange() {
		return pseudorange;
	}
	/**
	 * @return the carrierFreq
	 */
	public double getCarrierFreq() {
		return carrierFreq;
	}
	/**
	 * @return the carrierPhase
	 */
	public double getCarrierPhase() {
		return carrierPhase;
	}
	/**
	 * @return the deltaRangeInterval
	 */
	public double getDeltaRangeInterval() {
		return deltaRangeInterval;
	}
	/**
	 * @return the meanDeltaRangeTime
	 */
	public double getMeanDeltaRangeTime() {
		return meanDeltaRangeTime;
	}
	/**
	 * @return the cNo
	 */
	public int[] getFilteredCNo() {
		return filteredCNo;
	}



    public SirfSVMeasurement(int xPos, int yPos, int zPos, int xV, int yV, int zV,
			int clockBias, int clockDrift, int ionosphericDelay,
			int pseudorange, int carrierFreq, int carrierPhase,
			int deltaRangeInterval, int meanDeltaRangeTime, int gpsSoftwareTime, int azimuth, int elevation, int state, int[] filteredcNo, int[] cNo) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.zPos = zPos;
		this.xV = xV;
		this.yV = yV;
		this.zV = zV;
		this.clockBias = clockBias;
        this.OKSVM = false;
		this.clockDrift = clockDrift;
		this.ionosphericDelay = ionosphericDelay;
		this.pseudorange = pseudorange;
		this.carrierFreq = carrierFreq;
		this.carrierPhase = carrierPhase;
		this.deltaRangeInterval = deltaRangeInterval;
		this.meanDeltaRangeTime = meanDeltaRangeTime;
		this.filteredCNo = filteredcNo;
		this.CNo = cNo;
		this.gpsSoftwareTime = gpsSoftwareTime;
		this.azimuth = azimuth ;
		this.elevation = elevation;
		this.state = state;

	}
	/**
	 * @param d the xPos to set
	 */
	public void setxPos(double d) {
		this.xPos = d;
	}
	/**
	 * @param d the yPos to set
	 */
	public void setyPos(double d) {
		this.yPos = d;
	}
	/**
	 * @param d the zPos to set
	 */
	public void setzPos(double d) {
		this.zPos = d;
	}
	/**
	 * @param d the xV to set
	 */
	public void setxV(double d) {
		this.xV = d;
	}
	/**
	 * @param d the yV to set
	 */
	public void setyV(double d) {
		this.yV = d;
	}
	/**
	 * @param d the zV to set
	 */
	public void setzV(double d) {
		this.zV = d;
	}
	/**
	 * @param d the clockBias to set
	 */
	public void setClockBias(double d) {
		this.clockBias = d;
	}
	/**
	 * @param d the clockDrift to set
	 */
	public void setClockDrift(double d) {
		this.clockDrift = d;
	}
	/**
	 * @param d the ionosphericDelay to set
	 */
	public void setIonosphericDelay(double d) {
		this.ionosphericDelay = d;
	}
	/**
	 * @param d the pseudorange to set
	 */
	public void setPseudorange(double d) {
		this.pseudorange = d;
	}
	/**
	 * @param d the carrierFreq to set
	 */
	public void setCarrierFreq(double d) {
		this.carrierFreq = d;
	}
	/**
	 * @param d the carrierPhase to set
	 */
	public void setCarrierPhase(double d) {
		this.carrierPhase = d;
	}
	/**
	 * @param deltaRangeInterval the deltaRangeInterval to set
	 */
	public void setDeltaRangeInterval(int deltaRangeInterval) {
		this.deltaRangeInterval = deltaRangeInterval;
	}
	/**
	 * @param meanDeltaRangeTime the meanDeltaRangeTime to set
	 */
	public void setMeanDeltaRangeTime(int meanDeltaRangeTime) {
		this.meanDeltaRangeTime = meanDeltaRangeTime;
	}
	/**
	 * @param cNo the cNo to set
	 */
	public void setCNo(int[] cNo) {
		CNo = cNo;
	}

	/**
	 * @return the gpsSoftwareTime
	 */
	public double getGpsSoftwareTime() {
		return gpsSoftwareTime;
	}

	/**
	 * @param d the gpsSoftwareTime to set
	 */
	public void setGpsSoftwareTime(double d) {
		this.gpsSoftwareTime = d;
	}

	/**
	 * @return the cNo
	 */
	public int[] getCNo() {
		return CNo;
	}

	/**
	 * @param filteredCNo the filteredCNo to set
	 */
	public void setFilteredCNo(int[] filteredCNo) {
		this.filteredCNo = filteredCNo;
	}

	/**
	 * @return the azimuth
	 */
	public double getAzimuth() {
		return azimuth;
	}

	/**
	 * @param azimuth the azimuth to set
	 */
	public void setAzimuth(double azimuth) {
		this.azimuth = azimuth;
	}

	/**
	 * @return the elevation
	 */
	public double getElevation() {
		return elevation;
	}

	/**
	 * @param elevation the elevation to set
	 */
	public void setElevation(double elevation) {
		this.elevation = elevation;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}


    public void setPseudoRangeWithDeltaT(double pseudoRangeWithDeltaT) {
        this.pseudoRangeWithDeltaT = pseudoRangeWithDeltaT;
    }

    public double getPseudoRangeWithDeltaT() {
        return pseudoRangeWithDeltaT;
    }

    public Point3D getSatPosInEcef() {
        Point3D satPosInEcef = new Point3D(this.getxPos(), this.getyPos(), this.getzPos());
        return satPosInEcef;
    }

    public Sat getSatClass(Integer PRN) {

        double az = this.getAzimuth();
        double el =this.getElevation();
        Point3D satPosECEF = this.getSatPosInEcef();
        Sat newSat = new Sat(satPosECEF, az, el, PRN);
        return newSat;


    }

    public void ComputedPSrangeNoVelocityShift(double clockDriftMsg7) {

       this.setCorrectPseudoRangeNoVelocitySHift(this.pseudorange+ c*(this.getClockBias()-clockDriftMsg7));
    }

    public void  setPrevoiusCorrectedPseudoRangeNoVelocityShift(double prevoiusCorrectedPseudoRangeNoVelocityShift) {
        this.prevoiusCorrectedPseudoRangeNoVelocityShift = prevoiusCorrectedPseudoRangeNoVelocityShift;
        this.deltaCorrectedPrNovelocityShift = this.correctPseudoRangeNoVelocitySHift - prevoiusCorrectedPseudoRangeNoVelocityShift;

    }


    public void setPrevoiusDeltaCorrectedPrNoVelocityShift(Double prevoiusDeltaCorrectedPrNoVelocityShift) {
        this.prevoiusDeltaCorrectedPrNoVelocityShift = prevoiusDeltaCorrectedPrNoVelocityShift;

    }

    public Double getPrevoiusCorrectedPseudoRangeNoVelocityShift() {
//        assert prevoiusCorrectedPseudoRangeNoVelocityShift!=null;
        return this.prevoiusCorrectedPseudoRangeNoVelocityShift;


    }

    public double getSecondDerivativeDeltaCorrectedPrNoVelocityShift() {
        //return 0;
        return this.deltaCorrectedPrNovelocityShift - this.getPreviousDeltaCorrectedPrNovelocityShift();

    }

    private Double getPreviousDeltaCorrectedPrNovelocityShift() {
     //   assert this.prevoiusDeltaCorrectedPrNoVelocityShift!=null;
        return this.prevoiusDeltaCorrectedPrNoVelocityShift;

    }



    public Integer getPreivousMaxCno2seconds() {
        assert this.PrevoiusMaxCn02seconds!=null;
        return this.PrevoiusMaxCn02seconds;

    }

    public void computePsResiduals(Point3D receiverPosECEF) {

        double ComputeddistanceSatReceiver = receiverPosECEF.distance(this.getSatPosInEcef());
        this.PsResiduals = this.correctPseudoRangeNoVelocitySHift - ComputeddistanceSatReceiver;
    }

    public double getDeltaIonosphericDelay() {
        return deltaIonosphericDelay;
    }

    public double getCarrierFreqDelta() {
        return carrierFreqDelta;
    }

    public int ifCarrierPhaseLock() {
        int isCarrierMask = 2; //0b00000010
        int ans = isCarrierMask & this.getState();
        return ans;
    }
}
