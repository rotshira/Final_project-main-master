package Geometry;

import dataStructres.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Roi on 18/02/2015.
 */
public class SkyLine {
    Point3D pos;
    Map<Pair<Double, Double>, Pair<Double, Double>> skys;

    public void setPos(Point3D pos) {
        this.pos = pos;
    }

    public void setCurrentSkyLine2(){
        Map<Pair<Double, Double>, Pair<Double, Double>> map = new HashMap<>();
        map.put(new Pair<Double, Double>(1d, 10d), new Pair<Double, Double>(-125d, -124d));
        map.put(new Pair<Double, Double>(10d, 11d), new Pair<Double, Double>(-124d, -150d));
        map.put(new Pair<Double, Double>(11d, 60d), new Pair<Double, Double>(-150d, -165d));

        //todo roi - add elevetion in correct form

        map.put(new Pair<Double, Double>(60d, 62d), new Pair<Double, Double>(-150d, -165d));
        map.put(new Pair<Double, Double>(62d, 67d), new Pair<Double, Double>(-150d, -165d));
        map.put(new Pair<Double, Double>(67d, 78d), new Pair<Double, Double>(-150d, -165d));
        map.put(new Pair<Double, Double>(79d, 92d), new Pair<Double, Double>(-150d, -165d));
        map.put(new Pair<Double, Double>(92d, 103d), new Pair<Double, Double>(-150d, -165d));
        map.put(new Pair<Double, Double>(103d, 123d), new Pair<Double, Double>(-150d, -165d));
        map.put(new Pair<Double, Double>(123d, 130d), new Pair<Double, Double>(-150d, -165d));
        map.put(new Pair<Double, Double>(130d, 203d), new Pair<Double, Double>(-150d, -165d));
        map.put(new Pair<Double, Double>(203d, 213d), new Pair<Double, Double>(-150d, -165d));


        map.put(new Pair<Double, Double>(213d, 243d), new Pair<Double, Double>(-150d, -165d));
        map.put(new Pair<Double, Double>(243d, 251d), new Pair<Double, Double>(-150d, -165d));
        map.put(new Pair<Double, Double>(252d, 261d), new Pair<Double, Double>(-150d, -165d));
        map.put(new Pair<Double, Double>(262d, 270d), new Pair<Double, Double>(-150d, -165d));
        map.put(new Pair<Double, Double>(270d, 272d), new Pair<Double, Double>(-150d, -165d));
        map.put(new Pair<Double, Double>(272d, 279d), new Pair<Double, Double>(-150d, -165d));
        map.put(new Pair<Double, Double>(279d, 300d), new Pair<Double, Double>(-150d, -165d));
        map.put(new Pair<Double, Double>(300d, 335d), new Pair<Double, Double>(-150d, -165d));
        map.put(new Pair<Double, Double>(335d, 345d), new Pair<Double, Double>(-150d, -165d));


        map.put(new Pair<Double, Double>(345d, 356d), new Pair<Double, Double>(-150d, -165d));
        map.put(new Pair<Double, Double>(356d, 358d), new Pair<Double, Double>(-150d, -165d));
        map.put(new Pair<Double, Double>(359d, 360d), new Pair<Double, Double>(-150d, -165d));



        //fill the rest, make skys=map;
      /*  tmp = new Pair(60, -165);
        newList.add(tmp);
        tmp = new Pair(62, -153);
        newList.add(tmp);
        tmp = new Pair(67, -104);
        newList.add(tmp);
        tmp = new Pair(79, -105);
        newList.add(tmp);
        tmp = new Pair(77, -154);
        newList.add(tmp);
        tmp = new Pair(92, -156);
        newList.add(tmp);
        tmp = new Pair(103, -103);
        newList.add(tmp);
        tmp = new Pair(123, -102);
        newList.add(tmp);
        tmp = new Pair(130, -150);
        newList.add(tmp);
        tmp = new Pair(203, -140);
        newList.add(tmp);
        tmp = new Pair(213, -134);
        newList.add(tmp);
        tmp = new Pair(243, -134);
        newList.add(tmp);
        tmp = new Pair(252, -135);
        newList.add(tmp);
        tmp = new Pair(251, -126);
        newList.add(tmp);
        tmp = new Pair(253, -118);
        newList.add(tmp);
        tmp = new Pair(262, -117);
        newList.add(tmp);
        tmp = new Pair(260, -102);
        newList.add(tmp);
        tmp = new Pair(270, -102);
        newList.add(tmp);
        tmp = new Pair(272, -153);
        newList.add(tmp);
        tmp = new Pair(279, -173);
        newList.add(tmp);
        tmp = new Pair(300, -166);
        newList.add(tmp);
        tmp = new Pair(335, -104);
        newList.add(tmp);
        tmp = new Pair(345, -105);
        newList.add(tmp);
        tmp = new Pair(357, -128);
        newList.add(tmp);
        tmp = new Pair(356, -98);
        newList.add(tmp);
        tmp = new Pair(359, -98);
        newList.add(tmp);
        tmp = new Pair(359, -120);
        newList.add(tmp);
        skys = newList;
*/
    }

