package Jamming;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Roi on 9/23/2016.
 */
public class JamParticles {



    private List<JammerParticle> jamList;

    public List<JammerParticle> getJamList() {
        return jamList;
    }

    public void NaiveInit(double x1, double y1, double x2, double y2)
    {
        Random R1 = new Random();
        jamList = new ArrayList<>();
        for (int i=0; i<Utils.JamerParticles;i++)
        {
            JammerParticle tmp = new JammerParticle(x1,y1,x2,y2, R1);
            jamList.add(tmp);
        }
    }



    public void InitWithVelocityOutsideRegion(double x1, double y1, double x2, double y2, JammerParticle BestJammer)
    {

        Random R1 = new Random(3333);
        jamList = new ArrayList<>();
        for (int i=0; i<Utils.JamerParticles;i++)
        {
            int JammerPower = R1.nextInt((int)Utils.AbsoluteMaxJammerPower);
            //  int JammerPower = 40;
            JammerParticle tmp = new JammerParticle(x1,y1,x2,y2, R1, JammerPower, BestJammer );
            jamList.add(tmp);
        }
    }



    public void InitWithVelocity(double x1, double y1, double x2, double y2)
    {
        Random R1 = new Random();
        jamList = new ArrayList<>();
        for (int i=0; i<Utils.JamerParticles;i++)
        {
            JammerParticle tmp = new JammerParticle(x1,y1,x2,y2, R1);
            jamList.add(tmp);
        }
    }

    public void InitWithVelocityRandomJammingPower(double x1, double y1, double x2, double y2)
    {
        Random R1 = new Random();
        jamList = new ArrayList<>();
        for (int i=0; i<Utils.JamerParticles;i++)
        {
             int JammerPower = R1.nextInt((int)Utils.AbsoluteMaxJammerPower);
            //  int JammerPower = 40;
              JammerParticle tmp = new JammerParticle(x1,y1,x2,y2, R1, JammerPower );
              jamList.add(tmp);
       }
    }

    public static void PrintNornaledWeighrs(double[] weights)
    {
        double sigma=0;
        double max_weight=0;
        for(int i=0;i<weights.length; i++)
        {
            System.out.println("Weight # "+i+" : "+weights[i]);
            sigma+=weights[i];
            if(weights[i]>max_weight)
                max_weight=weights[i];

        }

        System.out.println("Sigmas of weights is "+ sigma+" . Max weight is "+ max_weight);
    }

    public  double[] Normal_Weights()
    {
        double MaxWeight=0;
        List<Double> doubleWeight = new ArrayList<Double>();
        double[] Weights=new double[Utils.JamerParticles];
        for(JammerParticle tmp : this.jamList)
        {
            MaxWeight+=tmp.getWeight();
            doubleWeight.add(tmp.getWeight());
        }



        int i=0;
        for(JammerParticle tmp : this.jamList)
        {
            Weights[i]=tmp.getWeight()/MaxWeight;
            i++;
        }

        return Weights;
    }

    public double GetMax(double[] Weights)
    {
        double max=Weights[0];
        for(int i=1;i<Weights.length; i++)
        {
            if(Weights[i]>=max)
                max=Weights[i];
        }
        return  max;
    }

    public void Resample()
    {
        double[] Weight = Normal_Weights();
        double max=0;
       // JamParticles.PrintNornaledWeighrs(Weight);
        for(int i=0; i<Utils.JamerParticles; i++)
            max+=Weight[i];
        // System.out.println("sum of weights is "+max);
        double beta=0.0;
        Random R1 = new Random();
        List<JammerParticle> NewWeightedList = new ArrayList<JammerParticle>();
        int index = (int)(R1.nextDouble()* Utils.JamerParticles);
        double mw = GetMax(Weight);
        for(int i=0; i<Utils.JamerParticles; i++)
        {
            beta+=R1.nextDouble()*2*mw;
            while(beta>Weight[index])
            {
                beta-= Weight[index];
                index = (index+1)%Utils.JamerParticles;

            }
            JammerParticle tmp = new JammerParticle(jamList.get(index));
            // tmp.OldWeight = ParticleList.get(index).OldWeight;
            // System.out.print(tmp.pos + " ");
            // tmp.setWeight(ParticleList.get(index).getWeight());
            NewWeightedList.add(tmp);
        }
        for(int i=0; i<Utils.JamerParticles; i++)
            jamList.get(i).Update(NewWeightedList.get(i));
    }



