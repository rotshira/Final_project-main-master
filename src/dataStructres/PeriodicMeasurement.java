package dataStructres;

import Geometry.Point3D;

import java.util.List;

/**
 * Created by Roi on 1/31/2015.
 */
public interface PeriodicMeasurement {

    public double getLon();

    public long getTime();

    public double getLat();

    public double getAlt();

    public List<SVMeasurement> getAllSvMeasurements();

    public Point3D getECEFLocation();

}
