package Parsing.nmea;

import dataStructres.NMEAPeriodicMeasurement;
import dataStructres.NMEASVMeasurement;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;



public class NMEACSVWriter {
	
	private static String header = "Time, Latitude, Longitude, Altitude, Height over elipsoid, HDOP, SVs";
	private static String newLine = "\r\n";
	
	static{
		for (int i = 0; i < 20; i++){
			header += ", PRN, Azimuth, Elevation, S/Nr";
		}
	}
	
	public static void printToFile(List<NMEAPeriodicMeasurement> measurements, String path) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(path + "parsed.csv"));

		writer.write(header);
		writer.write(newLine);
        Set<Integer> allPrns = new TreeSet<Integer>();
        for (NMEAPeriodicMeasurement m : measurements){
            allPrns.addAll(m.getAllPRNs());
        }
		for (int i = 0; i<measurements.size(); i++){
			String csvString = toCsvString(measurements.get(i), allPrns);
			writer.write(csvString);
		}
		writer.flush();
		writer.close();
		
	}

	private static String toCsvString(NMEAPeriodicMeasurement meas, Set<Integer> allPrns) {
		String res = "";
		if (meas.getTime() == 0l){
			return res;
		}
		res += meas.getTime()+ ",";
		res += meas.getLat() + ",";
		res += meas.getLon() + ",";
		res += meas.getAlt() + ",";
		res += meas.getAltElip() + ",";
		res += meas.getHDOP() + ",";
		res += meas.getAllSvMeasurements().size()+", ";
       Map<Integer,? extends NMEASVMeasurement> mappedSvMeasurements = meas.getMappedSvMeasurements();//todo Ayal : why not working here and do work in STM?
        for (Integer prn : allPrns){
            NMEASVMeasurement nmeaSvMeasurement = mappedSvMeasurements.get(prn);
            if (nmeaSvMeasurement == null){
                nmeaSvMeasurement = NMEASVMeasurement.nullMeas;
            }
            res += nmeaSvMeasurement.getPrn() + ",";
			res += nmeaSvMeasurement.getAz() + ",";
			res += nmeaSvMeasurement.getEl() + ",";
			res += nmeaSvMeasurement.getSnr() + ",";
		}
		res += "\r\n";
		return res;
		
	}

	
	



}
