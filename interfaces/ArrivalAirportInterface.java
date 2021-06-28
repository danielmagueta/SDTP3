package interfaces;

import java.rmi.*;

/**
 *   Operational interface of a remote object of type ArrivalAirport.
 *
 *     It provides the functionality to access the Arrival Airport.
 */
public interface ArrivalAirportInterface extends Remote {
    

   
   /**
   *  @return number of passengers that have leaved the plane in this flight.
   */
   
    public int getnOut() throws RemoteException;
    
    /**
   *  @param nOut Set number of passengers that have leaved the plane in this flight.
   */
    
    public void setnOut(int nOut) throws RemoteException;
   
   
   
   /**
   *    Get number of passengers that have arrived and deboarded already.
   * 
   *       @return number of passengers that have arrived and deboarded alerady
   */
    public int getnPassengerArrived() throws RemoteException;
   
  
        /**
    *  Announcing arrival to the arrival airport.
    *
    *  It is called by a pilot when the plane arrives at the arrival airport and is ready to initiate the debaording.
    * 
    */
    public void leaveThePlane () throws RemoteException;
    
    /**
   *   Operation server shutdown.
   *
   */

   public void shutdown () throws RemoteException;   

}
