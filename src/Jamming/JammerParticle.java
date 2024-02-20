package Jamming;

import Geometry.Point2D;
import com.sun.org.apache.regexp.internal.RE;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Created by Roi on 9/23/2016.
 */


public class JammerParticle implements Comparable<JammerParticle> {

    double JamPowe; //
    Point2D jamLoc;
    double minmalDistancetoLooseFix;
    double maximalDistancetoSense;
    double COG;
    double SOG;
    boolean isActive;
    Color color;
    public double pixelSize;
    double JamRatioToMax;
    double JammingAnge = Math.PI/5;
    double errorFigure=0;


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean inRadius(double x, double y)
   {
       double dist = this.maximalDistancetoSense;
       if(this.jamLoc.distance(new Point2D(x,y))<dist)
           return true;
       return false;
   }

    public double getJammingAnge() {
        return 360*JammingAnge/(2*Math.PI);
    }

    public void setJammingAnge(double jammingAnge) {
        JammingAnge = jammingAnge;
    }

    public JammerParticle(double x1, double y1, double x2, double y2, Random R, int jammerPower, JammerParticle bestJammer) {


        double X_val,Y_val;
        do {
             X_val = x1 +  (x2-x1)*R.nextDouble();
             Y_val = y1 +   (y2-y1)*R.nextDouble();
        } while(bestJammer.inRadius(X_val, Y_val));


        jamLoc = new Point2D(X_val, Y_val);
        this.initialVelocity(R);
        this.JamRatioToMax = jammerPower/Utils.AbsoluteMaxJammerPower;
        pixelSize = (JamRatioToMax*5)-0.001;
        JamPowe  = jammerPower;
        this.maximalDistancetoSense = Utils.AbsoluteMaxJammerRange*(jammerPower/Utils.AbsoluteMaxJammerPower);
        this.minmalDistancetoLooseFix = JamRatioToMax*Utils.AbsoluteMaxMinimumDistanceToLoseFix;
        //   System.out.println(JamPowe+" "+JamRatioToMax+" "+maximalDistancetoSense+" "+minmalDistancetoLooseFix+ " "+pixelSize);


    }


    public JammerParticle(double x1, double y1, double x2, double y2,Random R, int jammerPower) {

        double X_val = x1 +  (x2-x1)*R.nextDouble();
        double Y_val = y1 +   (y2-y1)*R.nextDouble();
        jamLoc = new Point2D(X_val, Y_val);
        this.initialVelocity(R);
        this.JamRatioToMax = jammerPower/Utils.AbsoluteMaxJammerPower;
        pixelSize = (JamRatioToMax*5)-0.001;
        JamPowe  = jammerPower;
        this.maximalDistancetoSense = Utils.AbsoluteMaxJammerRange*(jammerPower/Utils.AbsoluteMaxJammerPower);
        this.minmalDistancetoLooseFix = JamRatioToMax*Utils.AbsoluteMaxMinimumDistanceToLoseFix;
     //   System.out.println(JamPowe+" "+JamRatioToMax+" "+maximalDistancetoSense+" "+minmalDistancetoLooseFix+ " "+pixelSize);


    }

    public JammerParticle(double x1, double y1, double x2, double y2, Random r1, JammerParticle jammer) {

    }

    public double getJamRatioToMax() {
        return JamRatioToMax;
    }

    public JammerParticle(double JamPower, Point2D jam1) {
        this.jamLoc = jam1;
        this.isActive = true;
        this.JamPowe = JamPower;
        this.JamRatioToMax = JamPower/Utils.AbsoluteMaxJammerPower;
        pixelSize = JamRatioToMax*5;
        this.maximalDistancetoSense = Utils.AbsoluteMaxJammerRange*(JamPower/Utils.AbsoluteMaxJammerPower);
        this.minmalDistancetoLooseFix = JamRatioToMax*Utils.AbsoluteMaxMinimumDistanceToLoseFix;


    }

    public void setCOG(double COG) {
        this.COG = COG;
    }

    public void setSOG(double SOG) {
        this.SOG = SOG;
    }

    public int getPixelSize() {
        return Math.abs((int)Math.floor(this.pixelSize));
    }

