package Parsing.sirf;

import java.util.*;


public  class SirfMessage {

    private String messageId;
    private Map<String, Integer> sToInt;
    private Map<Integer, String> intToS;

    protected SirfMessage(String messageId, Map<String, Integer> fields){
        this.messageId = messageId;
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

    public String getMessageId(){
        return this.messageId;
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

    public static boolean shouldParseMessage(String messageId){
        for (SirfMessage m : messagesToParse){
            if (m.messageId.equals(messageId)){
                return true;
            }
        }
        return false;
    }

    public static List<SirfMessage> messagesToParse = new ArrayList<SirfMessage>();

    static{
        //message Id 2
        Map<String, Integer> fields2 = new TreeMap<String, Integer>();
        fields2.put("X Position", 1);
        fields2.put("Y Position", 2);
        fields2.put("Z Position", 3);
        fields2.put("X Velocity", 4);
        fields2.put("Y Velocity", 5);
        fields2.put("Z Velocity", 6);
        fields2.put("Mode1", 7);
        fields2.put("HDOP", 8);
fields2.put("Mode2", 9);
        fields2.put("SVs In Fix", 12);
        fields2.put("SV1 PRN", 13);
        fields2.put("SV2 PRN", 14);
        fields2.put("SV3 PRN", 15);
        fields2.put("SV4 PRN", 16);
        fields2.put("SV5 PRN", 17);
        fields2.put("SV6 PRN", 18);
        fields2.put("SV7 PRN", 19);
        fields2.put("SV8 PRN", 20);
        fields2.put("SV9 PRN", 21);
        fields2.put("SV10 PRN", 22);
        fields2.put("SV11 PRN", 23);
        fields2.put("SV12 PRN", 24);
        messagesToParse.add(new SirfMessage("2", fields2));

        //message Id 4
        Map<String, Integer> fields4 = new TreeMap<String, Integer>();
        int index = 4;
        for (int i = 1; i<=85; i++){
            fields4.put("SV"+ i + " ID", index);
            index++;
            fields4.put("SV"+ i +" Azimuth", index);
            index++;
            fields4.put("SV"+ i +" Elevation", index);
            index++;
            fields4.put("SV"+ i +" State", index);
            index++;
            for (int j = 1; j<=10; j++){
                fields4.put("SV"+ i +" C/No " + j, index);
                index++;
            }
        }
        messagesToParse.add(new SirfMessage("4", fields4));

        //message Id 28
        Map<String, Integer> fields28 = new TreeMap<String, Integer>();
        fields28.put("SV ID", 3);
        fields28.put("GPS Software Time", 4);
        fields28.put("Pseudorange", 5);
        fields28.put("Carrier Frequency", 6);
        fields28.put("Carrier Phase", 7);
        for (int i = 10; i<= 19; i++){
            fields28.put("filtered C/No "+(i-9), i);//filtered!!
        }
        fields28.put("Delta Range Interval", 20);
        fields28.put("Mean Delta Range Time", 21);
        messagesToParse.add(new SirfMessage("28", fields28));

        //message Id 30
        Map<String, Integer> fields30 = new TreeMap<String, Integer>();
        fields30.put("SV ID", 1);
        fields30.put("SV X Position", 3);
        fields30.put("SV Y Position", 4);
        fields30.put("SV Z Position", 5);
        fields30.put("SV X Velocity", 6);
        fields30.put("SV Y Velocity", 7);
        fields30.put("SV Z Velocity", 8);
        fields30.put("Clock Bias", 9);
        fields30.put("Clock Drift", 10);
        fields30.put("Ionospheric Delay", 14);
        messagesToParse.add(new SirfMessage("30", fields30));

//message id 7
Map<String, Integer> fields7 = new TreeMap<String, Integer>();
fields7.put("EXTENDED GPS Week", 1);
fields7.put("GPS TOW", 2);
fields7.put("SVs number", 3);
fields7.put("Clock Drift 7", 4);
fields7.put("Clock Bias 7", 5);
fields7.put("Estimated GPS Time", 6);
messagesToParse.add(new SirfMessage("7", fields7));

        //message Id 41
        Map<String, Integer> fields41 = new TreeMap<String, Integer>();
        fields41.put("UTC Year", 5);
        fields41.put("UTC Month", 6);
        fields41.put("UTC Day", 7);
        fields41.put("UTC Hour", 8);
        fields41.put("UTC Minute", 9);
        fields41.put("UTC Second", 10);
        fields41.put("SVs Used", 11);
        fields41.put("Lat", 12);
        fields41.put("Lon", 13);
        fields41.put("Alt From Ellipsoid", 14);
        fields41.put("Alt From MSL", 15);
        fields41.put("Speed", 17);
        fields41.put("Course", 18);
        fields41.put("Estimated Horizontal Positioning Error", 22);
        fields41.put("Estimated Vertical Positioning Error", 23);
        fields41.put("Estimated Time Error", 24);
        fields41.put("Estimated Horizontal Velocity Error", 25);
        fields41.put("Clock Bias", 26);
        fields41.put("Clock Bias Error", 27);
        fields41.put("Clock Drift", 28);
        fields41.put("Clock Drift Error", 29);
        fields41.put("HDOP", 34);
        messagesToParse.add(new SirfMessage("41", fields41));
    }


}

