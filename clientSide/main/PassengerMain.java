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
 *    Client side of the AirLift (passengers).
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI
 */

public class PassengerMain
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
    String nameEntryPlane = "Plane";                                // public name of the plane object
    PlaneInterface PlaneStub = null;                                // remote reference to the plane object
    String nameEntryArrivalAirport = "ArrivalAirport";              // public name of the arrival airport object
    ArrivalAirportInterface ArrivalAirportStub = null;              // remote reference to the arrival airport object
    Registry registry = null;                                       // remote reference for registration in the RMI registry service
    Passenger [] passenger = new Passenger [SimulPar.N];            // array of passenger threads
  
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
    { PlaneStub = (PlaneInterface) registry.lookup (nameEntryPlane);
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("Plane lookup exception: " + e.getMessage ());
      e.printStackTrace ();
      System.exit (1);
    }
    catch (NotBoundException e)
    { GenericIO.writelnString ("Plane not bound exception: " + e.getMessage ());
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


    for (int i = 0; i < SimulPar.N; i++)
      passenger[i] = new Passenger ("Passenger_" + (i+1), i, DepartureAirportStub, PlaneStub, ArrivalAirportStub);

    /* start of the simulation */

    for (int i = 0; i < SimulPar.N; i++)
        passenger[i].start();
    

    /* waiting for the end of the simulation */
    
    GenericIO.writelnString ();

    for (int i = 0; i < SimulPar.N; i++)
      { try
        { passenger[i].join ();
        }
        catch (InterruptedException e) {}
        GenericIO.writelnString ("The passenger " + (i+1) + " has terminated.");
      }
      GenericIO.writelnString ();

    try
    { DepartureAirportStub.shutdown ();
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("Passenger remote exception on Departure Airport shutdown: " + e.getMessage ());
      System.exit (1);
    }

    try
    { PlaneStub.shutdown ();
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("Passenger remote exception on Plane shutdown: " + e.getMessage ());
      System.exit (1);
    }

    try
    { ArrivalAirportStub.shutdown ();
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("Passenger remote exception on Arrival Airport shutdown: " + e.getMessage ());
      System.exit (1);
    }

    try
    { reposStub.shutdown ();
    }
    catch (RemoteException e)
    { GenericIO.writelnString ("Passenger remote exception on General Repository shutdown:: " + e.getMessage ());
      System.exit (1);
    }

  }
}
