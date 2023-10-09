public class Counter {
	boolean isT1turn=true;
	final Object lock = new Object();
	
	public synchronized void setTurn() {
			if(isT1turn==true) {
				isT1turn=false;
			}
			else {
				isT1turn=true;
			}
	}
	
	public synchronized boolean getTurn() {
		return this.isT1turn;
	}
	
}
