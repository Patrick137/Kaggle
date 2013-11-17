import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NominalToBinary;
import weka.filters.unsupervised.attribute.NominalToString;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;
 
import java.io.File;
import java.io.IOException;
public class CSV2ARFF {

	/**
	 * @param args
	 * @throws Exception 
	 */
	//public static void transformData(String[] args) throws Exception  {
	public static void main(String[] args) throws Exception  {
		  //Read file
		  String srcfile = "train_pre.csv";
		  String outfile = "train.arff";
	      // load CSV
		  String[] options = new String[6];
		  options[0] = "-S";//Treat 4th and 5th attribute as string
		  options[1] = "4-5";
		  options[2] = "-D"; // Treat 10 the attribute as date
		  options[3] = "10";
		  options[4] = "-format";	//set date format
		  options[5] = "yyyy-MM-dd HH:mm:ss";
	      CSVLoader loader = new CSVLoader();
	      loader.setSource(new File(srcfile));
	      loader.setOptions(options);
	      Instances data = loader.getDataSet();
	      
	      //Convert date to 24 date interval,i.e. 14:00->14
	      DateFilter DToInv = new DateFilter();
	      DToInv.setInputFormat(data);
	      Instances didata = Filter.useFilter(data, DToInv);
	      //Convert String to Word Vector
	      StringToWordVector SToW = new StringToWordVector();
	      SToW.setInputFormat(didata);    
	      String[] swoptions = new String[6];
	      swoptions[0] = "-R";	//Convert 4th 5th attribute to word vector
	      swoptions[1] = "4,5";
	      swoptions[2] = "-S";	//Ignore word in stop word list
	      swoptions[3] = "-C";	//Output word counts rather than boolean word presence.
	      swoptions[4] = "-W";	//Maximum number of words
	      swoptions[5] = "10";
	      SToW.setOptions(swoptions);
	      Instances swdata = Filter.useFilter(didata, SToW);
	      //Get the index of date attribute
	      int date_ind = 0;
	      for(int i = 0; i < data.numAttributes(); i ++){
	    	  if(data.attribute(i).isDate())
	    		  date_ind = i;
	      }
	      System.out.println(date_ind);
	      //Remove the orignial date attribute
	      Remove remove = new Remove();
	      remove.setAttributeIndices(String.valueOf(date_ind-1));
	      remove.setInputFormat(swdata);
	      data = Filter.useFilter(swdata, remove);
	      //Convert nominal attribute to Binary attribute
	      NominalToBinary NToB = new NominalToBinary();
	      NToB.setInputFormat(data);    
	      String[] nboptions = new String[1];
	      nboptions[0] = "-A";
	      SToW.setOptions(nboptions);
	      Instances nbdata = Filter.useFilter(data, NToB);
	      // save ARFF
	      ArffSaver saver = new ArffSaver();
		  saver.setInstances(nbdata);
		  saver.setFile(new File(outfile));
		  saver.writeBatch();
	   
	     
	      
	}

}
