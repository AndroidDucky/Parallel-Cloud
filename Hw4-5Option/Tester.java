
import java.text.DecimalFormat;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Tester {
	static Boolean isDone=false;
	static CyclicBarrier barrier;
	static int maxB = 0;
	static int RowTurn = 0;
	static DecimalFormat df = new DecimalFormat("0.000000");
	static int[][] b = {
			{92, 99, 1, 8, 15, 67, 74, 51, 58, 40},
			{98, 80, 7, 14, 16, 73, 55, 57, 64, 41},
			{4, 81, 88, 20, 22, 54, 56, 63, 70, 47},
			{85, 87, 19, 21, 3, 60, 62, 69, 71, 28},
			{86, 93, 25, 2, 9, 61, 68, 75, 52, 34},
			{17, 24, 76, 83, 90, 42, 49, 26, 33, 65},
			{23, 5, 82, 89, 91, 48, 30, 32, 39, 66},
			{79, 6, 13, 95, 97, 29, 31, 38, 45, 72},
			{10, 12, 94, 96, 78, 35, 37, 44, 46, 53},
			{11, 18, 100, 77, 84, 36, 43, 50, 27, 59}
	};
	static float[][] bN = new float [b.length][b[0].length];

	public static void main(String[] args) throws InterruptedException {
		Semaphore sem = new Semaphore(1);

		int threads = b.length;
		barrier = new CyclicBarrier(threads,new Runnable() {

			@Override
			public void run() {
				if(isDone==true)
					for(int i =0; i< bN.length;i++) {
						for(int j=0; j< bN[0].length;j++) {
							System.out.print(bN[i][j]+" ");
						}
						System.out.println();
					}
			}

		});

		Thread[] arr = new Thread[threads];

		for (int i = 0; i < b.length; ++i) {
			arr[i]= new Thread(new Worker(i));
		}
		for (int i = 0; i < b.length; ++i) {
			arr[i].start();
		}

		for (int i = 0; i < b.length; ++i) {
			arr[i]= new Thread(new Worker2(i,sem));
			arr[i].start();
		}

	}


	static class Worker2 implements Runnable{
		private int row;  
		Semaphore sem;
		public Worker2(int i, Semaphore sem) {
			this.sem = sem;
			this.row=i;  
		}

		@Override
		public void run() {
			try {
				sem.acquire();
				for(int i=row; i<b.length; i++){
					for(int j=0;j<b[0].length;j++)
						bN[i][j] = Float.parseFloat(df.format(((1.0*b[row][j])/maxB)));
				}
			}catch(InterruptedException e) {

			}
			sem.release();

			isDone=true;
			try {
				barrier.await();
			} catch (InterruptedException e) {
				return;
			} catch (BrokenBarrierException e) {
				return;
			}
		}

	}




	static class Worker implements Runnable{
		int ThreadscurrentRow;
		int RegionalMax = 0;
		float[] row= new float[b.length];

		public Worker(int currentRow){
			this.ThreadscurrentRow=currentRow;
		}

		@Override
		public void run() {
			//Finding the Local Max of the Row and checking if its greater than the Global Max
			findRowMax(ThreadscurrentRow);
			if(maxB<RegionalMax) {
				maxB = RegionalMax;
			}
			//Waiting for all threads to finish
			try {
				barrier.await();
			} catch (InterruptedException e) {
				return;
			} catch (BrokenBarrierException e) {
				return;
			}

		}

		public void findRowMax(int row) {
			for(int i =0; i<b.length;i++) {
				if(RegionalMax<b[row][i]) {
					RegionalMax=b[row][i];
				}
			}
		}
	}



}


