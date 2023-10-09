public class Alternation {
	
	Counter counter = new Counter();
	Thread t1;
	Thread t2;
	
	public Alternation() {
		

		t1 = new Thread(new Runnable() {
	        @Override
	        public void run() {
                for (int i = 1; i <= 50; i += 2) {
	                	while(!counter.getTurn() );  //guarded block
	                    System.out.println("T1=" + i);
	                    counter.setTurn();
	                    try {
	                    	Thread.sleep(100);
	                    	
	                    } catch (InterruptedException e) {}
                }//end of for
	        }
	    });
	    t2 = new Thread(new Runnable() {

	        @Override
	        public void run() {
                for (int i = 2; i <= 50; i += 2) {
	                    while (counter.getTurn());   //guarded block
	                        try {
	                        	Thread.sleep(100);
	                        	
	                        } catch (InterruptedException e) {}
	                    System.out.println("T2=" + i);
	                    counter.setTurn();

                }
	        }
	    });
	    t1.start();
	    t2.start();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Alternation();
	}
}


