/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide.entities;

import genclass.GenericIO;
import commInfra.*;
import interfaces.*;
import java.rmi.*;


/**
 *   Passenger thread.
 *
 *   It simulates the passenger life cycle.
 *   Implementation of a client-server model of type 2 (server replication).
 *   Communication is based on remote calls under Java RMI.
 */

public class Passenger extends Thread{
    
    /**
   *  Passenger identification.
   */
    
    private int passengerId;
    
     /**
   *  Passenger state.
   */

   private int passengerState;
   
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
   *   Instantiation of a passenger thread.
   *
   *     @param name thread name
   *     @param passengerId passenger id
   *     @param dAirport remote reference to the departure airport
   *     @param plane remote reference to the plane
   *     @param aAirport remote reference to the arrival airport
   */

   public Passenger (String name, int passengerId, DepartureAirportInterface dAirport, PlaneInterface plane, ArrivalAirportInterface aAirport)
   {
      super (name);
      this.passengerId = passengerId;
      passengerState = PassengerStates.GOING_TO_AIRPORT;
      this.dAirport = dAirport;
      this.plane = plane;
      this.aAirport = aAirport;
   }
   
    /**
     *   Set passenger id.
     *
     *     @param id passenger id
     */

    public void setPassengerId(int id) {
        this.passengerId = id;
    }

    /**
     *   Get passenger id.
     *
     *     @return passenger id
     */

    public int getPassengerId() {
        return passengerId;
     }

    /**
     *   Set passenger state.
     *
     *     @param state new passenger state
     */

    public void setPassengerState(int state) {
        this.passengerState = state;
     }

    /**
     *   Get passenger state.
     *
     *     @return passenger state
     */

    public int getPassengerState() {
        return passengerState;
     }

    /**
     *   Life cycle of the passenger.
     */
    
    @Override
    public void run ()
    {
       
       travelToAirport ();                                   // the passenger travels to airport
       waitInQueue ();                              // the passenger enters the queue to be checked by the hostess
       showDocuments ();                            // the passenger show docuemnts to hostess
       boardThePlane();                                // the passenger boards the plane
       waitForEndOfFligh();                            // the passenger awaits the end of the flight
       leaveThePlane();                             // the passenger leaves the plane at the destination point   
    }
    
   /**
    *  Passenger going to the departure airport.
    *
    *  Internal operation.
    */

    private void travelToAirport ()
    {
        try
        { sleep ((long) (1 + 200 * Math.random ()));
        }
        catch (InterruptedException e) {}
    }

    /**
   *  Passenger wait in queue
   *
   *  Remote operation.
   *
   */

  private void waitInQueue()
  {                           

     try
     { dAirport.waitInQueue(); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Passenger " + passengerId + " remote exception on waitInQueue: " + e.getMessage ());
       System.exit (1);
     }

  }
 
      /**
   *  Passenger show documents
   *
   *  Remote operation.
   *
   */

  private void showDocuments()
  {                           

     try
     { dAirport.showDocuments(); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Passenger " + passengerId + " remote exception on showDocuments: " + e.getMessage ());
       System.exit (1);
     }

  }

        /**
   *  Passenger board the plane
   *
   *  Remote operation.
   *
   */

  private void boardThePlane()
  {                           

     try
     { plane.boardThePlane(); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Passenger " + passengerId + " remote exception on boardThePlane: " + e.getMessage ());
       System.exit (1);
     }

  }

        /**
   *  Passenger wait for end of flight
   *
   *  Remote operation.
   *
   */

  private void waitForEndOfFligh()
  {                           

     try
     { plane.waitForEndOfFligh(); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Passenger " + passengerId + " remote exception on waitForEndOfFlight: " + e.getMessage ());
       System.exit (1);
     }

  }

        /**
   *  Passenger leave the plane
   *
   *  Remote operation.
   *
   */

  private void leaveThePlane()
  {                           

     try
     { aAirport.leaveThePlane(); 
     }
     catch (RemoteException e)
     { GenericIO.writelnString ("Passenger " + passengerId + " remote exception on leaveThePlane: " + e.getMessage ());
       System.exit (1);
     }

  }

}