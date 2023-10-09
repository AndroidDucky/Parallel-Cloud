
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;


//Jared Adams, 

public class Inputter extends JFrame implements KeyListener{
	static int counter=0;
    static Thread	thread_1 = new threads();
    static Thread	thread_2 = new threads();

	public static class threads extends Thread implements Runnable{
		@Override
		public void run() {
			try {
				while(true) {
					output.append("Message from thread--> "+Thread.currentThread().getName()+"\n");
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {

			}
			counter++;
		}
		
		
	}
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private static JTextArea output;
	
	public Inputter(String name) {
		super(name);
		//JTextArea output = new JTextArea(20,30) // Can this statement replace the next one?
		
		output = new JTextArea(20,30);                      //create JTextArea in which all messages are shown.
		DefaultCaret caret = (DefaultCaret)output.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);  // JTextArea always set focus on the last message appended.
		
		//
		add(new JScrollPane(output)); // add a Scroll bar to JFrame, scrolling associated with JTextArea object
		setSize(500, 500);            // when lines of messages exceeds the line capacity of JFrame, scroll bar scroll down.
		setVisible(true);
		output.addKeyListener(this);  // Adds the specified key listener to receive key events from this component.
	}
		
	
	@Override
	public void keyTyped(KeyEvent e) {	
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(counter==0) {
			output.append("Thread-0 Gets Interrupted! Terminate! \n");
			thread_1.interrupt();
		}
		if(counter==1) {
			output.append("Thread-1 Gets Interrupted! Terminate! \n");
			thread_2.interrupt();
		}
		if(counter==2) {
			output.append("All threads are interrupted \n");
			counter++;
		}
		
	}

	@Override
	// 
	public void keyReleased(KeyEvent e) {
		
		if(counter==0) {
			
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
	        Inputter inp = new Inputter("A JFrame and KeyListener Demo");

	        thread_1.start();
	        thread_2.start();

	        
	        
	        inp.addWindowListener(
				new WindowAdapter() {
					public void windowClosing( WindowEvent e)
					{
						System.exit(0);
					}
				});
	}
}




