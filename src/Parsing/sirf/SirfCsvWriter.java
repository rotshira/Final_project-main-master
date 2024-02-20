package Parsing.sirf;


import dataStructres.SirfPeriodicMeasurement;
import dataStructres.SirfSVMeasurement;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class SirfCsvWriter {
	
    static int NumOfGNSS=85;
  //  private static String header = "UTC Time, X Position, Y Position, Z Position, X Velocity, Y Velocity, Z Velocity, HDOP, Lat, Lon, Alt (Elipsoide), Alt (AMSL), Speed, Course, Estimated Horizontal Positioning Error, Estimated Vertical Positioning Error, Estimated Horizontal Velocity Error, Clock Bias, Clock Bias Error, Clock Drift, Clock Drift Error";
    private static String header = "UTC Time, X Position, Y Position, Z Position, X Velocity, Y Velocity, Z Velocity, HDOP, Lat, Lon, Alt (Elipsoide), Alt (AMSL), Speed, Course, Estimated Horizontal Positioning Error, Estimated Vertical Positioning Error, Estimated Horizontal Velocity Error, Clock Bias, Clock Bias Error, Clock Drift, Clock Drift Error, EXTENDED GPS Week, GPS TOW, SVs number, Clock Drift 7, Clock Bias 7, Estimated GPS Time";
	private static String newLine = "\r\n";
	
	static{
		for (int i = 0; i < 85; i++){
			header += ", PRN" +(i+1)+
                    ", GPS Software Time, Azimuth, Elevation, State, X Position, Y Position, Z Position, X Velocity, Y Velocity, Z Velocity, Pseudorange, C/No, Carrier Frequency, Carrier Phase, Delta Range Interval, Mean Delta Range Time, Clock Bias, Clock Drift, Ionospheric Delay, Filtered C/No";
		}
	}
	
	public static void printToFile(List<SirfPeriodicMeasurement> measurements, String path) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(path + ".csv"));
		writer.write(header);
		writer.write(newLine);
        List<Integer> parsedPrnsSorted = SirfProtocolParser.getParsedPrnsSorted();

        for (int i = 0; i<measurements.size(); i++){
			String csvString = toCsvString(measurements.get(i), parsedPrnsSorted);
			if (!csvString.equals("0,0,0,0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0,0,0,0,0,0,0,0,0,")){
				writer.write(csvString);
			}
		}
		writer.flush();
		writer.close();
		
	}

	private static String toCsvString(SirfPeriodicMeasurement meas, List<Integer> parsedPrnsSorted) {
        String res = "";
		//if (meas.getTime() == 0l){
		//	return res;
		//}
		for (int cno = 0; cno < 10; cno ++){
		res += (meas.getTime() + (cno * 100)) + ",";
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
        res += meas.getxExGPSWeek() +  ",";
        res += meas.getGPSTOW() + ",";
        res += meas.getSVNum() + ",";
        res += meas.getClockDrift7()+ ",";
        res += meas.getClockBias7() + ",";
        res += meas.getEstimatedGPSTime() + ",";
        //for (Integer PRN : meas.getSatellites().keySet()){
            for (Integer PRN : parsedPrnsSorted){
			SirfSVMeasurement sv = meas.getSatellites().get(PRN);
            if(sv==null)
           // for(int cnt=0;cnt<PRN;cnt++)
          //  {
               res +=",,,,,,,,,,,,,,,,,,,,,"; //21 spaces
          //  }
            else
            {
			res += PRN + ",";
			res += sv.getGpsSoftwareTime() +",";
			res += sv.getAzimuth()+",";
			res += sv.getElevation()+",";
			res += sv.getState()+",";
			res += sv.getxPos()+",";
			res += sv.getyPos()+",";
			res += sv.getzPos()+",";
			res += sv.getxV()+",";
			res += sv.getyV()+",";
			res += sv.getzV()+",";
			res += sv.getPseudorange()+",";
			res += sv.getCNo()[cno]+",";
			res += sv.getCarrierFreq()+",";
			res += sv.getCarrierPhase()+",";
			res += sv.getDeltaRangeInterval()+",";
			res += sv.getMeanDeltaRangeTime()+",";
			res += sv.getClockBias()+",";
			res += sv.getClockDrift()+",";
			res += sv.getIonosphericDelay()+",";
			res += sv.getFilteredCNo()[cno]+",";
            }
		}
		res += "\r\n";
		}
		return res;
		
	}
	
	
	

}
