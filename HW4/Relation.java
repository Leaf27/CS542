package DB4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

	public class Relation {
		// this result is used to store all information of this relation.
		private HashMap<String, ArrayList<String>> result=new HashMap<String, ArrayList<String>>();
		// store all keys of this relation.
		private HashSet<String> keys=new HashSet<String>();
		// this Iterator is used to iterate the relation
		private Iterator<String> it;
		private String fileName;
		// tell result which string is used to be the key for each record in result.
		int key_location;

		
		/**
		 * 
		 * @param fileName
		 * @param key_location
		 * @throws IOException
		 * 
		 * create a relation object and call open function to read data from disk
		 */
		public Relation(String fileName,int key_location) throws IOException
		{
			this.fileName=fileName;
			this.key_location=key_location;
			open();
		}
		
		/**
		 * 
		 * @throws IOException
		 * call methods to read file into result and initialize iterator.
		 */
		private void open() throws IOException 
		{
			fileToHashMap(fileName,key_location);	
			it =keys.iterator(); 
		}
		
		/**
		 * 
		 * @return
		 * get next record
		 */
		public ArrayList<String> getNext()
		{
			if(it.hasNext())
			{
				String key=it.next();
				return result.get(key);	
			}
			else 
			{
				close();
				return null;
			}
		}
		
		/**
		 * close the relation by clearing result and keys
		 */
		private void close()
		{
			result.clear();
			keys.clear();
		}
	    
		/**
		 * 
		 * @param key
		 * @return
		 * 
		 * get record information for a given key.
		 */ 
		public ArrayList<String> get(String key)
		{
			return result.get(key);
		}
		
		/**
		 * 
		 * @return
		 * check whether this relation has next or not
		 */
		public boolean hasNext()
		{
			return it.hasNext();
		}
		
		/**
		 * 
		 * @param Key
		 * @return
		 * check whether this key is in the relation or not
		 */
		public boolean hasKey(String Key)
		{
			return result.containsKey((String) Key);
		}
		
		/**
		 * 
		 * @param fileName, the file where data is stored
		 * @param key_location
		 * @throws IOException
		 * read data from disk
		 */
		 private void fileToHashMap(String fileName,int key_location) throws IOException
		 {
			 File file=new File(fileName);
	    	 if(file.exists())
	    	 {
	    		 FileReader fis=new FileReader(fileName);
	    		 BufferedReader br=new BufferedReader(fis);
	    		 String line=null;
	    		 while((line=br.readLine())!=null)
	    		 {
	    			 ArrayList<String> content=getContent(line.substring(0, line.length()-1));
	    			 result.put(content.get(key_location), content); 
	    			 keys.add(content.get(key_location));
	    		 }
	    		 br.close();
	    	 }	    	 
	     }
		 
		 /**
		  * write the updated data into disk
		  */ 
		 public void HashMaptofile()
		 {
			 File file = new File(fileName);
			 String recordString="";
			 if(file.exists())
			 {
				 try(BufferedWriter wr=new BufferedWriter(new FileWriter(file, false)))
				 {
					 for(Entry<String, ArrayList<String>> record: result.entrySet())
					 {
						 for(String element: record.getValue()){
							 try
							 {
								Double.parseDouble(element); 
								recordString=recordString.concat(element+",");
							 }
							 catch(NumberFormatException n)
							 {
								 recordString=recordString.concat("\'"+element+"\'"+","); 
							 }
						 }
					 recordString = recordString+"\n";	
					 }
					 wr.write(recordString);
				 }
				 catch(IOException e){
					 System.out.println("writing updated population into disk fails: "+fileName);
					 e.getMessage();
				 }
			 }
		 }
		 
		 /**
		  * 
		  * @param record
		  * @return ArrayList<String> which contains record information
		  * slice record into pieces and each piece contains one attribute of this record.
		  */
		 private static ArrayList<String> getContent(String record)
		 {
			 ArrayList<String> result =new ArrayList<String>(); 
			 record=','+record;
			 int point=0;
			 while(point<record.length())
			 {
				 if(record.charAt(point)==',') 
				 {
						// the following is a string
						 if(point+1<record.length()&&record.charAt(point+1)=='\'')
						 {	
							 if(point+2<record.length())
							 {
								int  nextQuote=record.indexOf("\'",point+2);
								if(nextQuote!=-1)
								{
									result.add(record.substring(point+2,nextQuote));
									point=nextQuote+1;
								}
								else 
								{
									result.add(record.substring(point+1,record.length()-1));
									break;
								}
								
							 }
							 else break;
						 }
	                     // the following is a number
						 else if(point+1<record.length())
						 { 
							int nextQuote=record.indexOf(",",point+1); 
							if(nextQuote!=-1)
							{
								result.add(record.substring(point+1,nextQuote));
								point=nextQuote;
							}
							else 
							{
								result.add(record.substring(point+1));
								break;
							}
							
						 }
					     else break;
				 }			 
				 else point++;
				 }
			 return result;
			 }
	}

