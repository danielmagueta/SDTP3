package clientSide.main;

import clientSide.entities.*;
import serverSide.main.*;
import commInfra.*;
import genclass.GenericIO;
import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;
import interfaces.*;


/**
 *    Client side of the AirLift (hostess).
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI
 */

public class HostessMain
{
  /**
   *  Main method.
   *
   *    @param args runtime arguments
   *        args[0] - name of the platform where is located the RMI registering service
   *        args[1] - port number where the registering service is listening to service requests
   */

  public static void main (String [] args)
  {

    String rmiRegHostName;                                         // name of the platform where is located the RMI registering service
    int rmiRegPortNumb = -1;                                       // port number where the registering service is listening to service requests

    /* getting problem runtime parameters */

    if (args.length != 2)
      { GenericIO.writelnString ("Wrong number of parameters!");
        System.exit (1);
      }
    rmiRegHostName = args[0];
    try
    { rmiRegPortNumb = Integer.parseInt (args[1]);
    }
    catch (NumberFormatException e)
    { GenericIO.writelnString ("args[1] is not a number!");
      System.exit (1);
    }
    if ((rmiRegPortNumb < 4000) || (rmiRegPortNumb >= 65536))
      { GenericIO.writelnString ("args[1] is not a valid port number!");
        System.exit (1);
      }    
  
    /* problem initialization */

    String nameEntryGeneralRepos = "GeneralRepository";             // public name of the general repository object
    GeneralReposInterface reposStub = null;                         // remote reference to the general repository object
    String nameEntryDepartureAirport = "DepartureAirport";          // public name of the departure airport object
    DepartureAirportInterface DepartureAirportStub = null;          // remote reference to the departure airport object
    String nameEntryArrivalAirport = "ArrivalAirport";               // public name of the arrival airport object
    ArrivalAirportInterface ArrivalAirportStub = null;              // remote reference to the arrival airport object
    Registry registry = null;                                       // remote reference for registration in the RMI registry service
    Hostess hostess;                                                // Hostess thread
  
    try
    { registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("RMI registry creation exception: " + e.getMessage ());
      e.printStackTrace ();
      System.exit (1);
    }
    
    try
    { reposStub = (GeneralReposInterface) registry.lookup (nameEntryGeneralRepos);
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("GeneralRepos lookup exception: " + e.getMessage ());
      e.printStackTrace ();
      System.exit (1);
    }
    catch (NotBoundException e)
    { GenericIO.writelnString ("GeneralRepos not bound exception: " + e.getMessage ());
      e.printStackTrace ();
      System.exit (1);
    }

    try
    { DepartureAirportStub = (DepartureAirportInterface) registry.lookup (nameEntryDepartureAirport);
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("Departure Airport lookup exception: " + e.getMessage ());
      e.printStackTrace ();
      System.exit (1);
    }
    catch (NotBoundException e)
    { GenericIO.writelnString ("Departure Airport not bound exception: " + e.getMessage ());
      e.printStackTrace ();
      System.exit (1);
    }
  
    try
    { ArrivalAirportStub = (ArrivalAirportInterface) registry.lookup (nameEntryArrivalAirport);
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("Arrival Airport lookup exception: " + e.getMessage ());
      e.printStackTrace ();
      System.exit (1);
    }
    catch (NotBoundException e)
    { GenericIO.writelnString ("Arrival Airport not bound exception: " + e.getMessage ());
      e.printStackTrace ();
      System.exit (1);
    }

    hostess = new Hostess("Hostess", DepartureAirportStub, ArrivalAirportStub);

    /* start of the simulation */

    hostess.start ();
    

    /* waiting for the end of the simulation */

    GenericIO.writelnString ();
    try
    { hostess.join ();
    }
    catch (InterruptedException e) {}
    GenericIO.writelnString ("The hostess has terminated.");
    
    GenericIO.writelnString();

    try
    { DepartureAirportStub.shutdown ();
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("Hostess remote exception on Departure Airport shutdown: " + e.getMessage ());
      System.exit (1);
    }

    try
    { ArrivalAirportStub.shutdown ();
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("Hostess remote exception on Arrival Airport shutdown: " + e.getMessage ());
      System.exit (1);
    }

    try
    { reposStub.shutdown ();
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("Hostess remote exception on General Repository shutdown:: " + e.getMessage ());
      System.exit (1);
    }

  }
}
