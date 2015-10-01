import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class testDB {
	static HW1 test=HW1.getInstance();
	static int length=1024*1024;
		public static void main(String[] args) throws InterruptedException, IOException{
			    byte[] halfData=new byte[length/2];	 
				for(int i=0;i<halfData.length;i++){
					halfData[i]=1;
				}
				test.put(1,halfData);
				display(test.getFile());
				// starts concurrency 
			    System.out.println("Now starts test of concurrency:");
			    concurrency();
			    // starts fragmentation test
			    System.out.println("Now starts test of fragmentation: ");	
			    fragmentation();
	    }
		
		/* The main thread does a Remove() and the new thread t does a Get() with the same key a millisecond later.
		 * 
		 */
		private static void concurrency() throws InterruptedException, IOException{
			 byte[] halfData=new byte[length/2];	 
			for(int i=0;i<length/2;i++){
				halfData[i]=1;
			}
			// create a new thread to get value of which key is 1.
			Thread t=new Thread(){
				public void run(){
					System.out.println("Another thread is running!");	
					byte[] result=test.get(1);
					if(result!=null) {
						System.out.println("This is length of the '1' element:"+result.length);
					}			
			}
			};
			// start the new thread
			t.start();			
			// remove key-value pair of which key is 1.
			test.remove(1);	
			t.join(1);
			display(test.getFile());		
			// let the main thread wait 1000 milliseconds so that the new thread t can be finished before starting fragmentation method.
			t.join(1000);
		}
		
		
		/* Put() 4 values, byte arrays of 1 MB each, with keys A, B, C and D. Remove key B. Put() ½ MB in size for key E. 
		 * Validate that a Put() 1 MB in size for key F fails. Remove C and now validate that a Put() 1 MB in size for key G succeeds. 
		 * Remove E and try Put() 1 MB in size for key H. With a naive implementation, it will fail even though there is room in store.db. 
		 * An extra bonus point if you can modify your code such that Put("H", …) succeeds. 
		 */
		private static void fragmentation() throws IOException{
			   byte[] testData=new byte[length];					 
				for(int i=0;i<length;i++){
					testData[i]=1;
				}
				byte[] halfData=new byte[length/2];					 
				for(int i=0;i<length/2;i++){
					halfData[i]=1;
				}
			// put key A and it's associated value, byte array of 1 MB, into file!
			  test.put(1,testData);
			  display(test.getFile());
			// put key B and it's associated value, byte array of 1 MB, into file!
			  test.put(2,testData);
			  display(test.getFile());
			// put key C and it's associated value, byte array of 1 MB, into file!
			  test.put(3,testData);
			  display(test.getFile());
			// put key D and it's associated value, byte array of 1 MB, into file!
			  test.put(4,testData);
			  display(test.getFile());
			//Remove key B
			  test.remove(2);
			  display(test.getFile());
			 //Put() ½ MB in size for key E
			  test.put(5, halfData);
			  display(test.getFile());
			  // Put() 1 MB in size for key F and check the result!
			  test.put(6, testData);
			  display(test.getFile());
			  //Remove C
			  test.remove(3);
			  display(test.getFile());
			  //Put() 1 MB in size for key G  and check the result!
			  test.put(7, testData);
			  display(test.getFile());
			  //Remove E
			  test.remove(5);
			  display(test.getFile());
			  //Put() 1 MB in size for key H and check the result!
			  test.put(8, testData);	
			  display(test.getFile());
		}
		
		private synchronized static void display(File file) throws IOException{
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
