/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide.entities;
import java.rmi.*;
import interfaces.*;
import genclass.GenericIO;
import commInfra.*;


/**
 *   Hostess thread.
 *
 *   It simulates the hostess life cycle.
 *   Implementation of a client-server model of type 2 (server replication).
 *   Communication is based on remote calls under Java RMI.
 */

public class Hostess extends Thread{

    
     /**
   *  Hostess state.
   */

   private int hostessState;
   
  /**
   *  Reference to the departure airport.
   */

   private final DepartureAirportInterface dAirport;
  

   
     /**
   *  Reference to the arrival airport.
   */

   private final ArrivalAirportInterface aAirport;
  

   

   /**
   *   Instantiation of a hostess thread.
   *
   *     @param name thread name
   *     @param dAirport remote reference to the departure airport
   *     @param aAirport remote reference to the arrival airport
   */

   public Hostess (String name, DepartureAirportInterface dAirport, ArrivalAirportInterface aAirport)
   {
      super (name);
      hostessState = HostessStates.WAIT_FOR_NEXT_FLIGHT;
      this.dAirport = dAirport;
      this.aAirport = aAirport;
   }
   

    /**
     *   Set hostess state.
     *
     *     @param state new hostess state
     */

    public void setHostessState(int state) {
        this.hostessState = state;
     }

    /**
     *   Get hostess state.
     *
     *     @return hostess state
     */

    public int getHostessState() {
        return hostessState;
     }

    /**
     *   Life cycle of the hostess.
     */
    
    @Override
    public void run ()
    {
        waitPilot();                                                 //for the initial lock of the hostess
        while (!(getnPassengerArrived() == SimulPar.N))      // check for end of operations
       { 
         prepareForPassBoarding();                            // the hostess awaits for the pilot to indicate that the plane is ready for boarding
         int passengerID;
         boolean takeOff = false;
         while(!takeOff){
            passengerID = checkDocuments();                    // the hostess checks the passenger documents
            takeOff = waitForNextPassenger(passengerID);                // the hostess awaits for the next passanger
         }
         informPlaneReadyToTakeOff();                         // the hostess informs the pilot that the plane is ready to take off
         waitForNextFlight();                                 // the hostess awaits for the next flight

       }
    }
    
   /**
   *  Hostess waits for pilot.
   *
   *  Remote operation.
   *
   */

   private void waitPilot()
   {                           

      try
      { dAirport.waitPilot(); 
      }
      catch (RemoteException e)
      { GenericIO.writelnString ("Hostess remote exception on waitPlot: " + e.getMessage ());
        System.exit (1);
      }
   }

   /**
   *  Hostess gets number of total passengers arrived.
   *
   *  Remote operation.
   *
   * @return total number of passengers that have arrived
   */

  private int getnPassengerArrived()
  {                           
     
     int res;
     try
     { res = aAirport.getnPassengerArrived(); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Hostess remote exception on getnPassengersArrived: " + e.getMessage ());
       System.exit (1);
     }
     return res;
  }

  /**
   *  the hostess awaits for the pilot to indicate that the plane is ready for boarding.
   *
   *  Remote operation.
   *
   */

  private void prepareForPassBoarding()
  {                           
     
     try
     { dAirport.prepareForPassBoarding(); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Hostess remote exception on prepareForPassBoarding: " + e.getMessage ());
       System.exit (1);
     }
  }


     /**
   *  Hostess checks passenger documents.
   *
   *  Remote operation.
   *
   * @return the id of the passenger being checked
   */

  private int checkDocuments()
  {                           
     
     int res;
     try
     { res = dAirport.checkDocuments(); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Hostess remote exception on checkDocuments: " + e.getMessage ());
       System.exit (1);
     }
     return res;
  }

     /**
   *  Hostess awaits a new passenger.
   *
   *  Remote operation.
   * @param passengerID if of the passenger
   *
   * @return true if plane ready to flight, false otherwise
   */

  private boolean waitForNextPassenger(int passengerID)
  {                           
     
     boolean res;
     try
     { res = dAirport.waitForNextPassenger(passengerID); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Hostess remote exception on getnPassengersArrived: " + e.getMessage ());
       System.exit (1);
     }
     return res;
  }

     /**
   *  Hostess informs the plane is ready to take off.
   *
   *  Remote operation.
   *
   */

  private void informPlaneReadyToTakeOff()
  {                           
     
     try
     { dAirport.informPlaneReadyToTakeOff(); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Hostess remote exception on informPlaneReadyToTakeOff: " + e.getMessage ());
       System.exit (1);
     }
  }

     /**
   *  Hostess waits for next flight.
   *
   *  Remote operation.
   *
   */

  private void waitForNextFlight()
  {                           
     
     try
     { dAirport.waitForNextFlight(); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Hostess remote exception on waitForNextFlight: " + e.getMessage ());
       System.exit (1);
     }
  }

  


   

}
