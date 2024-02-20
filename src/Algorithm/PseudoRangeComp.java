package Algorithm;

import Geometry.Point3D;
import Jama.Matrix;
//import Utils.GeoUtils;
import dataStructres.NMEAPeriodicMeasurement;
import dataStructres.STMPeriodMeasurment;
import dataStructres.SirfPeriodicMeasurement;

/**
 * Created by Roi on 09/04/2015.
 */
public class PseudoRangeComp {

    static public void computePseudoRangeFromSirfPeriodicMessurment(SirfPeriodicMeasurement me)
    {


        Point3D intialGeuss = new Point3D(0,0,0);
        int numberOfSats = me.getSatellites().size();
        double[] pseudoRanges = new double[numberOfSats];
        double rao[] = new double[numberOfSats];
        double bu=0;
        double[] gu = new double[3];

        for (int i = 0; i < numberOfSats; i++)
        {
            rao[i] = Math.sqrt(Math.pow(intialGeuss.getX()-me.getSatellites().get(i).getxPos(), 2)+Math.pow(intialGeuss.getY()-me.getSatellites().get(i).getyPos(), 2)+Math.pow(intialGeuss.getZ()-me.getSatellites().get(i).getzPos(), 2));
         //   pseudoRanges[i] = me.getSatellites().get(i).getCorrectedPR(); todo Roi : fix this pseudorange

        }
        double alpha[][] = creatAlpha(numberOfSats);

        double erro=1;

        while(erro>0.01){
            for (int i = 0; i < alpha.length; i++) {
                for (int j = 0; j < 3; j++) {
                    alpha[i][0] = (intialGeuss.getX()-me.getSatellites().get(i).getxPos())/(rao[i]);
                    alpha[i][1] = (intialGeuss.getY()-me.getSatellites().get(i).getyPos())/(rao[i]);
                    alpha[i][2] = (intialGeuss.getZ()-me.getSatellites().get(i).getzPos())/(rao[i]);

                }
            }
            double[] drao = new double[numberOfSats];
            setDrao(drao, rao, pseudoRanges, bu);

            Matrix aMatrix = new Matrix(alpha);
            aMatrix = aMatrix.inverse();
            double[][] pseudoInverse_Alpha = aMatrix.getArray();
            double[] dl = multMatrixToVector(pseudoInverse_Alpha, drao);
            bu+=dl[dl.length-1];

            for (int i = 0; i < 3; i++) {
                gu[i]+=dl[i];
            }

            erro = Math.pow(dl[0], 2)+Math.pow(dl[1], 2)+Math.pow(dl[2], 2);


            for (int i = 0; i < rao.length; i++) {
                rao[i] = Math.sqrt(Math.pow(intialGeuss.getX()- me.getSatellites().get(i).getxPos(), 2)+Math.pow(intialGeuss.getY()-me.getSatellites().get(i).getyPos(), 2)+Math.pow(intialGeuss.getZ()-me.getSatellites().get(i).getzPos(), 2));
            }
        }


        for (int i = 0; i < numberOfSats; i++)
        {
            me.getSatellites().get(i).setPseudoRangeWithDeltaT(rao[i]);
            System.out.println(rao[i] );
        }
        Point3D userPosEcef = new Point3D(gu[0], gu[1], gu[2]);
//        userPosEcef = GeoUtils.convertECEFtoLATLON(userPosEcef);
        System.out.println(" user pos is " + userPosEcef );


    }



    static public void computePseudoRangeFromStmMessurment(STMPeriodMeasurment me) {

        Point3D initialGeuss = new Point3D(0,0,0);
        computePseudoRangeFromStmMessurment(me, initialGeuss);
    }

