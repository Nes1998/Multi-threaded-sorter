/**
 * @author Nestor Fong
 * Program to implement multi-threaded merge sorting with up to 
 * 16 threads and an array of 100000000 elements
 */
import java.util.Random;
import java.util.Scanner;

public class ThreadSortDriver{
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try (Scanner scan = new Scanner(System.in)) {
			System.out.print("How many threads would you like to use?");
			int arrSize = 100000000, nThreads = scan.nextInt();
			double n = 1000000000.0;
			double[] arrUnS = new double[arrSize];
			
			/*
			 * Fills the array with random doubles
			 */
			for(int i = 0; i < arrSize; i++)
			{
				arrUnS[i] = new Random().nextDouble();
			}
			// divides the array size by the number of threads
			int subSize = arrUnS.length/nThreads;
			
			// array variables for the runner and thread objects
			ThreadSort[] threadArr = new ThreadSort[nThreads];
			Thread[] threadRun = new Thread[nThreads];
			
			// variable for the last element index of the array
			int last = 0;
			
			/*
			 * Loop to initialize each runner object in the array, using the respective portion
			 * of the full array depending on the number of threads
			 */
			for(int i = 0; i < nThreads; i++)
			{
				int first = subSize * i;
				last += subSize;
				threadArr[i] = new ThreadSort(arrUnS, first, last-1, nThreads);
			}
			
			for(int i = 0; i < nThreads; i++)
			{
				threadRun[i] = new Thread(threadArr[i]);
			}
			
			
			// variable to measure the start time
			long startT = System.nanoTime();
			for(int i = 0; i < nThreads; i++)
			{
				threadRun[i].start();
			}
			
			for(int i = 0; i < nThreads; i++)
			{
				try {
					threadRun[i].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			// variable to measure the end time
			long endT = System.nanoTime();
			
			// Prints how long it took for the array to be sorted in seconds
			System.out.println((endT - startT)/n);
		}
		
		// Prints the sorted array
		/*for(int i = 0; i < arrSize; i++)
		{
			System.out.println(arrUnS[i]);
		}*/

	}
}
