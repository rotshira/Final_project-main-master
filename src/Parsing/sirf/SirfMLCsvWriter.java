package Parsing.sirf;

import dataStructres.SirfPeriodicMeasurement;
import dataStructres.SirfSVMeasurement;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Roi on 13/04/2015.
 */


public class SirfMLCsvWriter {
    public static final Integer[] isChange = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,00,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};


    private static String newLine = "\r\n";
    private static String header = "UTC Time, X Position, Y Position, Z Position, X Velocity, Y Velocity, Z Velocity, HDOP, Lat, Lon, Alt (Elipsoide), Alt (AMSL), Speed, Course, Estimated Horizontal Positioning Error, Estimated Vertical Positioning Error, Estimated Horizontal Velocity Error, Clock Bias, Clock Bias Error, Clock Drift, Clock Drift Error, EXTENDED GPS Week, GPS TOW, SVs number, Clock Drift 7, Clock Bias 7, Estimated GPS Time, PRN, GPS Software Time, Azimuth, Elevation, State, X Position, Y Position, Z Position, X Velocity, Y Velocity, Z Velocity, Pseudorange, residuals, C/No Max, C/No Min, C/No Delta, MaxCno2Second, ExtremeMaxSnrValue, ExtremeMinSnrValue, Carrier Frequency, Carrier Phase, Previous Carrier Phase, Delta Carrier phase, Delta Range Interval, Mean Delta Range Time, Clock Bias, Clock Drift, Ionospheric Delay, Filtered C/No Max, Filtered C/No Min, Filtered C/No Delta, Corrected PR, PrevoiusCorected PR, deltaCorrected PR, 2nd delta,PS Residual, Los Value" + newLine;
    private static String header4GPgsSwitch = "UTC Time, X Position, Y Position, Z Position, X Velocity, Y Velocity, Z Velocity, HDOP, Lat, Lon, Alt (Elipsoide), Alt (AMSL), Speed, Course, Estimated Horizontal Positioning Error, Estimated Vertical Positioning Error, Estimated Horizontal Velocity Error, Clock Bias, Clock Bias Error, Clock Drift, Clock Drift Error, EXTENDED GPS Week, GPS TOW, SVs number, Clock Drift 7, Clock Bias 7, Estimated GPS Time, PRN, GPS Software Time, Azimuth, Elevation, State, X Position, Y Position, Z Position, X Velocity, Y Velocity, Z Velocity, Pseudorange, C/No 1, C/No 2,  C/No 3, C/No 4, C/No 5, C/No 6, C/No 7, C/No 8, C/No 9, C/No 10, Carrier Frequency, Carrier Phase, Delta Range Interval, Mean Delta Range Time, Clock Bias, Clock Drift, Ionospheric Delay, Filtered C/No Max, Filtered C/No Min, Filtered C/No Delta, Check" + newLine;


    private static String header2 = /*" *//*SVs number,*/" PRN, GPS Software Time, Elevation,"/*+  "State,"  */+ "Is Carrier Lock, residuals, C/No Max, C/No Min, MaxCno2Second,"+
    /*ExtremeMaxSnrValue, ExtremeMinSnrValue*/" Delta Carrier Freq, Delta Carrier phase, Clock Bias, Clock Drift, Delta Ionospheric Delay," +
            " deltaCorrected PR, 2nd delta,PS Residual, Los Value" + newLine;
    private static final int numOfColumsHeade2 =21;

    public static void printToFile(List<SirfPeriodicMeasurement> measurements, String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path + ".csv"));
        writer.write(header);
        //   writer.write(newLine);

        for (int i = 3; i<measurements.size(); i++){
            String csvString = toCsvString(measurements.get(i));
            if (!csvString.equals("0,0,0,0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0,0,0,0,0,0,0,0,0,")){
                writer.write(csvString);
            }
        }
        writer.flush();
        writer.close();

    }

    public static void printToFile4GpsSwitchProject(List<SirfPeriodicMeasurement> measurements, String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path + ".csv"));
        writer.write(header4GPgsSwitch);
        //   writer.write(newLine);

        for (int i = 3; i<measurements.size(); i++){
            String csvString = toCsvString4GpsSwitch(measurements.get(i));
            if (!csvString.equals("0,0,0,0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0,0,0,0,0,0,0,0,0,")){
                writer.write(csvString);
            }
        }
        writer.flush();
        writer.close();

    }

    public static void printToFileSpecificValues(List<SirfPeriodicMeasurement> measurements, String path, int LinesToThrow) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path + ".csv"));
        writer.write(header2);
        //   writer.write(newLine);

       // for (int i = 10; i<measurements.size(); i++){
        for (int i = LinesToThrow; i<measurements.size(); i++){
                String csvString = toCsvRestrictedString(measurements.get(i));
                if (!csvString.equals("0,0,0,0,0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0,0,0,0,0,0,0,0,0,")){
                    writer.write(csvString);
                }

        }
        writer.flush();
        writer.close();

    }
