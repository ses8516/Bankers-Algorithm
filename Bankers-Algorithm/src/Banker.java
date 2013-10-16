/**
 * A non-threaded object
 * @author Steven
 *
 */

public class Banker {

	private volatile int allocUnits;
	private volatile int availUnits;
	
	private Thread claim = null; 
	
	/**
	* Main constructor for Banker class
	* @param nUnits - # of units from the system the Banker wants to obtain initially
	*/
	public Banker(int nUnits){
		allocUnits = nUnits;
		availUnits = nUnits;
	}
	
	/**
	* Attempt to claim more units from the system, if they are available.
	* @param nUnits
	*/
	public void setClaim(int nUnits){
		if(claim != null || nUnits < 1 || nUnits > availUnits){
			System.exit(1);
		}

		claim = Thread.currentThread();
		
		System.out.println("Thread "+Thread.currentThread().getName()+" sets a claim for "+nUnits+" units.");
	}
	
	
	/**
	* Attempt to claim more units.
	*/
	public boolean request(int nUnits){
		System.out.println("Thread "+Thread.currentThread().getName()+" requests "+nUnits+" units.");
		
		if (safe()){
			
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
					allocUnits += nUnits;
					availUnits += nUnits;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Determine if state is safe
	 */
	private boolean safe(){
		return false;
	}
	
	public void release(int nUnits){
		if( claim != Thread.currentThread() || nUnits > 0 || nUnits > allocUnits){
			System.exit(1);
		}
		System.out.println("Thread "+Thread.currentThread().getName()+" releases "+nUnits+" units.");
		allocUnits -= nUnits;
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