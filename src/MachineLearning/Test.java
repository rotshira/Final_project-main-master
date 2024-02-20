package MachineLearning;


import dataStructres.SirfSVMeasurement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Roi
 * Date: 10/04/14
 * Time: 00:01
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args){
        List<SirfSVMeasurement> points = new ArrayList<SirfSVMeasurement>();
       SirfSVMeasurement tmp;
        int[] C0m= new int[]{1,2,3,4,5,6,7,8,9,10};

        for(int i=0;i<20;i++)
        {
            tmp = new SirfSVMeasurement();
            tmp.setCNo(C0m);
            points.add(tmp);
        }
        ChangeZ(points);
        for(int i=0;i<points.size();i++)
           points.get(i).PrintOldCn();
    }

    public static void ChangeZ(List<SirfSVMeasurement> points)
    {
        int t=0;
        int[] C0m= new int[]{1,2,3,4,5,6,7,8,9,10};
        for(int i=0; i<points.size();i++)
        {
            points.get(i).setOldCNo(C0m);
            C0m = new int[10];
            C0m[0]=i;
            C0m[1]=i;
            C0m[2]=i;
            C0m[3]=i;
            C0m[4]=i;
            C0m[5]=i;
            C0m[6]=i;
            C0m[7]=i;
            C0m[8]=i;
            C0m[9]=i;
        }

    }
}
