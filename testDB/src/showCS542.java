import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class showCS542 {
        public synchronized static void display(File file) throws IOException{
        	 System.out.println("This is the layout of CS542: ");
        	 if(file.exists()){
        		 FileReader fis=new FileReader(file);
        		 BufferedReader br=new BufferedReader(fis);
        		 String line=null;
        		 int count=0;
        		 while((line=br.readLine())!=null){ 			 
        			 if(count==0) System.out.println(line+"  // Number of key-value paris");
        			 else if((count%2)==1) {
        				 System.out.println(line+"  // the "+(count+1)/2 +"th key");
        			 }
        			 else { 
        				 System.out.println(line.length()+"  // length of the "+(count+1)/2 +"th value");
        			 }
        			 count++;
        		 }
        		 if(count==0) System.out.println("CS542 is empty!");
        		 System.out.println("");
        		 br.close();
        	 }
        	 else{
        		 System.out.println("I cannot find the file");
        	 }
        }
}
