package ML_Los_Nlos_Classifier;

import GNSS.Sat;
import weka.classifiers.Classifier;
import weka.core.Instances;

import java.util.List;

public class LOSPredictor {
	private Classifier classifier;

	public LOSPredictor(Classifier classifier) {
		this.classifier = classifier;
	}

	public boolean[] predictLOS(List<Sat> satellites) throws Exception {
		Instances features = FeatureExtractor.extractFeatures(satellites);

		// Set the class index for the LOS/NLOS classification
		features.setClassIndex(features.numAttributes() - 1);

		boolean[] losResults = new boolean[satellites.size()];

		for (int i = 0; i < features.numInstances(); i++) {
			features.instance(i).setDataset(features); // Set instance to the dataset
			double result = classifier.classifyInstance(features.instance(i));
			losResults[i] = result == 1.0; // 1 = LOS, 0 = NLOS
		}

		return losResults;
	}
}
