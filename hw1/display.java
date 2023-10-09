import javax.swing.JTextArea;

public class display extends Thread implements Runnable{
	
	
		JTextArea output;
		String str;
		
		
		public display( String str,JTextArea output) {

			this.output=output;
			this.str=str;
		}
		
		
		

		
		@Override
		public void run() {

			try {
			while(true) {

				output.append(str+"\n");
				Thread.currentThread().sleep(100);
				
			}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		
	}

