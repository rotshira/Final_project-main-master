package ParticleFilter;

import GNSS.Sat;
import Geometry.Building;
import Geometry.Point2D;
import Geometry.Point3D;
import dataStructres.NMEAPeriodicMeasurement;
import weka.classifiers.Classifier;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Particles {



    private List<Particle> ParticleList;

    static final int NumberOfParticles=625;

    public static final double VelocityGauusianError=0.1;
   public  static final double VelocityHeadingError=0.5;
   // static final int NumberOfParticles=5;
    private double MovingNoise=0;
    private double TurnNoise = 0;
    private int SenseNoise = 0;
    public static double MIN_SoG = 0.5, CoG_ERR = 15, ERR_VAL = 0.1, CORRECT_VAL = 3.0;
    public static double MIN_MOVE = 0.5, SOG_ERR_FACTOR=0.3, CoG_POLYNOM = 2.0, SoG_ERR_POW = 0.8;
    public static double MIN_SNR = 10, MAX_SNR=48;
    public static double NLOS_SNR = 28, LOS_SNR=37;
    public static double X_radius = 10;
    public static double Y_radius = 10;
    public static double PercentOfMaxWeight = 0.6; // 60 %


    public static double[] _nlos ={1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0.99,0.96,0.92,0.9,0.87,0.85,0.84,0.8,0.77,0.75,0.7,0.6,0.55,0.5,0.4,0.35,0.3,0.25,0.2,0.15,0.1,0.05,0,0,0,0,0,0,0,0};
    public static double[] _los = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0.05,0.06,0.07,0.08,0.09,0.1,0.11,0.13,0.15,0.17,0.2,0.23,0.27,0.3,0.35,0.4,0.45,0.5,0.6,0.7,0.85,0.9,0.92,0.93,0.95,1,1,1,1,1,1};
    Random R1;


    public static double eval(Boolean los, double snr) {
        double ans = -1;
        if(los) {
            int sn = (int)(snr+0.5);
            if(sn <MIN_SNR) return 0;
            if(sn>MAX_SNR) return 1;
            ans = _los[sn] * (1- _nlos[sn]);
        }
        else {
            int sn = (int)(snr+0.5);
            if(sn <MIN_SNR) return 1;
            if(sn>MAX_SNR) return 0;
            ans = (1-_los[sn]) * (_nlos[sn]);
        }
        return ans;
    }
    public static double evaluateWeightV0(Boolean[] los, double[] snr) {
        double ans = 1;
        for(int i=0;i<los.length;i++) {
            double ei = eval(los[i], snr[i]);
            ei = CORRECT_VAL*Math.max(ERR_VAL, ei);
            ans *= ei;
        }
        return ans;
    }

    public void sort()
    {
        java.util.Collections.sort(this.ParticleList);
        
    }

    public void PrintWeights1()
    {
        for(int i=0; i<NumberOfParticles; i++)
        {
            System.out.println(this.ParticleList.get(i).getWeight());
        }
    }

    public static double evaluateWeightV1(Boolean[] los, double[] snr) {
        double ans = 0;

        for(int i=0;i<los.length;i++) {
            if(snr[i]>=LOS_SNR || snr[i]<=NLOS_SNR)
            {
                if(snr[i]>=LOS_SNR && los[i]==true)
                    ans++;
                else if(snr[i]<NLOS_SNR  && los[i]==false)
                    ans++;
            }
        }
        return ans;
    }

    public List<Particle> getParticleList() {
        return ParticleList;
    }

    public void setParticleList(List<Particle> particleList) {
        ParticleList = particleList;
    }
    public Particles() {
        ParticleList = new ArrayList<Particle>();
        R1 = new Random();
    }


