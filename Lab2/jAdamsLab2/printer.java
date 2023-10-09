	public class printer extends Thread implements Runnable{
		int counter;
		Thread waiter;
		public printer(Thread waiter2) {
			this.waiter=waiter2;
		}

		
		@Override
		public void run() {
				for(int i =1;i<25;i++) {
				System.out.println("Message i = "+ i +", from "+ Thread.currentThread().getName());
			}
				waiter.interrupt();
				System.out.println("Waiter has done its work, terminating.");
				for(int i =25;i<51;i++) {
				System.out.println("Message i = "+ i +", from "+ Thread.currentThread().getName());
			}
		}
		
		
	}