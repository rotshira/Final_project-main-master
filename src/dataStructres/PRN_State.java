package dataStructres;

import Geometry.Point3D;

/**
 * Created by Boaz on 12/23/2014.
 /**
 * this class represents the state in time of a PRN:
 * MSG:30
 * MSG:28
 * @author boaz
 *
 */

public class PRN_State {
    private long time;
    boolean SirfOrSTM; // sirf is True;
    private Point3D _xyz;
    Point3D _Vxyz;
   public Point3D getLoc()
   {
       return this._xyz;
   }
    public double getClockDriftMsg30() {
        return clockDriftMsg30;
    }

    public void setClockDriftMsg30(double clockDriftMsg30) {
        this.clockDriftMsg30 = clockDriftMsg30;
    }

    double clockDriftMsg30;

    public double carirerPhase;

    public double get_pr1() {
        return _pr1;
    }

    private double _pr1;
    private int _prn;
    private double _sv_clock_bias;
    private static double _clock_bias=0;

    double GPS_softwareTimeMsg28 = 0.0;
    double carrierFreqMsg28 = 0.0;

    public Point3D getPRNvelocity()
    {
        return this._Vxyz;
    }

    public void SetPRNvelocity(Point3D vel)
    {
        this._Vxyz = vel;
    }

    public void setVxyz(double Vx, double Vy, double Vz)
    {
        this._Vxyz = new Point3D(Vx, Vy, Vz);
    }

    void changeSatPos(double delta)
    {
        double dx = delta* this._Vxyz.getX();
        double dy = delta*this._Vxyz.getY();
        double dz = delta*this._Vxyz.getZ();
        this._xyz.movePoint(dx, dy, dz);
    }

    public PRN_State(int prn, long t, Point3D xyz, Point3D Vxyz, double pr) {
        this.set_prn(prn);
        this.setTime(t);
        this.set_pr1(pr);
        this.set_xyz(xyz);
        this.set_Vxyz(Vxyz);
    }
    public PRN_State(int prn, double pr1, double sv_clock_bias_sec, Point3D pos, Point3D v) {
        this.set_prn(prn);
        this.set_pr1(pr1);
        _sv_clock_bias = sv_clock_bias_sec;
        this.set_xyz(new Point3D(pos));
        this.set_Vxyz(v);
    }


    public PRN_State(int prn, long t) {
        this.set_prn(prn);
        this.setTime(t);
    }
    public void set28(String m28){;}
    public void set30(String m30){;}

    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public Point3D get_xyz() {
        return _xyz;
    }
    public void set_xyz(Point3D _xyz) {
        this._xyz = _xyz;
    }
    public Point3D get_Vxyz() {
        return _Vxyz;
    }
    public void set_Vxyz(Point3D _Vxyz) {
        this._Vxyz = _Vxyz;
    }
    public double get_pr_row() {
        return _pr1;
    }
    //public double get_pr_fix() {
    //	double ans = Main.C*(this._sv_clock_bias-_clock_bias);
    //	ans += _pr1;
    //	return ans;
    //}
    public void set_pr1(double _pr) {
        this._pr1 = _pr;
    }

    public int get_prn() {
        return _prn;
    }
    public void set_prn(int _prn) {
        this._prn = _prn;
    }
    public static double get_clock_bias() {
        return _clock_bias;
    }
    public static void set_clock_bias(double _clock_bias) {
        PRN_State._clock_bias = _clock_bias;
    }

}
