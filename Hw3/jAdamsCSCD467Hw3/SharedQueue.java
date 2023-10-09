
import java.util.LinkedList;
import java.util.Queue;


public class SharedQueue {
	private class Node{
		private Object data;
		private Node next;
		public Node(Object elem) {
			this.data = elem;
			this.next = null;
		}
	}
	
	public Node head,tail;
	private int size;
	private int cap=100;
	int i=0;
	
	public SharedQueue() {
		head=null;tail=null;size=0;
	}
	
	
	public synchronized boolean isEmpty() {
		if((head==null)&&(tail==null)||this.size==0) {
			return true;
		}
		return false;
	}
	
	public synchronized void enqueue(Object value) {
		while(size>=cap) {
			try {
				wait();
			}
			catch(InterruptedException e) {
				
			}
		}
		Node node = new Node(value);
		if(size==0) {
			head=node;
		}
		else {
			tail.next=node;
		}
		i++;
		tail=node;
		size++;
		notifyAll();
	}
	
	public synchronized Object dequeue() {
		Object val;
		while(isEmpty()) {
		try {
			wait();
		}
		catch(InterruptedException e) {}
		}
		val=head.data;
		head=head.next;
		size--;
		notifyAll();
		return val;
	}
	
	
	
}
