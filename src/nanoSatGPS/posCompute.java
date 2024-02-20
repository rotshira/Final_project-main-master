package nanoSatGPS;

import GNSS.Sat;
import Geometry.Point3D;
import Jama.Matrix;
import dataStructres.STMPeriodMeasurment;

/**
 * Created by Roi on 1/28/2015.
 */
public class posCompute {

    private double[][] creatAlpha(int satNumber){
        double[][] alpha = new double[satNumber][4];
        for(int i=0;i<satNumber;i++){
            alpha[i][3]=1;
        }
        return alpha;
    }

    private void setDrao(double[] drao, double[] rao,double[] pr, double bu) {
        for (int i = 0; i < drao.length; i++) {
            drao[i] = (pr[i] - (rao[i] + bu));
        }

    }

    private void setGu(STMPeriodMeasurment measurment){
        Point3D intialGeuss = new Point3D(0,0,0);
        int numberOfSats = measurment.getSVs().size();
        double rao[] = new double[numberOfSats];

        for (int i = 0; i < numberOfSats; i++) {
            rao[i] = Math.sqrt(Math.pow(intialGeuss.getX()-measurment.getSVs().get(i).getEcefXpos(), 2)+Math.pow(intialGeuss.getY()-measurment.getSVs().get(i).getEcefYpos(), 2)+Math.pow(intialGeuss.getZ()-measurment.getSVs().get(i).getEcefZpos(), 2));
        }
        double alpha[][] = creatAlpha(numberOfSats);

        double erro=1;
        while(erro>0.01){
            for (int i = 0; i < alpha.length; i++) {
                for (int j = 0; j < 3; j++) {
                    alpha[i][0] = (intialGeuss.getX()-measurment.getSVs().get(i).getEcefXpos())/(rao[i]);
                    alpha[i][1] = (intialGeuss.getY()-measurment.getSVs().get(i).getEcefYpos())/(rao[i]);
                    alpha[i][2] = (intialGeuss.getZ()-measurment.getSVs().get(i).getEcefZpos())/(rao[i]);

                }
            }
            double[] drao = new double[numberOfSats];
     //       setDrao(drao, rao, pseudoRanges, bu);

            Matrix a = new Matrix(alpha);
            a=a.inverse();
            double[][] pseudoInverse_Alpha = a.getArray();

         //   double[] dl = multMatrixToVector(pseudoInverse_Alpha, drao);

          //  bu+=dl[dl.length-1];

            for (int i = 0; i < 3; i++) {
               // gu[i]+=dl[i];
            }

         //   erro = Math.pow(dl[0], 2)+Math.pow(dl[1], 2)+Math.pow(dl[2], 2);


            for (int i = 0; i < rao.length; i++) {
                rao[i] = Math.sqrt(Math.pow(intialGeuss.getX()- measurment.getSVs().get(i).getEcefXpos(), 2)+Math.pow(intialGeuss.getY()-measurment.getSVs().get(i).getEcefYpos(), 2)+Math.pow(intialGeuss.getZ()-measurment.getSVs().get(i).getEcefZpos(), 2));
        }
        }
    }
}
