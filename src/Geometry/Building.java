package Geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Roi on 1/7/2015.
 * Collection of walls
 */
public class Building {

    String buildingName;
    List<Wall> walls;
    double maxHeigth;
    List<Point3D> BuildindVertices;

    public Building(List<Point3D> buildingVertecies)
    {
        this.walls = new ArrayList<Wall>();
        this.maxHeigth = 0;
        this.buildingName = "";
        this.BuildindVertices = buildingVertecies;
        init(buildingVertecies);
    }

    private void generateBuildingFromPoint3dList(List<Point3D> buildingVertecies)
    {
        int i;
        for(i=0; i<buildingVertecies.size()-1; i++) {
            Wall tmp = new Wall(buildingVertecies.get(i), buildingVertecies.get(i + 1));
            walls.add(tmp);
        }
        Wall tmp2 = new Wall(buildingVertecies.get(i), buildingVertecies.get(0));
        walls.add(tmp2);
        this.BuildindVertices.add(this.BuildindVertices.get(0));
    }

    public List<Point3D> getBuildindVertices() {
        return BuildindVertices;
    }

    public boolean isContain(Point3D pos) // if the point lies in the building, return true;
    {
        return false;
    }
    private void init(List<Point3D> buildingVertecies){
        generateBuildingFromPoint3dList(buildingVertecies);
        setMaxHeight();
    }


    public boolean isPoint2D_inBuilding(Point2D pos)
    {

        boolean ans = true;
        int a = pos.pointLineTest(this.getBuildindVertices().get(0), this.getBuildindVertices().get(1));
//        System.out.println("a-"+a);
        for(int i=1;i<this.getBuildindVertices().size()-1;i++) {
            int b = pos.pointLineTest(this.getBuildindVertices().get(i), this.getBuildindVertices().get(i+1));
//            System.out.println("b-"+b);
            if(a!=b) {
                ans = false;
            }
        }
//        System.out.println(ans);
        return ans;
    }


    public List<Wall> getWalls() {
        return walls;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public Integer getNumberOfWalls() {
        return walls.size();
    }

    public double getMaxHeight() {
        return maxHeigth;
    }

    private void setMaxHeight()
    {
        maxHeigth = Collections.max(walls, new Comparator<Wall>() {
            @Override
            public int compare(Wall o1, Wall o2) {
                return Double.compare(o1.getMaxHeight(), o2.getMaxHeight());
            }
        }).getMaxHeight();
    }
}