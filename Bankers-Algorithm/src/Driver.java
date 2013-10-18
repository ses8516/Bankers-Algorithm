import java.util.ArrayList;


public class Driver {
	// Number of Clients
	private final static int NUM_CLIENTS = 5;
	
	// Number of units to give the banker
	private final static int BANKER_UNITS = 20;
	
	// Number of units each clients will request
	private final static int CLIENT_UNITS = 5;
	
	// Number of request/release loops for each client to loop through
	private final static int NUM_REQUESTS = 5;
	
	// Minimum time in milliseconds for each client to sleep between requests
	private final static long MIN_SLEEP_TIME = 1000;
	
	// Maximum time in milliseconds for each client to sleep between requests
	private final static long MAX_SLEEP_TIME = 5000;
	
	public static void main(String[] args){
		Banker banker = new Banker(BANKER_UNITS);
		
		ArrayList<Client> clients = new ArrayList<Client>(NUM_CLIENTS);
		
		for(int i = 0; i < NUM_CLIENTS; i++){
			clients.add( new Client(""+i,banker,CLIENT_UNITS,NUM_REQUESTS,MIN_SLEEP_TIME,MAX_SLEEP_TIME));
		}
		
		for (Client c: clients){
			c.start();
		}
		
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
