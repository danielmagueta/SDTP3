/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide.entities;

/**
 *
 * @author danie
 */
public class PassengerStates {
    /**
   *   The passenger is going to the departure airport.
   */

   public static final int GOING_TO_AIRPORT = 0;

  /**
   *   The passenger queue at the boarding gate waiting for the hostess to check documents.
   */

   public static final int IN_QUEUE = 1;

  /**
   *   The passenger is inside the plane during the flight.
   */

   public static final int IN_FLIGHT = 2;

  /**
   *   The passener arrived at the destination airport.
   */

   public static final int AT_DESTINATION = 3;

  /**
   *   It can not be instantiated.
   */

   private PassengerStates ()
   { }
}
