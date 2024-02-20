package Algorithm.lookup;

import Algorithm.LosAlgorithm;
import GNSS.Sat;
import Geometry.Building;
import Geometry.Point2D;
import Utils.Relation;

import java.util.List;
import java.util.Set;

/**
 * Created by Roi on 1/19/2015.
 */
public class LookupTable {

    Relation<Point2D, Integer, Set<Building>> relation;

    public LookupTable(List<Building> allBuildings, Grid grid){
        relation = new Relation<Point2D, Integer, Set<Building>>();
        fillTable(allBuildings, grid);
    }

    private void fillTable(List<Building> allBuildings, Grid grid) {
        //todo roi- get all point2Ds from grid, put in relation with possible azimuths and correct buildings
        int xDim = grid.getXdimmension();
        int yDim = grid.getYdimmension();
        int azimutResolution = 6; //in degrees
        for(int az = azimutResolution/2; az <=360-azimutResolution/2; az+=azimutResolution)//run of all 72 (360/5) diffrent angles
        for(int i=0; i<xDim; i++)
            for(int j=0; j<yDim; j++)
            {
                Point2D tmpPoint = grid.getPointfromGrid(i,j);
                relation.setValue(tmpPoint, az, LosAlgorithm.findBuildings(tmpPoint, az, allBuildings, azimutResolution));
            }

        //Point2D point = new Point2D(0, 0);
        //int az = 180;
        //relation.setValue(point, az, LosAlgorithm.findBuildings(point, az, buildings));
    }

    public Set<Building> getBuildings(Point2D me, Sat sat){
       //Point2D centerBin  =
        return null;
    }
}
