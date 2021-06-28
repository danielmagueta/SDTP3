package serverSide.objects;

import java.rmi.registry.*;
import java.rmi.*;
import interfaces.*;
import serverSide.main.*;
import clientSide.entities.*;
import genclass.GenericIO;
import java.util.Objects;
import commInfra.*;


/**
 *    Arrival Airport.
 *
 *    It is responsible to keep a continuously updated account of the passengers that arrived.
 *    This class doesn't use semaphores
 *    All public methods are executed in mutual exclusion.

 *    
 */

public class ArrivalAirport implements ArrivalAirportInterface {
    

   
  /**
   *   Reference to the general repository.
   */

   private final GeneralReposInterface repos;
   
   /**
   *  Semaphore to ensure mutual exclusion on the execution of public methods.
   */

   private final Semaphore access;
   
  /**
   *  Number of passengers that have arrived and deboarded at destination.
   */

   private int nPassengerArrived;
   
   /**
   *  Number of passengers that have leaved the plane.
   */
   private int nOut;
   
   
  /**
   *   Number of entity groups requesting the shutdown.
   */

   private int nEntities;

   
   /**
   *  Departure Airport instantiation.
   *
   *    @param repos reference to the general repository
   */

   public ArrivalAirport (GeneralReposInterface repos)
   {

      this.repos = repos;
      access = new Semaphore ();
      access.up ();
      nPassengerArrived = 0;
      nOut = 0;
      nEntities = 0;
      
   }
   
   /**
   *  @return number of passengers that have leaved the plane in this flight.
   * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   */
   
    public int getnOut() throws RemoteException {
        return nOut;
    }
    
    /**
   *  @param nOut Set number of passengers that have leaved the plane in this flight.
   * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   */
    
    public void setnOut(int nOut) throws RemoteException{
        this.nOut = nOut;
    }
   
   
   
   /**
   *    Get number of passengers that have arrived and deboarded already.
   * 
   *       @return number of passengers that have arrived and deboarded alerady
   * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   */
    public int getnPassengerArrived() throws RemoteException{
        return nPassengerArrived;
    }
   
  
        /**
    *  Announcing arrival to the arrival airport.
    *
    *  It is called by a pilot when the plane arrives at the arrival airport and is ready to initiate the debaording.
    * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
    *                             service fails
    * 
    */
    public void leaveThePlane () throws RemoteException
    {
        
        access.down();
        try
        { repos.subtractInF();
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Remote exception on leaveThePlane - subtarctINF: " + e.getMessage ());
            System.exit (1);
        } 
        try
        { repos.addPTAL();
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Remote exception on leaveThePlane - addPTAL: " + e.getMessage ());
            System.exit (1);
        } 
        ((Passenger) Thread.currentThread()).setPassengerState(3);
        try
        { repos.setPassengerState( ((Passenger) Thread.currentThread()).getPassengerId(),3);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Remote exception on leaveThePlane - setPassengerState 3: " + e.getMessage ());
            System.exit (1);
        } 
        nPassengerArrived ++;
        nOut ++;
        access.up();


    }
    
    /**
   *   Operation server shutdown.
   * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   *
   */

   public void shutdown () throws RemoteException
   {
       nEntities += 1;
       if (nEntities >= 3)
          ArrivalAirportMain.shutdown();
   }    

}
