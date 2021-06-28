package serverSide.objects;

import java.rmi.registry.*;
import java.rmi.*;
import interfaces.*;
import serverSide.main.*;
import clientSide.entities.*;
import genclass.GenericIO;
import java.util.Objects;
import commInfra.*;
import static java.lang.Thread.sleep;

/**
 *    Plane.
 *
 *    It is responsible to keep a continuously updated account of the passengers inside the plane.
 *    and is implemented using semaphores for synchronization.
 *    All public methods are executed in mutual exclusion.
 *    There are one internal synchronization points: an array of blocking points, one per each passenger, 
 *    where they wait for the end of the flight
 */

public class Plane implements PlaneInterface {
 
   
    /**
   *  Reference to number of passengers in flight.
   */

   private int nINF;
   
  /**
   *   Reference to the general repository.
   */

   private final GeneralReposInterface repos;
   
   /**
   *  Semaphore to ensure mutual exclusion on the execution of public methods.
   */

   private final Semaphore access;



    /**
   *   Passenger waiting in flight waiting to leave.
   */

   private MemFIFO<Integer> passengerINF;
   
        /**
   *  Blocking semaphore for the passengers while they are in flight.
   */

   private final Semaphore [] in_flight;
   
   /**
   *  Plane instantiation.
   *
   *    @param repos reference to the general repository
   */
   
   
  /**
   *   Number of entity groups requesting the shutdown.
   */

   private int nEntities;

   public Plane (GeneralReposInterface repos)
   {
      nINF = 0;
      this.repos = repos;
      access = new Semaphore ();
      access.up ();
      in_flight = new Semaphore [SimulPar.N];
      for (int i = 0; i < SimulPar.N; i++)
        in_flight[i] = new Semaphore ();
      try
      { passengerINF = new MemFIFO<> (new Integer [SimulPar.N]);
      }
      catch (MemException e)
      { GenericIO.writelnString ("Instantiation of in flight FIFO failed: " + e.getMessage ());
        passengerINF = null;
        System.exit (1);
      }
      nEntities = 0;
   }

    public int getnINF() throws RemoteException{
        return nINF;
    }

    public void setnINF(int nINF) throws RemoteException{
        this.nINF = nINF;
    }
    

   
     /**
   *    Passengers in flight
   * 
   *       @return FIFo of ids of passengers in the flight
   * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   */
    public MemFIFO<Integer> getPassengerINF() throws RemoteException{
        return passengerINF;
    }
    
   /**
    *  Flying to destination airport.
    *
    *  It is called by a pilot when he is flying to the destination airport.
    * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
    */
   
    public void flyToDestinationPoint () throws RemoteException
   {
      access.down();
      ((Pilot) Thread.currentThread()).setPilotState(3);
      try
      { repos.setPilotState(3);
      }
      catch (RemoteException e)
      { GenericIO.writelnString ("Remote exception on flyToDestinationPoint - setPilotState 3: " + e.getMessage ());
          System.exit (1);
      } 
      access.up();
      try
      { sleep ((long) (1 + 100 * Math.random ()));
      }
      catch (InterruptedException e) {}
   }
    
   /**
    *  Flying back to initial airport.
    *
    *  It is called by a pilot when he is flying to the departure airport.
    * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
    */
   
    public void flyToDeparturePoint () throws RemoteException
   {
      
      access.down();
      ((Pilot) Thread.currentThread()).setPilotState(5);
      while(nINF != 0){}
      try
      { repos.reportreturning();
      }
      catch (RemoteException e)
      { GenericIO.writelnString ("Remote exception on flyToDeparturePoint - reportReturning: " + e.getMessage ());
          System.exit (1);
      } 
      try
      { repos.setPilotState(5);
      }
      catch (RemoteException e)
      { GenericIO.writelnString ("Remote exception on flyToDeparturePoint - setPilotState 5: " + e.getMessage ());
          System.exit (1);
      } 
      access.up();
      try
      { sleep ((long) (1 + 100 * Math.random ()));
      }
      catch (InterruptedException e) {}
   }
    
   
     //passenger life cicle
   
   /**
   *  Operation for the passenger board the plane.
   *
   *  It is called by a passenger boarding the plane.
   * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   *
   */
    public void boardThePlane() throws RemoteException
    {
        access.down();
        try
        { repos.addInF();
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Remote exception on boardThePlane - addINF: " + e.getMessage ());
            System.exit (1);
        } 
        int passengerID = ((Passenger) Thread.currentThread()).getPassengerId();     
        try
        { passengerINF.write (passengerID);                    
        }
        catch (MemException e)
        { GenericIO.writelnString ("Insertion of passenger in flight failed: " + e.getMessage ());
          access.up ();                
          System.exit (1);
        }
        ((Passenger) Thread.currentThread()).setPassengerState(2);
        try
        { repos.setPassengerState(passengerID,2);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Remote exception on boardThePlane - setPassengerState 2: " + e.getMessage ());
            System.exit (1);
        } 
        nINF ++;
        access.up();
    }
    
    
    /**
    *  Announcing arrival to the arrival airport.
    *
    *  It is called by a pilot when the plane arrives at the arrival airport and is ready to initiate the debaording.
    * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
    * 
    */ 
    public void announceArrival () throws RemoteException
    {
        access.down();
        int passengerID = -1;
        try
        { repos.reportArrived();
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Remote exception on announceArrival - reportArrived: " + e.getMessage ());
            System.exit (1);
        } 
        for(int i = 0; i<nINF; i++)
        {
            try
            { passengerID = passengerINF.read ();
              if ((passengerID < 0) || (passengerID >= SimulPar.N))
                 throw new MemException ("illegal passenger id!");
            }
            catch (MemException e)
            { GenericIO.writelnString ("Retrieval of passenger id from flight failed: " + e.getMessage ());
              access.up ();                                                // exit critical region
              System.exit (1);
            }

            ((Pilot) Thread.currentThread()).setPilotState(4);
            try
            { repos.setPilotState(4);
            }
            catch (RemoteException e)
            { GenericIO.writelnString ("Remote exception on announceArrical - setPilotState 4: " + e.getMessage ());
                System.exit (1);
            } 
            access.up();
            in_flight[passengerID].up();
        }
 
    }
    
    
        /**
   *  Operation for the passenger waiting for the end of the flight.
   *
   *  It is called by a passenger awaiting for the arrival of the plane at the arrival airport.
   * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   *
   */
    public void waitForEndOfFligh () throws RemoteException
    {
    
        access.down();
        int passengerID = ((Passenger) Thread.currentThread()).getPassengerId();
        access.up();
        in_flight[passengerID].down();
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
       if (nEntities >= 2)
          PlaneMain.shutdown();
   }
    
    


}
