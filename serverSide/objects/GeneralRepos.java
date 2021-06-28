package serverSide.objects;

import java.rmi.registry.*;
import java.rmi.*;
import interfaces.*;
import serverSide.main.*;
import clientSide.entities.*;
import genclass.GenericIO;
import genclass.TextFile;
import java.util.Objects;
import commInfra.*;


/**
 *  General Repository.
 *
 *    It is responsible to keep the visible internal state of the problem and to provide means for it
 *    to be printed in the logging file.
 *    It is implemented using semaphores for synchronization.
 *    All public methods are executed in mutual exclusion.
 *    There are no internal synchronization points.
 */

public class GeneralRepos implements GeneralReposInterface
{
  /**
   *  Name of the logging file.
   */

   private String logFileName;

  /**
   *  Number of passengers presently forming a queue to board the plane.
   */

   private int InQ;
   
  /**
   *  Number of passengers in the plane.
   */

   private int InF;
   
  /**
   *  Number of passengers that have already arrived at their destination.
   */

   private int PTAL;
   
   /**
   *  Number of the flight.
   */

   private int nFlight;

  /**
   *  State of the pilot.
   */

   private int pilotState;
   
  /**
   *  State of the hostess.
   */

   private int hostessState; 

  /**
   *  State of the passengers.
   */

   private final int [] passengerState;

  /**
   *  Semaphore to ensure mutual exclusion on the execution of public methods.
   */

   private final Semaphore access;
   
  /**
   *   Number of entity groups requesting the shutdown.
   */

   private int nEntities;

  /**
   *   Instantiation of a general repository object.
   *
   *     @param logFileName name of the logging file
   */

   public GeneralRepos ()
   {
      logFileName = "logger";
      nEntities = 0;
      pilotState = PilotStates.AT_TRANSFER_GATES;
      hostessState = HostessStates.WAIT_FOR_NEXT_FLIGHT;
      passengerState = new int[SimulPar.N]; 
      for (int i = 0; i < SimulPar.N; i++)
        passengerState[i] = PassengerStates.GOING_TO_AIRPORT;
      InQ = 0;
      InF = 0;
      PTAL = 0;
      nFlight = 1;
      access = new Semaphore ();
      access.up ();
   }
   
   /**
   *   Operation initialization of simulation.
   *
   *
   *     @param logFileName name of the logging file
   *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   */

  public void initSimul (String logFileName) throws RemoteException
  {
     if (!Objects.equals (logFileName, ""))
        this.logFileName = logFileName;
     reportInitialStatus ();
  }
   
  
  
   /**
   *   @return number of passengers in queue.
   *   @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   *
   */
   
    public int getInQ() throws RemoteException{
        return InQ;
    }
    
   /**
   *   
   *   @return number of passengers in flight.
   *   @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   */
    public int getInF() throws RemoteException{
        return InF;
    }
    
   /**
   *   @return numbr of passengers that have arrived.
   *   @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   *
   */
    public int getPTAL() throws RemoteException{
        return PTAL;
    }
   
 
   
  /**
   *   Add one to the numbers of passengers in queue.
   *   @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   *
   */

   public void addInQ () throws RemoteException
   {
      access.down ();                                      // enter critical region
      InQ +=1;
      access.up ();                                        // exit critical region
   }
   
    /**
   *   Subtract one to the numbers of passengers in queue.
   *   @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   *
   */

   public void subtractInQ () throws RemoteException
   {
      access.down ();                                      // enter critical region
      InQ -=1;
      access.up ();                                        // exit critical region
   }
   
  /**
   *   Add one to the numbers of passengers in flight.
   *   @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   *
   */

   public void addInF () throws RemoteException
   {
      access.down ();                                      // enter critical region
      InF +=1;
      access.up ();                                        // exit critical region
   }
   
   /**
   *   Subtract one to the numbers of passengers in flight.
   *   @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   *
   */

