package Algorithm;

import GNSS.Sat;
import Geometry.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Roi on 1/7/2015.
 * Those set of functions return true for a LOS sattelite and false for NLOS satelite.
 */
public class LosAlgorithm {


    public static void main(String[] args) {
//        ourtest1();
//        ourtest2();

    }

    private static void ourtest1() {
        Point3D p1 = new Point3D(0, 0, 0);
        Point3D p2 = new Point3D(10, 10, 10);
        Wall wall = new Wall(p1, p2);

        Sat sat = new Sat(45, 45, 0);

        Point3D pos = new Point3D(1, 1, 1);
        double losDistance = ourComputeLos(pos, wall, sat);

        System.out.println("Distance from intersection point to top of wall: " + losDistance);

    }
    private static void ourtest2() {
        // Create a sample building with walls
        Point3D p1 = new Point3D(0, 0, 0);
        Point3D p2 = new Point3D(10, 10, 10);
        Point3D p3 = new Point3D(20, 20, 20);
        Point3D p4 = new Point3D(30, 30, 30);
        List<Point3D> temp = new ArrayList<>();
        temp.add(p1);
        temp.add(p2);
        temp.add(p3);
        temp.add(p4);
        Building building = new Building(temp);

        // Create a sample satellite
        Sat sat = new Sat(45, 45, 0); // Sample azimuth and elevation angles
        // Create a sample observer position
        Point3D pos = new Point3D(0, 0, 35); // Sample observer position
        // Test the ourComputeLos function
        double losDistance = ourComputeLos(pos, building, sat);
        System.out.println("Line-of-sight distance to top of building: " + losDistance);



    }

    //Computes LOS between a point, a wall, and a satellite.
    public static boolean ComputeLos(Point3D pos, Wall wall, Sat sat)
    {
        Line3D ray = new Line3D(pos, sat.getAzimuth(),sat.getElevetion(), 300);
        boolean ans = wall.isIntersecting(ray);
        if(ans==true) // wall is intersection, hence NLOS
            return !ans;
        return !ans; //wall does not intersecting, hence LOS
    }
    /**
     receives a wall and satellite point,
     You find the line between the satellite and the point and calculate whether there is a point of intersection between this line and the wall.
     If none, returns -1
     If there is, returns the distance between the intersection point and the height of the wall.
     */
    public static double ourComputeLos(Point3D pos, Wall wall, Sat sat){
        Line3D ray = new Line3D(pos, sat.getAzimuth(),sat.getElevetion(), 300);

        Point3D cutPoint = wall.intersectionPoint3D(ray); // Intersection point between a wall and the ray
        if(cutPoint == null) {
            return -1; // No intersection, return -1
        } else {
            return wall.distanceToTop(cutPoint); // Calculate and return distance to top of the wall
        }
    }




    //Computes LOS between a point, a building, and a satellite by iterating over the walls of the building.
    public static boolean ComputeLos(Point3D pos, Building building, Sat sat)
    {
        for(Wall wall : building.getWalls())
        {
            if(!ComputeLos(pos, wall, sat))
                return false;
        }
        return true;
    }

    /**
     * Computes the maximum Line of Sight (LOS) distance from a given point to the top of a building,
     *
     * @param pos The position of the observer (Point3D object).
     * @param building The building object representing the structure.
     * @param sat The satellite object used for LOS calculations.
     * @return The maximum LOS distance to the top of the building.
     */
    public static double ourComputeLos(Point3D pos, Building building, Sat sat)
    {
        double max_distanceToTop = Integer.MIN_VALUE;
        for(Wall wall : building.getWalls())
        {
            // Compute the LOS distance from the observer's position to the current wall.
            double distance = ourComputeLos(pos, wall, sat);
            if((distance != -1) && (distance > max_distanceToTop)){
                max_distanceToTop = distance;

            }
        }

        return max_distanceToTop == Integer.MIN_VALUE ? -1: max_distanceToTop;
    }

    //Computes LOS between a point, a list of buildings, and a satellite by iterating over the buildings and calling the previous function.
    public static boolean ComputeLos(Point3D pos,List<Building> buildings, Sat sat)
    {
        for(Building building : buildings)
        {
            if(!ComputeLos(pos, building, sat))
                return false;
        }
        return true;
    }

    /**
     * Computes the maximum Line of Sight (LOS) distance from a given point to the top of multiple buildings,
     *
     * @param pos The position of the observer (Point3D object).
     * @param buildings A list of Building objects representing the structures.
     * @param sat The satellite object used for LOS calculations.
     * @return The maximum LOS distance to the top of any building in the list.
     */
    public static double ourComputeLos(Point3D pos, List<Building> buildings, Sat sat)
    {
        double max_distanceToTop = Integer.MIN_VALUE;
        for(Building building : buildings)
        {
            // Compute the LOS distance from the observer's position to the top of the current building.
            double distance  = ourComputeLos(pos, building, sat);
            if((distance != -1) && (distance > max_distanceToTop))
                max_distanceToTop = distance;
        }
        return max_distanceToTop == Integer.MIN_VALUE ? -1: max_distanceToTop;
    }




    public static Set<Building> findBuildings(Point2D base, double az, List<Building> allBuildings, int azimutResolution) {

        double minAz = az-azimutResolution/2;
        double maxAz = az + azimutResolution/2;
        Set<Building> resultSet = new HashSet<>();
        boolean added;
        for (Building building : allBuildings) {
            added = false;
            List<Wall> walls = building.getWalls();
            for (Wall wall : walls) {
                Point3D[] point3dArray = wall.getPoint3dArray();
                for (Point3D point3D : point3dArray) {
                    double angRandians = Math.atan2(point3D.getY() - base.getY(), point3D.getX() - base.getX());
                    double angDegrees = Math.toDegrees(angRandians);
                    if (angDegrees < 0){
                        angDegrees += 360;
                    }
                    if (angDegrees < 0 || angDegrees >= 360){
                        assert false;
                    }
                    double angDegNorthHead = 450 - angDegrees;
                    if (angDegNorthHead >= 360){
                        angDegNorthHead -= 360;
                    }
                    if (angDegNorthHead < 0 || angDegNorthHead >= 360){
                        assert false;
                    }
                    if (angDegNorthHead <= maxAz && angDegNorthHead >= minAz){
                        resultSet.add(building);
                        added = true;
                    }
                    if (added){
                        break;
                    }
                }
                if (added){
                    break;
                }
            }
        }
        return resultSet;
    }
}