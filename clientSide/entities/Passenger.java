/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide.entities;

import genclass.GenericIO;
import commInfra.*;
import interfaces.*;

/**
 *   Passenger thread.
 *
 *   It simulates the passenger life cycle.
 *   Static solution.
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

   private final DepartureInterface dAirport;
   
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
   *     @param dAirport reference to the departure airport
   *     @param plane reference to the plane
   *     @param aAirport reference to the arrival airport
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
       dAirport.waitInQueue ();                              // the passenger enters the queue to be checked by the hostess
       dAirport.showDocuments ();                            // the passenger show docuemnts to hostess
       plane.boardThePlane();                                // the passenger boards the plane
       plane.waitForEndOfFligh();                            // the passenger awaits the end of the flight
       aAirport.leaveThePlane();                             // the passenger leaves the plane at the destination point   
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
 

}