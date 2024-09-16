package ML_Los_Nlos_Classifier;

import GNSS.Sat;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.Attribute;

import java.util.ArrayList;
import java.util.List;

public class FeatureExtractor {
	public static Instances extractFeatures(List<Sat> satellites) {
		ArrayList<Attribute> attributes = new ArrayList<>();
		attributes.add(new Attribute("SNR"));
		attributes.add(new Attribute("Azimuth"));
		attributes.add(new Attribute("Elevation"));

		ArrayList<String> classValues = new ArrayList<>();
		classValues.add("LOS");
		classValues.add("NLOS");
		attributes.add(new Attribute("class", classValues));

		Instances dataSet = new Instances("satellite_data", attributes, satellites.size());
		dataSet.setClassIndex(dataSet.numAttributes() - 1); // Set the class attribute index

		for (Sat sat : satellites) {
			double[] features = new double[]{
					sat.getSingleSNR(),
					sat.getAzimuth(),
					sat.getElevetion(),
//					sat.isLOS() ? 0.0 : 1.0  // Set 0 for LOS, 1 for NLOS
			};
			dataSet.add(new DenseInstance(1.0, features));
		}

		return dataSet;
	}
}
