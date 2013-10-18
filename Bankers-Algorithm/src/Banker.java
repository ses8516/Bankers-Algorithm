import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A non-threaded object
 * @author Steven
 *
 */

public class Banker {

	private volatile int allocUnits;
	private volatile int availUnits;
	
	private Map<Thread,int[]> claims; 
	
	/**
	* Main constructor for Banker class
	* @param nUnits - # of units from the system the Banker wants to obtain initially
	*/
	public Banker(int nUnits){
		// Map of thread => [allocation, remaining claim]
		claims = new HashMap<Thread,int[]>();
		allocUnits = 0;
		availUnits = nUnits;
	}
	
	/**
	* Attempt to claim more units from the system, if they are available.
	* @param nUnits
	*/
	public synchronized void setClaim(int nUnits){
		if(claims.containsKey(Thread.currentThread()) || nUnits <= 0 || nUnits > availUnits){
			System.exit(1);
		}

		int[] allocRemain = {0,nUnits};
		claims.put(Thread.currentThread(), allocRemain );
		
		System.out.println("Thread "+Thread.currentThread().getName()+" sets a claim for "+nUnits+" units.");
	}
	
	
	/**
	* Attempt to claim more units.
	*/
	public synchronized boolean request(int nUnits){
		System.out.println("Thread "+Thread.currentThread().getName()+" requests "+nUnits+" units.");
		
		if( !claims.containsKey(Thread.currentThread()) || nUnits <= 0 || nUnits > claims.get(Thread.currentThread())[1]){
			System.exit(1);
		}
		
		if (safe()){
			System.out.println("Thread "+Thread.currentThread().getName()+" has "+nUnits+" units allocated.");
			availUnits -= nUnits;
			allocUnits += nUnits;
			claims.get(Thread.currentThread())[0] += nUnits;
			claims.get(Thread.currentThread())[1] -= nUnits;
			return true;
			
		}else{
			while(true){
				System.out.println("Thread "+Thread.currentThread().getName()+" waits.");
				try {
					Thread.currentThread().wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Thread "+Thread.currentThread().getName()+" awakened.");
				
				if (safe()) {
					System.out.println("Thread "+Thread.currentThread().getName()+" has "+nUnits+" units allocated.");
					availUnits -= nUnits;
					claims.get(Thread.currentThread())[0] += nUnits;
					claims.get(Thread.currentThread())[1] -= nUnits;
					return true;
				}
			}
		}
	}
	
	/**
	 * Determine if state is safe
	 */
	private boolean safe(){
		ArrayList<int[]> array = new ArrayList<int[]>();
		
		int unitsOnHand = availUnits;
		
		for(Thread t: claims.keySet()){
			array.add(claims.get(t));
		}
		sort(array);
		for(int i = 0; i < claims.size() - 1; i++){
			if (array.get(i)[1] > unitsOnHand){
				return false;
			}
			unitsOnHand += array.get(i)[0];
		}
		return true;
	}
	
	/**
	 * @param, array, array of pairs of remaining and allocated units
	 * Sorts the array by increasing remaining claim
	 */
	private void sort(ArrayList<int[]> array){
		int temp[] = null;
		
		for(int i = 0; i < array.size(); i++){
			for (int j = i + 1; j < array.size(); j++){
				if (array.get(i)[1] > array.get(j)[1] ){
					temp = array.get(i);
					array.set(i, array.get(j));
					array.set(j, temp);
				}
			}
		}
		
	}
	
	/**
	 * The current thread releases nUnits units
	 * @param nUnits
	 */
	public void release(int nUnits){
		if( !claims.containsKey(Thread.currentThread()) || nUnits <= 0 || nUnits > claims.get(Thread.currentThread())[0]){
			System.exit(1);
		}
		System.out.println("Thread "+Thread.currentThread().getName()+" releases "+nUnits+" units.");
		availUnits += nUnits;
		allocUnits -= nUnits;
		claims.get(Thread.currentThread())[0] -= nUnits;
		claims.get(Thread.currentThread())[1] += nUnits;
		Thread.currentThread().notifyAll();
	}
	
	/**
	* @return number of allocated units
	*/
	public int allocated( ){
		return allocUnits;
	}
	
	/**
	 * @return number of available units
	 */
	public int remaining( ){
		return availUnits;
	}
}
