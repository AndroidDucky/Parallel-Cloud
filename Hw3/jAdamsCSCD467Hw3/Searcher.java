import java.util.regex.Pattern;
import java.io.IOException;
import java.util.regex.Matcher;

import javax.swing.JTextArea;

public class Searcher extends Thread{

	String pattern;
	SharedQueue q;
	int count=0;
	boolean readerThreadIsAlive=true;
	
	
	public Searcher( String str,SharedQueue q) {

		this.q=q;
		this.pattern=str;
	}
	
	
	@Override
	public void run() {
		
		while(!q.isEmpty()||readerThreadIsAlive) {
			if(Thread.interrupted()) {
				break;
			}
		Pattern patt= Pattern.compile(pattern);
		Matcher matcher = patt.matcher(""+q.dequeue());
		
		int count = 0;
			while(matcher.find()) {
				this.count++;
				}
		}

		System.out.println("Total occurence of pattern \'" + pattern + "\' is " + this.count);
		
		}


	}
	