    static public void computePseudoRangeFromStmMessurment(STMPeriodMeasurment me, Point3D intialGeuss)
    {


        int numberOfSats = me.getSVs().size();
       // numberOfSats = 4;
        System.out.println("number of sats "+ numberOfSats);
        if (numberOfSats<4)
        {
            System.out.println("Not enough SV for computation");
            return;
        }
       numberOfSats = 4;
        for(int i=0; i<numberOfSats; i++)
        {
            System.out.println("PRN "+ me.getSVs().get(i).getPrn()+". Position "+ me.getSVs().get(i).getECEFpos()+ ". PseudoRnage is "+me.getSVs().get(i).getCorrectedPR() +". Navigation data "+ me.getSVs().get(i).isNavigationData() );
        }
        double[] pseudoRanges = new double[numberOfSats];
        double rao[] = new double[numberOfSats];
        double bu=0;
        double[] gu = new double[3];
        gu[0]= intialGeuss.getX();
        gu[1] = intialGeuss.getY();
        gu[2] = intialGeuss.getZ();
        for (int i = 0; i < numberOfSats; i++)
        {
            rao[i] = Math.sqrt(Math.pow(intialGeuss.getX()-me.getSVs().get(i).getEcefXpos(), 2)+Math.pow(intialGeuss.getY()-me.getSVs().get(i).getEcefYpos(), 2)+Math.pow(intialGeuss.getZ()-me.getSVs().get(i).getEcefZpos(), 2));
            pseudoRanges[i] = me.getSVs().get(i).getCorrectedPR();
            System.out.println(me.getSVs().get(i).getPrn()+ " ");
         //   System.out.println("  PRN " + me.getSVs().get(i).getPrn() + ":" + pseudoRanges[i] + me.getSVs().get(i).getECEFpos());
        }

        double alpha[][] = creatAlpha(numberOfSats);

        double erro=1;

        while(erro>0.01){
            for (int i = 0; i < alpha.length; i++) {


                alpha[i][0] = (gu[0]-me.getSVs().get(i).getEcefXpos())/(rao[i]);
                alpha[i][1] = (gu[1]-me.getSVs().get(i).getEcefYpos())/(rao[i]);
                alpha[i][2] = (gu[2]-me.getSVs().get(i).getEcefZpos())/(rao[i]);

            }

            double[] drao = new double[numberOfSats];
                   setDrao(drao, rao, pseudoRanges, bu);

            Matrix aMatrix = new Matrix(alpha);
            aMatrix = aMatrix.inverse();

            double[][] pseudoInverse_Alpha = aMatrix.getArray();
            double[] dl = multMatrixToVector(pseudoInverse_Alpha, drao);
            bu+=dl[dl.length-1];

            for (int i = 0; i < 3; i++) {
                 gu[i]+=dl[i];

            }
              erro = Math.pow(dl[0], 2)+Math.pow(dl[1], 2)+Math.pow(dl[2], 2);


            for (int i = 0; i < rao.length; i++) {
                rao[i] = Math.sqrt(Math.pow(gu[0]- me.getSVs().get(i).getEcefXpos(), 2)+Math.pow(gu[1]-me.getSVs().get(i).getEcefYpos(), 2)+Math.pow(gu[2]-me.getSVs().get(i).getEcefZpos(), 2));
            }
        }


        for (int i = 0; i < numberOfSats; i++)
        {
            me.getSVs().get(i).setPseudoRangeWithDeltaT(rao[i]);

        }
        Point3D userPosEcef = new Point3D(gu[0], gu[1], gu[2]);
//        userPosEcef = GeoUtils.convertECEFtoLATLON(userPosEcef);
        System.out.println(" user pos is " + userPosEcef );


    }

    static public void computePseudoRangeFromNmeaMessurmnet(NMEAPeriodicMeasurement me)
    {

    }

    static private double[] multMatrixToVector(double[][] m,double[] v){
        if(m[0].length != v.length)return null;
        double[] ans = new double[m.length];
        double sum=0;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                sum+=m[i][j]*v[j];
            }
            ans[i]=sum;
            sum=0;
        }
        return ans;
    }

    static private double[][] creatAlpha(int satNumber)
    {
        double[][] alpha = new double[satNumber][4];
        for(int i=0;i<satNumber;i++){
            alpha[i][3]=1;
        }
        return alpha;
    }

    static private void setDrao(double[] drao, double[] rao, double[] pr, double bu)
    {
        for (int i = 0; i < drao.length; i++) {
            drao[i] = (pr[i] - (rao[i] + bu));
        }

    }



}
