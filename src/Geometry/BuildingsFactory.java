package Geometry;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
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
        //System.out.println("size: "+Size);
        if(buildingVertices.get(Size-1).equals(buildingVertices.get(0))){
            buildingVertices.remove(Size-1); //remove the last point since it similar to the first point
        }
        else{
            throw new Exception("Bad KML File");
        }
        return new Building(buildingVertices);
}
    public static List<Point3D> parseKML(String filePath) {
        List<Point3D> points = new ArrayList<>();

        try {
            File file = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new FileInputStream(file));

            doc.getDocumentElement().normalize();

            NodeList coordinatesList = doc.getElementsByTagName("coordinates");

            for (int i = 0; i < coordinatesList.getLength(); i++) {
                Node coordinatesNode = coordinatesList.item(i);
                String coordinates = coordinatesNode.getTextContent().trim();

                // Split coordinates by newline character to get individual points
                String[] pointsArray = coordinates.split("\\s+");

                for (String pointStr : pointsArray) {
                    String[] coordinatesArr = pointStr.split(",");
                    if (coordinatesArr.length >= 3) {
                        double x = Double.parseDouble(coordinatesArr[0]);
                        double y = Double.parseDouble(coordinatesArr[1]);
                        double z = Double.parseDouble(coordinatesArr[2]);
                        points.add(new Point3D(x, y, z));
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return points;
    }






}