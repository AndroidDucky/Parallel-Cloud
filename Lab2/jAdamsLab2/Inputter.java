
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;


//Jared Adams 

public class Inputter extends JFrame {
	int counter=0;
    static Thread waiter ;
    static Thread printer; 
	

	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
	        waiter =new Thread(new waiter(), "waiter");
	        printer =new Thread(new printer(waiter), "printer");
	        waiter.start();
	        printer.start();
	        printer.join();
	        System.out.println("Both Waiter and Printer have finished their work!");
	        

	}
	
}









