public class MyPrimeTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		if (args.length < 3) {
			System.out.println("Usage: MyPrimeTest numThread low high \n");
			return;
		}
		int nthreads = Integer.parseInt(args[0]);
		int low = Integer.parseInt(args[1]);
		int high = Integer.parseInt(args[2]);
		Counter c = new Counter();

		//test cost of serial code
		long start = System.currentTimeMillis();
		int numPrimeSerial = SerialPrime.numSerailPrimes(low, high);
		long end = System.currentTimeMillis();
		long timeCostSer = end - start;
		System.out.println("Time cost of serial code: " + timeCostSer + " ms.");

		//test of concurrent code
		// **************************************
		// Write me here
		long start1 = System.currentTimeMillis();
		int range=high/nthreads;
		//System.out.println("The range is: "+range);
		ThreadPrime threadList[]= new ThreadPrime[nthreads+1]; 

		for(int i=1;i<nthreads+1;i++) {
			threadList[i]=new ThreadPrime((range*(i-1)+1),range*i,c);
			threadList[i].start();
		}
		for(int i=1;i<nthreads+1;i++) {
			try {
				threadList[i].join();
			}
			catch(InterruptedException e) {

			}
		}


		long end1 = System.currentTimeMillis();
		long timeCostCon = end1 - start1;



		// **************************************
		System.out.println("Time cost of parallel code: " + timeCostCon + " ms.");
		System.out.format("The speedup ration is by using concurrent programming: %5.2f. %n", (double)timeCostSer / timeCostCon);

		System.out.println("Number prime found by serial code is: " + numPrimeSerial);
		System.out.println("Number prime found by parallel code is " + c.total());
	}


}
