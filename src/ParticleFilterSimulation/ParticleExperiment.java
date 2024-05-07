package ParticleFilterSimulation;

import GNSS.Sat;
import Geometry.Building;
import Geometry.BuildingsFactory;
import Geometry.Point3D;
import Parsing.nmea.NMEAProtocolParser;
import ParticleFilter.Particles;
import ParticleFilter.UtilsAlgorithms;
import dataStructres.NMEAPeriodicMeasurement;
import dataStructres.SirfPeriodicMeasurement;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roi on 5/8/2016.
 */
public class ParticleExperiment {

    public static void main(String[] args) {

        Test_Naive_Boolean();
    }

    public static void Test_Naive_Boolean() {

        String buildingFilePath = "src\\ParticleFilterSimulation\\EsriBuildingsBursaNoindentWithBoazBuilding.kml";
        BuildingsFactory fact = new BuildingsFactory();

        List<Building> buildings1 = null;
        try {
            buildings1 = fact.generateUTMBuildingListfromKMLfile(buildingFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Point3D pivot = new Point3D(670053, 3551100, 1);
        Point3D pivot2 = new Point3D(pivot);
        pivot2.offset(100, 100, 0);

        //Initialization of particles.
        Particles ParticleList = new Particles();
        ParticleList.initParticlesWithHeading(pivot, pivot2);

        NMEAProtocolParser parser = new NMEAProtocolParser();
        String NMEAFilePath = "route_abcd_twice.gps";
        List<NMEAPeriodicMeasurement> NMEAMeas = null;
        try {
            NMEAMeas = parser.parse(NMEAFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Number of Instncec : " + NMEAMeas.size());

        List<Sat> allSats;
        for(NMEAPeriodicMeasurement meas : NMEAMeas)
        {
           allSats = UtilsAlgorithms.GetUpdateSatList(meas);
            ParticleList.getNaiveLosNlosState(meas);

        }
    }

}
