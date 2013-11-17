import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import weka.core.*;
 import weka.core.Capabilities.*;
import weka.filters.*;
 
 public class DateFilter
   extends SimpleBatchFilter {
 
   public String globalInfo() {
     return   "A simple batch filter that transfer the data attribute to a 24 nominal attribute ";
   }
 
   public Capabilities getCapabilities() {
     Capabilities result = super.getCapabilities();
     result.enableAllAttributes();
     result.enableAllClasses();
     result.enable(Capability.NO_CLASS);  //// filter doesn't need class to be set//
     return result;
   }
 
   protected Instances determineOutputFormat(Instances inputFormat) {
     Instances result = new Instances(inputFormat, 0);
     result.insertAttributeAt(new Attribute("dateInterval"), result.numAttributes());
     return result;
   }
 
   protected Instances process(Instances inst) {
     Instances result = new Instances(determineOutputFormat(inst), 0);
     for (int i = 0; i < inst.numInstances(); i++) {
    	 
    	 double[] values = new double[result.numAttributes()];
    	 int date_interval = 0;
    	 for (int n = 0; n < inst.numAttributes(); n++){
    		 if(inst.attribute(n).isDate()){
    			 Date date = new Date((long)inst.instance(i).value(n));
    			 Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
    			 calendar.setTime(date);   // assigns calendar to given date 
    			 date_interval = calendar.get(Calendar.HOUR_OF_DAY); 
    		 }
    		 values[n] = inst.instance(i).value(n);
    	 }
    	 values[values.length - 1] = date_interval;
    	 result.add(new Instance(1, values));
     }
   
     return result;
   }
 
   public static void main(String[] args) {
     runFilter(new DateFilter(), args);
   }
 }