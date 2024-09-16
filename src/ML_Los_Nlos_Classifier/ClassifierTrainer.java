package ML_Los_Nlos_Classifier;

import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

public class ClassifierTrainer
{
	/*
	The purpose of the `trainClassifier` method is to train a **RandomForest** machine learning model using a given dataset.
	 It first sets the class label (the attribute to be predicted) and then builds the model by learning patterns from the data.
	 The trained model is then returned and can be used to make predictions on new data based on what it has learned.

	 */
	public static Classifier trainClassifier(Instances data) throws Exception {
		data.setClassIndex(data.numAttributes() - 1);  // Set class index to the last attribute (class label)
		RandomForest classifier = new RandomForest();
		classifier.buildClassifier(data);
		return classifier;
	}

}
