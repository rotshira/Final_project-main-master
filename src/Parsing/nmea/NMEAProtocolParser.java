package Parsing.nmea;

import dataStructres.NMEAPeriodicMeasurement;
import dataStructres.NMEASVMeasurement;
import dataStructres.STMPeriodMeasurment;
import dataStructres.STMSVMeasurement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class NMEAProtocolParser {

	public NMEAProtocolParser() {
	}


	public NMEAPeriodicMeasurement ParseStringList(List<String> parseString) {

		double lat = 0, lon = 0, alt = 0, altElip = 0, hDOP = 0;
		double Course = 0, Speed = 0;
		long time = 0;
		double UtcTime = 0;
		double time2 = 0;
		List<NMEASVMeasurement> svList = new ArrayList<NMEASVMeasurement>();

		for (int i = 0; i < parseString.size(); i++) {
			String[] data = filter(parseString.get(i)).split(",");
			NMEASentence currentSentence = NMEASentence.sentencesToParse.get(data[0]);
			if (currentSentence == null){
				continue;
			}


			if (currentSentence.getSentenceName().equals("$GPRMC")) {
				// DateFormat df = new SimpleDateFormat("HHmmss.SSS");
				time2 = Double.parseDouble(data[currentSentence.getfieldsIndexByName("UtcTime")]);
				//UtcTime  = df.parse(data[currentSentence.getfieldsIndexByName("UtcTime")]).getTime();
				UtcTime = time2;
				Course = Double.parseDouble(data[currentSentence.getfieldsIndexByName("course")]);
				Speed = Double.parseDouble(data[currentSentence.getfieldsIndexByName("speedKm_H")]);
			}
			if (currentSentence.getSentenceName().equals("$GPGGA")) {
				time = Long.parseLong(data[currentSentence.getfieldsIndexByName("time")].replace(".", ""));
				String latString = data[currentSentence.getfieldsIndexByName("lat")];
				if (!latString.equals("")) {
					lat = Double.parseDouble(latString) / 100;
					lon = Double.parseDouble(data[currentSentence.getfieldsIndexByName("lon")]) / 100;
					hDOP = Double.parseDouble(data[currentSentence.getfieldsIndexByName("hDOP")]);
					alt = Double.parseDouble(data[currentSentence.getfieldsIndexByName("alt")]);
					altElip = Double.parseDouble(data[currentSentence.getfieldsIndexByName("altElip")]);
				}
			}
			if (isSVDataSentence(currentSentence)) {
				addSVData(svList, data);
			}
			if (isExtra(currentSentence)) {
				doSomething(currentSentence);
			}


	}

		NMEAPeriodicMeasurement ans = new NMEAPeriodicMeasurement(time, UtcTime, lat, lon, alt, altElip, hDOP, svList, Course, Speed);

	return ans;
	}

	public List<NMEAPeriodicMeasurement> parse(String path) throws IOException, ParseException {
		List<NMEAPeriodicMeasurement> result = new ArrayList<NMEAPeriodicMeasurement>();
		BufferedReader br = new BufferedReader(new FileReader("src/Parsing/nmea/route_abcd_twice .gps")); //todo throw spesific exeption. Check where an NMEA msg starts
		String line;
		double lat = 0, lon = 0, alt = 0, altElip = 0, hDOP = 0;
		double Course=0, Speed=0;
		long time = 0;
		double UtcTime=0;
		double time2=0;
		List<NMEASVMeasurement> svList = new ArrayList<NMEASVMeasurement>();
		while((line = br.readLine()) != null){
			String[] data = filter(line).split(",");
			if (data.length == 0){
				continue;
			}
			NMEASentence currentSentence = NMEASentence.sentencesToParse.get(data[0]);
			if (currentSentence == null){
				continue;
			}
			if (hasTimestamp(currentSentence) && Long.parseLong(data[currentSentence.getfieldsIndexByName("UtcTime")].replace(".", "")) != UtcTime){
				result.add(new NMEAPeriodicMeasurement(time, UtcTime,  lat, lon, alt, altElip, hDOP, svList, Course, Speed));
				lat = 0;
				lon = 0; alt = 0;
				altElip = 0;
				hDOP = 0;
				time = 0;
				Course=0;
				Speed=0;
				svList = new ArrayList<NMEASVMeasurement>();
			}
			if(currentSentence.getSentenceName().equals("$GPRMC"))
			{
               // DateFormat df = new SimpleDateFormat("HHmmss.SSS");
                time2  = Double.parseDouble(data[currentSentence.getfieldsIndexByName("UtcTime")]);
				//UtcTime  = df.parse(data[currentSentence.getfieldsIndexByName("UtcTime")]).getTime();
				UtcTime = time2;
					Course = Double.parseDouble(data[currentSentence.getfieldsIndexByName("course")]);
				Speed =  Double.parseDouble(data[currentSentence.getfieldsIndexByName("speedKm_H")]);
			}
			if (currentSentence.getSentenceName().equals("$GPGGA")){
				time = Long.parseLong(data[currentSentence.getfieldsIndexByName("time")].replace(".", ""));
				String latString = data[currentSentence.getfieldsIndexByName("lat")];
				if (!latString.equals("")){
					lat = Double.parseDouble(latString) / 100;
					lon = Double.parseDouble(data[currentSentence.getfieldsIndexByName("lon")]) / 100;
					hDOP = Double.parseDouble(data[currentSentence.getfieldsIndexByName("hDOP")]);
					alt = Double.parseDouble(data[currentSentence.getfieldsIndexByName("alt")]);
					altElip = Double.parseDouble(data[currentSentence.getfieldsIndexByName("altElip")]);
				}
			}
			if (isSVDataSentence(currentSentence)){
					addSVData(svList, data);
			}
			if (isExtra(currentSentence)){
				doSomething(currentSentence);
			}
		}
		result.add(new NMEAPeriodicMeasurement(time, UtcTime, lat, lon, alt, altElip, hDOP, svList, Course, Speed));
		return result;
	}

	protected void doSomething(NMEASentence currentSentence) {
		return;
	}

	protected boolean isExtra(NMEASentence currentSentence) {
		return false;
	}

	protected void addSVData(List<NMEASVMeasurement> svList, String[] data) {
		List<String[]> miniData = splitSatellites(data);
		for (String[] svData : miniData){
            NMEASVMeasurement sv = new NMEASVMeasurement(Integer.parseInt(svData[0]),
                    Integer.parseInt(svData[1]),
                    Integer.parseInt(svData[2]),
                    Integer.parseInt(svData[3]));
            svList.add(sv);
        }
	}





	protected boolean isSVDataSentence(NMEASentence sentence){
		return sentence.getSentenceName().equals("$GPGSV")
				|| sentence.getSentenceName().equals("$GLGSV");
	}

	private String filter(String line) {
		if (line.contains("$")){//start of message
			line = line.substring(line.indexOf("$"));
		}
		else{//corrupt line
			return "N/A";
		}
		if (line.contains("*")){//checksum
			line = line.substring(0, line.indexOf("*"));
		}
		return line;
	}

	private List<String[]> splitSatellites(String[] data) {
		List<String[]> res = new ArrayList<String[]>();
		for (int i = 4; i + 3 < data.length; i = i + 4){
			if (data[i].equals("") || data[i + 1].equals("") || data[i + 2].equals("") || data[i + 3].equals("")){
				continue;
			}
			res.add(new String[]{data[i], data[i + 1], data[i + 2], data[i + 3]});
		}
		return res;
	}

	private boolean hasTimestamp(NMEASentence currentSentence) {
		return currentSentence.getSentenceName().equals("$GPRMC");
	}

}