//    " SVs number, PRN, GPS Software Time, Azimuth, Elevation, State, Is Carrier Lock, residuals, C/No Max, C/No Min, MaxCno2Second, ExtremeMaxSnrValue,
// ExtremeMinSnrValue,Delta Carrier Freq, Delta Carrier phase, Clock Bias, Clock Drift, Delta Ionospheric Delay, deltaCorrected abs PR, 2nd delta,PS Residual, Los Value" + newLine;

    private static String toCsvRestrictedString(SirfPeriodicMeasurement meas)
    {
        String res = "";
        for (Integer PRN : meas.getSatellites().keySet()){
            SirfSVMeasurement sv = meas.getSatellites().get(PRN);
            if(IsGoodMeassurment(sv)) {

              //  res += meas.getSVNum() + ",";
                res += PRN + ",";
                res += sv.getGpsSoftwareTime() + ",";
              //  res += sv.getAzimuth() + ",";
                res += sv.getElevation() + ",";
             //   res += sv.getState() + ",";
                res += sv.ifCarrierPhaseLock() + ",";
                res += meas.getResidual(PRN) + ",";
                res += sv.getMaxCn0() + ",";
                res += sv.getMinCn0() + ",";
                res += sv.getPreivousMaxCno2seconds() + ",";
               // res += sv.getMaxCn0InSatHistory() + ",";
               // res += sv.getMinCn0InSatHistory() + ",";
                res += sv.getCarrierFreqDelta() + ",";
                res += sv.getCarrierPhase()-sv.getPreviousCarrierPhase() +",";
                res += sv.getClockBias() + ",";
                res += sv.getClockDrift() + ",";
                res += sv.getDeltaIonosphericDelay() + ",";
                res += Math.abs(sv.getDeltaCorrectedPrNovelocityShift()) + ",";
                res += sv.getSecondDerivativeDeltaCorrectedPrNoVelocityShift() + ",";
                res += sv.getCorrectPsResiduals() + ",";
                Boolean los = sv.getLOS();
                if (los != null) {
                    res += (los ? "LOS" : "NLOS") + ",";
                } else {
                    res += "null,";
                }

                res += "\r\n";
            }
        }
        return res;
    }

    private static boolean IsGoodMeassurment(SirfSVMeasurement sv) {
        boolean ans = true;

        //list of rules to discard bad samples
        if(sv.getElevation()<=5)
            ans = false;


        double deltaCarrierPhase = sv.getCarrierPhase()-sv.getPreviousCarrierPhase();
        if(Math.abs(deltaCarrierPhase)>50000)
            ans = false;
        if(sv.getCarrierFreqDelta()==0 || sv.getCarrierFreqDelta()>100)
            ans = false;

       if(Math.abs(sv.getDeltaCorrectedPrNovelocityShift())>1000)
           ans = false;
        if(Math.abs(sv.getSecondDerivativeDeltaCorrectedPrNoVelocityShift())>300)
            ans = false;
        if(Math.abs(sv.getCorrectPsResiduals())>3000)
            ans = false;


            return ans;
    }


    private static String toCsvString4GpsSwitch(SirfPeriodicMeasurement meas)
    {
        String res = "";
        for (Integer PRN : meas.getSatellites().keySet()){
            SirfSVMeasurement sv = meas.getSatellites().get(PRN);
                res += (meas.getTime()) + ",";
                res += meas.getxPos() + ",";
                res += meas.getyPos() + ",";
                res += meas.getzPos() + ",";
                res += meas.getxV() + ",";
                res += meas.getyV() + ",";
                res += meas.getzV() + ",";
                res += meas.getHdop() + ",";
                res += meas.getLat() + ",";
                res += meas.getLon() + ",";
                res += meas.getAltEllipsoid() + ",";
                res += meas.getAltMSL() + ",";
                res += meas.getSpeed() + ",";
                res += meas.getCourse() + ",";
                res += meas.getHorizontalPosError() + ",";
                res += meas.getVerticalPosError() + ",";
                res += meas.getHorizontalVelocityError() + ",";
                res += meas.getClockBias() + ",";
                res += meas.getClockBiasError() + ",";
                res += meas.getClockDrift() + ",";
                res += meas.getClockDriftError() + ",";
                res += meas.getxExGPSWeek() + ",";
                res += meas.getGPSTOW() + ",";
                res += meas.getSVNum() + ",";
                res += meas.getClockDrift7() + ",";
                res += meas.getClockBias7() + ",";
                res += meas.getEstimatedGPSTime() + ",";
                res += PRN + ",";
                res += sv.getGpsSoftwareTime() + ",";
                res += sv.getAzimuth() + ",";
                res += sv.getElevation() + ",";
                res += sv.getState() + ",";
                res += sv.getxPos() + ",";
                res += sv.getyPos() + ",";
                res += sv.getzPos() + ",";
                res += sv.getxV() + ",";
                res += sv.getyV() + ",";
                res += sv.getzV() + ",";
                res += sv.getPseudorange() + ",";
              //  res += meas.getResidual(PRN) + ",";
                for(int j=0; j<10; j++)
                  res += sv.getCNo()[j]+ ",";
                res += sv.getCarrierFreq() + ",";
                res += sv.getCarrierPhase() + ",";
               // res += sv.getPreviousCarrierPhase() + ",";
                //res += sv.getCarrierPhase()-sv.getPreviousCarrierPhase() +",";
                res += sv.getDeltaRangeInterval() + ",";
                res += sv.getMeanDeltaRangeTime() + ",";
                res += sv.getClockBias() + ",";
                res += sv.getClockDrift() + ",";
                res += sv.getIonosphericDelay() + ",";
                res += sv.getMaxFilterCn0() + ",";
                res += sv.getMinFilterCn0() + ",";
                res += sv.getFilteredCNo()[9] - sv.getFilteredCNo()[0] + ",";
              //  res += sv.getCorrectPseudoRangeNoVelocitySHift() + ",";
               // res += sv.getPrevoiusCorrectedPseudoRangeNoVelocityShift() + ",";
               // res += Math.abs(sv.getDeltaCorrectedPrNovelocityShift()) + ",";
               // res += sv.getSecondDerivativeDeltaCorrectedPrNoVelocityShift() + ",";
               // res += sv.getCorrectPsResiduals() + ",";
                res += "End,";


                res += "\r\n";

        }
        return res;
    }
    private static String toCsvString(SirfPeriodicMeasurement meas) {
        String res = "";
        for (Integer PRN : meas.getSatellites().keySet()){
            SirfSVMeasurement sv = meas.getSatellites().get(PRN);
            if(sv.getElevation()>=5 ) {
                res += (meas.getTime()) + ",";
                res += meas.getxPos() + ",";
                res += meas.getyPos() + ",";
                res += meas.getzPos() + ",";
                res += meas.getxV() + ",";
                res += meas.getyV() + ",";
                res += meas.getzV() + ",";
                res += meas.getHdop() + ",";
                res += meas.getLat() + ",";
                res += meas.getLon() + ",";
                res += meas.getAltEllipsoid() + ",";
                res += meas.getAltMSL() + ",";
                res += meas.getSpeed() + ",";
                res += meas.getCourse() + ",";
                res += meas.getHorizontalPosError() + ",";
                res += meas.getVerticalPosError() + ",";
                res += meas.getHorizontalVelocityError() + ",";
                res += meas.getClockBias() + ",";
                res += meas.getClockBiasError() + ",";
                res += meas.getClockDrift() + ",";
                res += meas.getClockDriftError() + ",";
                res += meas.getxExGPSWeek() + ",";
                res += meas.getGPSTOW() + ",";
                res += meas.getSVNum() + ",";
                res += meas.getClockDrift7() + ",";
                res += meas.getClockBias7() + ",";
                res += meas.getEstimatedGPSTime() + ",";
                res += PRN + ",";
                res += sv.getGpsSoftwareTime() + ",";
                res += sv.getAzimuth() + ",";
                res += sv.getElevation() + ",";
                res += sv.getState() + ",";
                res += sv.getxPos() + ",";
                res += sv.getyPos() + ",";
                res += sv.getzPos() + ",";
                res += sv.getxV() + ",";
                res += sv.getyV() + ",";
                res += sv.getzV() + ",";
                res += sv.getPseudorange() + ",";
                res += meas.getResidual(PRN) + ",";
                res += sv.getMaxCn0() + ",";
                res += sv.getMinCn0() + ",";
                res += sv.getCNo()[9] - sv.getCNo()[0] + ",";
                res += sv.getPreivousMaxCno2seconds() + ",";
                res += sv.getMaxCn0InSatHistory() + ",";
                res += sv.getMinCn0InSatHistory() + ",";
                res += sv.getCarrierFreq() + ",";
                res += sv.getCarrierPhase() + ",";
                res += sv.getPreviousCarrierPhase() + ",";
                res += sv.getCarrierPhase()-sv.getPreviousCarrierPhase() +",";
                res += sv.getDeltaRangeInterval() + ",";
                res += sv.getMeanDeltaRangeTime() + ",";
                res += sv.getClockBias() + ",";
                res += sv.getClockDrift() + ",";
                res += sv.getIonosphericDelay() + ",";
                res += sv.getMaxFilterCn0() + ",";
                res += sv.getMinFilterCn0() + ",";
                res += sv.getFilteredCNo()[9] - sv.getFilteredCNo()[0] + ",";
                res += sv.getCorrectPseudoRangeNoVelocitySHift() + ",";
                res += sv.getPrevoiusCorrectedPseudoRangeNoVelocityShift() + ",";
                res += Math.abs(sv.getDeltaCorrectedPrNovelocityShift()) + ",";
                res += sv.getSecondDerivativeDeltaCorrectedPrNoVelocityShift() + ",";
                res += sv.getCorrectPsResiduals() + ",";
                Boolean los = sv.getLOS();
                if (los != null) {
                    res += (los ? "LOS" : "NLOS") + ",";
                } else {
                    res += "null,";
                }

                res += "\r\n";
            }
        }
        return res;
    }





}
