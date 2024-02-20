package dataStructres;

import Geometry.Point3D;

public class NMEASVMeasurement {

	public static NMEASVMeasurement nullMeas = new NMEASVMeasurement(-1, -1, -1, -1);
	protected int az, el, snr, prn;
	private boolean isLos;

	public NMEASVMeasurement(int prn, int el, int az, int snr) {
		this.az = az;
		this.el = el;
		this.snr = snr;
		this.prn = prn;
	}


	public NMEASVMeasurement(int prn, int snr)
	{
		this.prn = prn;
		this.snr = snr;
	}


	public void ComputeNaiveLOSWithTHreshold(int SNRthreshold, int margin)
	{
		if(this.getSnr()>SNRthreshold)
			this.setLos(true);
		else if(this.getSnr()<(SNRthreshold-margin))
			this.setLos(false);

	}
	

	public boolean isLos() {
		return isLos;
	}

	public void setLos(boolean los) {
		isLos = los;
	}

	/**
	 * @return the az
	 */
	public int getAz() {
		return az;
	}

	/**
	 * @return the el
	 */
	public int getEl() {
		return el;
	}

	/**
	 * @return the snr
	 */
	public int getSnr() {
		return snr;
	}

	/**
	 * @return the prn
	 */
	public int getPrn() {
		return prn;
	}


	public enum GNSSType {
		GPS, GLONASS
	}
}
