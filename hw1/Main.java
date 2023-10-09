
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;


//Jared Adams 
public class Main extends JFrame implements KeyListener{

    static Thread display; 
    static String text="";
    int counter=0;
    
    
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private static JTextArea output;
	
	public Main(String name) {
		super(name);
		//JTextArea output = new JTextArea(20,30) // Can this statement replace the next one?
		
		output = new JTextArea(20,30);                      //create JTextArea in which all messages are shown.
		DefaultCaret caret = (DefaultCaret)output.getCaret();
		
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
		if(display.isAlive()) {
			display.interrupt();
			text+=e.getKeyChar()+"";
		}
		else if(e.getKeyChar()=='\n') {

			display=new display(text,output);
			display.start();
			text="";
		}
		
		else {
			text+=e.getKeyChar()+"";
			if(text.equals("exit")) {
				System.exit(0);
			}
		}
		
		
	}

	@Override
	// 
	public void keyReleased(KeyEvent e) {

	}

	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Main inp = new Main("A JFrame and KeyListener Demo");
	        display =new display("",output);
	        

	        inp.addWindowListener(
				new WindowAdapter() {
					public void windowClosing( WindowEvent e)
					{
						System.exit(0);
					}
				});
	      
	}
	
}









