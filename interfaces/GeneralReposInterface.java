package interfaces;

import java.rmi.*;

/**
 *   Operational interface of a remote object of type GeneralRepos.
 *
 *     It provides the functionality to access the General Repository of Information.
 */

public interface GeneralReposInterface extends Remote
{
  

   /**
   *   Operation initialization of simulation.
   *
   *
   *     @param logFileName name of the logging file
   */

    public void initSimul (String logFileName) throws RemoteException;


   /**
   *   @return number of passengers in queue.
   *
   */
   
    public int getInQ() throws RemoteException;

   /**
   *   
   *   @return number of passengers in flight.
   */
    public int getInF() throws RemoteException;
    
   /**
   *   @return number of passengers that have arrived.
   *
   */
    public int getPTAL() throws RemoteException;
   

  /**
   *   Add one to the numbers of passengers in queue.
   *
   */

   public void addInQ () throws RemoteException;
   
    /**
   *   Subtract one to the numbers of passengers in queue.
   *
   */

   public void subtractInQ () throws RemoteException;
   
  /**
   *   Add one to the numbers of passengers in flight.
   *
   */

   public void addInF () throws RemoteException;
   
   /**
   *   Subtract one to the numbers of passengers in flight.
   *
   */

   public void subtractInF () throws RemoteException;
   
  /**
   *   Add one to the numbers of passengers that arrived at destination.
   *
   */
   public void addPTAL () throws RemoteException;

  /**
   *   Set pilot state.
   *
   *     @param pilot pilot
   *     @param state pilot state
   */

   public void setPilotState (int state) throws RemoteException;
   
  /**
   *   Set hostess state.
   *
   *     @param hostess hostess
   *     @param state hostess state
   */

   public void setHostessState (int state) throws RemoteException;

  /**
   *   Set h state.
   *
   *     @param id passenger id
   *     @param state passenger state
   */

   public void setPassengerState (int id, int state) throws RemoteException;


  /**
   *  Report that the flight boarding started.
   *  Internal operation.
   */

   public void reportBoarding () throws RemoteException;
   
  /**
   *  Report that the passenger checked his/her documents.
   *  Internal operation.
   *     @param id passenger id
   */

   public void reportCheck (int id) throws RemoteException;
   
     /**
   *  Report that the flight has departed.
   *  Internal operation.
   */

   public void reportDeparted () throws RemoteException;
   
  /**
   *  Report that the flight has arrived at arrival airport.
   *  Internal operation.
   */

   public void reportArrived () throws RemoteException;
     /**
   *  Report that the flight is returning to the initial airport.
   *  Internal operation.
   */

   public void reportreturning () throws RemoteException;
   
  /**
   *   Operation server shutdown.
   *
   */

   public void shutdown () throws RemoteException;
   
}
