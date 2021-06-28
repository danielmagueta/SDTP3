package interfaces;

import java.rmi.*;

/**
 *   Operational interface of a remote object of type Plane.
 *
 *     It provides the functionality to access the Plane.
 */

public interface PlaneInterface extends Remote {
 
   
  

    public int getnINF() throws RemoteException;

    public void setnINF(int nINF) throws RemoteException;
    

    
   /**
    *  Flying to destination airport.
    *
    *  It is called by a pilot when he is flying to the destination airport.
    */
   
    public void flyToDestinationPoint () throws RemoteException;
    
   /**
    *  Flying back to initial airport.
    *
    *  It is called by a pilot when he is flying to the departure airport.
    */
   
    public void flyToDeparturePoint () throws RemoteException;
    
   
     //passenger life cicle
   
   /**
   *  Operation for the passenger board the plane.
   *
   *  It is called by a passenger boarding the plane.
   *
   */
    public void boardThePlane() throws RemoteException;
    
    
    /**
    *  Announcing arrival to the arrival airport.
    *
    *  It is called by a pilot when the plane arrives at the arrival airport and is ready to initiate the debaording.
    * 
    */ 
    public void announceArrival () throws RemoteException;
    
    
        /**
   *  Operation for the passenger waiting for the end of the flight.
   *
   *  It is called by a passenger awaiting for the arrival of the plane at the arrival airport.
   *
   */
    public void waitForEndOfFligh () throws RemoteException;

    /**
   *   Operation server shutdown.
   *
   */

   public void shutdown () throws RemoteException;
    
    


}
