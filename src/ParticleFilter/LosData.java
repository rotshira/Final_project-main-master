package ParticleFilter;

import Algorithm.LosAlgorithm;
import GNSS.Sat;
import Geometry.Building;
import Geometry.Point3D;

import ML_Los_Nlos_Classifier.LOSPredictor;
import weka.classifiers.Classifier;
import java.util.List;


import java.util.*;

/**
 * Created by Roi on 5/23/2016.
 */
public class LosData {

    private static LOSPredictor predictor;
    private Map<Integer, Point3D> timeToLoc;
    private  Map<Point3D, Boolean[]> locToLos;

    public  LosData(List<Building> buildings, List<Point3D> path , List<Sat> satellites){
        timeToLoc = new HashMap<Integer, Point3D>();
        locToLos = new HashMap<Point3D, Boolean[]>();
        init(buildings, path, satellites);
    }

    public LosData(Classifier classifier) {
        // Initialize LOS predictor with the trained classifier
        predictor = new LOSPredictor(classifier);
    }

    // Modified LOS function with machine learning
    public static boolean[] los(List<Sat> satellites, Particles particles) {
        try {
            System.out.println("Satellite count: " + satellites.size());
            return predictor.predictLOS(satellites, particles);
        } catch (Exception e) {
            e.printStackTrace();
            return new boolean[satellites.size()];
        }
    }




    // if intersection== null, there is no intersection poin3D, hence there is a LOS to sattelite.
    //so if los retun true, there is lOS
    /**
     This function takes a satellite (sat), a point (pos), and a list of buildings (roi) as parameters.
     It calls LosAlgorithm.ComputeLos function to determine if there's LOS between the satellite and the point within the region of interest specified by the buildings.
     It returns the result of the LOS computation.
     */
    public static boolean los(Sat sat ,Point3D pos, List<Building> roi) {

        boolean LOS = LosAlgorithm.ComputeLos(pos, roi, sat);
        return LOS;
    }
    /**
     This function takes a list of satellites (allsats), a point (pos), and a list of buildings (bs) as parameters.
     It iterates over each satellite in the allsats list.
     For each satellite, it calls the los function to check if there's LOS between the satellite and the point (pos) with respect to the buildings (bs).
     If LOS is blocked for any satellite, it returns false immediately, indicating that there is no LOS for at least one satellite.
     If LOS is clear for all satellites, it returns true, indicating that there is LOS for all satellites.
     */
    public static boolean MachineLearningLOS(List<Sat> allsats, Point3D pos, List<Building> bs)
    {
        boolean tmp=true;
        for(Sat sat: allsats)
        {
            tmp= los(sat, pos, bs);
            if(tmp==false)
                return tmp;

        }
        return tmp;
    }

    // For each location in the `path`, it calculates the LOS to each satellite and stores the results.
    private void init(List<Building> buildings, List<Point3D> path, List<Sat> satellites) {
        for (int i = 0; i< path.size(); i++){
            Point3D loc = path.get(i);
            Boolean[] los = new Boolean[satellites.size()];
            for (Sat sat : satellites){
                los[sat.getSatID()] = los(sat, loc, buildings);
            }
            timeToLoc.put(i, loc);
            locToLos.put(loc, los);
        }
    }

    public Set<Integer> getAllTimes(){
        return timeToLoc.keySet();
    }

    public Set<Point3D> getAllLocs(){
        return locToLos.keySet();
    }

    public Boolean[] getSatData(Point3D loc){
        return locToLos.get(loc);
    }

    public Boolean[] getSatData(Integer time){
        return locToLos.get(timeToLoc.get(time));
    }

    public Point3D getLoc(int time){
        return timeToLoc.get(time);
}

}
