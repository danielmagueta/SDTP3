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
 *    Departure Airport.
 *
 *    It is responsible to keep a continuously updated account of the passengers in queue.
 *    and is implemented using semaphores for synchronization.
 *    All public methods are executed in mutual exclusion.
 *    There are five internal synchronization points: an array of blocking points, one per each passenger, 
 *    where they wait to show documents and after to board the plane,
 *    one where the pilot awaits for the boarding to end, one where the hostess awaits the next flight to arrive,
 *    one where the hostess wait for the passenger to show the documents and a last one where she awaits for a new passenger in queue
 *    
 */

public class DepartureAirport implements DepartureAirportInterface{




  /**
   *   Passenger waiting in queue to be checked.
   */

   private MemFIFO<Integer> passengerFIFO;
   
   
  /**
   *   Reference to the general repository.
   */

   private final GeneralReposInterface repos;
   
   /**
   *  Semaphore to ensure mutual exclusion on the execution of public methods.
   */

   private final Semaphore access;

  /**
   *  Blocking semaphore for the pilot thread who is waiting for the boarding to be concluded.
   */

   private final Semaphore waiting_boarding;

  /**
   *  Blocking semaphore for the hostess thread while she waits for the next flight.
   */

   private final Semaphore waiting_next_flight;
  
   /**
   *  Blocking semaphore for the hostess thread while she waits for the next passenger.
   */
   
   
   private final Semaphore waiting_passenger;
  
   /**
   *  Blocking semaphore for the hostess thread while she checks the passenger.
   */
   
   private final Semaphore checking_passenger;
  
   /**
   *  Array of blocking semaphores for the passenger threads while they wait to be checked and while they wait for a new passenger to arrive.
   */
   
     private final Semaphore [] inQueue;
   
   
   /**
   *  Number of passengers in queue.
   */
   
   private int nINQ;
   
   /**
   *  Number of passengers in this flight.
   */
   
   private int nINF;
   
   /**
   *  Total number of passengers that have departed.
   */
   
   private int ntotalINF;
   
    /**
   *  Passengers that haven't still arrived at airport and boarded.
   */
   
   private int passengersLEFT;

   
  /**
   *   Number of entity groups requesting the shutdown.
   */

   private int nEntities;
   

   

   /**
   *  Departure Airport instantiation.
   *
   *     @param repos reference to the general repository
   */

   public DepartureAirport (GeneralReposInterface repos)
   {
      try
      { passengerFIFO = new MemFIFO<> (new Integer [SimulPar.N]);
      }
      catch (MemException e)
      { GenericIO.writelnString ("Instantiation of waiting FIFO failed: " + e.getMessage ());
        passengerFIFO = null;
        System.exit (1);
      }
      this.repos = repos;
      access = new Semaphore ();
      access.up ();
      waiting_boarding = new Semaphore ();
      waiting_next_flight = new Semaphore ();
      waiting_passenger = new Semaphore ();
      checking_passenger = new Semaphore ();
      inQueue = new Semaphore [SimulPar.N];
      for (int i = 0; i < SimulPar.N; i++)
        inQueue[i] = new Semaphore ();
     nINQ = 0;
     nINF = 0;
     ntotalINF = 0;
     passengersLEFT = SimulPar.N;
     nEntities = 0;
   }

    
    

//pilot life cicle
   
  /**
   *  Operation for the pilot to unblock the waiting hostess.
   *
   *  It is called by a pilot to unlock the hostess thread, so it can terminate.
   *  @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   *
   */
    
    public void endHostess () throws RemoteException
    {
        waiting_next_flight.up();
    }
    
  /**
   *  Operation inform that the plane is ready for boarding.
   *
   *  It is called by a pilot when the plane is ready for the next boarding.
   *  @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   *
   */
    
