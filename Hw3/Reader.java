import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Reader implements Runnable{


    BufferedReader bufferedReader;
    SharedQueue q;
    Searcher searcher;
	
	public Reader( String str,SharedQueue q, Searcher searcher) throws FileNotFoundException {

		this.bufferedReader= new BufferedReader(new FileReader(str));
		this.q=q;
		this.searcher=searcher;
	}
	
	@Override
	public void run() {
		String line=null;
        int totalFound = 0;
        
        try {
			while ((line = bufferedReader.readLine()) != null) {
				q.enqueue(line);
				totalFound ++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

			try {
				bufferedReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
searcher.readerThreadIsAlive=false;

        System.out.println("Total number of lines searched is "+totalFound);
//        if(q.isEmpty()) {
//			System.out.println("Total occurence of pattern \'" + searcher.pattern + "\' is " + searcher.count);
//        	searcher.interrupt();
//        	
//        }

		
	}
	
}
