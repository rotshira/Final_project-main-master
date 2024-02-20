package Algorithm.lookup;

import Algorithm.LosAlgorithm;
import Geometry.Building;
import Geometry.Point2D;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Roi on 1/19/2015.
 * resolution is in meter. 1 means each meter. 0.5 means each 0.5 meters . 2 means each 2 meters.
 */
public class Grid {

    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;
    private final int xJumpsNumber;
    private final int yJumpsNumber;
    private final Set<Building> buildings;
    int resolution;
    Point2D[][] pointArray;

    public Grid(Point2D p1, Point2D p2, int resolution, Set<Building> buildings) throws Exception {
        this.resolution = resolution;
        minX = Math.min(p1.getX(), p2.getX());
        minY = Math.min(p1.getY(), p2.getY());
        maxX = Math.max(p1.getX(), p2.getX());
        maxY = Math.max(p1.getY(), p2.getY());
        //
        xJumpsNumber = (int)((maxX - minX) / resolution); //
        yJumpsNumber = (int)((maxY - minY) / resolution);
        this.buildings = buildings;
        if ((maxY - minY) % resolution != 0){
            throw new Exception("BAD BAD BAD");
        }
        if ((maxX - minX) % resolution != 0){
            throw new Exception("BAD BAD BAD");
        }
        pointArray = new Point2D[xJumpsNumber][yJumpsNumber];
        for(int i=0; i<xJumpsNumber; i++)
            for(int j=0; j<yJumpsNumber; j++)
                pointArray[i][j] = new Point2D(minX +i*resolution, minY+i*resolution);
    }

    public Point2D getPointfromGrid(int i, int j)
    {
        return pointArray[i][j];
    }
    public int getXdimmension()
    {
        return pointArray.length;
    }
    public int getYdimmension()
    {
        return pointArray[0].length;
    }

    private Point2D getContainingBinCenter(Point2D point){
        //todo roi do it
        //given arbitrary point, find nearest center of bin.
        return null;
    }

    private Set<Point2D> getCornersOfBin(Point2D binCenter){
        //todo roi - find points with resolution/2
        //given bin center, return 4 corders of bin's grid.
        int halfRes = (int)((double)this.resolution / 2);
        Set<Point2D> result = new HashSet<>();
        result.add(pointArray[(int)(binCenter.getX() - halfRes) / resolution][(int)(binCenter.getY() - halfRes) / resolution]);
        result.add(pointArray[(int)binCenter.getX() + halfRes][(int)binCenter.getY() - halfRes]);
        result.add(pointArray[(int)binCenter.getX() - halfRes][(int)binCenter.getY() + halfRes]);
        result.add(pointArray[(int)binCenter.getX() + halfRes][(int)binCenter.getY() + halfRes]);
        return null;
    }

    private Map<Integer, Set<Building>> calculateBuildingsLOSPerAzimuth(Set<Point2D> binCorners, int azimuthInterval) throws Exception {
        if (azimuthInterval % 360 != 0){
            throw new Exception("BAD BAD BAD");
        }
        Map<Integer, Set<Building>> result = new HashMap<Integer, Set<Building>>();
        for (int currAz = 0; currAz < 360; currAz += azimuthInterval){
            result.put(currAz, calcBuildingsLos(binCorners, currAz));

        }
        return result;

    }

    private Set<Building> calcBuildingsLos(Set<Point2D> binCorners, int currAz) {
        Set<Building> result = new HashSet<Building>();
        for (Point2D currPoint : binCorners){
            result.add(getBuildingsIntersecting(currAz, currPoint));
        }
        return result;
    }

    private Building getBuildingsIntersecting(int currAz, Point2D currPoint) {
        //todo roi: use this.buildings and input info to calc correct buildings
        return null;
    }

}
