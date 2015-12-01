package DB4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Log {
	 private String relationName;
     private String LogName;
     private String result;	
     private String transaction;
     
     /**
      * 
      * @param relationName
      * @param transaction
      * 
      * pass in the log name and which relation the log log.
      */
     public Log(String relationName,String transaction)
     {
    	 this.relationName=relationName;   
    	 this.transaction=transaction;
    	 LogName=relationName+"Log";
    	 result="start transaction " +transaction+"\n";
    	 
     }
     
     /**
      * 
      * @param recordPointer
      * @param oldValue
      * @param newValue
      * create a new log of which format is <transaction name, key of the updated record, old value, new value>
      */
     public void addLog(String recordPointer, String oldValue, String newValue)
     {
    	 result=result.concat(transaction+","+recordPointer+","+oldValue+","+newValue+"\n");
     }
     
     /**
      * push the log into disk
      */
     public void pushLog()
     {
    	 File LogFile=new File(LogName);
    	 if(!LogFile.exists())
    	 {    		 
    		 try{
    			 LogFile.createNewFile();
    		 }
    		 catch(IOException e){
    			 System.out.println("create log file fails!");
    			 e.getMessage();
    		 }
    	 }

    	 try(BufferedWriter out = new BufferedWriter(new FileWriter(LogFile, false)))
	    	 {
	    		 out.write(result);
	    		 out.write("commit "+ transaction);
	    	 }
	    	 catch (Exception e)
	         {
	             System.out.println("Failed to write log file into disk, " + e);
	             System.exit(1);
	         }
	 }
     
     /**
      * 
      * @return  logs information
      * @throws FileNotFoundException
      * @throws IOException
      * 
      * read logs from disk 
      */
     public ArrayList<String[]> readLog() throws FileNotFoundException, IOException{
    	 ArrayList<String[]> logs=new ArrayList<>();
    	 File logFile=new File(LogName);
    	 String line;
    	 String[] oneLog=new String[4];
    	 try(BufferedReader br=new BufferedReader(new FileReader(LogName)))
    	 {
    		 while((line=br.readLine())!=null)
    		 {
    			if(line.contains("start")||line.contains("commit")) continue;
    			logs.add(line.split(","));
    		 }
    	 } 
    	 return logs;
     }
     
     /**
      * display log
      */
     public void showLog(){
    	 System.out.println(result);
     }

}