    public void evalWeights(ClientList receiverList) {

        for(int i=0;i<Utils.JamerParticles; i++)
        {
            this.jamList.get(i).evalWeight(receiverList);

        }
    }

    public void PrintResults(JammerParticle realJammer) {

        for(int i=0;i<Utils.JamerParticles; i++)
        {
            this.jamList.get(i).PrintResults(realJammer);

        }

    }

    public void PrintAll(JammerParticle realJammer) {

        for(int i=0; i<Utils.JamerParticles; i++)
            this.jamList.get(i).PrintJam(i, realJammer);
    }

    public void PrintwithDistMargins(JammerParticle realJammer ,double j, double k) {

        for(int i=0; i<Utils.JamerParticles; i++)
            this.jamList.get(i).PrintJamWithMargins(i, realJammer, j, k );
    }

    public void moveJammers(int Max)
    {
        Random R1 = new Random();

      //  System.out.println("Move Jammers:");
        for(int i=0; i< Utils.JamerParticles; i++) {
            int dx = (int)(R1.nextDouble()*Max - Max/2);
            int dy = (int)(R1.nextDouble()*Max - Max/2);
          //  System.out.println("Dx : "+dx+ ". Dy : "+ dy);
            double X = this.jamList.get(i).getJamLoc().getX();
            double Y = this.jamList.get(i).getJamLoc().getY();
            this.getJamList().get(i).setPos(X+dx, Y+dy);
            System.out.println("x is "+this.jamList.get(i).jamLoc.getX()+" y is "+this.jamList.get(i).jamLoc.getY());

        //    this.jamList.get(i).ChangeJammerByDxDy(dx, dy);
        }
     //   System.out.println("After Change");
     //   for(int i=0; i<Utils.JamerParticles; i++)
       //     System.out.println(i+") x is "+this.jamList.get(i).getJamLoc().getX()+" y is "+this.jamList.get(i).getJamLoc().getY());

    }

    public void FindClosestJammer(JammerParticle realJammer) {

        double dist = this.getJamList().get(0).jamLoc.distance(realJammer.getJamLoc());
        int index = 0;
        for (int i = 1; i < Utils.JamerParticles; i++) {
            double tmpDist = this.getJamList().get(i).jamLoc.distance(realJammer.getJamLoc());
            if (tmpDist < dist) {
                dist = tmpDist;
                index = i;
            }
        }
        this.jamList.get(index).Print2(dist);
    }

    public void moveJammersbyCOGSOG() {
        Random R1 = new Random();
        R1.setSeed(12345);
        double Sog_eror;
        for (int i=0; i< Utils.JamerParticles; i++) {
            Sog_eror = R1.nextGaussian()*Utils.Client_SOG_erorr;
            this.getJamList().get(i).moveByCOGSOG(Sog_eror);
        }
    }

    public void evalWeightsUnknownJammerPower(ClientList receiverList) {
        for(int i=0;i<Utils.JamerParticles; i++)
        {
            this.jamList.get(i).evalWeightUnKnownJammerPower(receiverList);

        }
    }

    public boolean isConvergence(double dist, JammerParticle bestJamer) {

        for(int i=0; i<this.getJamList().size(); i++)
            if(bestJamer.jamLoc.distance(this.getJamList().get(i).jamLoc)>dist)
                return false;

        return true;

    }
}
