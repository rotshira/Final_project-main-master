package Parsing.SVM;

import Parsing.sirf.SirfProtocolParser;
import dataStructres.SVMachineLearningData;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Roi
 * Date: 08/04/14
 * Time: 17:17
 * To change this template use File | Settings | File Templates.
 */
public class SVMCSVWriter {
    private static String newLine = "\r\n";



    public static void printToFile(List<SVMachineLearningData> measurements, String path) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(path + ".csv"));
        writer.write(newLine);
        List<Integer> parsedPrnsSorted = SirfProtocolParser.getParsedPrnsSorted();

        for (int i = 0; i<measurements.size(); i++)
        {
            measurements.get(i).ScaleSimpleData(); //Here we do the scaling
            String csvString = toCsvString(measurements.get(i));
            writer.write(csvString);
        }

        writer.flush();
        writer.close();
        System.out.println("Done creating an SVM file");

    }


    private static String toCsvString(SVMachineLearningData meas ) {
        String res = "";
        //if (meas.getTime() == 0l){
        //	return res;
        //}
        res += meas.getGNSS_Type() + ",";
        res += meas.getAzimuth() + ",";
        res += meas.getElevation() + ",";
        res += meas.getHdop() + ",";
        res += meas.getPseudoRange() + ",";
        res += meas.getComputedRange() + ",";
        res += meas.getClock_drift() + ",";
        res += meas.getClockBias() + ",";
        for (int cno = 0; cno < 10; cno ++)
        {

            res += meas.getCNo()[cno] + ",";
            res += meas.getFilteredCNo()[cno] + ",";
            res += meas.getOldCNo()[cno] + ",";


         }
         res += "\r\n";

        return res;

    }

}
