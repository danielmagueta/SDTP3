/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide.entities;
import clientSide.stubs.*;
import commInfra.*;
import genclass.GenericIO;
import java.rmi.*;
import interfaces.*;


/**
 *   Pilot thread.
 *
 *   It simulates the pilot life cycle.
 *   Implementation of a client-server model of type 2 (server replication).
 *   Communication is based on remote calls under Java RMI.
 */

public class Pilot extends Thread{
    
    
     /**
   *  Pilot state.
   */

   private int pilotState;
   
   /**
   *  Reference to the departure airport.
   */

   private final DepartureAirportInterface dAirport;
   
   /**
   *  Reference to the plane.
   */

   private final PlaneInterface plane;
   
   /**
   *  Reference to the arrival airport.
   */

   private final ArrivalAirportInterface aAirport;

   /**
   *   Instantiation of a pilot thread.
   *
   *     @param name thread name
   *     @param dAirport remote reference to the departure airport
   *     @param plane remote reference to the plane
   *     @param aAirport remote reference to the arrival airport
   */

   public Pilot (String name, DepartureAirportInterface dAirport, PlaneInterface plane, ArrivalAirportInterface aAirport)
   {
      super (name);
      pilotState = PilotStates.AT_TRANSFER_GATES;
      this.dAirport = dAirport;
      this.plane = plane;
      this.aAirport = aAirport;
   }
   
    /**
     *   Set pilot state.
     *
     *     @param state new pilot state
     */

    public void setPilotState(int state) {
        this.pilotState = state;
     }

    /**
     *   Get pilot state.
     *
     *     @return passenger state
     */

    public int getPilotState() {
        return pilotState;
     }

    /**
     *   Life cycle of the pilot.
     */
    
    @Override
    public void run ()
    {
       
       while ( !(getnPassengerArrived() == SimulPar.N))                                 // check for end of operations
       { 
         informPlaneReadyForBoarding ();               // the pilot informs the plane is ready for boarding  
         waitForAllInBoard ();                         // the pilot awaits for all the passenger to be in board
         flyToDestinationPoint ();                        // the pilot flies to destination point
         announceArrival();                               // the pilot awaits for all the passengers to deboard the plane 
         while(getnINF() != getnOut()){
             GenericIO.writeString("      ");
         }         //wait for all the passengers to deboard
         setnINF(0);
         setnOut(0);
         flyToDeparturePoint ();                          // the pilot flies to departure point
         parkAtTransferGate();                         //the pilot parks the plane in the departure airport
         if(getnPassengerArrived() == SimulPar.N)
         {
             dAirport.endHostess();                             //unblock the hostess thread so she can end
         }
       }
    }


/**
   *  Pilot gets number of total passengers arrived.
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
     { GenericIO.writelnString ("Pilot remote exception on getnPassengersArrived: " + e.getMessage ());
       System.exit (1);
     }
     return res;
  }

  /**
   *  Pilot inform plane is ready for boarding.
   *
   *  Remote operation.
   *
   */

  private void informPlaneReadyForBoarding()
  {                           
     
     try
     { dAirport.informPlaneReadyForBoarding(); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Pilot remote exception on informPlaneReadyForBoarding: " + e.getMessage ());
       System.exit (1);
     }

  }

  /**
   *  Pilot waits for all to be aboard.
   *
   *  Remote operation.
   *
   */

  private int waitForAllInBoard()
  {                           
     
     try
     { dAirport.waitForAllInBoard(); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Pilot remote exception on waitForAllInBoard: " + e.getMessage ());
       System.exit (1);
     }

  }

  /**
   *  Pilot fly to destination point
   *
   *  Remote operation.
   *
   */

  private void flyToDestinationPoint()
  {                           
     
     try
     { plane.flyToDestinationPoint(); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Pilot remote exception on flyToDestinationPoint: " + e.getMessage ());
       System.exit (1);
     }
  }

  /**
   *  Pilot announce arrival and waits for all passengers to deboard
   *
   *  Remote operation.
   *
   */

  private void announceArrival()
  {                           

     try
     { plane.announceArrival(); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Pilot remote exception on announceArrival: " + e.getMessage ());
       System.exit (1);
     }
  }

  /**
   *  Pilot number of passenger in flight.
   *
   *  Remote operation.
   *
   * @return total number of passengers in flight
   */

  private int getnINF()
  {                           
     
     int res;
     try
     { res = plane.getnINF(); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Pilot remote exception on getnINF: " + e.getMessage ());
       System.exit (1);
     }
     return res;
  }

  /**
   *  Pilot gets number of passengers that have deboarded in this flight
   *
   *  Remote operation.
   *
   * @return total number of passengers that have deboarded in this flight
   */

  private int getnOut()
  {                           
     
     int res;
     try
     { res = aAirport.getnOut(); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Pilot remote exception on getnPassengersArrived: " + e.getMessage ());
       System.exit (1);
     }
     return res;
  }

  /**
   *  Pilot sets number of passengers in flight to num 
   *
   *  Remote operation.
   *
   * @param num number of passengers in flight
   */

  private void setnINF(int num)
  {                           
     
     try
     { plane.setnINF(num); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Pilot remote exception on setnINF: " + e.getMessage ());
       System.exit (1);
     }

  }

  /**
   *  Pilot sets number of passengers that deboarded to num
   *
   *  Remote operation.
   *
   * @param num number of passengers that deboarded
   */

  private void setnOut(int num)
  {                           
     
     int res;
     try
     { aAirport.setnOut(num); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Pilot remote exception on setnOut: " + e.getMessage ());
       System.exit (1);
     }
  }

  /**
   *  Pilot fly to departure point
   *
   *  Remote operation.
   *
   */

  private void flyToDeparturePoint()
  {                           
     
     try
     { plane.flyToDeparturePoint(); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Pilot remote exception on flyToDeparturePoint: " + e.getMessage ());
       System.exit (1);
     }
  }

    /**
   *  Pilot parks at transfer gate.
   *
   *  Remote operation.
   *
   */

  private void parkAtTransferGate()
  {                           
     
     try
     { dAirport.parkAtTransferGate(); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Pilot remote exception on parkAtTrasnferGate: " + e.getMessage ());
       System.exit (1);
     }

  }

  /**
   *  Pilot tells hostess thread it can terminate
   *
   *  Remote operation.
   *
   */

  private void endHostess()
  {                           
     
     try
     { dAirport.endHostess(); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Pilot remote exception on endHostess: " + e.getMessage ());
       System.exit (1);
     }

  }


   
}