   public void subtractInF () throws RemoteException
   {
      access.down ();                                      // enter critical region
      InF -=1;
      access.up ();                                        // exit critical region
   }
   
  /**
   *   Add one to the numbers of passengers that arrived at destination.
   *   @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   *
   */
   public void addPTAL () throws RemoteException
   {
      access.down ();                                      // enter critical region
      PTAL +=1;
      access.up ();                                        // exit critical region
   }
   

  /**
   *   Set pilot state.
   *
   *     @param pilot pilot
   *     @param state pilot state
   *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   */

   public void setPilotState (int state) throws RemoteException
   {
      access.down ();                                      // enter critical region
      pilotState = state;
      reportStatus ();
      access.up ();                                        // exit critical region
   }
   
  /**
   *   Set hostess state.
   *
   *     @param hostess hostess
   *     @param state hostess state
   *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   */

   public void setHostessState (int state) throws RemoteException
   {
      access.down ();                                      // enter critical region
      hostessState = state;
      reportStatus ();
      access.up ();                                        // exit critical region
   }

  /**
   *   Set h state.
   *
   *     @param id passenger id
   *     @param state passenger state
   *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   */

   public void setPassengerState (int id, int state) throws RemoteException
   {
      access.down ();                                      // enter critical region
      passengerState[id] = state;
      reportStatus ();
      access.up ();                                        // exit critical region
   }


  /**
   *  Write the header to the logging file.
   *
   *  The passengers are going to the airport, the hostess is waiting for next flight and the pilot is at the transfer gate.
   *  Internal operation.
   */

   private void reportInitialStatus ()
   {
      TextFile log = new TextFile ();                      // instantiation of a text file handler

      if (!log.openForWriting (".", logFileName))
         { GenericIO.writelnString ("The operation of creating the file " + logFileName + " failed!");
           System.exit (1);
         }
      log.writelnString ("                                                   AirLift - Description of the internal state");
      log.writelnString ("\n");
      log.writelnString ("  PT    HT    P00   P01   P02   P03   P04   P05   P06   P07   P08   P09   P10   P11   P12   P13   P14   P15   P16   P17   P18   P19   P20   InQ   InF   PTAL");
      if (!log.close ())
         { GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
           System.exit (1);
         }
      reportStatus ();
   }

  /**
   *  Write a state line at the end of the logging file.
   *
   *  The current state of the barbers and the customers is organized in a line to be printed.
   *  Internal operation.
   */

   private void reportStatus ()
   {
      TextFile log = new TextFile ();                      // instantiation of a text file handler

      String lineStatus = "";                              // state line to be printed

      if (!log.openForAppending (".", logFileName))
         { GenericIO.writelnString ("The operation of opening for appending the file " + logFileName + " failed!");
           System.exit (1);
         }
      switch (pilotState)
        { case PilotStates.AT_TRANSFER_GATES:   lineStatus += " ATRG ";
                                        break;
          case PilotStates.READY_FOR_BOARDING: lineStatus += " RDFB ";
                                        break;
          case PilotStates.WAIT_FOR_BOARDING: lineStatus += " WTFB ";
                                        break;
          case PilotStates.FLYING_FORWARD: lineStatus += " FLFW ";
                                        break;
          case PilotStates.DEBOARDING: lineStatus += " DRPP ";
                                        break;
          case PilotStates.FLYING_BACK: lineStatus += " FLBK ";
                                        break;
        }
      
      switch (hostessState)
        { case HostessStates.WAIT_FOR_NEXT_FLIGHT:   lineStatus += " WTFL ";
                                        break;
          case HostessStates.WAIT_FOR_PASSENGER:   lineStatus += " WTPS ";
                                        break;
          case HostessStates.CHECK_PASSENGER:   lineStatus += " CKPS ";
                                        break;
          case HostessStates.READY_TO_FLY:   lineStatus += " RDTF ";
                                        break;
        }
      
      for (int i = 0; i < SimulPar.N; i++)
        switch (passengerState[i])
        { case PassengerStates.GOING_TO_AIRPORT:   lineStatus += " GTAP ";
                                        break;
          case PassengerStates.IN_QUEUE:   lineStatus += " INQE ";
                                        break;
          case PassengerStates.IN_FLIGHT:   lineStatus += " INFL ";
                                        break;
          case PassengerStates.AT_DESTINATION:   lineStatus += " ATDS ";
                                        break;
        }
      
      lineStatus += "    " + InQ + "     " + InF + "      " + PTAL;
      log.writelnString (lineStatus);
      if (!log.close ())
         { GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
           System.exit (1);
         }
   }
   
   
  /**
   *  Report that the flight boarding started.
   *  Internal operation.
   *  @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   */

