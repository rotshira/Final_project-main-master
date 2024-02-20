package dataStructres;

import java.util.*;

/**
 * Created by Roi on 2/1/2015.
 */
public class STMPeriodMeasurment extends NMEAPeriodicMeasurement {//todo : check if we want to keep it.

    private double oscilatorError;
    List<STMSVMeasurement> SVs;

    public List<STMSVMeasurement> getSVs() {
        return SVs;
    }
    private Map<Integer, STMSVMeasurement> mappedSvMeasurement;

    public STMPeriodMeasurment( double UtcTime, double lat, double lon, double alt, double altElip, double hDOP, List<STMSVMeasurement> sVs) {
        super( UtcTime, lat, lon, alt, altElip, hDOP);
        this.SVs = sVs;
        this.mappedSvMeasurement = new HashMap<>();
        for (STMSVMeasurement svm : sVs){
            mappedSvMeasurement.put(svm.getPrn(), svm);
        }
    }

    public Map<Integer,STMSVMeasurement> getMappedSvMeasurements() {
        return mappedSvMeasurement;
    }
    public double getNumOfSVs(){
        return mappedSvMeasurement.size();
    }
    public Collection<STMSVMeasurement> getAllSvMeasurement(){
        return mappedSvMeasurement.values();
    }

    public NMEASVMeasurement getSvMeasurement(int prn){
        for (NMEASVMeasurement meas : mappedSvMeasurement.values()){
            if (meas.getPrn() == prn){
                return meas;
            }
        }
        return null;
    }

    public List<Integer> getAllPRNs(){
        List<Integer> res = new ArrayList<Integer>();
        for (NMEASVMeasurement meas : mappedSvMeasurement.values()){
            res.add(meas.getPrn());
        }
        return res;
    }

    public void setSVs(List<STMSVMeasurement> SVs) {
        this.SVs = SVs;
    }
}
