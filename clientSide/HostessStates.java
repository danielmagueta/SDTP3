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
public class HostessStates {
     /**
   *   The hostess is waiting for a plane to be ready to boarding.
   */

   public static final int WAIT_FOR_NEXT_FLIGHT = 0;

  /**
   *   The hostess waits for a passenger to check his documents.
   */

   public static final int WAIT_FOR_PASSENGER = 1;

  /**
   *   The hostess check the passenger documents.
   */

   public static final int CHECK_PASSENGER = 2;

  /**
   *   The hostess is ready to fly.
   */

   public static final int READY_TO_FLY = 3;

  /**
   *   It can not be instantiated.
   */

   private HostessStates ()
   { }
}