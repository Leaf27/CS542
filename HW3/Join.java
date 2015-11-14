package DB3;

import java.util.ArrayList;

public class Join {
      private Relation Relation1;
      private Relation Relation2;

      // constructor
      public Join(Relation Relation1,Relation Relation2)
      {
    	  this.Relation1=Relation1;
    	  this.Relation2=Relation2;
      }
     
      // by calling open function to call getNext
      public void open()
      {
    	  getNext();
      }
      
      /* For each record in relation1, get countrycode of this record and call method relation.get(key) to find the associated record 
       	 in relation2. After that, check where relation1.population is greater than 0.4*population in relation2. 
       	 If true, print this record in relation1.If not, get next record in relation1.
       */
      public void getNext()
      {
    	   while(Relation1.hasNext())
           {
          	 ArrayList<String> record1=Relation1.getNext();
          	 String countryCode=record1.get(2);
          	 double CityPopulation=Double.parseDouble(record1.get(4));
          	 if(Relation2.hasKey(countryCode))
          	 {
          		 ArrayList<String> record2=Relation2.get(countryCode);
          		 double countryPopulation=Double.parseDouble(record2.get(6));
          		 if(CityPopulation>countryPopulation*0.4)
          		 {
          			 record1.stream().forEach((String)->System.out.print(String+"  "));   
          			 System.out.println("");
          		 }
          	 }
           }
      }
      
      public void close(){
    	  Relation1=null;
    	  Relation2=null;
    	  
      }
}
