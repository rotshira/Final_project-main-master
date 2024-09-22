package ML_Los_Nlos_Classifier;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.SerializationHelper;
import java.io.BufferedReader;
import java.io.FileReader;

public class TrainAndSaveModel {
	public static void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("src/ML_Los_Nlos_Classifier/satellite_data.arff"));
			Instances dataset = new Instances(reader);
			reader.close();

// Set the class attribute (the last attribute)
			dataset.setClassIndex(dataset.numAttributes() - 1); // Set the class index


// Pass the data to trainClassifier method
			Classifier classifier = ClassifierTrainer.trainClassifier(dataset);

			// Save the model
			SerializationHelper.write("los_nlos_model.model", classifier);

			System.out.println("Model training complete and saved !!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
