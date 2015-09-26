package CS542;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HW1 {
	 private String path="/Users/HuanYe/Desktop/grad/semester3/DB/CS542";
     private File tempFile=new File(path+"/tempFile.db","UTF-8");
     public  File officialFile = new File(path+"/cs542.db","UTF-8");
     private HashMap<Integer,byte[]> data=new HashMap<Integer,byte[]>(); 
     private int MAX_FILE_LENGTH=5*1024*1024;
     private int MAX_DATA_LENGTH=4*1024*1024;
     private int length=0;
     private int dataLength=0;
    
     public HW1(){
    		 try {
				fileToHashMap();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	 
     }
     // stores data under the given key
     public synchronized void put(int key, byte[] value){ 	
    	     long startTime=System.currentTimeMillis();
    	     
    		 data.put(key, value);
    		 HashMapToFile();
    		 if(tempFile.length()<=MAX_FILE_LENGTH&&(value.length+dataLength)<=MAX_DATA_LENGTH){
    			 exchangeFileName();	 
    		 }
    		 else {
    			 System.out.println("The file does not have enough room for the new data");
    		 }
    		 System.out.println("we took"+(System.currentTimeMillis()-startTime));
    	 
     }
     // retrieves data for a given key
     public byte[] get(int key){
    	 return data.get(key);
     }
     
     // deletes the key-value pair for a given key
     public synchronized void remove(int key){
    	 byte[] removeElement=data.remove(key);
    	 if(removeElement==null){
    		 System.out.println("This key does not exist!");
    	 }
    	 else{
    		 System.out.println("Removing successes!");
    		 HashMapToFile();
    		 exchangeFileName();
    	 }
    	 
    	 
     }
     
     // read the officialFile file into HashMap
     private void fileToHashMap() throws IOException{
    	 if(officialFile.exists()){
    		 FileInputStream fis=new FileInputStream(officialFile);
    		 BufferedReader br=new BufferedReader(new InputStreamReader(fis));
    		 String line=null;
    		 int count=0;
    		 int key=0;
    		 while((line=br.readLine())!=null){
    			 if(count==0) length=Integer.parseInt(line);
    			 else if((count%2)==1) {
    				 key=Integer.parseInt(line);
    				 data.put(key,null);   				 
    			 }
    			 else {
    				 data.put(key, line.getBytes());
    				 dataLength+=line.length();   				 
    			 }
    			 count++;
    		 }
    		 br.close();
    	 }
    	 else{
    		 officialFile.createNewFile();
    	 }
    	 
     }
     // write the HashMap into tempFile
     private void HashMapToFile(){
    	 try{
    	 BufferedWriter out=new BufferedWriter(new FileWriter(tempFile));
    	 Iterator<Entry<Integer,byte[]>> it=data.entrySet().iterator();
    	 out.write(data.size());
    	 while(it.hasNext()){
    		 Map.Entry<Integer,byte[]> pairs=it.next();
    		 out.write(pairs.getKey()+"\n");
    		 out.write(pairs.getValue()+"\n");   		 
    	 } 
    	 System.out.println("Reading data to tempFile success!");
    	 }
    	 catch(IOException e){
    		 e.getMessage();
    		 System.out.println("Reading data to tempFile fails!");
    	 }
     }
    
     // exchange names of tempFile and officialFile
     private void exchangeFileName(){
    	 try{
    	 File tempAgain=new File(officialFile.getParent(),"tempAgain.db");
    	 Files.move(tempFile.toPath(), officialFile.toPath());
    	 Files.move(tempAgain.toPath(), tempFile.toPath());
    	 System.out.println("Exchanging names of files success!");
    	 }
    	 catch(IOException e){
    		 e.getMessage();
    		 System.out.println("Exchanging names of files fails!");
    	 }
     }     
}
