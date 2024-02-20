package Parsing.stm;

import Parsing.nmea.NMEAProtocolParser;
import Parsing.nmea.NMEASentence;
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
import java.util.function.Predicate;

/**
 * Created by Roi on 1/31/2015.
 */
public class STMProtocolParser {

    public STMProtocolParser(){
    }

    public List<STMPeriodMeasurment> parse(String path) throws IOException, ParseException {
        List<STMPeriodMeasurment> result = new ArrayList<STMPeriodMeasurment>();
        BufferedReader br = new BufferedReader(new FileReader(path)); //todo throw spesific exeption. Check where an NMEA msg starts
        String line;
        double Course, Speed;
        double lat = 0, lon = 0, alt = 0, altElip = 0, hDOP = 0;
        long time = 0;
        long UtcTime=0;
        List<STMSVMeasurement> svList = new ArrayList<STMSVMeasurement>();
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
                result.add(new STMPeriodMeasurment(UtcTime,  lat, lon, alt, altElip, hDOP, svList));
                lat = 0;
                lon = 0; alt = 0;
                altElip = 0;
                hDOP = 0;
                time = 0;
                svList = new ArrayList<STMSVMeasurement>();
            }
            if(currentSentence.getSentenceName().equals("$GPRMC"))
            {
                DateFormat df = new SimpleDateFormat("HHmmss.SSS");
                UtcTime  = df.parse(data[currentSentence.getfieldsIndexByName("UtcTime")]).getTime();
                Course = Double.parseDouble(data[currentSentence.getfieldsIndexByName("course")]);
                Speed =  Double.parseDouble(data[currentSentence.getfieldsIndexByName("speedKm_H")]);

            }
            if (currentSentence.getSentenceName().equals("$GPGGA")){
                time = Long.parseLong(data[currentSentence.getfieldsIndexByName("time")].replace(".", ""));
                String latString = data[currentSentence.getfieldsIndexByName("lat")];
                if (!latString.equals("")){
                    System.out.println(latString);

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
        }
        result.add(new STMPeriodMeasurment(UtcTime, lat, lon, alt, altElip, hDOP, svList));
        return result;
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


    public STMPeriodMeasurment clearBadSattelites(STMPeriodMeasurment meas)
    {
        List<STMSVMeasurement> Svmeas = meas.getSVs();
        List<STMSVMeasurement> tmpList = new ArrayList<>();

        for(int i=0; i<Svmeas.size(); i++)
        {
            if(Svmeas.get(i).isGoodSVforPseudoRangeComputation() && Svmeas.get(i).getPrn()<40)
              tmpList.add(Svmeas.get(i));//todo Maybe there is a better way?
        }

        meas.setSVs(tmpList);
        return meas;
    }

    public List<STMPeriodMeasurment> ClearBadSattelitesOnlyGps(List<STMPeriodMeasurment> meas)
    {

        for(int i=0; i < meas.size(); i++) {

            meas.set(i, clearBadSattelites(meas.get(i)) );
        }//todo Roi check if works.


        return meas;
    }

    protected boolean isSVDataSentence(NMEASentence sentence){
        return sentence.getSentenceName().equals("$PSTMTS");
    }


    protected void addSVData(List<STMSVMeasurement> svList, String[] data) {
        boolean navigationData = false;
        int prn = Integer.parseInt(data[2]);
        double rawPR = Double.parseDouble(data[3]);
        double freq = Double.parseDouble(data[4]);
        boolean lockSignal = Boolean.parseBoolean(data[5]);
        int Cn0 = Integer.parseInt(data[6]);
        double trackedTime = Double.parseDouble(data[7]);
        int isNavigation = Integer.parseInt(data[8]);
       if(isNavigation==1)
           navigationData = true;
        double EcefPosX = Double.parseDouble(data[9]);
        double EcefPosY = Double.parseDouble(data[10]);
        double EcefPosZ = Double.parseDouble(data[11]);
        double EcefVelX = Double.parseDouble(data[12]);
        double EceFVelY = Double.parseDouble(data[13]);
        double EceFVelz = Double.parseDouble(data[14]);
        double deltaPsv = Double.parseDouble(data[15]);
        double deltaPatm = Double.parseDouble(data[16]);
        STMSVMeasurement currentStmSVmesserment = new STMSVMeasurement(prn, rawPR, freq, lockSignal, Cn0, trackedTime, navigationData, EcefPosX, EcefPosY, EcefPosZ, EcefVelX, EceFVelY, EceFVelz, deltaPsv, deltaPatm);
        svList.add(currentStmSVmesserment);

    }


}
