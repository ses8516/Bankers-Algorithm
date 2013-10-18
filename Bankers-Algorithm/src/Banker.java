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
		claims = new HashMap<Thread,int[]>();
		allocUnits = nUnits;
		availUnits = nUnits;
	}
	
	/**
	* Attempt to claim more units from the system, if they are available.
	* @param nUnits
	*/
	public void setClaim(int nUnits){
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
	public boolean request(int nUnits){
		System.out.println("Thread "+Thread.currentThread().getName()+" requests "+nUnits+" units.");
		
		if (safe(nUnits, claims)){
			
		}else{
			while(true){
				System.out.println("Thread "+Thread.currentThread().getName()+" waits.");
				try {
					Thread.currentThread().wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Thread "+Thread.currentThread().getName()+" awakened.");
				
				if (safe(nUnits, claims)) {
					System.out.println("Thread "+Thread.currentThread().getName()+" has "+nUnits+" units allocated.");
					availUnits -= nUnits;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Determine if state is safe
	 */
	private boolean safe(int numberOfUnitsOnHand, Map<Thread,int[]> threadClaims ){
		ArrayList<int[]> array = new ArrayList<int[]>();
		
		for(Thread t: threadClaims.keySet()){
			array.add(threadClaims.get(t));
		}
		
		for(int i = 0; i < threadClaims.size() - 1; i++){
			if (array.get(i)[1] > numberOfUnitsOnHand){
				return false;
			}
			numberOfUnitsOnHand += array.get(i)[0];
		}
		return true;
	}
	
	public void release(int nUnits){
		if( !claims.containsKey(Thread.currentThread()) || nUnits <= 0 || nUnits > allocUnits){
			System.exit(1);
		}
		System.out.println("Thread "+Thread.currentThread().getName()+" releases "+nUnits+" units.");
		availUnits += nUnits;
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
