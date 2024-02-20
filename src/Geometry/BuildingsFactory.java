package Geometry;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roi on 1/7/2015.
 */
public class BuildingsFactory {


    //this function return true is the list was created and false if an error occurs.
    public static List<Building> generateUTMBuildingListfromKMLfile(String file) throws Exception {
        List<Building> buildingList = new ArrayList<Building>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line= "";
            line = reader.readLine();
            while(line!=null)
            {
                if(line.startsWith("<coordinates")) {
                    line = reader.readLine();
                    Building tmpBuilding = generateUTMBuildingFromCordString(line);
                    buildingList.add(tmpBuilding);
                }
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return buildingList;

    }

    private static Building generateUTMBuildingFromCordString(String cordFromKMLString) throws Exception {
        String[] cords = cordFromKMLString.split(",| ");
        List<Point3D> buildingVertices = new ArrayList<Point3D>();
        int size = cords.length;
        int idx=0;
        Double x, y, z;
        while(idx<size-2)
        {
            y = Double.parseDouble(cords[idx]);
            x = Double.parseDouble(cords[idx+1]);
            z= Double.parseDouble(cords[idx+2]);
            Point3D tmpPoint = new Point3D(x, y, z);
//            tmpPoint = GeoUtils.convertLATLONtoUTM(tmpPoint);
            buildingVertices.add(tmpPoint);
            idx+=3;

        }
        int Size = buildingVertices.size();
        if(buildingVertices.get(Size-1).equals(buildingVertices.get(0))){
            buildingVertices.remove(Size-1); //remove the last point since it similar to the first point
        }
        else{
            throw new Exception("Bad KML File");
        }
        return new Building(buildingVertices);
}




}