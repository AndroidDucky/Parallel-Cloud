
public class waiter extends Thread implements Runnable{
	@Override
	public void run() {
		while(true) {
		if(Thread.interrupted()) {
				System.out.println("The printer has gotten his work half done");
				break;
		}
		}
	}
}
