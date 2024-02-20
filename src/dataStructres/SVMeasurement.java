package dataStructres;

import Geometry.Point3D;
import Utils.Relation;

/**
 * Created by Roi on 1/31/2015.
 */
public interface SVMeasurement {

    public Relation<Long, Long, Integer> getCNo();//start in ms, end in ms, cno value

    public Point3D getSvPos();

    public double getAz();

    public double getElevation();

}
