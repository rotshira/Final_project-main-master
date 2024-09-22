package ML_Los_Nlos_Classifier;

import GNSS.Sat;
import ParticleFilter.Particle;
import ParticleFilter.Particles;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.Attribute;

import java.util.ArrayList;
import java.util.List;

public class FeatureExtractor {
	public static Instances extractFeatures(List<Sat> satellites,  Particles particles) {
		ArrayList<Attribute> attributes = new ArrayList<>();
		attributes.add(new Attribute("x"));
		attributes.add(new Attribute("y"));
		attributes.add(new Attribute("z"));
		attributes.add(new Attribute("satelliteID"));

		ArrayList<String> classValues = new ArrayList<>();
		classValues.add("LOS");
		classValues.add("NLOS");
		attributes.add(new Attribute("los_nlos", classValues));

		Instances dataSet = new Instances("los_nlos_prediction", attributes, particles.getParticleList().size());
		dataSet.setClassIndex(dataSet.numAttributes() - 1); // Set the class attribute index
		int i = 0;
		for (Particle particle : particles.getParticleList()) {
			i=0;
			for (Sat sat : satellites) {
				double[] features = new double[]{
						particle.pos.getX(),
						particle.pos.getY(),
						particle.pos.getZ(),
						sat.getSatID(),
						particle.LOS[i] ? 0.0 : 1.0  // Set 0 for LOS, 1 for NLOS
				};
				i++;
				dataSet.add(new DenseInstance(1.0, features));
			}
		}

		return dataSet;
	}
}