    public void informPlaneReadyForBoarding () throws RemoteException
    {
        access.down();
        try
        { repos.reportBoarding();
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Remote exception on informPlaneReadyForBoarding - reportBoarding: " + e.getMessage ());
            System.exit (1);
        } 
        ((Pilot) Thread.currentThread()).setPilotState(1);
        try
        { repos.setPilotState(1);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Remote exception on informPlaneReadyForBoarding - setPilotState 1: " + e.getMessage ());
            System.exit (1);
        } 
        access.up();
        waiting_next_flight.up();


    }
    
  /**
   *  Operation for the pilot to wait for conclusion of boarding.
   *
   *  It is called by a pilot when the hostess informs the plane is ready to take off.
   * 
   * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   *
   */
    
    public void waitForAllInBoard () throws RemoteException
    {
        access.down();
        ((Pilot) Thread.currentThread()).setPilotState(2);
        try
        { repos.setPilotState(2);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Remote exception on waitForAllInBoard - setPilotState 2: " + e.getMessage ());
            System.exit (1);
        } 
        access.up();
        waiting_boarding.down();
    }
    

  /**
   *  Operation for the pilot to park at the transfer gate.
   *
   *  It is called by a pilot when he parks the plane at the transfer gate, .
   * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   *
   */
    public void parkAtTransferGate () throws RemoteException
    {
        access.down();
        ((Pilot) Thread.currentThread()).setPilotState(0);
        try
        { repos.setPilotState(0);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Remote exception on parkAtTransferGate - setPilotState 0: " + e.getMessage ());
            System.exit (1);
        } 
        access.up(); 
    }
    
    //hostess life cicle
    
   /**
   *  Operation for the hostess to prepare for pass boarding.
   *
   *  It is called by a hostess when she prepares to wait for the passengers.
   * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   *
   */
    public void prepareForPassBoarding () throws RemoteException
    {
        access.down();
        ((Hostess) Thread.currentThread()).setHostessState(1);
        try
        { repos.setHostessState(1);;
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Hostess remote exception on prepareForPassBoarding - setHostessState 1: " + e.getMessage ());
            System.exit (1);
        } 
        access.up();
        waiting_passenger.down();
    }
    
    
    /**
   *  Operation for the hostess to check documents.
   *
   *  It is called by a hostess when she is checking the passenger documents.
   * @return id of the passenger
   * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   *
   */
    public int checkDocuments () throws RemoteException
    {
        int passengerID = -1;
        access.down();
        try
        { passengerID = passengerFIFO.read ();                            // the hostess calls the passenger to check documents
            if ((passengerID < 0) || (passengerID >= SimulPar.N))
             throw new MemException ("illegal passenger id!");
        }
        catch (MemException e)
        { GenericIO.writelnString ("CheckDocuments failed: " + e.getMessage ());
          access.up ();                                                // exit critical region
          System.exit (1);
        }
        ((Hostess) Thread.currentThread()).setHostessState(2);
        nINQ --;
        try
        { repos.subtractInQ();
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Remote exception on checkDocuments - subtractInQ: " + e.getMessage ());
            System.exit (1);
        } 
        try
        { repos.reportCheck(passengerID);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Remote exception on checkDocuments - reportCheck: " + e.getMessage ());
            System.exit (1);
        } 
        try
        { repos.setHostessState(2);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Hostess remote exception on checkDocuments - setHostessState 2: " + e.getMessage ());
            System.exit (1);
        } 
        access.up();
        inQueue[passengerID].up();
        checking_passenger.down();
        return passengerID;
    }


    

    
    
    /**
   *  Operation for the hostess to be locked in the first iteration.
   *
   *  It is called by a hostess in the first iteration so shw waits for the plane to be ready for boarding.
   * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   * 

   */
    public void waitPilot () throws RemoteException
    {
        waiting_next_flight.down();

    }
    
