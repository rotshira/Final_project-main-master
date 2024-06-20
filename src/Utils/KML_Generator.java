package Utils;

import GNSS.Sat;
import Geometry.Building;
import Geometry.BuildingsFactory;
import Geometry.Point3D;
import Geometry.Wall;
import ParticleFilter.Particle;
import ParticleFilter.Particles;
import ParticleFilter.UtilsAlgorithms;
import ParticleFilter.test_our_function;
import Utils.GeoUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roi on 5/23/2016.
 */

public class KML_Generator
{
    private static final double EARTH_RADIUS = 6378137; // in meters

    // Calculate the endpoint given a start point, azimuth, and elevation
    public static Point3D OurCalculateEndpoint(Point3D start, double azimuth, double elevation, double distance) {
        double azimuthRad = Math.toRadians(azimuth);
        double elevationRad = Math.toRadians(elevation);

        // Calculate changes in coordinates based on azimuth and elevation
        double dx = distance * Math.cos(elevationRad) * Math.cos(azimuthRad);
        double dy = distance * Math.cos(elevationRad) * Math.sin(azimuthRad);
        double dz = distance * Math.sin(elevationRad);

        // Calculate endpoint in Cartesian coordinates
        double x = start.getX() + dx;
        double y = start.getY() + dy;
        double z = start.getZ() + dz;

        return new Point3D(x, y, z);
    }
    public static String OurBuildKml(List<Point3D> points, List<Sat> satellites) {
        StringBuilder kml = new StringBuilder();
        kml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        kml.append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n");
        kml.append("<Document>\n");

        // Define the style for the lines
        kml.append("<Style id=\"blueLine\">\n");
        kml.append("<LineStyle>\n");
        kml.append("<color>ff0000ff</color> <!-- Blue color in aabbggrr format -->\n");
        kml.append("<width>4</width> <!-- Thicker line -->\n");
        kml.append("</LineStyle>\n");
        kml.append("</Style>\n");



        // Add points and satellite lines
        for (Point3D point : points) {
            for (Sat satellite : satellites) {
                Point3D endpoint = OurCalculateEndpoint(point, satellite.getAzimuth(), satellite.getElevetion(), 10); // assuming 10km lines
                System.out.println("the endpoint is: "+endpoint);

                kml.append("<Placemark>\n");
                kml.append("<styleUrl>#blueLine</styleUrl>\n"); // Reference the blue line style
                kml.append("<LineString>\n");
                kml.append("<altitudeMode>relativeToGround</altitudeMode>\n");
                kml.append("<coordinates>\n");
                kml.append(point.getX()).append(",").append(point.getY()).append(",").append(point.getZ()).append(" ");
                kml.append(endpoint.getX()).append(",").append(endpoint.getY()).append(",").append(endpoint.getZ()).append("\n");
                kml.append("</coordinates>\n");
                kml.append("</LineString>\n");
                kml.append("</Placemark>\n");
            }
        }

        kml.append("</Document>\n");
        kml.append("</kml>\n");

        return kml.toString();
    }

