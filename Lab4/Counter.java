public class Counter {
	private int c = 0;
	private ThreadPrime array[];
	private int counter=0;


	public synchronized void getName() throws InterruptedException {
		//System.out.println("C is "+array[this.c].getName()+" and the I is: "+Thread.currentThread().getName());

		if(c==array.length) {
			this.c=0;
			counter++;
		}
		while(!array[this.c].getName().equals(Thread.currentThread().getName())) {
			wait();
		}

		this.c++;
		notifyAll();

		switch(counter) {
		case 0: System.out.println("1st Message from " + Thread.currentThread().getName()+".");
		break;
		case 1:System.out.println("2nd Message from " + Thread.currentThread().getName()+".");
		break;
		case 2:System.out.println("3rd Message from " + Thread.currentThread().getName()+".");
		break;
		default:System.out.println(counter+"th Message from " + Thread.currentThread().getName()+".");
		}

	}

	public void setArray(ThreadPrime[] array) {
		this.array=array;

	}







}
