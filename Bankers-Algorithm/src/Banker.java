/**
 * A non-threaded object
 * @author Steven
 *
 */

public class Banker {

public volatile int allocUnits = 0;
public volatile int availUnits = 0;

/**
* Main constructor for Banker class
* @param nUnits - # of units from the system the Banker wants to obtain initially
*/
public Banker(int nUnits){

}

/**
* Attempt to claim more units from the system, if they are available.
* @param nUnits
*/
public void setClaim(int nUnits){
if(nUnits < 0){

}
//if(nUnits > System units)
//if(this.hasMadeClaim)
//Same return as the first one.
}


/**
* Attempt to claim more units.
*/
public boolean request(int nUnits){

}

public void release(int nUnits){

}

/**
* Returns no
*/
public int allocated( ){
return allocUnits;
}

    public int remaining( ){
     return availUnits;
    }

}