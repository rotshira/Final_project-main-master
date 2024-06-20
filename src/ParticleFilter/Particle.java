package ParticleFilter;

import GNSS.Sat;
import Geometry.Building;
import Geometry.Point3D;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Roi
 * Date: 08/05/14
 * Time: 11:31
 * To change this template use File | Settings | File Templates.
 */
public class Particle implements Comparable<Particle> {

    public Point3D pos;
    public double Weight;
    public boolean OutOfRegion; // this is true if out of region
    double velocity_magnitude;
    double velocity_heading;
    double DistError;
    int NumberOfMatchedSats;
    public double OldWeight;
    public double OldMatchingSats;
    public static final  double OldNewRatio=0.8;
    private double oldWeight;



    public Particle(Particle tmp) {
        this.pos = tmp.pos;
        this.Weight = tmp.getWeight();
        this.setOldMatchingSats(tmp.getOldMatchingSats());
        this.setNumberOfMatchedSats(tmp.getNumberOfMatchedSats());
        this.LOS = tmp.getLOS();
        this.OldWeight = tmp.OldWeight;
    }

    public double getVelocity_heading() {
        return velocity_heading;
    }

    public void setVelocity_heading(double velocity_heading) {
        this.velocity_heading = velocity_heading;
    }
    public double getOldMatchingSats() {
        return OldMatchingSats;
    }

    public void setOldMatchingSats(double oldMatchingSats) {
        OldMatchingSats = oldMatchingSats;
    }

    public int getNumberOfMatchedSats() {
        return NumberOfMatchedSats;
    }

