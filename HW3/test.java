package DB3;

import java.io.IOException;
import java.util.ArrayList;

public class test {
     public static void main(String[] args) throws IOException{
    	 String path="/Users/HuanYe/Documents/workspace/DBTEST/src/DB3/";
    	 // read data from db
    	 Relation citys=new Relation(path+"city.txt",0);
    	 Relation countrys=new Relation(path+"country.txt",0);
    	 
    	 citys.open();
    	 countrys.open();
    	 /* when calling join.open, join.getNext() will be triggered automatically and all qualified record 
    	  * will be printed out.
    	  */
    	 Join joinResult=new Join(citys, countrys);
    	 joinResult.open();
    	 
    	 // after finishing, close all stuff.
    	 joinResult.close();
    	 countrys.close();
    	 citys.close();
    	 }
     
}
