package DB4;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Test {
	
	private static String path=System.getProperty("user.dir");
	private static String CityPath=path+"/cs542.db/city.txt";
	private static String CountryPath=path+"/cs542.db/country.txt";
	private static String CityAPath=path+"/cs542A.db/city.txt";
	private static String CountryAPath=path+"/cs542A.db/country.txt";
	
	// index of population in city relation.
	private static int CityPopulation=4;
	
	// index of population in country relation.
	private static int CountryPopulation=6;
	
	private static ArrayList<String> record=null;
	
	// names of transactions
	private static String UpdateCity="UpdateCity";
	private static String UpdateCountry="UpdateCountry";
	
    public static void main(String[] args) throws IOException{
    	// update cs542.db and write logs
    	  Relation City=new Relation(CityPath,0);
    	  Relation Country=new Relation(CountryPath,0);
    	  
    	  Log CityLog=new Log("City",UpdateCity);
    	  Log CountryLog=new Log("Country",UpdateCountry);
  
    	  update(City,CityLog,CityPopulation);
    	  CityLog.pushLog();
    	  City.HashMaptofile();
    	  
    	  update(Country,CountryLog,CountryPopulation);
    	  CountryLog.pushLog();
    	  Country.HashMaptofile();
    	  
    	// use the log to update cs542A.db
    	  Relation CityA=new Relation(CityAPath,0);
    	  Relation CountryA=new Relation(CountryAPath,0);
    	  
    	  updateBackup(CityA,CityLog,CityPopulation);
    	  CityA.HashMaptofile();
    	  System.out.println("city in cs542 and city in cs542A are identical or not?");
    	  System.out.println("the result is: "+check(CityPath,CityAPath,CityPopulation));
    
    	  updateBackup(CountryA,CountryLog,CountryPopulation);
    	  CountryA.HashMaptofile();
    	  System.out.println("country in cs542 and country in cs542A are identical or not?");
    	  System.out.println("the result is: "+check(CountryPath,CountryAPath,CountryPopulation));
    	  
      }
      
      /**
       * 
       * @param relation, passed in to specify which relation need to be updated
       * @param log, specify where logs to be written.
       * @param population, index of population in one record.
       *
       * For each record in relation, write the undo/redo log into its associated log file.
       * The format of log is <transaction_name, pointer of record, old population, updated population >
       * After generating log, increase population in this record by 2%.
       */
    
      public static void update(Relation relation, Log log,int population){
    	  while(relation.hasNext())
    	  {
    		  record=relation.getNext();
    		  log.addLog(record.get(0), record.get(population), increasePopulation(record.get(population)));
    		  record.set(population, increasePopulation(record.get(population)));
    	  }
    	  log.showLog();
      }
      
      /**
       * 
       * @param relation
       * @param log
       * @param population
       * @throws FileNotFoundException
       * @throws IOException
       * 
       * read log from disk, and we can replace old population by new value in log.
       */
      public static void updateBackup(Relation relation, Log log,int population) throws FileNotFoundException, IOException
      {
    	  ArrayList<String[]> logs = log.readLog();
    	  for(String[] oneLog : logs){
    		  String pointer=oneLog[1];
    		  relation.get(pointer).set(population, oneLog[3]);
    	  }
    	  
      }
     
      /**
       * 
       * @param population
       * @return  updated population which is product of population and 102%.
       */
      private static String increasePopulation(String population)
      {
    	  int newPopulation = (int)(Integer.parseInt(population)*1.02);
    	  return Integer.toString(newPopulation);
      }
      
      /**
       * 
       * @param relationPath
       * @param relationAPath
       * @param population
       * @return
       * @throws IOException
       * check whether two relations are identical or not
       */
      private static boolean check(String relationPath, String relationAPath,int population) throws IOException
      {
    	  Relation DB1=new Relation(relationPath,0);
    	  Relation DB2=new Relation(relationAPath,0);
    	  while(DB1.hasNext())
    	  {   
    		  ArrayList<String> record = DB1.getNext();
    		  if(record.get(population).equals(DB2.get(record.get(0)).get(population))) continue;
    		  else return false;
    	  }
    	  return true;
      }
}