    public void setNumberOfMatchedSats(int numberOfMatchedSats) {
        NumberOfMatchedSats = numberOfMatchedSats;
    }


    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }

    public void SetLocation(Point3D pos)
    {
        this.pos=pos;
    }


    public void PrintError()
    {
        System.out.println("Error is "+ DistError+ ". Weight is: "+ this.getWeight());
    }

    public Boolean[] getLOS() {
        return LOS;
    }

    public Boolean[] LOS;
    public static void PrintArr(Boolean[] LOS){
        if(LOS==null){
            System.out.println("null");
            return;
        }
        System.out.print("{,");
        for (Boolean b: LOS){
            System.out.print(b + ",");
        }
        System.out.println("}");
    }


    public void MessureSesnor(List<Building> bs,  List<Sat> allSats)
    {

        Boolean[] los = new Boolean[allSats.size()];
        for (int i=0; i<allSats.size(); i++)
        {
            los[i] = LosData.los(allSats.get(i), Point3D.convertUTMToLatLon(this.pos,"36N"), bs);
        }
        this.LOS = los;
        //  System.out.println();

    }

    public void PrintParticle()
    {
        System.out.println("Position is "+ this.pos.toString() );
        System.out.println("Weight is +" + this.getWeight());
    }
    public Particle(double x, double y, double z) {
        this.pos = new Point3D(x,y,z);

    }
    public Particle(Point3D pos )
    {
       OutOfRegion=false;
       this.pos=pos;
        Weight = 1;
        OldWeight = 0;
        velocity_heading=0;
        velocity_magnitude=0;
    }

    public Particle(Point3D pos, double velocity_heading, double velocity_magnitude)
    {
        OutOfRegion=false;
        this.pos=pos;
        Weight = 1;
        OldWeight = 0;
        this.velocity_heading = velocity_heading;
        this.velocity_magnitude = velocity_magnitude;
    }


  /*  public void MessureSesnorWithNoise(Build bs,  List<Sat> allSats, double er)
    {

        Boolean[] los = new Boolean[allSats.size()];
        for (Sat sat : allSats){
            boolean l =  LosData.los(sat, this.pos, bs);
            double rnd = UtilsAlgorithms.nextRnd(0,1);
            if(rnd<er) {l=!l;}
            los[sat.id()] =  l;
            // System.out.print(los[sat.id()]+" ");
        }
        this.LOS = los;
        //  System.out.println();

    }
     public void MessureSesnor(Buildings bs,  List<Sat> allSats)
     {

         Boolean[] los = new Boolean[allSats.size()];
         for (int i=0; i<allSats.size(); i++)
         {
             los[i] = LosData.los(allSats.get(i), this.pos, bs);
            // System.out.print(los[sat.id()]+" ");
         }
         this.LOS = los;
       //  System.out.println();

     }
*/

  /*  double WeightFunction(double tmp, int NumberOfSats)
    {
        double newWeight=0.4*this.getNumberOfMatchedSats()+0.6*this.getOldMatchingSats();
       this.setOldMatchingSats(this.getNumberOfMatchedSats());
        newWeight = newWeight/NumberOfSats;
        newWeight = newWeight*newWeight*newWeight;
        return (newWeight);

    }
      */

    public void EvaluateWeights2(Boolean[] recordedLos, double[] LOSLikelihood)
    {
        int tmp_Weight=0;
        double newWeight;
        for(int i=0; i<recordedLos.length; i++)
        {
            if(recordedLos[i]==true) {tmp_Weight += LOSLikelihood[i];}
            else{tmp_Weight += (1-LOSLikelihood[i]);}
        }

        this.setNumberOfMatchedSats(tmp_Weight);

        if(this.OldWeight==-1)
        {
            newWeight = this.getNumberOfMatchedSats();
            newWeight = newWeight/recordedLos.length;
        }
        else
        {
            newWeight=OldNewRatio*this.getNumberOfMatchedSats()/(recordedLos.length)+(1-OldNewRatio)*this.OldWeight;

        }

        this.setOldMatchingSats(this.getNumberOfMatchedSats());
        this.setWeight(newWeight);
        this.OldWeight = newWeight;

    }
     public void EvaluateWeights(Boolean[] recordedLos)
     {
         int tmp_Weight=0;
         double newWeight;
         for(int i=0; i<recordedLos.length; i++)
         {
             if(recordedLos[i]==this.LOS[i]) {
                 if(this.LOS[i]==true)
                     tmp_Weight += 1;
                 if(this.LOS[i]==false)
                     tmp_Weight += 1;
             }

         }

        //tmp_Weight += 1;
        this.setNumberOfMatchedSats(tmp_Weight);

        if(this.OldWeight==-1)
        {
            newWeight = this.getNumberOfMatchedSats();
            newWeight = newWeight/recordedLos.length;
        }
            else
        {
          newWeight=OldNewRatio*this.getNumberOfMatchedSats()/(recordedLos.length)+(1-OldNewRatio)*this.OldWeight;

        }

         this.setOldMatchingSats(this.getNumberOfMatchedSats());
         this.setWeight(newWeight);
         this.OldWeight = newWeight;

     }


    public void EvaluateWeightsNoHistory(Boolean[] recordedLos)
    {
        int tmp_Weight=0;
        double newWeight;
        for(int i=0; i<recordedLos.length; i++)
        {
            if(recordedLos[i]==this.LOS[i]){
                tmp_Weight++;
            }
        }

        this.setNumberOfMatchedSats(tmp_Weight);

        newWeight = this.getNumberOfMatchedSats();
        newWeight = newWeight/recordedLos.length;

        this.setOldMatchingSats(this.getNumberOfMatchedSats());
        this.setWeight(newWeight);

    }
    public int ReturnNumberOfMatchingSats(Boolean[] recordedLos)
    {
        int tmp_Weight=0;
        for(int i=0; i<recordedLos.length; i++)
        {
            if(recordedLos[i]==this.LOS[i])
                tmp_Weight+=1;
        }
    return tmp_Weight;
    }


    public double ComputeError(Point3D refPoint)
    {
      this.DistError = refPoint.distance(this.pos);
        return DistError;
    }

    public void MoveParticle(ActionFunction action)
    {

        this.pos.offset(action.PivotX, action.PivotY, action.PivotZ);

    }

    public void MoveParticle(ActionFunction action, double vE, double HE)
    {
        action.velocity += vE;
        action.heading += HE;
        double PivotX = Math.cos(action.heading)*action.velocity;
        double PivotY =Math.sin(action.heading)*action.velocity;
      //  System.out.println(PivotX+ " "+PivotY);
        this.pos.offset(PivotX, PivotY, 0);

    }

    public void MoveParticleWithHeading(ActionFunction action, double vE, double hE)
    {

    }





    //this function checks if
    public boolean OutOfRegion(List<Building> bs, Point3D p1, Point3D p2)
    {
        double conf=1;
        boolean contain;
        this.OutOfRegion=false;
        if(pos.getX()<p1.getX()|| pos.getX()>p2.getX()||pos.getY()<p1.getY()||pos.getY()>p2.getY())
        {
//            System.out.println("out of range");
            this.OutOfRegion=true;
            return true;
        }

        for (Building tmp : bs)
        {
               contain=tmp.isPoint2D_inBuilding(Point3D.convertUTMToLatLon(this.pos,"36N"));
               if(contain==true)
               {
                   this.OutOfRegion=true;
//                   System.out.println("building is contain ");
                   return true;
               }
        }

//        System.out.println("stam folse");
        return false;

    }


    public double getOldWeight() {
        return oldWeight;
    }

    public void setOldWeight(double oldWeight) {
        this.oldWeight = oldWeight;
    }

    @Override
    public int compareTo(Particle o) {
        if(this.getWeight()>o.getWeight())
            return -1;
        if(this.getWeight()<o.getWeight())
            return 1;
        return 0;
    }
}