    public void setCurrentSkyline()
    {
        List<Pair<Double, Double>> newList = new ArrayList<Pair<Double, Double>>();

        Pair<Double, Double> tmp = new Pair(1, -125);
        newList.add(tmp);
        tmp = new Pair(10, -124);
        newList.add(tmp);
        tmp = new Pair(11, -150);
        newList.add(tmp);
        tmp = new Pair(60, -165);
        newList.add(tmp);
        tmp = new Pair(62, -153);
        newList.add(tmp);
        tmp = new Pair(67, -104);
        newList.add(tmp);
        tmp = new Pair(79, -105);
        newList.add(tmp);
        tmp = new Pair(77, -154);
        newList.add(tmp);
        tmp = new Pair(92, -156);
        newList.add(tmp);
        tmp = new Pair(103, -103);
        newList.add(tmp);
        tmp = new Pair(123, -102);
        newList.add(tmp);
        tmp = new Pair(130, -150);
        newList.add(tmp);
        tmp = new Pair(203, -140);
        newList.add(tmp);
        tmp = new Pair(213, -134);
        newList.add(tmp);
        tmp = new Pair(243, -134);
        newList.add(tmp);
        tmp = new Pair(252, -135);
        newList.add(tmp);
        tmp = new Pair(251, -126);
        newList.add(tmp);
        tmp = new Pair(253, -118);
        newList.add(tmp);
        tmp = new Pair(262, -117);
        newList.add(tmp);
        tmp = new Pair(260, -102);
        newList.add(tmp);
        tmp = new Pair(270, -102);
        newList.add(tmp);
        tmp = new Pair(272, -153);
        newList.add(tmp);
        tmp = new Pair(279, -173);
        newList.add(tmp);
        tmp = new Pair(300, -166);
        newList.add(tmp);
        tmp = new Pair(335, -104);
        newList.add(tmp);
        tmp = new Pair(345, -105);
        newList.add(tmp);
        tmp = new Pair(357, -128);
        newList.add(tmp);
        tmp = new Pair(356, -98);
        newList.add(tmp);
        tmp = new Pair(359, -98);
        newList.add(tmp);
        tmp = new Pair(359, -120);
        newList.add(tmp);


    }
    public void SortArrayByAzimuth()
    {

    }

    public boolean isAboveSkyLine(Pair<Double, Double> tmp) //first double is azimuth. Second double is elev.
    {
        //iterate over skys keys, find key which is the correct range for tmp.first (which is the query azimuth), then take the value of the range, which
        //represents the linear formula of the building top, and check if tmp.second is over or under.
        for (Pair<Double, Double> azimuthRanges : skys.keySet()) {

            if(tmp.getFirst()>=azimuthRanges.getFirst() && tmp.getFirst()<=azimuthRanges.getSecond()){
                //checkif tmp.second (elevation) is under equation formed from skys.get(azimuthRanges).
                Pair<Double, Double> elevationPair = skys.get(azimuthRanges);
                double rateToAdd = (tmp.getFirst() - azimuthRanges.getFirst()) / (azimuthRanges.getSecond() - azimuthRanges.getFirst());
                double elevationAtPoint = elevationPair.getFirst() + (rateToAdd * (elevationPair.getSecond() - elevationPair.getFirst()));
                if (elevationAtPoint > tmp.getSecond()){
                    return false;//NLOS
                }
            }

        }
        return true; //LOS
    }


/*
 class Pair<Azimuth, Elevetion>{
     double az;
     double el;

     public Pair(double az, double el) {
         this.az = az;
         this.el = el;
     }

     public double getAz() {
         return az;
     }

     public double getEl() {
         return el;
     }

     public void setAz(double az) {
         this.az = az;
     }

     public void setEl(double el) {
         this.el = el;
     }
*/
}