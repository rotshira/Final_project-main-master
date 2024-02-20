package Parsing.sirf;


import Algorithm.LosAlgorithm;
import GNSS.Sat;
import Geometry.Building;
import Geometry.Point3D;
import dataStructres.SirfPeriodicMeasurement;
import dataStructres.SirfSVMeasurement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class SirfProtocolParser {
	
	private static final String endOfBlock = "ThrPut";

    private static Set<Integer> prns = new HashSet<Integer>();
	
	/*public static void main(String[] args){
		try {
			List<SirfPeriodicMeasurement> me = SirfProtocolParser.parseFile("c://Parsing//test5.gps");
			SirfCsvWriter.printToFile(me, "c://Parsing//ggg.csv");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

    public static List<Integer> getParsedPrnsSorted(){
        ArrayList<Integer> result = new ArrayList<Integer>(prns);
        Collections.sort(result);
        return result;
    }

    public static void CreateFeatures(List<SirfPeriodicMeasurement> me)
    {

        for(int i=0; i<me.size(); i++)
      {
         Point3D ReciverPos = new Point3D(me.get(i).getxPos(), me.get(i).getyPos(), me.get(i).getzPos());
         for(Integer prn :prns)
         {
             me.get(i).getSatellites().get(prn).computeRange(ReciverPos);

         }
      }
    }
	public static List<SirfPeriodicMeasurement> parseFile(String path) throws IOException{
		System.out.println("Parsing..");
        double Old_FLag,New_flag;
        Old_FLag=0;
		List<SirfPeriodicMeasurement> result = new ArrayList<SirfPeriodicMeasurement>();
		BufferedReader br = new BufferedReader(new FileReader("src/Parsing/sirf/POINT_A_STATIONARY.txt"));
		String line;
		SirfPeriodicMeasurement currentMeasurement = new SirfPeriodicMeasurement();
		while((line = br.readLine()) != null)
        {
            String[] array = line.split(",");

            if(array[0].equals("7"))
            {

               SirfMessage currentMessage = getMessageById(array[0]);
               processMessage(currentMessage, currentMeasurement, array);
                result.add(currentMeasurement);
                currentMeasurement = new SirfPeriodicMeasurement();

                  continue;
               }


			if (!SirfMessage.shouldParseMessage(array[0])){
				continue;				
			}
			SirfMessage currentMessage = getMessageById(array[0]);
			processMessage(currentMessage, currentMeasurement, array);

           // result.add(currentMeasurement);

        }

		System.out.println("Done Parsing.");

        return result;
	}



   public static void GetHistoryValuesforSVM(List<SirfPeriodicMeasurement> MeassureSirf,List<Integer> Sortedprns) throws IOException {


       int j=0,i;
       int tmp=0;
       SirfSVMeasurement sv = MeassureSirf.get(0).getSatellites().get(0);
       double DeltaReciverPosition,prevloc;
       int OldvalueMax;
       int OldValueMin;
       int[] OldSNRValues = new int[10];

           for(int k=0; k<MeassureSirf.size()-6; k++)
           {

           for(Integer PRN : Sortedprns)
            {
                for ( i=0; i<5; i++)
                {
                tmp=i*2;
                    if(MeassureSirf.get(k+i).getSatellites().get(PRN)!=null)
                    {
                        OldSNRValues[tmp] = MeassureSirf.get(k+i).getSatellites().get(PRN).getMaxCn0();
                        OldSNRValues[tmp+1] = MeassureSirf.get(k+i).getSatellites().get(PRN).getMinCn0();
                    }
                    else
                    {
                        OldSNRValues[tmp]=0;
                        OldSNRValues[tmp+1]=0;
                    }

                }
                if(MeassureSirf.get(k+i).getSatellites().get(PRN)!=null )
                {
                     MeassureSirf.get(k+i).getSatellites().get(PRN).setOldCNo(OldSNRValues);
                    //System.out.println("Print from the function");
                 //  MeassureSirf.get(k+i).getSatellites().get(PRN).PrintOldCn();
                  //  char ch = (char) System.in.read();
                }
            }
           }

       int OldC[] = new int[] {1,2,3,4,0,60,71,80,9,991 };

       MeassureSirf.get(8).getSatellites().get(4).PrintOldCn();

       //return MeassureSirf;

   }





    public  static List<SirfPeriodicMeasurement> GetHistoryValuesforSVM2(List<SirfPeriodicMeasurement> MeassureSirf,List<Integer> Sortedprns) throws IOException {


        int j=0,i;
        boolean flag=false;
        int tmp=0;
        List<SirfPeriodicMeasurement> Me2 =new ArrayList<SirfPeriodicMeasurement>(MeassureSirf);



        int OldSNRValues[];


        for(int k=0; k<MeassureSirf.size()-5; k++)
        {

            for(Integer PRN : Sortedprns)
            {
                OldSNRValues = new int[10];
                for ( i=0; i<5; i++)
               {


                    tmp=i*2;
                    if(MeassureSirf.get(k+i).getSatellites().get(PRN)!=null)
                    {
                        OldSNRValues[tmp] = MeassureSirf.get(k+i).getSatellites().get(PRN).getMaxCn0();
                        OldSNRValues[tmp+1] = MeassureSirf.get(k+i).getSatellites().get(PRN).getMinCn0();
                    }
                    else
                    {
                        OldSNRValues[tmp]=0;
                        OldSNRValues[tmp+1]=0;
                    }

                }
                SirfSVMeasurement currentSV = Me2.get(k+i).getSatellites().get(PRN);

                if(currentSV!=null )
                {
                    currentSV.setOldCNo(OldSNRValues);
                    Me2.get(k+i).getSatellites().put(PRN, currentSV);


                }


            }
        }


        return Me2;

    }


    private static void processMessage(SirfMessage currentMessage,
			SirfPeriodicMeasurement currentMeasurement, String[] line) {
		int currentSVID;
		SirfSVMeasurement currentSV;

		switch (Integer.parseInt(currentMessage.getMessageId())){
			case 2:
				currentMeasurement.setxPos(
						Integer.parseInt(line[currentMessage.getfieldsIndexByName("X Position")]));
				currentMeasurement.setyPos(
						Integer.parseInt(line[currentMessage.getfieldsIndexByName("Y Position")]));
				currentMeasurement.setzPos(
						Integer.parseInt(line[currentMessage.getfieldsIndexByName("Z Position")]));
				currentMeasurement.setxV(
						Double.parseDouble(line[currentMessage.getfieldsIndexByName("X Velocity")]));
				currentMeasurement.setyV(
						Double.parseDouble(line[currentMessage.getfieldsIndexByName("Y Velocity")]));
				currentMeasurement.setzV(
						Double.parseDouble(line[currentMessage.getfieldsIndexByName("Z Velocity")]));
				currentMeasurement.setHdop(
						Double.parseDouble(line[currentMessage.getfieldsIndexByName("HDOP")]));
                currentMeasurement.setMode1(
                        Byte.parseByte(line[currentMessage.getfieldsIndexByName("Mode1")]));
                currentMeasurement.setMode2(
                        Byte.parseByte(line[currentMessage.getfieldsIndexByName("Mode2")]));
				break;

           case 7:
                currentMeasurement.setxExGPSWeek(
                        Integer.parseInt(line[currentMessage.getfieldsIndexByName("EXTENDED GPS Week")]));


               currentMeasurement.setGPSTOW(
                        Double.parseDouble(line[currentMessage.getfieldsIndexByName("GPS TOW")]));
                currentMeasurement.setSVNum(
                        Integer.parseInt(line[currentMessage.getfieldsIndexByName("SVs number")]));
                currentMeasurement.setClockDrift7(
                        Double.parseDouble(line[currentMessage.getfieldsIndexByName("Clock Drift 7")]));
                currentMeasurement.setClockBias7(
                        Double.parseDouble(line[currentMessage.getfieldsIndexByName("Clock Bias 7")]));
                currentMeasurement.setEstimatedGPSTime(
                        Double.parseDouble(line[currentMessage.getfieldsIndexByName("Estimated GPS Time")]));

                break;

			case 4:
				for (int i = 1; i <= 85; i++){
                    int svIdIndex = currentMessage.getfieldsIndexByName("SV" + i + " ID");
                    if (svIdIndex >= line.length){
                        break;
                    }
                    currentSVID = Integer.parseInt(line[svIdIndex]);
                    prns.add(currentSVID);
					currentSV = currentMeasurement.getSatellites().get(currentSVID);
					if (currentSV == null){
						currentSV = new SirfSVMeasurement();
						currentMeasurement.getSatellites().put(currentSVID, currentSV);
					}
					currentSV.setAzimuth(Integer.parseInt(line[currentMessage.getfieldsIndexByName("SV"+ i +" Azimuth")]));
					currentSV.setElevation(Integer.parseInt(line[currentMessage.getfieldsIndexByName("SV"+ i +" Elevation")]));
					currentSV.setState(Integer.parseInt(line[currentMessage.getfieldsIndexByName("SV"+ i +" State")]));
					int[] cno = new int[10];
					for (int j = 1; j<=10; j++){
						cno[j-1] = Integer.parseInt(line[currentMessage.getfieldsIndexByName("SV"+ i +" C/No " + j)]);						
					}
					currentSV.setCNo(cno);
				}
				break;
				
			case 28:
				currentSVID = Integer.parseInt(line[currentMessage.getfieldsIndexByName("SV ID")]);
                prns.add(currentSVID);
                currentSV = currentMeasurement.getSatellites().get(currentSVID);
				if (currentSV == null){
					currentSV = new SirfSVMeasurement();
					currentMeasurement.getSatellites().put(currentSVID, currentSV);
				}
				currentSV.setGpsSoftwareTime(
						Double.parseDouble(line[currentMessage.getfieldsIndexByName("GPS Software Time")]));
				currentSV.setPseudorange(
						Double.parseDouble(line[currentMessage.getfieldsIndexByName("Pseudorange")]));
				currentSV.setCarrierFreq(
						Double.parseDouble(line[currentMessage.getfieldsIndexByName("Carrier Frequency")]));
				currentSV.setCarrierPhase(
						Double.parseDouble(line[currentMessage.getfieldsIndexByName("Carrier Phase")]));
				currentSV.setGpsSoftwareTime(
						Double.parseDouble(line[currentMessage.getfieldsIndexByName("GPS Software Time")]));
				int[] cno = new int[10];
				for (int i = 1; i<=10; i++){
					cno[i-1] =
						Integer.parseInt(line[currentMessage.getfieldsIndexByName("filtered C/No "+i)]);
				}
				currentSV.setFilteredCNo(cno);
				currentSV.setDeltaRangeInterval(
						Integer.parseInt(line[currentMessage.getfieldsIndexByName("Delta Range Interval")]));
				currentSV.setDeltaRangeInterval(
						Integer.parseInt(line[currentMessage.getfieldsIndexByName("Mean Delta Range Time")]));
				break;
				
			case 30:
				currentSVID = Integer.parseInt(line[currentMessage.getfieldsIndexByName("SV ID")]);
                prns.add(currentSVID);
                currentSV = currentMeasurement.getSatellites().get(currentSVID);
				if (currentSV == null){
					currentSV = new SirfSVMeasurement();
					currentMeasurement.getSatellites().put(currentSVID, currentSV);
				}
				currentSV.setxPos(
						Double.parseDouble(line[currentMessage.getfieldsIndexByName("SV X Position")]));
				currentSV.setyPos(
						Double.parseDouble(line[currentMessage.getfieldsIndexByName("SV Y Position")]));
				currentSV.setzPos(
						Double.parseDouble(line[currentMessage.getfieldsIndexByName("SV Z Position")]));
				currentSV.setxV(
						Double.parseDouble(line[currentMessage.getfieldsIndexByName("SV X Velocity")]));
				currentSV.setyV(
						Double.parseDouble(line[currentMessage.getfieldsIndexByName("SV Y Velocity")]));
				currentSV.setzV(
						Double.parseDouble(line[currentMessage.getfieldsIndexByName("SV Z Velocity")]));
				currentSV.setClockBias(
						Double.parseDouble(line[currentMessage.getfieldsIndexByName("Clock Bias")]));
				currentSV.setClockDrift(
						Double.parseDouble(line[currentMessage.getfieldsIndexByName("Clock Drift")]));
				currentSV.setIonosphericDelay(
						Double.parseDouble(line[currentMessage.getfieldsIndexByName("Ionospheric Delay")]));
				break;
				
			case 41:
				String year = line[currentMessage.getfieldsIndexByName("UTC Year")];
				String month = line[currentMessage.getfieldsIndexByName("UTC Month")];
				String day = line[currentMessage.getfieldsIndexByName("UTC Day")];
				String hour = line[currentMessage.getfieldsIndexByName("UTC Hour")];
				String minute = line[currentMessage.getfieldsIndexByName("UTC Minute")];
				String sec = line[currentMessage.getfieldsIndexByName("UTC Second")].replace(".", "");
				currentMeasurement.setTime(Long.parseLong(year + month + day + hour + minute + sec) / 1000);
				String lat = line[currentMessage.getfieldsIndexByName("Lat")];
				if(!lat.equals("0")) //todo  ayal - right?
                    lat = lat.substring(0, 2) + "." + lat.substring(2);
				currentMeasurement.setLat(
						Double.parseDouble(lat));
				String lon = line[currentMessage.getfieldsIndexByName("Lon")];
                if(!lon.equals("0"))
                    lon = lon.substring(0, 2) + "." + lon.substring(2);
				currentMeasurement.setLon(
						Double.parseDouble(lon));
				currentMeasurement.setAltEllipsoid(
						Double.parseDouble(line[currentMessage.getfieldsIndexByName("Alt From Ellipsoid")]) / 100);
				currentMeasurement.setAltMSL(
						Double.parseDouble(line[currentMessage.getfieldsIndexByName("Alt From MSL")]));
				currentMeasurement.setSpeed(
						Integer.parseInt(line[currentMessage.getfieldsIndexByName("Speed")]));
				currentMeasurement.setCourse(
						Integer.parseInt(line[currentMessage.getfieldsIndexByName("Course")]));
				currentMeasurement.setHorizontalPosError(
						Integer.parseInt(line[currentMessage.getfieldsIndexByName("Estimated Horizontal Positioning Error")]));
				currentMeasurement.setVerticalPosError(
						Integer.parseInt(line[currentMessage.getfieldsIndexByName("Estimated Vertical Positioning Error")]));
				currentMeasurement.setHorizontalVelocityError(
						Integer.parseInt(line[currentMessage.getfieldsIndexByName("Estimated Horizontal Velocity Error")]));
				currentMeasurement.setClockBias(
						Integer.parseInt(line[currentMessage.getfieldsIndexByName("Clock Bias")]));
				currentMeasurement.setClockBiasError(
						Integer.parseInt(line[currentMessage.getfieldsIndexByName("Clock Bias Error")]));
				currentMeasurement.setClockDrift(
						Integer.parseInt(line[currentMessage.getfieldsIndexByName("Clock Drift")]));
				currentMeasurement.setClockDriftError(
						Integer.parseInt(line[currentMessage.getfieldsIndexByName("Clock Drift Error")]));
				break;
				
			default:
				break;
		
		}
	}

	private static SirfMessage getMessageById(String id) {
		for (SirfMessage m : SirfMessage.messagesToParse){
			if (m.getMessageId().equals(id)){
				return m;
			}
		}
		return null;
	}


    public void ComputeLosNlosForSingleTimeStamp(SirfPeriodicMeasurement meas, List<Building> buildings1, Point3D receiverPointInUTM)
    {
        Sat sat =null;
        for (Integer Prn : meas.getSatellites().keySet()) {
            sat = meas.getSatellites().get(Prn).getSatClass(Prn);
            Boolean isLos = LosAlgorithm.ComputeLos(receiverPointInUTM, buildings1, sat);
            meas.getSatellites().get(Prn).setLOS(isLos);

        }
    }

    public void ComputeLosNLOSFromStaticPoint(List<SirfPeriodicMeasurement> sirfMeas, List<Building> buildings1, Point3D receiverPointInUTM) {

        Sat sat =null;
        for(int i=2; i<sirfMeas.size(); i++ )
        {
           // Point3D reciverPos = sirfMeas.get(i).GetPosInUTM();

            for (Integer Prn : sirfMeas.get(i).getSatellites().keySet()) {
                sat = sirfMeas.get(i).getSatellites().get(Prn).getSatClass(Prn);
                Boolean isLos = LosAlgorithm.ComputeLos(receiverPointInUTM, buildings1, sat);
                sirfMeas.get(i).getSatellites().get(Prn).setLOS(isLos);

            }
        }
    }
    public void ComputeLosNLOS(List<SirfPeriodicMeasurement> sirfMeas, List<Building> buildings1) {

        Sat sat =null;
        for(int i=2; i<sirfMeas.size(); i++ )
        {
            Point3D reciverPos = sirfMeas.get(i).GetPosInUTM();

                for (Integer Prn : sirfMeas.get(i).getSatellites().keySet()) {
                    sat = sirfMeas.get(i).getSatellites().get(Prn).getSatClass(Prn);
                    Boolean isLos = LosAlgorithm.ComputeLos(reciverPos, buildings1, sat);
                    sirfMeas.get(i).getSatellites().get(Prn).setLOS(isLos);

            }
        }
    }
}
