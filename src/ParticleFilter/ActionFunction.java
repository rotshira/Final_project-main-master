package ParticleFilter;

import Geometry.Point2D;
import Geometry.Point3D;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Roi
 * Date: 08/05/14
 * Time: 14:05
 * To change this template use File | Settings | File Templates.
 */

//Action function is the function of movment.
 // action[0] is the move from path[0] to path[1]
 // hence, action[i].ChangeinSatState is the change of state from path[i] to path[i+1]
public class ActionFunction {


    public static double MIN_SoG = 0.5, CoG_ERR = 45;
    public static double MIN_MOVE = 0.5, SOG_ERR_FACTOR=1.0, CoG_POLYNOM = 2.0, SoG_ERR_POW = 0.5;


    public static Point2D randomPoint(double CoG, double SoG) {
        return randomPoint(CoG, SoG, 1.0);}
    public static Point2D randomPoint(double CoG_deg, double SoG_m2s, double dt) {
        double x=0, y=0;
        double dist = SoG_m2s*dt;
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
    private static Point2D cen_of_g(ArrayList<Point2D> b) {
        double x=0, y=0;
        int len = b.size();
        for(int i=0;i<len;i++) {
            x += b.get(i).getX();
            y += b.get(i).getY();
        }
        x /= len;
        y /= len;
        Point2D ans = new Point2D(x,y);
        return ans;
    }



    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    double velocity;
    double heading;

    public double getHeadingChange() {
        return headingChange;
    }

    public void setHeadingChange(double headingChange) {
        this.headingChange = headingChange;
    }

    double headingChange;
    double noise;



    boolean ChangeInSatState;
    int SatIndexOfChange;
    public double PivotX;
    public double PivotY;
    public double PivotZ;


    public ActionFunction(double velocity, double heading, double noise) {
        this.velocity = velocity;
        this.heading = heading;
        this.noise = noise;
    }

    public ActionFunction(double velocity, double heading)
    {
        this.velocity = velocity/100;
       // this.velocity = 2.5;
        this.heading = heading/100;
        this.heading = Math.toRadians(this.heading);
        PivotX = Math.cos(heading)*velocity;
        if(PivotX>3)
            PivotX = 0;
        PivotY = Math.sin(heading)*velocity;
        if(PivotY>3)
            PivotY = 0;
    }



    public ActionFunction(double headingChange)
    {
        this.headingChange = headingChange;
    }

    public ActionFunction(Point3D p1, Point3D p2, double velocityNoise, double headingNoise)
    {
        velocity = p1.distance(p2)+velocityNoise;
        heading = p1.angleXY_2PI(p2)+headingNoise;


        PivotX= p2.getX()-p1.getX();
        PivotY= p2.getY() - p1.getY();
      //  PivotY = Math.sin(heading)*velocity;
        ChangeInSatState=false;//defult
        PivotZ = 0;
    }

    public ActionFunction(Point3D p1, Point3D p2, double velocityNoise, double headingNoise, double heightNoise)
    {
        velocity = p1.distance(p2);
        heading = p1.angleXY_2PI(p2);


        PivotX= p2.getX()-p1.getX();
        PivotY= p2.getY() - p1.getY();
        PivotZ = p2.getZ()-p1.getZ();
        ChangeInSatState=false;//defult
    }


    public ActionFunction(Point3D p1, Point3D p2, double maxVelo)
    {
        velocity = p1.distance(p2);
        velocity = UtilsAlgorithms.AbsValue;//absValue
        heading = p1.angleXY_2PI(p2);


        //PivotX= p2.x()-p1.x();
      //  PivotY= p2.y() - p1.y();
        PivotX = Math.cos(heading)*velocity;
        PivotY = Math.sin(heading)*velocity;
        PivotZ = 0;
        if(velocity>=maxVelo)
        {
            PivotX=0;
            PivotY=0;
            PivotZ=0;
        }
        ChangeInSatState=false;//defult
    }

    public void ComputeErrors(double velocityErorr, double HeadingEror)
    {
        velocity = velocity+velocityErorr;
        heading += HeadingEror;
        PivotX = Math.cos(heading)*velocity;
        PivotY = Math.sin(heading)*velocity;
    }
    public void PrintPivots()
    {

        System.out.println("PivotX  :"+this.PivotX+" . PivotY :" +this.PivotY+" . PivotZ : " + this.PivotZ+". Abs Value ="+this.velocity+ "  .Headig is +"+ this.heading*57.3);

    }
    public boolean isChangeInSatState() {
        return ChangeInSatState;
    }


    public void ComputeChangeInSatState(Boolean[] timeStamp1, Boolean[] timeStamp2)
    {
        int index = timeStamp1.length;
        ChangeInSatState = false;
        for(int i=0;i<index; i++)
        {
          if(timeStamp1[i]!=timeStamp2[i])
          {
              ChangeInSatState=true;
              SatIndexOfChange=i;
              return;
        }
    }
    }

    @Override
    public String toString() {
        return "ActionFunction{" +
                "heading=" + heading +
                ", velocity=" + velocity +
                '}';
    }
}