    /**
   *  Operation for the hostess to wait for a new passenger.
   *
   *  It is called by a hostess when she waits for a new passenger.
   * 
   * @param passengerID id of passenger
   *
   * @return true if the plane is ready to fly, false otherwise
   * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   */
    public boolean waitForNextPassenger (int passengerID) throws RemoteException
    {
        access.down();
        ((Hostess) Thread.currentThread()).setHostessState(1);
        try
        { repos.setHostessState(1);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Hostess remote exception on waitForNextPassenger - setHostessState 1: " + e.getMessage ());
            System.exit (1);
        } 
        inQueue[passengerID].up();
        nINF++;
        ntotalINF++;
        passengersLEFT--;
        access.up();
        try
        { sleep ((long) (2));       //small break so the passenger has time to print his final state in the repository
        }
        catch (InterruptedException e) {}
        if( ((nINF>=SimulPar.MIN) && (nINQ == 0))  || (nINF == SimulPar.MAX) ||  (passengersLEFT == 0))
        {
            nINF = 0;
            return true; 
        }
        else
        {   waiting_passenger.down();
            return false;
        }
        
        
        
    }
    
    /**
   *  Operation for the hostess to inform the plane is ready to flight.
   *
   *  It is called by a hostess after the plane gets ready to leave.
   * 
   * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   *
   */
    public void informPlaneReadyToTakeOff () throws RemoteException
    {
        access.down();
        ((Hostess) Thread.currentThread()).setHostessState(3);
        try
        { repos.reportDeparted();
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Remote exception on informPlaneReadyToTakeOff - reportDeparted: " + e.getMessage ());
            System.exit (1);
        } 
        try
        { repos.setHostessState(3);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Hostess remote exception on informPlaneReadyToTakeOff - setHostessState 3: " + e.getMessage ());
            System.exit (1);
        } 
        access.up();
        waiting_boarding.up();
    }
    
    /**
   *  Operation for the hostess to wait for the next flight.
   *
   *  It is called by a hostess while she is waiting for the next flight.
   * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   *
   */
    public void waitForNextFlight () throws RemoteException
    {
        access.down();
        ((Hostess) Thread.currentThread()).setHostessState(0);
        try
        { repos.setHostessState(0);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Hostess remote exception on waitForNextFlight - setHostessState 0: " + e.getMessage ());
            System.exit (1);
        } 
        access.up();
        waiting_next_flight.down();
    }
    
     //passenger life cicle
    
    
   /**
   *  Operation for the passenger to go to the waiting queue.
   *
   *  It is called by a passenger after going to the airport.
   * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   *
   */
    public void waitInQueue () throws RemoteException
    {
        nINQ ++;
        access.down();
        int passengerID = ((Passenger) Thread.currentThread()).getPassengerId();
        try
        { passengerFIFO.write (passengerID); 
        
        }
        catch (MemException e)
        { GenericIO.writelnString ("Insertion of passenger id in queue for plane failed: " + e.getMessage ());
          access.up ();                
          System.exit (1);
        }
        try
        { repos.addInQ();
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Passenger " + passengerID + " remote exception on waitInQueue - addInQ 1: " + e.getMessage ());
            System.exit (1);
        } 
        ((Passenger) Thread.currentThread()).setPassengerState(1);
        try
        { repos.setPassengerState(passengerID,1);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Passenger " + passengerID + " remote exception on waitInQueue - setPassengerState 1: " + e.getMessage ());
            System.exit (1);
        } 
        access.up();
        waiting_passenger.up();
        inQueue[passengerID].down();
    }
    
    /**
   *  Operation for the passenger to show his/her documents to the hostess.
   *
   *  It is called by a passenger showing his/her documents to teh hostess.
   * 
   * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   *
   */
    public void showDocuments () throws RemoteException
    {
        access.down();
        int passengerID = ((Passenger) Thread.currentThread()).getPassengerId();
        access.up();
        checking_passenger.up();
        inQueue[passengerID].down();
        
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
          DepartureAirportMain.shutdown();
   }    

    
}
