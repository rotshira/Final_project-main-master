package Parsing.nmea;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class NMEASentence {

	private String sentenceName;
	private Map<String, Integer> sToInt;
	private Map<Integer, String> intToS;

	protected NMEASentence(String sentenceName, Map<String, Integer> fields){
		this.sentenceName = sentenceName;
		this.sToInt = fields;
		this.intToS = new HashMap<Integer, String>();
		for (String s : fields.keySet()){
			this.intToS.put(fields.get(s), s);
		}
	}
	
	public String getHeaderForPrint(){
		String result = "";
		for (String s : sToInt.keySet()){
			result += s + "\t";
		}
		return result;
	}
	
	public String getSentenceName(){
		return this.sentenceName;
	}
	
	public int getfieldsIndexByName(String s){
		return this.sToInt.get(s);
	}
	
	public String getFieldNameByIndex(int i){
		return this.intToS.get(i);
	}
	
	public Collection<String> getFieldNames(){
		return this.intToS.values();
	}
	
	public Collection<Integer> getIndeces(){
		return this.sToInt.values();
	}
	
	public static boolean shouldParseMessage(String sentenceName){
		for (NMEASentence s : sentencesToParse.values()){
			if (s.sentenceName.equals(sentenceName)){
				return true;
			}
		}
		return false;
	}
	
	public static Map<String, NMEASentence> sentencesToParse = new HashMap<String, NMEASentence>();

	static{
		//GPGSV
		Map<String, Integer> GPGSV = new TreeMap<String, Integer>();
		GPGSV.put("PRN", 0);
		GPGSV.put("el", 1);
		GPGSV.put("az", 2);
		GPGSV.put("S/Nr", 3);
		sentencesToParse.put("$GPGSV", new NMEASentence("$GPGSV", GPGSV));

		//GLGSV
		Map<String, Integer> GLGSV = new TreeMap<String, Integer>();
		GPGSV.put("PRN", 0);
		GPGSV.put("el", 1);
		GPGSV.put("az", 2);
		GPGSV.put("S/Nr", 3);
		sentencesToParse.put("$GLGSV", new NMEASentence("$GLGSV", GLGSV));

		//GPGLL
		Map<String, Integer> GPGGA = new TreeMap<String, Integer>();
		GPGGA.put("time", 1);
		GPGGA.put("lat", 2);
		GPGGA.put("lon", 4);
		GPGGA.put("fixQuality", 6);
		GPGGA.put("numOfSVs", 7);
		GPGGA.put("hDOP", 8);
		GPGGA.put("alt", 9);
		GPGGA.put("altElip", 11);
		sentencesToParse.put("$GPGGA", new NMEASentence("$GPGGA", GPGGA));



		Map<String, Integer> PSTMTS = new TreeMap<String, Integer>();
		PSTMTS.put("PRN", 2);
		PSTMTS.put("RawPR", 3);
		PSTMTS.put("Frequency", 4);
		PSTMTS.put("lockSignal", 5);
		PSTMTS.put("C/N0", 6);
		PSTMTS.put("trackedTime", 7);
		PSTMTS.put("dataAvilable", 8);
		PSTMTS.put("satEcefPosX", 9);
		PSTMTS.put("satEcefPosY", 10);
		PSTMTS.put("satEcefPosZ", 11);
		PSTMTS.put("satEcefVelX", 12);
		PSTMTS.put("satEcefVelY", 13);
		PSTMTS.put("satEcefVelZ", 14);
		PSTMTS.put("deltaPsv", 15);
		PSTMTS.put("deltaPatm", 16);
		sentencesToParse.put("$PSTMTS", new NMEASentence("$PSTMTS", PSTMTS));


		Map<String, Integer> GPRMC = new TreeMap<String, Integer>();
		GPRMC.put("UtcTime", 1);
		GPRMC.put("status", 2);
		GPRMC.put("lat", 3);
		GPRMC.put("N_S", 4); // can be North or south/
		GPRMC.put("lon", 5);
		GPRMC.put("E_W", 6); // can be west or east
		GPRMC.put("speedKm_H", 7);
		GPRMC.put("course", 8);
		GPRMC.put("date", 9);
		sentencesToParse.put("$GPRMC", new NMEASentence("$GPRMC", GPRMC));

		Map<String, Integer> PSTMTG = new TreeMap<String, Integer>();
		PSTMTG.put("weekNumber", 1);
		PSTMTG.put("TOW", 2);
		PSTMTG.put("numberOfSV", 3);
		PSTMTG.put("CpuTime", 4); // can be North or south/
		PSTMTG.put("timeValidity", 5);
		PSTMTG.put("oscError", 6); // can be west or east
		sentencesToParse.put("$PSTMTG", new NMEASentence("$PSTMTG", PSTMTG));


	}

	
}