    public static void OurWriteKmlToFile(String kml, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(kml);
        }
    }



    public static void Generate_kml_from_ParticleList(Particles Particles, String FilePath, int MaxSat)
    {
        String line= new String("");


        int i=0;
        double  lat,lon;
        Point3D tmp;
        FileWriter fstream;
        try {
            fstream = new FileWriter(FilePath);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("<Document>\n");
            out.write("<Style id=\"redred\">\n");
            out.write("<IconStyle>\n");
            out.write("<color>00ff00ff</color>\n<scale>0.5</scale>\n</IconStyle>\n");

            out.write("</Style>\n");
            ////////////////////////////////////////////////////////////////////////////////////

            out.write("\n<Style id=\"poly\">\n<LineStyle>\n<width>2.7</width>\n</LineStyle>\n");
            out.write("<PolyStyle>\n<fill>0</fill>\n</PolyStyle>\n</Style>\n ");
            ////////////////////////////////////////////////////////////////////////////////////////
            out.write("\n<Style id=\"fill_poly\">\n<LineStyle>\n<width>2.7</width>\n</LineStyle>\n");
            out.write("<PolyStyle>\n<fill>1</fill>\n</PolyStyle>\n</Style>\n ");
            /////////////////////////////////////////////////////////////////////////////////
            out.write("\n\n<Style id=\"red\">\n");
            out.write("<IconStyle>\n");
            out.write("<color>ff0000ff</color>\n<scale>0.5</scale>\n</IconStyle>\n");
            out.write("</Style>\n");

            out.write("\n\n<Style id=\"blue\">\n");
            out.write("<IconStyle>\n");
            out.write("<color>ffff0000</color>\n<scale>0.5</scale>\n</IconStyle>\n");
            out.write("</Style>\n");

            out.write("\n\n<Style id=\"green\">\n");
            out.write("<IconStyle>\n");
            out.write("<color>ff00ff00</color>\n<scale>0.5</scale>\n</IconStyle>\n");
            out.write("</Style>\n");

            out.write("\n\n<Style id=\"yellow\">\n");
            out.write("<IconStyle>\n");
            out.write("<color>ffffffff</color>\n<scale>0.5</scale>\n</IconStyle>\n");
            out.write("</Style>\n");





            // System.out.println("");
            int NumberOfMatchingSats;
            //out.write("")
            for (i=0; i<Particles.getParticleList().size(); i++)
            {

                out.write("\n\n<Placemark>\n");
                tmp=Particles.getParticleList().get(i).pos;
                tmp=GeoUtils.convertUTMtoLATLON(tmp, 36);
                lat=tmp.getX();
                lon=tmp.getY();
                line=Double.toString(lon)+" "+Double.toString(lat)+" "+tmp.getZ();
                NumberOfMatchingSats = Particles.getParticleList().get(i).getNumberOfMatchedSats();
                int MaxiSat = MaxSat;

                if(NumberOfMatchingSats==MaxiSat)
                    out.write(" <styleUrl>#green</styleUrl>\n");

                else if(NumberOfMatchingSats==(MaxiSat-1))
                    out.write(" <styleUrl>#yellow</styleUrl>\n");

                else if(NumberOfMatchingSats==(MaxiSat-2))
                    out.write(" <styleUrl>#blue</styleUrl>\n");

                else if(NumberOfMatchingSats<(MaxiSat-3))
                    out.write(" <styleUrl>#red</styleUrl>\n");
                String los = "";
                if (Particles.getParticleList().get(i).LOS != null) {
                    for (int j = 0; j < MaxiSat; j++) {
                        los += Particles.getParticleList().get(i).LOS[j]+", ";
                    }
                }



                //out.write("<Style>\n<BalloonStyle>\n<text>This point was taken at time "+ i +"</text>\n</BalloonStyle>\n</Style>\n ");
                //out.write("<Style>\n<BalloonStyle>\n<text>His OutOfRegion is: "+Particles.getParticleList().get(i).OutOfRegion  +"</text>\n</BalloonStyle>\n</Style>\n ");
                //out.write("<Style>\n<BalloonStyle>\n<text>Los:  "+ los +"</text>\n</BalloonStyle>\n</Style>\n ");
                out.write("<Style>\n<BalloonStyle>\n<text>NumberOfMatchingSats:  "+ NumberOfMatchingSats +"</text>\n</BalloonStyle>\n</Style>\n ");
                out.write("<TimeStamp>\n");
                out.write("<when>"+i+"</when>\n");
                out.write(" </TimeStamp>\n");
                out.write("<Point>\n<altitudeMode>relativeToGround</altitudeMode>\n<coordinates>");

                out.write(line);

                out.write("</coordinates>\n");
                out.write("</Point>\n</Placemark>");

            }//end of for
            out.write("</Document>");

            //Close the output stream
            out.close();
            // System.out.println( "The File was created succsefuly");
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();}
    }// end of main


    public static void Generate_kml_from_List(List<Point3D> PointList, String FilePath) {

        Generate_kml_from_List( PointList,  FilePath, false);
    }
    public static void Generate_kml_from_List(List<Point3D> PointList, String FilePath, Boolean isPointsInLATLON)
    {
        String line= new String("");


        int i=0;
        double  lat,lon;
        Point3D tmp;
        FileWriter fstream;
        try {
            fstream = new FileWriter(FilePath);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("<Document>\n");
            out.write("<Style id=\"redred\">\n");
            out.write("<IconStyle>\n");
            out.write("<color>00ff00ff</color>\n<scale>0.5</scale>\n</IconStyle>\n");

            out.write("</Style>\n");
            ////////////////////////////////////////////////////////////////////////////////////

            out.write("\n<Style id=\"poly\">\n<LineStyle>\n<width>2.7</width>\n</LineStyle>\n");
            out.write("<PolyStyle>\n<fill>0</fill>\n</PolyStyle>\n</Style>\n ");
            ////////////////////////////////////////////////////////////////////////////////////////
            out.write("\n<Style id=\"fill_poly\">\n<LineStyle>\n<width>2.7</width>\n</LineStyle>\n");
            out.write("<PolyStyle>\n<fill>1</fill>\n</PolyStyle>\n</Style>\n ");
            /////////////////////////////////////////////////////////////////////////////////
            out.write("\n\n<Style id=\"green\">\n");
            out.write("<IconStyle>\n");
            out.write("<color>ff0000ff</color>\n<scale>0.5</scale>\n</IconStyle>\n");
            out.write("</Style>\n");








            System.out.println("");

            //out.write("")
            for (i=0; i<PointList.size(); i++)
            {
                out.write("\n\n<Placemark>\n");
                //	if(i%2==0)

                //	out.write(" <styleUrl>#green</styleUrl>\n");
                //	else
                out.write(" <styleUrl>#green</styleUrl>\n");
                out.write("<Style>\n<BalloonStyle>\n<text>This point was taken at time " + i + " with coordinates (x: " + PointList.get(i).getX() + ", y: " + PointList.get(i).getY() + ", z: " + PointList.get(i).getZ() + ")</text>\n</BalloonStyle>\n</Style>\n");
                out.write("<TimeStamp>\n");
                out.write("<when>"+i+"</when>\n");
                out.write(" </TimeStamp>\n");
                out.write("<Point>\n<altitudeMode>relativeToGround</altitudeMode>\n<coordinates>");
                tmp=PointList.get(i);
                // tmp=Algo0.convertToLatLon(PointList.get(i), 36);
               // tmp = GeoUtils.convertECEFtoLATLON(PointList.get(i));
                if(!isPointsInLATLON)
                    tmp = GeoUtils.convertUTMtoLATLON(PointList.get(i), 36);
                lat=tmp.getX();
                lon=tmp.getY();
                line=Double.toString(lon)+" "+Double.toString(lat)+" "+tmp.getZ();
                out.write(line);

                out.write("</coordinates>\n");
                out.write("</Point>\n</Placemark>");

            }//end of for
            out.write("</Document>");

            //Close the output stream
            out.close();
            //System.out.println( "The File was created succsefuly");
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();}
    }// end of main


    public static void generateKMLfromList(List<List<Point3D>> pointInTime, String FilePath) {

        String line= new String("");


        int i=0;
        double  lat,lon;
        Point3D tmp;
        FileWriter fstream=null;
        try {
            fstream = new FileWriter(FilePath);

        BufferedWriter out = new BufferedWriter(fstream);
            out.write("<Document>\n");
            out.write("<Style id=\"redred\">\n");
            out.write("<IconStyle>\n");
            out.write("<color>00ff00ff</color>\n<scale>0.5</scale>\n</IconStyle>\n");

            out.write("</Style>\n");
            ////////////////////////////////////////////////////////////////////////////////////

            out.write("\n<Style id=\"poly\">\n<LineStyle>\n<width>2.7</width>\n</LineStyle>\n");
            out.write("<PolyStyle>\n<fill>0</fill>\n</PolyStyle>\n</Style>\n ");
            ////////////////////////////////////////////////////////////////////////////////////////
            out.write("\n<Style id=\"fill_poly\">\n<LineStyle>\n<width>2.7</width>\n</LineStyle>\n");
            out.write("<PolyStyle>\n<fill>1</fill>\n</PolyStyle>\n</Style>\n ");
            /////////////////////////////////////////////////////////////////////////////////
            out.write("\n\n<Style id=\"green\">\n");
            out.write("<IconStyle>\n");
            out.write("<color>ff0000ff</color>\n<scale>0.5</scale>\n</IconStyle>\n");
            out.write("</Style>\n");

            for(int j=0; j<pointInTime.size();j++) {

                for (int t = 0; t < pointInTime.get(j).size(); t++) {
                    out.write("\n\n<Placemark>\n");
                    out.write(" <styleUrl>#green</styleUrl>\n");
                    out.write("<Style>\n<BalloonStyle>\n<text>This point was taken at time " + i + "</text>\n</BalloonStyle>\n</Style>\n ");
                    out.write("<TimeStamp>\n");
                    out.write("<when>" + j + "</when>\n");
                    out.write(" </TimeStamp>\n");
                    out.write("<Point>\n<altitudeMode>relativeToGround</altitudeMode>\n<coordinates>");
                    tmp = pointInTime.get(j).get(t);
                    // tmp=Algo0.convertToLatLon(PointList.get(i), 36);
                    // tmp = GeoUtils.convertECEFtoLATLON(PointList.get(i));
                    tmp = GeoUtils.convertUTMtoLATLON(pointInTime.get(j).get(t), 36);
                    lat = tmp.getX();
                    lon = tmp.getY();
                    line = Double.toString(lon) + " " + Double.toString(lat) + " " + tmp.getZ();
                    out.write(line);
                    out.write("</coordinates>\n");
                    out.write("</Point>\n</Placemark>");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        }

    public static void generateKMLfromParticles(List<Point3D> finalList, String particle_ans_path, int numOfParticles)
    {
         int i=0;
    double  lat,lon;
    Point3D tmp;
    FileWriter fstream=null;
    try {
        fstream = new FileWriter(particle_ans_path);

        BufferedWriter out = new BufferedWriter(fstream);
        out.write("<Document>\n");
        out.write("<Style id=\"redred\">\n");
        out.write("<IconStyle>\n");
        out.write("<color>00ff00ff</color>\n<scale>0.5</scale>\n</IconStyle>\n");

        out.write("</Style>\n");
        ////////////////////////////////////////////////////////////////////////////////////

        out.write("\n<Style id=\"poly\">\n<LineStyle>\n<width>2.7</width>\n</LineStyle>\n");
        out.write("<PolyStyle>\n<fill>0</fill>\n</PolyStyle>\n</Style>\n ");
        ////////////////////////////////////////////////////////////////////////////////////////
        out.write("\n<Style id=\"fill_poly\">\n<LineStyle>\n<width>2.7</width>\n</LineStyle>\n");
        out.write("<PolyStyle>\n<fill>1</fill>\n</PolyStyle>\n</Style>\n ");
        /////////////////////////////////////////////////////////////////////////////////
        out.write("\n\n<Style id=\"green\">\n");
        out.write("<IconStyle>\n");
        out.write("<color>ff0000ff</color>\n<scale>0.5</scale>\n</IconStyle>\n");
        out.write("</Style>\n");
        String line;
        for(int j=0; j<finalList.size();j++) {

            for (int t = 0; t < numOfParticles; t++) {
                out.write("\n\n<Placemark>\n");
                out.write(" <styleUrl>#green</styleUrl>\n");
                out.write("<Style>\n<BalloonStyle>\n<text>This point was taken at time " + i + "</text>\n</BalloonStyle>\n</Style>\n ");
                out.write("<TimeStamp>\n");
                out.write("<when>" + j/10 + "</when>\n");
                out.write(" </TimeStamp>\n");
                out.write("<Point>\n<altitudeMode>relativeToGround</altitudeMode>\n<coordinates>");
                tmp = finalList.get(j);
                // tmp=Algo0.convertToLatLon(PointList.get(i), 36);
                // tmp = GeoUtils.convertECEFtoLATLON(PointList.get(i));
                tmp = GeoUtils.convertUTMtoLATLON(tmp, 36);
                lat = tmp.getX();
                lon = tmp.getY();
                line = Double.toString(lon) + " " + Double.toString(lat) + " " + tmp.getZ();
                out.write(line);
                out.write("</coordinates>\n");
                out.write("</Point>\n</Placemark>");
            }
        }

        out.write("</Document>");

        //Close the output stream
        out.close();
    } catch (IOException e) {
        e.printStackTrace();
    }


}

}
