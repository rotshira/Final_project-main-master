package Jamming;

import Geometry.Point2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Roi on 9/27/2016.
 */
public class Simulation {

    JammerParticle realJammer;
    public List<JammerParticle> RealJammerList;
    JamParticles JammerParticles;
    List<JamParticles> additionalParticles;
    ClientList receiverList;
    JammerParticle BestJammerEstimation;

    public Simulation(Point2D realJammerLoc) {

        realJammer = new JammerParticle(50, realJammerLoc, 10);
        this.JammerParticles = new JamParticles();
        JammerParticles.InitWithVelocity(0,0,800,800); // only one Tx Power. A stationary single Jammer.
        //   JammerParticles.getJamList().get(0).setJamLoc(new Point2D(50,51))
        this.receiverList  = new ClientList();
        this.receiverList.Init(0,0,800,800);
        this.getRealJammer().initialVelocity(new Random());

    }


    public Simulation(Point2D Jam1, Point2D Jam2) {
        additionalParticles = new ArrayList<>();

        JammerParticle tmp1 = new JammerParticle(Utils.FirstSimulationJammerTxPower, Jam1);
        JammerParticle tmp2 = new JammerParticle(Utils.SecondSimulationJammerTxPower, Jam2);
        RealJammerList = new ArrayList<>();
        RealJammerList.add(tmp1);
      RealJammerList.add(tmp2);
        this.JammerParticles = new JamParticles();
      //  JammerParticles.InitWithVelocity(0,0,800,800); // only one Tx Power. A stationary single Jammer.
        JammerParticles.InitWithVelocityRandomJammingPower(0,0,800,800);
        //   JammerParticles.getJamList().get(0).setJamLoc(new Point2D(50,51))
        this.receiverList  = new ClientList();
        this.receiverList.Init(0,0,800,800);
        RealJammerList.get(0).initialVelocity(new Random());
       RealJammerList.get(1).initialVelocity(new Random(30));
    }

    public List<JamParticles> getAdditionalParticles() {
        return additionalParticles;
    }

    public void  addParticlesSet(JammerParticle bestJammer)
    {
        JamParticles newParticles = new JamParticles();
        newParticles.InitWithVelocityOutsideRegion(0,0,800,800,bestJammer);
        this.additionalParticles.add(newParticles);


    }
    public ClientList getReceiverList() {
        return receiverList;
    }

    public void setReceiverList(ClientList receiverList) {
        this.receiverList = receiverList;
    }

    public JamParticles getJammerParticles() {
        return JammerParticles;
    }

    public void setJammerParticles(JamParticles jammerParticles) {
        JammerParticles = jammerParticles;
    }

    public JammerParticle getRealJammer() {
        return realJammer;
    }

    public void setRealJammer(JammerParticle realJammer) {
        this.realJammer = realJammer;
    }

    public void init() {

    }

    public void PrintJammerPos() {

        for(int i=0; i<this.getJammerParticles().getJamList().size(); i++)
        {
            System.out.println(i+") X pos : "+this.getJammerParticles().getJamList().get(i).getJamLoc().getX()+" Y pos : "+this.getJammerParticles().getJamList().get(i).getJamLoc().getY());
        }

    }

    public void moveJammers(int max) {
        this.JammerParticles.moveJammers(max);
    }

    public void ComputeBestJammer() {

        double[] Weight = this.getJammerParticles().Normal_Weights();
        double X_pos=0;
        double Y_Pos=0;
        double Jam_Power=0;
        int i=0;
        for(JammerParticle tmp : this.getJammerParticles().getJamList())
        {
                X_pos += tmp.jamLoc.getX()*Weight[i];
                Y_Pos += tmp.jamLoc.getY() * Weight[i];
                Jam_Power +=tmp.getJamPowe()*Weight[i];
                 i++;
        }
        this.BestJammerEstimation = new JammerParticle(Jam_Power, new Point2D(X_pos, Y_Pos));
        System.out.println("Real JamPower is "+Utils.FirstSimulationJammerTxPower+". Estimated Jam Power is "+Jam_Power);

    }

    public JammerParticle getBestJammerEstimation() {

        return BestJammerEstimation;
    }
}
