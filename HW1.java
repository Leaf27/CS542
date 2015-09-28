package CS542;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HW1 {
	 private  static HW1 singleton = new HW1();
     private  File tempFile=new File("tempFile.db");
     private  File officialFile = new File("cs542.db");
     private  HashMap<Integer,byte[]> data=new HashMap<Integer,byte[]>(); 
     private final int MAX_FILE_LENGTH=5*1024*1024;
     private final int MAX_DATA_LENGTH=4*1024*1024;
     private int dataLength=0;
     private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
     private final Lock r = rwl.readLock();
     private final Lock w = rwl.writeLock();
    
     private HW1(){
    		 try {
				fileToHashMap();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     }
     
     public static synchronized HW1 getInstance(){
    	 return singleton;
     }
     
     // stores data under the given key
     public void put(int key, byte[] value){ 	
    	     int prevLength=0;
    	     w.lock();
    	     if(data.containsKey(key)){
    	    	 prevLength=data.get(key).length;
    	     }
    		 data.put(key, value);
    		 HashMapToFile();  		 
    		 if(tempFile.length()<=MAX_FILE_LENGTH&&(value.length+dataLength-prevLength)<=MAX_DATA_LENGTH){
    			 exchangeFileName();	 
    			 dataLength=value.length+dataLength-prevLength;
    			 System.out.println("we put "+key+" and its associated value into the official file!");
    		 }
    		 else {
    			 System.out.println("The file does not have enough room for data of which key is: "+key);
    		 }
    		 w.unlock();
    		 
    	 
     }
     // retrieves data for a given key
     public byte[] get(int key){
    	 byte[] result=data.get(key);
		 r.lock();
		 try{
			 if(result!=null){
			 System.out.println("We got the value of length: "+data.get(key).length);
			 return data.get(key);}
			 else System.out.println("We did not find the key");
			 return result;
		 }
		 finally{r.unlock();}
     }
     
     // deletes the key-value pair for a given key
     public void remove(int key){
	     w.lock();
    	 byte[] removeElement=data.remove(key);
    	 if(removeElement==null){
    		 System.out.println("This key does not exist!");
    	 }
    	 else{
    		 System.out.println("Removing "+key+" and its associated data successes!");
    		 HashMapToFile();
    		 dataLength-=removeElement.length;
    		 exchangeFileName();
    	 }
    	 w.unlock(); 	 
     }
     
     // read the officialFile file into HashMap
     private void fileToHashMap() throws IOException{
    	 if(officialFile.exists()){
    		 FileInputStream fis=new FileInputStream(officialFile);
    		 BufferedReader br=new BufferedReader(new InputStreamReader(fis,"UTF-8"));
    		 String line=null;
    		 int count=0;
    		 int key=0;
    		 int length=0;
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
    		 System.out.println("I am reading data from file into HashMap!");
    	 }
    	 else{
    		 officialFile.createNewFile();
    		 System.out.println("I cannot find the file, so I create an official file!");
    	 }
    	 
     }
     // write the HashMap into tempFile
     private void HashMapToFile(){
    	 try{
         FileOutputStream fos=new FileOutputStream(tempFile,false);
    	 BufferedWriter out=new BufferedWriter(new OutputStreamWriter(fos,"UTF-8"));
    	 Iterator<Entry<Integer,byte[]>> it=data.entrySet().iterator();
    	 out.write(data.size());
    	 while(it.hasNext()){
    		 Map.Entry<Integer,byte[]> pairs=it.next();
    		 out.write(pairs.getKey()+"\n");
    		 out.write(pairs.getValue()+"\n");   		 
    	 }
    	 }
    	 catch(IOException e){
    		 e.getMessage();
    	 }
     }
    
     // exchange names of tempFile and officialFile
     private void exchangeFileName(){
    	 try{
    	 Files.move(tempFile.toPath(), officialFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    	 }
    	 catch(IOException e){
    		 e.getMessage();
    	 }
     } 
}

