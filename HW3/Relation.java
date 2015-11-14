package DB3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
	// number of records
	int recordSize=0;
	
	// constructor
	public Relation(String fileName,int key_location) throws IOException
	{
		this.fileName=fileName;
		this.key_location=key_location;
	}
	
	// call methods to read file into result and initialize iterator.
	public void open() throws IOException 
	{
		fileToHashMap(fileName,key_location);	
		it =keys.iterator(); 
	}
	
	public ArrayList<String> getNext()
	{
		if(it.hasNext()){
			String key=it.next();
			return result.get(key);	
		}
		else return null;
	}
	
	public void close()
	{
		result.clear();
		keys.clear();
	}
    
	// get record information for a given key.
	public ArrayList<String> get(String key)
	{
		return result.get(key);
	}
	
	// check whether this relation has next or not
	public boolean hasNext()
	{
		return it.hasNext();
	}
	
	// check whether this key is in the relation or not
	public boolean hasKey(String Key){
		return result.containsKey((String) Key);
	}
	
	 private void fileToHashMap(String fileName,int key_location) throws IOException
	 {
		 File file=new File(fileName);
    	 if(file.exists()){
    		 FileReader fis=new FileReader(fileName);
    		 BufferedReader br=new BufferedReader(fis);
    		 String line=null;
    		 int i=0;
    		 while((line=br.readLine())!=null){
    			 ArrayList<String> content=getContent(line.substring(0, line.length()-1));
    			 if(i==0)
    			 {
    			     recordSize=content.size();
    			     i++;
    			 }
    			 result.put(content.get(key_location), content); 
    			 keys.add(content.get(key_location));
    		 }
    		 br.close();
    	 }
    	 
     }
	 
	 // slice strings
	 private static ArrayList<String> getContent(String record)
	 {
		 ArrayList<String> result =new ArrayList<String>(); 
		 record=','+record;
		 int point=0;
		 while(point<record.length()){
			 if(record.charAt(point)==',') {
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
