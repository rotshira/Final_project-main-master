package Parsing.stm;

import dataStructres.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Roi on 22/02/2015.
 */
public class STMcsvWriter {

    private static String header = "UTC Time, Latitude, Longitude, Altitude, Height over elipsoid, HDOP, SVs";
    private static String newLine = "\r\n";

    static {
        for (int i = 0; i < 20; i++) {
            header += ", PRN, Azimuth, Elevation, S/Nr,Xpos, Ypos, Zpos, Xvel, Yvel, Zvel, freq, PR";
        }

    }

    public static void printStmToFile(List<STMPeriodMeasurment> measurements, String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path + "parsed.csv"));

        writer.write(header);
        writer.write(newLine);
        Set<Integer> allPrns = new TreeSet<Integer>();

        for (STMPeriodMeasurment m : measurements) {
            allPrns.addAll(m.getAllPRNs());
        }
        for (int i = 0; i < measurements.size(); i++) {
            String csvString = toCsvString((measurements.get(i)), allPrns);
            writer.write(csvString);
        }
        writer.flush();
        writer.close();


    }

    private static String toCsvString(STMPeriodMeasurment meas, Set<Integer> allPrns) {
        String res = "";
        if (meas.getTime() == 0l) {
            return res;
        }
        res += meas.getTime() + ",";
        res += meas.getLat() + ",";
        res += meas.getLon() + ",";
        res += meas.getAlt() + ",";
        res += meas.getAltElip() + ",";
        res += meas.getHDOP() + ",";
        res += meas.getAllSvMeasurements().size() + ", ";

        Map<Integer, STMSVMeasurement> mappedSvMeasurements = meas.getMappedSvMeasurements();
        //  Map<Integer, STMSVMeasurement> mappedSvMeasurements = meas.getMappedSvMeasurement();
        for (Integer key : mappedSvMeasurements.keySet()) {
            STMSVMeasurement stmSvMeasurement = mappedSvMeasurements.get(key);
            double correctedPR = stmSvMeasurement.getCorrectedPR();
            System.out.println(key + "\t " + correctedPR);

            if (stmSvMeasurement == null) {
                stmSvMeasurement = stmSvMeasurement.nullMeas;

                res += stmSvMeasurement.getPrn() + ",";
                res += stmSvMeasurement.getAz() + ",";
                res += stmSvMeasurement.getEl() + ",";
                res += stmSvMeasurement.getSnr() + ",";
                res += stmSvMeasurement.getEcefXpos() + ",";
                res += stmSvMeasurement.getEcefYpos() + ",";
                res += stmSvMeasurement.getEcefZpos() + ",";
                res += stmSvMeasurement.getEcefXvel() + ",";
                res += stmSvMeasurement.getEcefYvel() + ",";
                res += stmSvMeasurement.getEcefZpos() + ",";
                res += stmSvMeasurement.getFrequncy() + ",";
                res += stmSvMeasurement.getCorrectedPR() + ",";
                res += "\r\n";
            }
        }
        return res;

    }//end of function (toCsvString)
}



