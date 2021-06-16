/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide.entities;
import clientSide.stubs.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *   Pilot thread.
 *
 *   It simulates the pilot life cycle.
 *   Static solution.
 */

public class Pilot extends Thread{
    
    
     /**
   *  Pilot state.
   */

   private int pilotState;
   
   /**
   *  Reference to the departure airport.
   */

   private final DepartureAirportStub dAirport;
   
   /**
   *  Reference to the plane.
   */

   private final PlaneStub plane;
   
   /**
   *  Reference to the arrival airport.
   */

   private final ArrivalAirportStub aAirport;

   /**
   *   Instantiation of a pilot thread.
   *
   *     @param name thread name
   *     @param dAirport reference to the departure airport
   *     @param plane reference to the plane
   *     @param aAirport reference to the arrival airport
   */

   public Pilot (String name, DepartureAirportStub dAirport, PlaneStub plane, ArrivalAirportStub aAirport)
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
       
       while ( !(aAirport.getnPassengerArrived() == SimulPar.N))                                 // check for end of operations
       { 
         dAirport.informPlaneReadyForBoarding ();               // the pilot informs the plane is ready for boarding  
         dAirport.waitForAllInBoard ();                         // the pilot awaits for all the passenger to be in board
         plane.flyToDestinationPoint ();                        // the pilot flies to destination point
         plane.announceArrival();                               // the pilot awaits for all the passengers to deboard the plane 
         while(plane.getnINF() != aAirport.getnOut()){
             GenericIO.writeString("      ");
         }         //wait for all the passengers to deboard
         plane.setnINF(0);
         aAirport.setnOut(0);
         plane.flyToDeparturePoint ();                          // the pilot flies to departure point
         dAirport.parkAtTransferGate();                         //the pilot parks the plane in the departure airport
         if(aAirport.getnPassengerArrived() == SimulPar.N)
         {
             dAirport.endHostess();                             //unblock the hostess thread so she can end
         }
       }
    }

   
}