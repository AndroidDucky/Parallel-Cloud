public class Counter {
	private int c = 0;
	boolean t1turn = true;

	public synchronized void increment( int n) throws InterruptedException {

		this.c+=n;

	}

	public synchronized int total() {
		return this.c;
	}




}
