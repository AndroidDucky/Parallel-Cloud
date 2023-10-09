public class AltNThreads {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		if (args.length < 1) {
			System.out.println("Usage: MyPrimeTest numThread low high \n");
			return;
		}
		int nthreads = Integer.parseInt(args[0]);
		Counter c = new Counter();
		ThreadPrime array[]=new ThreadPrime[nthreads];
		ThreadPrime threadList[]= new ThreadPrime[nthreads]; 

		for(int i=0;i<nthreads;i++) {

			threadList[i]=new ThreadPrime(c,i);
			array[i]=threadList[i];
		}
		c.setArray(array);
		for(int i=0;i<nthreads;i++) {
			threadList[i].start();
			//threadList[i].join();
		}

		/*for(int j=0;j<10;j++) {

		}*/
	}

}
