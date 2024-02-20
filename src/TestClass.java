import Geometry.Building;
import Geometry.BuildingsFactory;
import Parsing.stm.STMProtocolParser;
import dataStructres.STMPeriodMeasurment;
import dataStructres.STMSVMeasurement;

import java.io.IOException;
import java.util.List;

/**
 * Created by Roi on 1/6/2015.
 */
public class TestClass {

    public static void main(String[] args) throws IOException {


      //  BuildingFactoryTest();
       // STMParserTest();




    }

  /*  public static void STMParserTest() throws IOException {
        String NMEAFilePath = "STMsampleFile.txt";
        List<STMPeriodMeasurment> meas = STMProtocolParser.parse(NMEAFilePath);//todo ayal : why the error?
        System.out.println("SSIZE  OF LIST = " + meas.size());
        for(int i=0; i<meas.size();  i++)
        {
            List<STMSVMeasurement> satList = meas.get(i).getSVs();
            System.out.println("TimeStamp no "+i);
            for(STMSVMeasurement SV : satList)
            {
                System.out.println("PRN: "+SV.getPrn()+" . PSeudoRange : "+SV.getCorrectedPR());
            }

        }

    }
    */

    public static void BuildingFactoryTest()
    {
        String buildingFilePath = "bursa_mapping_v0.3.kml";
        System.out.println("The program begins");
        BuildingsFactory fact = new BuildingsFactory();
        try {
            List<Building> buildings1 = fact.generateUTMBuildingListfromKMLfile(buildingFilePath);
            System.out.println(buildings1.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
