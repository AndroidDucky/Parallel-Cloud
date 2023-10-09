public class ThreadPrime extends Thread {
	private Counter c;
	int i;

	// each thread only  takes care of one subrange (low, high)
	public ThreadPrime ( Counter ct,int i) {
		this.i=i;
		this.c = ct;
	}

	@Override
	public void run(){

		try {
			for(int j=0;j<11;j++) {
				c.getName();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	public int getID() {return this.i;}
}
