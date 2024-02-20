package Jamming;

/**
 * Created by Roi on 9/26/2016.
 */
public class Utils {

    static final int JamerParticles = 500;
    static final int NumberOfClient = 20;
    static double MaximumClientSpeed = 7;

    static final double FirstSimulationJammerTxPower = 48;
    static final double SecondSimulationJammerTxPower = 35;


    public static double MaxJammerRange = 240;
    public static double AbsoluteMaxJammerRange = 300;
    public static double AbsoluteMaxJammerPower = 50;
    public static double AbsoluteMaxMinimumDistanceToLoseFix = AbsoluteMaxJammerRange/10;
    public static final int X_Screen = 800;
    public static final int Y_Screen = 800;
    public static double JammerRaduis4Drawing = MaxJammerRange*2;

    public static double MinJammerRange  = 20;
    public static double SmallJammerRaduis4Drawing = MinJammerRange*2;

    static double MaximumJammerSpeed = 5;
    static final double MaximumCLinetSnr = 50;

    static final double Client_COG_eror = 0.2; // variance
    static final double Client_SOG_erorr = 5; // varicance

    public static final long TimeTodDealy = 1000;
    static double Client_Sense_Noise_Figure = 3;

    public final static double SenseEror =     3;// this figure is for Gauissan equation

    static public double GausianPorb(double mu, double sigma, double x)
    {
        double ans = Math.exp(-Math.pow((mu-x),2)/(sigma*sigma)/2)/Math.sqrt(2.0*Math.PI*sigma*sigma);
        return ans;
    }


}

//toyota petach tikva -