   public void reportBoarding () throws RemoteException
   {
      TextFile log = new TextFile ();                      // instantiation of a text file handler

      if (!log.openForAppending(".", logFileName))
         { GenericIO.writelnString ("The operation of creating the file " + logFileName + " failed!");
           System.exit (1);
         }
      log.writelnString ("\n");
      log.writelnString (" Flight " + nFlight + ": boarding started.");
      if (!log.close ())
         { GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
           System.exit (1);
         }
   }
   
  /**
   *  Report that the passenger checked his/her documents.
   *  Internal operation.
   *     @param id passenger id
   *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   */

   public void reportCheck (int id) throws RemoteException
   {
      TextFile log = new TextFile ();                      // instantiation of a text file handler

      if (!log.openForAppending(".", logFileName))
         { GenericIO.writelnString ("The operation of creating the file " + logFileName + " failed!");
           System.exit (1);
         }
      log.writelnString ("\n");
      log.writelnString ("Flight " + nFlight + ": passenger " + id +  " checked.");
      if (!log.close ())
         { GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
           System.exit (1);
         }
   }
   
     /**
   *  Report that the flight has departed.
   *  Internal operation.
   *  @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   */

   public void reportDeparted () throws RemoteException
   {
      TextFile log = new TextFile ();                      // instantiation of a text file handler

      if (!log.openForAppending(".", logFileName))
         { GenericIO.writelnString ("The operation of creating the file " + logFileName + " failed!");
           System.exit (1);
         }
      log.writelnString ("\n");
      log.writelnString ("Flight " + nFlight + ": departed with " + InF +  " passengers.");
      if (!log.close ())
         { GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
           System.exit (1);
         }
   }
   
  /**
   *  Report that the flight has arrived at arrival airport.
   *  Internal operation.
   *  @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   */

   public void reportArrived () throws RemoteException
   {
      TextFile log = new TextFile ();                      // instantiation of a text file handler

      if (!log.openForAppending(".", logFileName))
         { GenericIO.writelnString ("The operation of creating the file " + logFileName + " failed!");
           System.exit (1);
         }
      log.writelnString ("\n");
      log.writelnString ("Flight " + nFlight + ": arrived.");
      if (!log.close ())
         { GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
           System.exit (1);
         }
   }
   
     /**
   *  Report that the flight is returning to the initial airport.
   *  Internal operation.
   *  @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   */

   public void reportreturning () throws RemoteException
   {
      TextFile log = new TextFile ();                      // instantiation of a text file handler

      if (!log.openForAppending (".", logFileName))
         { GenericIO.writelnString ("The operation of creating the file " + logFileName + " failed!");
           System.exit (1);
         }
      log.writelnString ("\n");
      log.writelnString ("Flight " + nFlight + ": returning.");
      if (!log.close ())
         { GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
           System.exit (1);
         }
      nFlight++;
   }
   
  /**
   *   Operation server shutdown.
   *   @throws RemoteException if either the invocation of the remote method, or the communication with the registry
   *                             service fails
   *
   */

   public void shutdown () throws RemoteException
   {
       nEntities += 1;
       if (nEntities >= 3)
          GeneralReposMain.waitConnection = false;
   }
   
}
