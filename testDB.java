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
					 byte[] testData=new byte[length];
					// create a byte[] of length 1024*1024;
					for(int i=0;i<length;i++){
						testData[i]=1;
					}
					System.out.println("Another thread is running!");			
					if(test.get(1)!=null) {
						System.out.println("This is length of the '1' element:"+length);
					}			
			}
			};
			t.start();
			test.remove(1);			
		}	
		
		private static void fragmentation(){
			   byte[] testData=new byte[length];
				// create a byte[] of length 1024*1024/2;	 
				for(int i=0;i<length/2;i++){
					testData[i]=1;
				}
				byte[] halfData=new byte[length/2];
				// create a byte[] of length 1024*1024/2;	 
				for(int i=0;i<length/2;i++){
					halfData[i]=1;
				}
			  test.put(1,testData);
			  test.put(2,testData);
			  test.put(3,testData);
			  test.put(4,testData);		
			  test.remove(2);
			  test.put(5, halfData);
			  test.put(6, testData);
			  test.remove(3);
			  test.put(7, testData);
			  test.remove(5);
			  test.put(8, testData);			  
		}
	}
