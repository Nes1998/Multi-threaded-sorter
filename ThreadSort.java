/**
 * @author Nestor Fong
 * Runner class for the ThreadSort program
 */

import java.util.concurrent.locks.ReentrantLock;

public class ThreadSort  implements Runnable
{

	// array to hold the array we must sort
	private double[] sortArr;
	// int elements that stand for the first and last elements of the array we must sort, and the number of threads
	private int sortF, sortL, nSize;
	// control variable that signals when we are in the last thread
	private static int resultInd;
	// lock variable that keeps the shared variable atomic
	private ReentrantLock lock;
	/**
	 * Constructor for the ThreadSort class
	 * @param arrUn the array we must sort
	 * @param first int representing the index of the first element of the array in the given portion
	 * @param last int representing the index of the last element of the array in the given portion
	 * @param n int representing number of threads
	 */
	public ThreadSort(double[] arrUn, int first, int last, int n) 
	{
		sortArr = arrUn;
		sortF = first;
		sortL = last; 
		nSize = n;
		resultInd = 0;
		lock = new ReentrantLock();
	}
	
	/**
	 * Implements the mergeSort algorithm
	 * @param arrSortCop the array we will sort
	 * @param f the index of the first element
	 * @param l the index of the last element
	 */
	private void mergeSort(double[] arrSortCop, int f, int l) 
	{
		// recursion until the first index and last index are equal
		if(f < l)
		{
			// midpoint of the array portion
			int m = (f+l)/2;
			
			mergeSort(arrSortCop, f, m);
			mergeSort(arrSortCop, m+1, l);
			// merges the sections of the array separated by the midpoint, sorting it out
			merge(arrSortCop,f, m, l);
		}
	}
	
	/**
	 * Merges the array sections separated by the midpoint while sorting
	 * the array in an increasing order
	 * @param fArr the array to be sorted
	 * @param low the first element index
	 * @param mid the midpoint index
	 * @param high the last element index
	 */
	 public void merge(double[] fArr, int low, int mid, int high) {
		 	// size of the array portion we are sorting
	        int s = high - low + 1;
	        // temporary array that will hold the sorted portion of the array
	        double copArr[] = new double[s];
	        // the start index of the temporary array
	        int arrInd = 0;
	        // loop control variables for both sections split by the midpoint
	        int i = low, j = mid+1;
	        
	       /*
	        * Loop through both sections split by the midpoint, placing the lower numbers first or otherwise
	        * just ordering them in the order they come
	        */
	       while(i <= mid && j <= high)
	       {
	    	   if(fArr[i] <= fArr[j])
	    	   {
	    		   copArr[arrInd] = fArr[i];
	    		   i++;
	    	   }
	    	   else
	    	   {
	    		   copArr[arrInd] = fArr[j];
	    		   j++;
	    	   }
	    	   arrInd++;
	       }
	       
	       /*
	        * In case one section was larger and the loop ended before all the elements could be sorted
	        * assign the remaining elements to their respective place
	        */
	       while(i <= mid)
	       {
	    	   copArr[arrInd] = fArr[i];
	    	   i++;
	    	   arrInd++;
	       }
	       
	       while(j <= high)
	       {
	    	   copArr[arrInd] = fArr[j];
	    	   j++;
	    	   arrInd++;
	       }
	       
	       // Pass the section back to the original array
	       System.arraycopy(copArr, 0, fArr, low, s);
	    }
	 
	 /**
	  * Function that implements merge sort for multiple threads, so that
	  * the last thread mixes them all together
	  * @param arrF the array to be sorted
	  * @param low the first element index of the array 
	  * @param high the last element index of the array
	  * @param numT the number of threads
	  */
	 public void threadedMergeSort(double[] arrF, int low, int high, int numT)
	 {
		mergeSort(arrF, low, high);
		// Makes sure that the access to the resultInd variable is atomic
		lock.lock();
		resultInd++;
		lock.unlock();
			
		// In case this is the last thread, sort of all the sections of the array
		if(resultInd == (numT-1))
		{
			//System.out.println(resultInd);
			int end = arrF.length-1;
			mergeSort(arrF, 0, end);
		}
		
			
	 }
	    
	@Override
	public void run() 
	{
		threadedMergeSort(sortArr, sortF, sortL, nSize);
	}
	
}
