package Jamming;

import Geometry.Point2D;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Roi on 9/23/2016.
 */
public class Client {
    Point2D loc;
    double recivedSNR;
    double maximumSNR;
    double COG,SOG;




    public Client(Point2D loc, double recivedSNR, double maximumSNR) {
        this.loc = loc;
        this.recivedSNR = recivedSNR;
        this.maximumSNR = maximumSNR;
    }


    public Client(double x1, double y1, double x2, double y2, Random R1)
    {

        double X_val = x1 +  (x2-x1)*R1.nextDouble();
        if(X_val>Utils.X_Screen)
            X_val=Utils.X_Screen/2;
        if(X_val<0)
            X_val=Utils.X_Screen/2;
        double Y_val = y1 +   (y2-y1)*R1.nextDouble();
        if(Y_val>Utils.Y_Screen)
            Y_val=Utils.Y_Screen/2;
        if(Y_val<0)
            Y_val = Utils.Y_Screen/2;
        loc = new Point2D(X_val, Y_val);
        this.maximumSNR = Utils.MaximumCLinetSnr;
        COG = 2*Math.PI *R1.nextDouble();
        SOG= Utils.MaximumClientSpeed*R1.nextDouble();


    }
    public void moveByCOGSOG(Random R1)
    {
        double noisyCOG= COG + R1.nextGaussian()*Utils.Client_COG_eror;
        double noisySOG = SOG + R1.nextGaussian()*Utils.Client_SOG_erorr ;

        double dx = noisySOG*Math.cos(noisyCOG);
        double dy = noisySOG*Math.sin(noisyCOG);

        this.loc.offset(dx, dy);
    }

    public Point2D getLoc() {
        return loc;
    }

    public void setLoc(Point2D loc) {
        this.loc = loc;
    }

    public double getRecivedSNR() {
        return recivedSNR;
    }

    public void setRecivedSNR(double recivedSNR) {
        this.recivedSNR = recivedSNR;
    }

    public double getMaximumSNR() {
        return maximumSNR;
    }

    public void setMaximumSNR(double maximumSNR) {
        this.maximumSNR = maximumSNR;
    }

    public void moveClient(double dx, double dy)
    {
        this.loc.offset(dx, dy);
    }

    public void senseNoise(JammerParticle jammer) {

        this.recivedSNR = jammer.computeSNRofReceiver(this);


    }

    public void PrintClient(int index) {
        System.out.println("Clinet # "+index+". X_val : "+ this.loc.getX()+". Y_val : "+this.loc.getY() +". COG : "+ this.COG + "SOG : "+ this.SOG);
    }


    public void senseNoise(List<JammerParticle> realJammerList) {
        double[] SNR = new double[realJammerList.size()];
        for(int i=0; i< realJammerList.size(); i++)
            SNR[i] = realJammerList.get(i).computeSNRofReceiver(this);

        double tmp = SNR[0];
        for(int i=1; i<SNR.length; i++)
        {
            if(SNR[i]<tmp)
                tmp= SNR[i];
        }
        this.recivedSNR = tmp;

     //   System.out.println("Client: X pos : "+ this.loc.getX()+" Y pos : "+ this.loc.getY()+" Received SNR : "+ this.recivedSNR);
    }

    public void senseNoiseUnknownJammerPower(List<JammerParticle> realJammerList) {

        double[] SNR = new double[realJammerList.size()];
        for(int i=0; i< realJammerList.size(); i++) {
            //   SNR[i] = realJammerList.get(i).computeSNRofReceiver(this);
            SNR[i] = realJammerList.get(i).computeSNRofReceiverUnkownJammerPower(this);
        }


        double tmp = SNR[0];
        for(int i=1; i<SNR.length; i++)
        {
            if(SNR[i]<tmp)
                tmp= SNR[i];
        }
        this.recivedSNR = tmp;

    }
}