/** edited By Boaz **/
   /* public void DiffrentCells (int SatNumber)   // SatNumber is the number of sattelties.
    {
        Hashtable<Integer, Integer> part = new    Hashtable<Integer, Integer> ();
    	Boolean[] LOS;
        for(Particle tmp : ParticleList)
        {
          LOS = tmp.getLOS();
          int ind = compInd(LOS);
          Integer i=part.get(ind);
          if(i==null) {
        	  i = new Integer(0);
          }
          i = new Integer(i.intValue()+1);
          part.put(ind, i);
        }
        System.out.println("number of cells: "+part.size());
    }
    
    public int compInd(Boolean[] los) {
    	int ans = 0,v=1;
    	for(int i=0;i<los.length;i++) {
    		if(los[i]==true) {
    			ans += v;
    		}
    		v=v*2;
    	}
    	return ans;
    }*/

    public void ComputeWeights(Boolean[] RecordedLos)
    {

        double tmp;
        int NumberOfSats = RecordedLos.length;
        int counter=0;
        int ind;
        double [] mat = new double[NumberOfSats+1];
        for(int i=0;i<NumberOfParticles; i++)
        {

            if(ParticleList.get(i).OutOfRegion==true)
                ParticleList.get(i).setWeight(0);
            else if(ParticleList.get(i).OutOfRegion==false)
            {
                ParticleList.get(i).EvaluateWeights(RecordedLos);
                ind = ParticleList.get(i).ReturnNumberOfMatchingSats(RecordedLos);
                ParticleList.get(i).setNumberOfMatchedSats(ind);

                mat[ind]++;
            }
        }

    }

    public void ComputeWeights2(Boolean[] RecordedLos, double[] LOSLikelihood)
    {

        double tmp;
        int NumberOfSats = RecordedLos.length;
        int counter=0;
        int ind;
        double [] mat = new double[NumberOfSats+1];
        for(int i=0;i<NumberOfParticles; i++)
        {

            if(ParticleList.get(i).OutOfRegion==true)
                ParticleList.get(i).setWeight(0);
            else if(ParticleList.get(i).OutOfRegion==false)
            {
                ParticleList.get(i).EvaluateWeights2(ParticleList.get(i).LOS, LOSLikelihood);
                ind = ParticleList.get(i).ReturnNumberOfMatchingSats(RecordedLos);
                ParticleList.get(i).setNumberOfMatchedSats(ind);

                mat[ind]++;
            }
        }

    }


    public void ComputeWeightNewFunction(Boolean[] RecordedLos, List<Sat> satList)

    {
        for(int i=0;i<NumberOfParticles; i++)
        {

            if(ParticleList.get(i).OutOfRegion==true)
                ParticleList.get(i).setWeight(0);
            else if(ParticleList.get(i).OutOfRegion==false)
            {
                ParticleList.get(i).EvaluateWeightsNoHistory(RecordedLos);

            }
        }


    }


    public void ComputeWeight4KaminV1(List<Sat> allSats) // only >40 dbhz and <20 dbhz
    {

        double[] SNRvalue= new double[allSats.size()];
        for(int i=0; i<allSats.size(); i++)
            SNRvalue[i] = allSats.get(i).getSingleSNR();
        for(int i=0;i<NumberOfParticles; i++)
        {

            if(ParticleList.get(i).OutOfRegion==true)
                ParticleList.get(i).setWeight(0);
            else if(ParticleList.get(i).OutOfRegion==false)
            {
                Boolean[] Los = ParticleList.get(i).getLOS();
                double weight = evaluateWeightV0(Los, SNRvalue);

                ParticleList.get(i).setWeight(weight);
            }
        }


    }


    public void ComputeWeight4KaminV0(List<Sat> allSats)
    {

        double[] SNRvalue= new double[allSats.size()];
        for(int i=0; i<allSats.size(); i++)
            SNRvalue[i] = allSats.get(i).getSingleSNR();
        for(int i=0;i<NumberOfParticles; i++)
        {

            if(ParticleList.get(i).OutOfRegion==true)
                ParticleList.get(i).setWeight(0);
            else if(ParticleList.get(i).OutOfRegion==false)
            {
                Boolean[] Los = ParticleList.get(i).getLOS();
                double weight = evaluateWeightV0(Los, SNRvalue);
                double oldWeight =  ParticleList.get(i).getWeight();


                        ParticleList.get(i).setWeight(weight*oldWeight);
                      //  ParticleList.get(i).setOldWeight(weight*oldWeight);
            }
        }


    }
    /**
     *This function calculates the equation for all particles according to the LOS array of the original particle
     * It receives an array called RecordedLos which is the array of the LOS of the original particle at the specific point in time
     * and for each particle calculates the weight for it.
     * The weight of each particle is a calculation of the number of adjustments of its LOS array to RecordedLos,
     * divided by the number of satellites
     *  **/
    public void ComputeWeightsNoHistory(Boolean[] RecordedLos)
    {

        double tmp;
        int NumberOfSats = RecordedLos.length;
        int counter=0;
        int ind;
        double [] mat = new double[NumberOfSats+1];
        for(int i=0;i<NumberOfParticles; i++)
        {

            if(ParticleList.get(i).OutOfRegion==true)
                ParticleList.get(i).setWeight(0);
            else if(ParticleList.get(i).OutOfRegion==false)
            {
                ParticleList.get(i).EvaluateWeightsNoHistory(RecordedLos);
                ind = ParticleList.get(i).ReturnNumberOfMatchingSats(RecordedLos);
                ParticleList.get(i).setNumberOfMatchedSats(ind);

                mat[ind]++;
            }
        }

    }


   public Point3D GetOptimalParticle(Point3D lastPar)
    {

        double w=this.ParticleList.get(0).getWeight();
        double tmpW=0;
        double thres=5;
        int index=0;
        int i;
        for( i=0; i<NumberOfParticles; i++)

            tmpW = this.ParticleList.get(i).getWeight();
            if(tmpW>w)
            {
                w=tmpW;
                index=i;
            }

      //  System.out.print(w+ " ");
        double x=0, y=0, z=0,p=0;
            Point3D tmp = new Point3D(lastPar);
       // tmp.offset(actionFunction.PivotX, actionFunction.PivotY,0);

        for( i=0; i<NumberOfParticles; i++)
        {

            double dist = this.ParticleList.get(i).pos.distance(lastPar);
            if (dist < thres) {
                double tmpWeight = ParticleList.get(i).getWeight();
                if (tmpWeight != 0) {
                    tmpWeight = tmpWeight / w;
                    double cw = Math.pow(tmpWeight, UtilsAlgorithms.Weight_pow);

                    p += cw;
                    x += cw * ParticleList.get(i).pos.getX();
                    y += cw * ParticleList.get(i).pos.getY();
                    z += cw * ParticleList.get(i).pos.getZ();
                }
            }

        }
        x=x/p;y=y/p;z=z/p;
        Point3D ans = new Point3D(x,y,z);

        return ans;
    }




    public Point3D GetBestParticle()
    {
        Particle tmp  = getParticleWithMaxWeight();
        double x = tmp.pos.getX();
        double y = tmp.pos.getY();

        double NewX = 0;
        double NewY=0;
        double NumOfRelevantParticles=0;
        double MinWeight = tmp.getWeight()*PercentOfMaxWeight;
        for(int i=0; i<NumberOfParticles; i++)
        {
            if(InRegion(ParticleList.get(i), x, y, MinWeight))
            {
                NewX+=ParticleList.get(i).pos.getX();
                NewY+=ParticleList.get(i).pos.getY();
                NumOfRelevantParticles++;

            }


        }
        NewX = NewX/NumOfRelevantParticles;
        NewY  = NewY/NumOfRelevantParticles;
        return new Point3D(NewX, NewY, 1);
    }





    private boolean InRegion(Particle particle, double x, double y, double weight) {

        if(Math.abs(particle.pos.getX()-x)<X_radius && Math.abs(particle.pos.getY()-y)<Y_radius && particle.getWeight() > weight)
            return true;
        return false;


    }

    private Particle getParticleWithMaxWeight() {
        Particle tmp = ParticleList.get(0);
        for(int i=1; i<NumberOfParticles; i++)
        {
            if(ParticleList.get(i).getWeight()>tmp.getWeight())
                tmp = new Particle(ParticleList.get(i));
        }

        return tmp;
    }

    /**
     *This function returns the point that is the "Particle With Max Weight",
     *but it is a weighted point for all the particles according to their weight
     */
    public Point3D GetParticleWithMaxWeight()
    {
        double w=this.ParticleList.get(0).getWeight();

        double tmpW=0;
        int index=0;
        for(int i=1; i<NumberOfParticles; i++)

        {
            tmpW = this.ParticleList.get(i).getWeight();

            if(tmpW>w)
            {
                w=tmpW;
                index=i;
            }

        }

      //  System.out.print(w+ " ");
        double x=0, y=0, z=0,p=0;
        for(int i=0; i<NumberOfParticles; i++)
        {

            double tmpWeight = ParticleList.get(i).getWeight();
            if(tmpWeight!=0)
            {
                tmpWeight = tmpWeight/w;
                double cw = Math.pow(tmpWeight,UtilsAlgorithms.Weight_pow);
                p+=cw;
                x+=cw*ParticleList.get(i).pos.getX();
                y+=cw*ParticleList.get(i).pos.getY();
                z+=cw*ParticleList.get(i).pos.getZ();
            }

        }
        x=x/p;y=y/p;z=z/p;
        Point3D ans = new Point3D(x,y,z);
        Point3D ans2 = new Point3D(ParticleList.get(index).pos);
        return ans;
    }


    public Point3D GetOptimalLocation(int Number)
    {
        double Xpos=0, Ypos=0, Zpos=0;
        int Matched=0;
        Point3D ans;
        for(int i=0; i<NumberOfParticles; i++)
        {
            if(ParticleList.get(i).getNumberOfMatchedSats()>Number)
            {
                Xpos += ParticleList.get(i).pos.getX();
                Ypos += ParticleList.get(i).pos.getY();
                Zpos += ParticleList.get(i).pos.getZ();
                Matched++;
            }
        }

        Xpos = Xpos/Matched;
        Ypos = Ypos/Matched;
        Zpos = Zpos/Matched;
        ans = new Point3D(Xpos, Ypos, Zpos);
        return ans;

    }

    /**
     *This function goes through all the particles and sets each of the particles the corresponding OutFfRegion value
     */
    public void OutFfRegion(List<Building> bs, Point3D p1, Point3D p2)
    {
        double tmp;
        boolean OOR; // out of region
        for (int i=0; i<NumberOfParticles; i++)
        {
            OOR = ParticleList.get(i).OutOfRegion(bs, p1, p2);
            if(OOR==true)
            {
                ParticleList.get(i).OutOfRegion=true;

            }
            else
            {
                ParticleList.get(i).OutOfRegion=false;
            }
        }
        //  System.out.println("number for OOR is "+ numberOutOfrefion);
    }


    public double[] Normal_Weights()
    {
       double MaxWeight=0;
       List<Double> doubleWeight = new ArrayList<Double>();
       double[] Weights=new double[NumberOfParticles];
        for(Particle tmp : this.ParticleList)
        {
              MaxWeight+=tmp.getWeight();
            doubleWeight.add(tmp.getWeight());
        }



        int i=0;
        for(Particle tmp : this.ParticleList)
        {
           Weights[i]=tmp.getWeight()/MaxWeight;
            i++;
        }

        return Weights;
    }

    public double[] Normal_WeightsIncludeOriginal()
    {
        double MaxWeight=0;
        List<Double> doubleWeight = new ArrayList<Double>();
        double[] Weights=new double[NumberOfParticles];
        for(Particle tmp : this.ParticleList)
        {
            MaxWeight+=tmp.getWeight();
            doubleWeight.add(tmp.getWeight());
        }




        double NomalWeightTMP;
        for(int j=0; j<this.ParticleList.size(); j++)
        {

            NomalWeightTMP =ParticleList.get(j).getWeight()/MaxWeight;
            Weights[j] = NomalWeightTMP;
            ParticleList.get(j).setWeight(NomalWeightTMP);

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

    public List<Point3D>  GetPoint3dList()
    {
        List<Point3D> PointList = new ArrayList<Point3D>();
        for(Particle tmp: ParticleList)
            PointList.add(tmp.pos);
        return PointList;
    }



    public void PrintNoGoodParticles(double threshold)
    {
        int nom=0;
        for(Particle tmp: ParticleList)
            if(tmp.getWeight()>=threshold)
                nom++;
        System.out.println("Number of particles with match above "+threshold + ": "+nom);
    }
    public void Resample2()
    {

        int index=0;
        boolean oor=true;
        Random R1 = new Random();
       List<Point3D> NewWeightedList = new ArrayList<Point3D>();
       /// int index = (int)(R1.nextDouble()* NumberOfParticles);
   //     double mw = GetMax(Weight);
          double tmpWeight=0;

        for(int i=0; i<NumberOfParticles; i++)
        {

            while(tmpWeight<17)
            {

                index = R1.nextInt(NumberOfParticles-1);
                tmpWeight = ParticleList.get(index).getWeight();


            }
            //ParticleList.set(i, this.ParticleList.get(index));
            Point3D tmp = new Point3D(ParticleList.get(index).pos);
           // System.out.print(tmp.pos + " ");
           // tmp.setWeight(ParticleList.get(index).getWeight());
            NewWeightedList.add(tmp);
            tmpWeight=0;

        }

       SetAfterResample(NewWeightedList);
       // ParticleList.addAll(NewWeightedList);
    }

    /**
     *This function receives a list of points called NewList
     *and initializes the particles according to the positions of the points in NewList
     *And the weight of each particle is initialized to 1
     *so that in the next round all the particles will have the same weight for the sample
     */
    public void SetAfterResample(List<Point3D> NewList)
    {
        for(int i=0; i<NumberOfParticles; i++)
        {
            ParticleList.get(i).SetLocation(NewList.get(i));
            ParticleList.get(i).setWeight(1);
        }

    }

    public void SetAfterParticleResample(List<Particle> NewList)
    {
        for(int i=0; i<NumberOfParticles; i++)
        {
            ParticleList.set(i,NewList.get(i));

        }

    }

    /**
     * This function resamples the particles,
     * Each particle has a weight between 0 and 1, which is its probability of being the original particle.
     * According to this probability the function resamples particles and initializes the particles to be the particles that the funccia sampled.
     * A particle with a greater weight has a greater probability of being sampled
     */

    public void Resample()
    {
       double[] Weight = Normal_Weights();
        double max=0;
        for(int i=0; i<NumberOfParticles; i++)
              max+=Weight[i];
       // System.out.println("sum of weights is "+max);
        double beta=0.0;
        Random R1 = new Random();
        List<Point3D> NewWeightedList = new ArrayList<Point3D>();
        int index = (int)(R1.nextDouble()* NumberOfParticles);
        double mw = GetMax(Weight);
        for(int i=0; i<NumberOfParticles; i++)
        {
            beta+=R1.nextDouble()*2*mw;
            while(beta>Weight[index])
            {
                beta-= Weight[index];
                index = (index+1)%NumberOfParticles;

            }
            Point3D tmp = new Point3D(ParticleList.get(index).pos);
//            tmp.OldWeight = ParticleList.get(index).OldWeight;
//             System.out.print(tmp.pos + " ");
//             tmp.setWeight(ParticleList.get(index).getWeight());
            NewWeightedList.add(tmp);
        }
        //NewWeightedList is the list of the new sampled points with which the particles will be initialized
        SetAfterResample(NewWeightedList);
    }


    public void ResampleWithMemory()
    {
        double[] Weight = Normal_Weights();
        double max=0;
        for(int i=0; i<NumberOfParticles; i++)
            max+=Weight[i];
        // System.out.println("sum of weights is "+max);
        double beta=0.0;
        Random R1 = new Random();
        List<Particle> NewWeightedList = new ArrayList<Particle>();
        int index = (int)(R1.nextDouble()* NumberOfParticles);
        double mw = GetMax(Weight);
        for(int i=0; i<NumberOfParticles; i++)
        {
            beta+=R1.nextDouble()*2*mw;
            while(beta>Weight[index])
            {
                beta-= Weight[index];
                index = (index+1)%NumberOfParticles;

            }
            Particle tmp = new Particle(ParticleList.get(index));
           // Point3D tmp = new Point3D(ParticleList.get(index).pos);
            // System.out.print(tmp.pos + " ");
            // tmp.setWeight(ParticleList.get(index).getWeight());
            NewWeightedList.add(tmp);
        }

        SetAfterParticleResample(NewWeightedList);

    }



        public void TestRegion(Point3D p1, Point3D p2)
    {
          for( int i=0; i<NumberOfParticles; i++)
          {
              if(ParticleList.get(i).pos.getX()<p1.getX()||ParticleList.get(i).pos.getX()>p2.getX()||ParticleList.get(i).pos.getY()<p1.getY()||ParticleList.get(i).pos.getY()>p2.getY())
              {
                  System.out.println("Out of Region. Weight is  "+ ParticleList.get(i).getWeight()+" . Bool State "+ ParticleList.get(i).OutOfRegion);
              }
          }

    }
      //  this.ParticleList = NewWeightedList;
      /*  index = int(random.random() * N)
        beta = 0.0
        mw = max(w)
        for i in range(N):
        beta += random.random() * 2.0 * mw
        while beta > w[index]:
        beta -= w[index]
        index = (index + 1) % N
        p3.append(p[index])
        p = p3    */


    Point3D WeightedCenterOfMass()
    {  return    WeightedCenterOfMass(1.0);}

     Point3D WeightedCenterOfMass(double pow)
     {
         double x=0,y=0,z=0, p=0;
         if(pow<=0) {pow =1.0;}
         for(int i=0;i<NumberOfParticles;i++) {
             double w = ParticleList.get(i).getWeight();
             if(w!=0)
             {
             p+=w;
             x+=w*ParticleList.get(i).pos.getX();
             y+=w*ParticleList.get(i).pos.getY();
             z+=w*ParticleList.get(i).pos.getZ();
             }

         }
         x=x/p;y=y/p;z=z/p;
         Point3D ans = new Point3D(x,y,z);

         return ans;
     }
    public void DrawParticles()
    {}

    public void ComputeCenterOfMassError(double pow,Point3D refpoint)
    {

        Point3D Center = WeightedCenterOfMass(pow);
        double AverErr = refpoint.distance(Center);
        System.out.print(AverErr + ",");
    }


    public double ComputeAndPrintErrors(Point3D refPoint)
    {
        double AverageError = 0;
        double P=0;
        for(int i=0; i<NumberOfParticles; i++)
        {
            if(ParticleList.get(i).getWeight()!=0.0)
            {
                double weight = ParticleList.get(i).getWeight();
                weight = Math.pow(ParticleList.get(i).getWeight(),UtilsAlgorithms.Weight_pow);
                AverageError= AverageError+ ParticleList.get(i).ComputeError(refPoint)*weight;
                // ParticleList.get(i).PrintError();
                P+=weight;

            }
        }
        AverageError = AverageError/P;
        System.out.print(AverageError + ",");
        return AverageError;
    }


    public void initParticles(Point3D p1, Point3D p2)
    {

        double sqrtPar = Math.sqrt(NumberOfParticles);
        double tmp = 100/sqrtPar;
        double height=1;
        for(int i=0;i<sqrtPar;i++)
            for(int  j =0; j<sqrtPar ; j++ )
            {

                Particle tmpParticle = new Particle(p1.getX()+tmp*i,p1.getY()+tmp*j, height);
                tmpParticle.setWeight(1);
//                Particle tmpParticle = new Particle(670103.5, 3551179.5, 1);
                ParticleList.add(tmpParticle);

           }


    }




    public void initParticlesWithHeading(Point3D p1, Point3D p2)
    {

        double sqrtPar = Math.sqrt(NumberOfParticles);
        Random R1 = new Random(88);
        double heading;
        heading = R1.nextDouble()*2*Math.PI;
        double tmp = 100/sqrtPar;
        double height=2; // The height of the reciver - over Roi's head.
        for(int i=0;i<sqrtPar;i++)
            for(int  j =0; j<sqrtPar ; j++ )
            {

                Particle tmpParticle = new Particle(p1.getX()+tmp*i,p1.getY()+tmp*j, height);
                heading = Math.toDegrees(R1.nextDouble()*2*Math.PI);
               tmpParticle.setVelocity_heading(heading);
                //todo Roi Fix it
                tmpParticle.OldWeight = -1;
//                  Particle tmpParticle = new Particle(670103.5, 3551179.5, 1);
                ParticleList.add(tmpParticle);

            }


    }



    public void initParticlesIn3D(Point3D p1, Point3D p2,double height)
    {
        //we need to make this function much more generic
        /*int xMeter =(int)Math.abs(p2.x()-p1.x());
        int yMeter = (int)Math.abs(p2.y()-p1.y());
        int ParticlesX = NumberOfParticles/xMeter;
        int ParticlesY = NumberOfParticles/yMeter;*/

        for(double h=0; h<height; h+=25)
        for(int i=0;i<25; i++)
            for(int  j =0; j<25 ; j++ )
            {
               //Point3D p = new Point3D(p1.x()+4*i+0.5,p1.y()+4*j+0.5);
                Particle tmpParticle = new Particle(p1.getX()+4*i+0.5,p1.getY()+4*j+0.5, h);
                //  Particle tmpParticle = new Particle(670103.5, 3551179.5, 1);
                ParticleList.add(tmpParticle);

            }


    }


    public void MessureSignalFromSatsWithNoise(List<Building> bs, List<Sat> allSats, double er)
    {
        for(int i=0; i<NumberOfParticles; i++)
        {
           // ParticleList.get(i).MessureSesnorWithNoise(bs, allSats, er);
            //todo ROI fix it
        }
    }

    /**
     * This function goes through each and every particle and calculates for it the value of its LOS array
     */
    public void MessureSignalFromSats(List<Building> bs, List<Sat> allSats)
    {
         for(int i=0; i<NumberOfParticles; i++)
         {
             ParticleList.get(i).MessureSesnor(bs, allSats);
         }
    }

    public Point3D randomPoint(double x, double z){
        double r = UtilsAlgorithms.nextRnd(-x,+x);
        double rz = UtilsAlgorithms.nextRnd(-z,+z);
        double ra = UtilsAlgorithms.nextRnd(0,Math.PI*2);
        double rx = r*Math.cos(ra);
        double ry = r*Math.sin(ra);
        return new Point3D(rx,ry,rz);
    }

    public Point3D ourrandomPoint(double x, double z){
        // Reduced noise range
        double r = UtilsAlgorithms.nextRnd(-x * 0.5, +x * 0.5);  // Half the original range
        double rz = UtilsAlgorithms.nextRnd(-z * 0.5, +z * 0.5);  // Half the original range
        double ra = UtilsAlgorithms.nextRnd(0, Math.PI * 2);
        double rx = r * Math.cos(ra);
        double ry = r * Math.sin(ra);
        return new Point3D(rx, ry, rz);
    }

    public Point3D randomPoint(double x){ return randomPoint(x,0);
    }



    public void MoveParticlesBySOG_COG(double SOG, double COG)

    {
        MoveParticlesBySOG_COG( SOG,  COG,1);
    }


    public void MoveParticlesBySOG_COG(double SOG, double COG, double dt)
    {

        //System.out.println();
        SOG = UtilsAlgorithms.convertKnots2m_s(SOG);
        for(int i=0; i<NumberOfParticles; i++)
        {
            Point2D pivot =  randomPivotPoint(SOG, COG, dt);
          //  System.out.print(pivot.getX()+"-"+pivot.getY()+"  ");
            ParticleList.get(i).pos.offsetByPoint(pivot);

        }

    }

    public void MoveParticlesBySOG_COGWithHeading(double SOG, double COG)
    {

        //System.out.println();
        double c=0.5;
        double currnetCOG;
        SOG = UtilsAlgorithms.convertKnots2m_s(SOG);
        for(int i=0; i<NumberOfParticles; i++)
        {
             currnetCOG = c*COG + (1-c)*ParticleList.get(i).getVelocity_heading();
            ParticleList.get(i).setVelocity_heading(currnetCOG);
            Point2D pivot =  randomPivotPoint(SOG, COG);
            //  System.out.print(pivot.getX()+"-"+pivot.getY()+"  ");
            ParticleList.get(i).pos.offsetByPoint(pivot);

        }

    }

    public static Point2D randomPivotPoint(double SoG, double CoG) //COG - degrees. SOG m/s
    {
        return randomPivotPoint(SoG, CoG, 1.0);}
    public static Point2D randomPivotPoint( double SoG_m2s, double CoG_deg, double dt) {
        double x=0, y=0;
        double dist = SoG_m2s*dt;
        if(SoG_m2s <=0 ) return new Point2D(0,0);
        if(SoG_m2s < MIN_SoG) {
            double ds = Math.max(dist, 	MIN_MOVE);
            double rx = Math.random()-0.5;
            double ry = Math.random()-0.5;
            x = rx*ds;
            y = ry*ds;
        }
        else{
            double d1 = Math.random()-0.5;
            dist += d1*SOG_ERR_FACTOR;

            double d2 = Math.random();
            d2 = Math.pow(d2, CoG_POLYNOM);
            if(Math.random()<0.5) d2 = -d2;
            double err_cog = 1/Math.pow(SoG_m2s,SoG_ERR_POW);
            double d_cog = err_cog*d2*CoG_ERR;
            double ang_rad = Math.toRadians(CoG_deg+d_cog);
            x = dist*Math.sin(ang_rad);
            y = dist*Math.cos(ang_rad);
        }
        Point2D ans = new Point2D(x,y);
        return ans;
    }

    /**
     * This function moves all the particles according to the corresponding Action in the current round with an error
     */
    public void MoveParticleWithError(ActionFunction action)
    {

        double PivotX, PivotY;
        for(int i=0; i<NumberOfParticles; i++)
        {
            Point3D Noise = randomPoint(1.0,0);
            PivotX = action.PivotX+Noise.getX();
            PivotY = action.PivotY+Noise.getY();
            ParticleList.get(i).pos.offset(PivotX, PivotY, action.PivotZ);
        }

    }

    public void ourMoveParticleWithError(ActionFunction action)
    {

        double PivotX, PivotY;
        for(int i=0; i<NumberOfParticles; i++)
        {
            Point3D Noise = ourrandomPoint(1.0,0);
            PivotX = action.PivotX+Noise.getX();
            PivotY = action.PivotY+Noise.getY();
            ParticleList.get(i).pos.offset(PivotX, PivotY, action.PivotZ);
        }

    }

    public void MoveParticlesByHeading2D(ActionFunction action)
    {
        double PivotX, PivotY;
        double Heading, Velocity;
       // Velocity=action.getVelocity();
        Velocity=action.getVelocity();
        if(Velocity>4)
            Velocity = 0;
        double Vel_error;
        double Head_Error;
        Random R1 = new Random();
        for(int i=0; i<NumberOfParticles; i++)
        {
            Heading = ParticleList.get(i).getVelocity_heading();
            Heading = Heading - (action.headingChange*2*Math.PI/360);
            ParticleList.get(i).setVelocity_heading(Heading);
         //   Vel_error = R1.nextDouble()*2;
            Vel_error = Velocity;

            Head_Error = R1.nextDouble()/10;
            PivotX = Math.cos(Heading+Head_Error)*Vel_error;
            PivotY = Math.sin(Heading+Head_Error)*Vel_error;
          //  ParticleList.get(i).setVelocity_heading(Heading);
            ParticleList.get(i).pos.offset(PivotX, PivotY, 0);
        }
    }

    public void MoveNaive()
    {
        Random R1= new Random();
        double tmpdeg;
        double dist = 1.5;
        double N=0.8;
        double PivotX, PivotY;
        for(int i=0; i<NumberOfParticles; i++)
        {
            double r = R1.nextGaussian()*0.1+dist;
          //  r = Math.pow(r,N);
            tmpdeg = R1.nextDouble()*2*Math.PI;
            PivotX = Math.cos(tmpdeg)*r;
            PivotY = Math.sin(tmpdeg)*r;
            ParticleList.get(i).pos.offset(PivotX, PivotY, 0);
        }
    }
    public void MoveParticles(ActionFunction action)
    {


        //action.ComputeErrors(Velocity_error, Heading_error);
        double Velocity_error, Heading_error;
        for(int i=0; i<NumberOfParticles; i++)
        {
            double tmp=  UtilsAlgorithms.getVelocityGauusianError();
            double tmp2=UtilsAlgorithms.getVelocityHeadingError();
            Velocity_error =R1.nextGaussian()*UtilsAlgorithms.getVelocityGauusianError();
            Heading_error = R1.nextGaussian()*UtilsAlgorithms.getVelocityHeadingError();

            ParticleList.get(i).MoveParticle(action, Velocity_error, Heading_error);
//            ParticleList.get(i).MoveParticle(action);

        }

    }

    public void MoveParticlesNaive(ActionFunction action)
    {


        //action.ComputeErrors(Velocity_error, Heading_error);
        double Velocity_error, Heading_error;
        for(int i=0; i<NumberOfParticles; i++)
        {
            double tmp=  UtilsAlgorithms.getVelocityGauusianError();
            double tmp2=UtilsAlgorithms.getVelocityHeadingError();
            Velocity_error =R1.nextGaussian()*UtilsAlgorithms.getVelocityGauusianError();
            Heading_error = R1.nextGaussian()*UtilsAlgorithms.getVelocityHeadingError();

            ParticleList.get(i).MoveParticle(action, Velocity_error, Heading_error);
        }

    }


    public void Print3DPoints()
    {
        int i=0;
        for(i=0;i<NumberOfParticles; i++)
            System.out.print(ParticleList.get(i).pos + " ");
    }

    @Override
    public String toString() {
        return "Particles{" +
                "ParticleList=" + ParticleList.size() +
                '}';
    }

    public void PrintParticles()
    {
        int i=1;
        for (Particle tmp : ParticleList)
        {
            System.out.println( "Particle number "+ i);
            i++;
            tmp.PrintParticle();
        }

    }

    public void getNaiveLosNlosState(NMEAPeriodicMeasurement meas) {

    }

    public void printWeights() {
        for(int i=0; i<ParticleList.size(); i++)
            System.out.print(ParticleList.get(i).getWeight()+ " ");
    }

//    public void printParticlePositions() {
//        System.out.println("Particle Positions:");
//        for (Particle p : ParticleList) {  // Corrected from particleList to ParticleList
//            System.out.println(p.pos);
//        }
//    }

    public void MessureSignalFromSatsWithML(List<Building> bs, List<Sat> allSats, Classifier classifier, boolean[] recordedLos, Instances dataset) throws Exception {
        for (Particle particle : ParticleList) {
            double[] featureVector = extractFeatures(particle, allSats, bs);

            // Use the modified createInstance method
            weka.core.Instance instance = createInstance(featureVector, dataset);

            // Use the classifier to predict LOS/NLOS
            double prediction = classifier.classifyInstance(instance);

            Boolean[] los = new Boolean[allSats.size()];
            for (int i = 0; i < los.length; i++) {
                los[i] = (prediction == 1.0); // Assuming 1.0 represents LOS
            }
            particle.LOS = los;
        }
    }



    // This function creates a feature vector based on particle and satellite information.
    private double[] extractFeatures(Particle particle, List<Sat> allSats, List<Building> bs) {
        // Example feature extraction (you can customize this based on your use case):
        double[] features = new double[allSats.size()];
        for (int i = 0; i < allSats.size(); i++) {
            Sat sat = allSats.get(i);
            features[i] = sat.distanceFromSatToPos(particle.pos);  // Feature could be distance, azimuth, etc.
        }
        return features;
    }

    // Create a Weka Instance from a feature vector.
    // Modify this method to accept Instances dataset
    private weka.core.Instance createInstance(double[] featureVector, Instances dataset) {
        weka.core.DenseInstance instance = new weka.core.DenseInstance(1.0, featureVector);
        instance.setDataset(dataset);  // This assigns the instance to the dataset
        return instance;
    }



}
