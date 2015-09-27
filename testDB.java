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
			System.out.println("This is the result of concurrency test: "+ conCurrency());
			
	    }
		
		private static boolean conCurrency() throws InterruptedException{
			try{
			 byte[] halfData=new byte[length/2];
			// create a byte[] of length 1024*1024/2;	 
			for(int i=0;i<length/2;i++){
				halfData[i]=1;
			}
			Runnable threadJob= new Myrunnable();
			Thread t=new Thread();
			t.start();
			t.sleep(100);
			test.put(3,halfData);
			return true;
			}
			catch(InterruptedException e){
				e.getMessage();
				return false;
			}
			
		}
		
		private static class Myrunnable implements Runnable{
			public void run(){
				go();
			}
			public void go(){
				 byte[] testData=new byte[length];
				// create a byte[] of length 1024*1024;
				for(int i=0;i<length;i++){
					testData[i]=1;
				}
				System.out.println("This is the length of getting element:"+test.get(1).length);
			}
		}
	}
