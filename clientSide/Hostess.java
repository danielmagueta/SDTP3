/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide.entities;
import clientSide.stubs.*;
import commInfra.*;


/**
 *   Hostess thread.
 *
 *   It simulates the hostess life cycle.
 *   Static solution.
 */

public class Hostess extends Thread{

    
     /**
   *  Hostess state.
   */

   private int hostessState;
   
  /**
   *  Reference to the departure airport.
   */

   private final DepartureAirportStub dAirport;
  

   
     /**
   *  Reference to the arrival airport.
   */

   private final ArrivalAirportStub aAirport;
  

   

   /**
   *   Instantiation of a hostess thread.
   *
   *     @param name thread name
   *     @param dAirport reference to the departure airport
   *     @param aAirport reference to the arrival airport
   */

   public Hostess (String name, DepartureAirportStub dAirport, ArrivalAirportStub aAirport)
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
       dAirport.waitPilot();                                             //for the initial lock of the hostess
        while (!(aAirport.getnPassengerArrived() == SimulPar.N))      // check for end of operations
       { 
         dAirport.prepareForPassBoarding();                            // the hostess awaits for the pilot to indicate that the plane is ready for boarding
         int passengerID;
         boolean takeOff = false;
         while(!takeOff){
            passengerID = dAirport.checkDocuments();                    // the hostess checks the passenger documents
            takeOff = dAirport.waitForNextPassenger(passengerID);                // the hostess awaits for the next passanger
         }
         dAirport.informPlaneReadyToTakeOff();                         // the hostess informs the pilot that the plane is ready to take off
         dAirport.waitForNextFlight();                                 // the hostess awaits for the next flight

       }
    }
    
   

}
