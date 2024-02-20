package Jamming.RealRecording;

import Geometry.Point2D;

import java.util.ArrayList;

/**
 * Created by Roi_Yozevitch on 5/13/2018.
 */
public class JamRing {

    Point2D xy_Loc;
    double InnerRadius;
    double MaxRadius;
    double timestamp;
    double raduis;
    double stdatadDeviation;

    public JamRing(Point2D xy_Loc, double innerRadius, double maxRadius, double raduis, double stdatadDeviation) {
        this.xy_Loc = xy_Loc;
        InnerRadius = innerRadius;
        MaxRadius = maxRadius;
        this.raduis = raduis;
        this.stdatadDeviation = stdatadDeviation;
    }
    public JamRing(double x, double y, double innerRadius, double maxRadius, double raduis, double stdatadDeviation) {
        this.xy_Loc = new Point2D(x,y);
        InnerRadius = innerRadius;
        MaxRadius = maxRadius;
        this.raduis = raduis;
        this.stdatadDeviation = stdatadDeviation;
    }

    public double getInnerRadius() {
        return InnerRadius;
    }

    public double getMaxRadius() {
        return MaxRadius;
    }

    public double getRaduis() {
        return raduis;
    }

    public double getStdatadDeviation() {
        return stdatadDeviation;
    }

    public void ComputeRadadiusFromSnr(double SNR)
    {
        this.raduis = SNR*10.;

    }

    int[][] ComputeHeatMap(int xSize, int ySize, ArrayList<JamRing> rings)
    {
        int[][] heatMap = new int[xSize][ySize];

        for(JamRing ring: rings)
        {

        }
        return heatMap;
    }

}


