package CS542;
public class testDB {
	static HW1 test=HW1.getInstance();
	static int length=1024*1024;
	
		public static void main(String[] args) throws InterruptedException{
			    byte[] halfData=new byte[length/2];
				// create a byte[] of length 1024*1024/2;	 
				for(int i=0;i<length/2;i++){
					halfData[i]=1;
				}
				test.put(1, halfData);
				test.put(2, halfData);
			    System.out.println("Now starts test of concurrency:");
			    concurrency();
			    System.out.println("Now starts test of fragmentation: ");	
			    fragmentation();
	    }
		
		private static void concurrency() throws InterruptedException{
			 byte[] halfData=new byte[length/2];
			// create a byte[] of length 1024*1024/2;	 
			for(int i=0;i<length/2;i++){
				halfData[i]=1;
			}
			Thread t=new Thread(){
				public void run(){
					System.out.println("Another thread is running!");
					 byte[] testData=new byte[length];
					// create a byte[] of length 1024*1024;
					for(int i=0;i<length;i++){
						testData[i]=1;
					}			
					if(test.get(1)!=null) {
						System.out.println("This is length of the '1' element:"+length);
					}			
			}
			};
			t.start();
			test.remove(1);		
			t.join(1000);
		}	
		
		private static void fragmentation(){
			// create a byte[] of length 1024*1024;
			   byte[] testData=new byte[length];					 
				for(int i=0;i<length/2;i++){
					testData[i]=1;
				}
				// create a byte[] of length 1024*1024/2;
				byte[] halfData=new byte[length/2];					 
				for(int i=0;i<length/2;i++){
					halfData[i]=1;
				}
			// put key A and it's associated value, byte array of 1 MB, into file!
			  test.put(1,testData);
			// put key B and it's associated value, byte array of 1 MB, into file!
			  test.put(2,testData);
			// put key C and it's associated value, byte array of 1 MB, into file!
			  test.put(3,testData);
			// put key D and it's associated value, byte array of 1 MB, into file!
			  test.put(4,testData);
			//Remove key B
			  test.remove(2);
			 //Put() ½ MB in size for key E
			  test.put(5, halfData);
			  // Put() 1 MB in size for key F and check the result!
			  test.put(6, testData);
			  //Remove C
			  test.remove(3);
			  //Put() 1 MB in size for key G  and check the result!
			  test.put(7, testData);
			  //Remove E
			  test.remove(5);
			  //Put() 1 MB in size for key H and check the result!
			  test.put(8, testData);			  
		}
	}
