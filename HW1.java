package CS542;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
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
	 // create two file objects:officialFile named cs542.db, tempFile named tempFile.db
     private  File tempFile=new File("tempFile.db");
     private  File officialFile = new File("cs542.db");
     // create a HashMap storing the key-value pairs
     private  HashMap<Integer,byte[]> data=new HashMap<Integer,byte[]>(); 
     // declare the max file length. According Prof's requirements, the max file length equals 5MB
     private final int MAX_FILE_LENGTH=5*1024*1024;
  // declare the max data length. According Prof's requirements, actual data length should equal to 4MB
     private final int MAX_DATA_LENGTH=4*1024*1024;
     // count how many key-value pairs.
     private int dataLength=0;
     // create writelock and readlock
     private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
     private final Lock r = rwl.readLock();
     private final Lock w = rwl.writeLock();
    /* initiate HW1 class and at the same time, read data in cs542.db into data HashMap.
     * The HW1 constructor is private so that there is only one HW1 object that can be created which is essential to readWriteLock.
    */ 
     private HW1(){
    		 try {
				fileToHashMap();
			} catch (IOException e) {
				e.printStackTrace();
			}
     }
     // This method is used when testDB wants to get the only one HW1 object.
     public static synchronized HW1 getInstance(){
    	 return singleton;
     }
     
     /* stores data under the given key.if there is any key in cs542 file as same as the given key, previous value will be replaced by the new value. 
     	In order to track length of actual data, the method records the length of previous value if exists and 
     	updated length=length of previous length-length of previous data(0 if the key was not in the file before calling the put method) + length of new data.
     	After we put the new key-value pair into data hashMap, we synchronize the hashMap and tempFile. If the tempfile meets the length requirements, 
     	then we rename the tempFile cs542.    	
       
      */
     public void put(int key, byte[] value){ 	
    	     int prevLength=0;
    	     // writelock to lock the HW1 object.
    	     w.lock();
    	     if(data.containsKey(key)){
    	    	 prevLength=data.get(key).length;
    	     }
    		 data.put(key, value);
    		 HashMapToFile();  		 
    		 if(tempFile.length()<=MAX_FILE_LENGTH&&(value.length+dataLength-prevLength)<=MAX_DATA_LENGTH){
    			 exchangeFileName();	 
    			 dataLength=value.length+dataLength-prevLength;
    			 Double toBeTruncated = new Double((double)value.length/(1024*1024));
    			 System.out.println("we put "+key+" and its value of "+toBeTruncated.toString()+" MB into the official file!");
    		 }
    		 else {
    			 System.out.println("The file does not have enough room for data of which key is: "+key);
    		 }
    		 //unlock the HW1 object
    		 w.unlock();
    		 
    	 
     }
     // retrieves data for a given key. If found the key, print out length of the byte[]. Else, print out "we did not find the key".
     public byte[] get(int key){
    	 byte[] result=data.get(key);
		 r.lock();
		 try{
			 if(result!=null){
			 System.out.println("We got the value of length: "+data.get(key).length);
			 return data.get(key);}
			 else System.out.println("We did not find the key: "+key);
			 return result;
		 }
		 finally{r.unlock();}
     }
     
     // deletes the key-value pair for a given key. If the key does not exist, print out "this key does not exist!". 
     //Else, print out the removed key and the associated value.
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
     
     /* read the officialFile file into HashMap and track amount of key-value pairs. If the cs542 file does not exist, then we will create a file named cs542.
      
     */
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

