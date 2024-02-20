package Jamming;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roi_Yozevitch on 7/16/2017.
 */
public class RealClientList {

    public List<RealClient> Clients;

    int deviceID;
    public String deviceIdString;
    public int empty;

    public RealClientList(String deviceID) {
        Clients = new ArrayList<>();
        this.deviceIdString= deviceID;
        empty=0;
    }

    public RealClient getNextFix(double UTC_time)
    {

        for(int i=0;i<this.Clients.size();i++)
        {
           // System.out.println(i+") "+ this.Clients.get(i).getTime()+  "...."+UTC_time);
            if (this.Clients.get(i).getTime()>UTC_time)
            {
                for(int indx=i;indx<this.Clients.size();indx++)
                {
                    if(this.Clients.get(indx).getLat()!=0 && this.Clients.get(indx).getLon()!=0)
                        return this.Clients.get(indx);
                }
            }

        }
        return null;
    }
    public RealClient getClinetTimeStampByUTC_time(double UTC_time)
    {
        System.out.println(Clients.get(0).getTime());
        System.out.println(Clients.get(Clients.size()-1).getTime());
        if(Clients.get(0).getTime()>UTC_time || Clients.get(Clients.size()-1).getTime()<UTC_time)
            return null;
        for(int i=0;i<Clients.size();i++)
        {
            if (Math.abs(Clients.get(i).getTime()-UTC_time)<=20)
                return Clients.get(i);
        }
        System.out.println("Problem!!!");
        return null;
    }

    public RealClientList() {
        empty=1;
        Clients = new ArrayList<>();
    }

    public void printStat()
    {
        System.out.println("number of instances is "+Clients.size());
    }

    public void setClients(List<RealClient> clients) {
        Clients = clients;

    }

    public void setDeviceID(String deviceID) {
        this.deviceIdString= deviceID;
        if(Clients==null)
            Clients = new ArrayList<>();
        empty=0;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
        if(Clients==null)
            Clients = new ArrayList<>();
        empty=0;
    }

    public void empyList() {
        if(this.Clients.size()>0)
         for(int i=this.Clients.size()-1;i>=0;i--)
             this.Clients.remove(i);
    }

    public double getInitialTIme() {
        return Clients.get(0).getTime();
    }

    public double getSystemTImeOnIndex(int indx)
    {
        return this.Clients.get(indx).getTime();
    }



    public void mergeLists2(RealClientList tmpList) {
        double initTime = tmpList.Clients.get(0).getTime();
        int secIndex=0;
        int j=0;
        for (int i=0; i<tmpList.Clients.size();i++)
        {
            double curentTime = tmpList.Clients.get(i).getTime();
            double tmpTIme = this.Clients.get(i).getTime();
            int secIndextmp = this.getCorrectIndex2( curentTime);
            if(secIndextmp!=-1) {
                {
                    this.Clients.get(secIndextmp).merge(tmpList.Clients.get(i));
                    //System.out.println("Horaay "+i+ " "+secIndextmp+ " "+curentTime+"  "+tmpTIme);
                    j++;

                }
            }
        }
    }

    public void mergeLists(RealClientList tmpList) {
        double initTime = this.Clients.get(0).getTime();
        int secIndex=0;
        for (int i=0; i<tmpList.Clients.size();i++)
        {
            double curentTime = tmpList.getSystemTImeOnIndex(i);
             int secIndextmp = this.getCorrectIndex(secIndex, curentTime);
             if(secIndextmp!=-1) {
                 secIndex = secIndextmp;
                 this.Clients.get(secIndex).merge(tmpList.Clients.get(i));
             }
        }
    }

    private int getCorrectIndex(int secIndex, double curentTime) {

        for(int i = secIndex; i< this.Clients.size();i++)
        {
            if(this.getSystemTImeOnIndex(i)==curentTime)
            {
              //  System.out.print(" good "+curentTime);
                return i;
            }
        }
        //System.out.println("error "+curentTime);
        return -1;
    }

    private int getCorrectIndex2( double curentTime) {

        for(int i = 0; i< this.Clients.size();i++)
        {
            if(this.getSystemTImeOnIndex(i)==curentTime)
            {
                //  System.out.print(" good "+curentTime);
                return i;
            }
        }
        //System.out.println("error "+curentTime);
        return -1;
    }
}
