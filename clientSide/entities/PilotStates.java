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
public class PilotStates {
   
     /**
   *   The pilot arrives with the plane.
   */

   public static final int AT_TRANSFER_GATES = 0;

  /**
   *   The pilot confirms that the plane is ready for boarding.
   */

   public static final int READY_FOR_BOARDING = 1;

  /**
   *   The pilot awaits for the indication of the hostess that the boarding is complete.
   */

   public static final int WAIT_FOR_BOARDING = 2;

  /**
   *   The pilot takes the plane from the departure airport to the arrival airpot.
   */

   public static final int FLYING_FORWARD = 3;
   
   /**
   *   The pilot awaits for all the passengers to deboard.
   */

   public static final int DEBOARDING = 4;
   
   /**
   *   The pilot takes the plane back to the original departure airport.
   */

   public static final int FLYING_BACK = 5;

  /**
   *   It can not be instantiated.
   */

   private PilotStates ()
   { }
}
