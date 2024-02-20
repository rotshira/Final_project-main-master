package ParticleFilter;

/**
 * Created by Roi on 8/1/2016.
 */


public class SenseEden {


    public static int InitialMinDistanceToFix  = 40;
    public static int InitialMaxDistacneToSense = 250;
    public static int MaxSnr  = 50;

   public static double getDist(int X1, int Y1, int X2, int Y2)
   {
       return Math.sqrt(Math.pow((X1-X2),2)+Math.pow((Y1-Y2),2));

   }


    //// TODO: 8/1/2016 Eden & Michal. Power can be between 1-2.
    public static int GetSnr(int JamX, int Jamy, int SensorX, int SensorY, int Power)
    {

        double MaxDistacneToSense = InitialMaxDistacneToSense*Power;
        double MinDistanceToFix =  InitialMinDistanceToFix*Power;

        double dist = getDist(JamX,Jamy,SensorX,SensorY);
        if(dist>MaxDistacneToSense)
            return MaxSnr;
        else if(dist<MinDistanceToFix)
            return 0;
        else
        {
            double m = MaxSnr/(MaxDistacneToSense-MinDistanceToFix);
            double ComputedSnr = m*(dist-MinDistanceToFix);
            return (int)ComputedSnr;
        }

    }


}
