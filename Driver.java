import java.io.BufferedReader;
import java.io.FileReader;

import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.SMOreg;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;


public class Driver {
	public static void main2(String[] args) throws Exception  {
//		if need transform CSV to ARFF
//		CSV2ARFF transformData = new CSV2ARFF();
//		transformData.transformData(inputfile, outputfile);
		Instances data = new Instances(new BufferedReader(new
					FileReader("train_chunk1.arff")));
		data.setClassIndex(6);
		//build model
		SMOreg model = new SMOreg();
		model.buildClassifier(data); //the last instance with missing class is not used
		System.out.println(model);
		
		
		//If you want to save the model
		//SerializationHelper.write("model", model);
		
		
		//classify the last instance
		Instance click = data.firstInstance();
		double views = model.classifyInstance(click);
		System.out.println("Click:" + click + " " + views);
		}
		
}