    public void setPixelSize() {
        this.maximalDistancetoSense = Utils.AbsoluteMaxJammerRange*(this.JamPowe/Utils.AbsoluteMaxJammerPower);
        this.minmalDistancetoLooseFix = JamRatioToMax*Utils.AbsoluteMaxMinimumDistanceToLoseFix;
        this.JamRatioToMax = this.JamPowe/Utils.AbsoluteMaxJammerPower;
        this.pixelSize =(JamRatioToMax*5)-0.01;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getWeight() {
        return weight;
    }

    public double getCOG() {
        return COG;
    }

    public double getSOG() {
        return SOG;
    }

    public void initialVelocity(Random R1)
    {
        R1.nextDouble();
        this.COG = 2*Math.PI *R1.nextDouble();
        this.SOG= Utils.MaximumJammerSpeed*R1.nextDouble()-0.5;
      //  System.out.println(COG+" "+SOG);
    }

    public void moveByCOGSOG(double Sog_Error)
    {

        double noisyCOG= COG;// + R1.nextGaussian()*Utils.Client_COG_eror;
        double noisySOG = SOG+ Sog_Error;

        double dx = noisySOG*Math.cos(noisyCOG);
        double dy = noisySOG*Math.sin(noisyCOG);

        this.setPos(this.getJamLoc().getX()+dx, this.getJamLoc().getY()+dy);
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getJamPowe() {
        return JamPowe;
    }

    public double getMaximalDistancetoSense() {
        return maximalDistancetoSense;
    }

    public double getMinmalDistancetoLooseFix() {
        return minmalDistancetoLooseFix;
    }

    double weight;


    public JammerParticle(double x1, double y1, double x2, double y2, Random R)
    {
        double X_val = x1 +  (x2-x1)*R.nextDouble();
        double Y_val = y1 +   (y2-y1)*R.nextDouble();
        jamLoc = new Point2D(X_val, Y_val);
        this.initialVelocity(R);
        pixelSize = 5;
        JamPowe  = 50;
    }


    public void EvalJammer(ClientList list)
    {}

    public void setPos(double x, double y)
    {
        jamLoc = new Point2D(x, y);
    }

    public void Update(JammerParticle tmp)
    {
        JamPowe = tmp.getJamPowe();
        this.jamLoc = tmp.getJamLoc();
        this.minmalDistancetoLooseFix = tmp.getMinmalDistancetoLooseFix();
        this.maximalDistancetoSense = tmp.getMaximalDistancetoSense();
        this.weight = 1;
        this.pixelSize = tmp.getPixelSize();
    }

    public JammerParticle(JammerParticle tmp) {
        JamPowe = tmp.getJamPowe();
        this.jamLoc = tmp.getJamLoc();
        this.minmalDistancetoLooseFix = tmp.getMinmalDistancetoLooseFix();
        this.maximalDistancetoSense = tmp.getMaximalDistancetoSense();
        this.JamRatioToMax = tmp.JamRatioToMax;
        this.setPixelSize();
        this.COG = tmp.getCOG();
        this.SOG = tmp.getSOG();
    }

    public JammerParticle(double jamPowe, Point2D jamLoc, double minmalDistancetoLooseFix) {
        JamPowe = jamPowe;
        this.jamLoc = jamLoc;
        this.minmalDistancetoLooseFix = minmalDistancetoLooseFix;
        this.weight = 1;
        Random R1 = new Random();
       // this.initialVelocity(R1);
    }

    public Point2D getJamLoc() {
        return jamLoc;
    }

    public double computeSNRofReceiver(Client reciever)
    {

        double dist = this.jamLoc.distance(reciever.getLoc());
        if(dist<this.minmalDistancetoLooseFix)
            return -1;
        else if(dist> Utils.MaxJammerRange)
        {
            return 50;
        }
        else {
            Random R1 = new Random();
            double ans = (dist /Utils.MaxJammerRange) * 50  + R1.nextGaussian()*Utils.Client_Sense_Noise_Figure;
            return ans;
        }

    }
    public double computeSNRofReceiverUnkownJammerPower(Client reciever)
    {

        double dist = this.jamLoc.distance(reciever.getLoc());
        if(dist<this.minmalDistancetoLooseFix)
            return -1;
        else if(dist> this.maximalDistancetoSense)
        {
            return Utils.MaximumCLinetSnr;
        }
        else {
            Random R1 = new Random();
            double ans = (dist /this.maximalDistancetoSense) * Utils.MaximumCLinetSnr  +  R1.nextGaussian()*Utils.Client_Sense_Noise_Figure;
            return ans;
        }

    }

    public void  evalWeight(ClientList receiverList) {

        double weight = 1;
        Point2D real = new Point2D(400,400);
        System.out.println();
      //  System.out.println("Distance to Real jammer is "+ this.jamLoc.distance(real)+ "X loc :"+ this.jamLoc.getX()+"  Y pos : "+ this.jamLoc.getY());
        for(int i=0; i<Utils.NumberOfClient; i++)
        {

           Client tmp = receiverList.getClients().get(i);
            double dist = this.jamLoc.distance(tmp.getLoc());
         //   System.out.println("Clinet SNR is : " + tmp.getRecivedSNR()+". Client x pos : " +tmp.getLoc().getX()+". Client y pos : " +tmp.getLoc().getY());
            if(tmp.getLoc()!=null && dist<200) {
                double RecivedSNR = this.computeSNRofReceiver(tmp);
                weight *= Utils.GausianPorb(tmp.getRecivedSNR(), Utils.SenseEror, RecivedSNR);
              // System.out.println("Recived SNR is "+ RecivedSNR+". New Weight is : "+ weight);
            }


        }
        this.weight = weight;
    }

    public void setJamLoc(Point2D jamLoc) {
        this.jamLoc = jamLoc;
    }

    public void PrintResults(JammerParticle realJammer) {
        double dist = this.jamLoc.distance(realJammer.getJamLoc());
        System.out.println("Error[m]:"+dist+". Weight:"+this.weight);
    }

    public void PrintJam(int i, JammerParticle realJammer) {

        System.out.println("# "+i+". X_val : "+ this.jamLoc.getX() + ". Y_val : "+ this.jamLoc.getY()+". Weight : "+ this.weight+" Dist to Real Jammer : "+ this.jamLoc.distance(realJammer.getJamLoc()));
    }

    @Override
    public int compareTo(JammerParticle o) {

        if(this.weight>o.getWeight())
                return 1;
        else
         return -1;
    }

    public void PrintJamWithMargins(int i, JammerParticle realJammer, double min, double max) {

        double dist = this.jamLoc.distance(realJammer.getJamLoc());
        if(dist>min && dist<max)
            System.out.println("# "+i+". X_val : "+ this.jamLoc.getX() + ". Y_val : "+ this.jamLoc.getY()+". Weight : "+ this.weight+" Dist to Real Jammer : "+ dist);


    }

    public void Print2(double dist) {
        System.out.println("Best Jammer: X_val : "+ this.jamLoc.getX() + ". Y_val : "+ this.jamLoc.getY()+". Weight : "+ this.weight+" Dist to Real Jammer : "+ dist);
    }

    public void moveJammer(Random R1, int margin) {

     //   System.out.println("Old Point"+this.jamLoc.getX()+ " "+ this.jamLoc.getY());
        int dx = R1.nextInt(margin) - margin/2;
        int dy = R1.nextInt(margin) - margin/2;
        this.jamLoc.offset(dx,dy);
       // System.out.println("new  Point"+this.jamLoc.getX()+ " "+ this.jamLoc.getY());


    }

    public void ChangeJammerByDxDy(int dx, int dy) {
        this.jamLoc.offset(dx, dy);
    }

    public void setJamPowe(double jamPowe) {
        JamPowe = jamPowe;
    }

    public void evalWeightUnKnownJammerPower(ClientList receiverList) {
        double weight = 1;
        System.out.println();
        for(int i=0; i<Utils.NumberOfClient; i++)
        {

            Client tmp = receiverList.getClients().get(i);
            if(tmp.getLoc()!=null) {
                double RecivedSNR = this.computeSNRofReceiverUnkownJammerPower(tmp);
                weight *= Utils.GausianPorb(tmp.getRecivedSNR(), Utils.SenseEror, RecivedSNR);
                // System.out.println("Recived SNR is "+ RecivedSNR+". New Weight is : "+ weight);
            }


        }
        this.weight = weight;
    }

    public double getCOGinDegrees() {
        return this.COG*360/(Math.PI*2);
    }
}

