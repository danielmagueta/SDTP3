echo "Compiling source code."
javac */*.java */*/*.java
echo "Distributing intermediate code to the different execution environments."
echo "  RMI registry"
rm -rf dirRMIRegistry/interfaces
mkdir -p dirRMIRegistry/interfaces
cp interfaces/*.class dirRMIRegistry/interfaces
echo "  Register Remote Objects"
rm -rf dirRegistry/serverSide dirRegistry/interfaces
mkdir -p dirRegistry/serverSide dirRegistry/serverSide/main dirRegistry/serverSide/objects dirRegistry/interfaces
cp serverSide/main/RegisterRemoteObjectMain.class dirRegistry/serverSide/main
cp serverSide/objects/RegisterRemoteObject.class dirRegistry/serverSide/objects
cp interfaces/Register.class dirRegistry/interfaces
echo "  General Repository of Information"
rm -rf dirGeneralRepos/serverSide dirGeneralRepos/clientSide dirGeneralRepos/interfaces dirGeneralRepos/commInfra
mkdir -p dirGeneralRepos/serverSide dirGeneralRepos/serverSide/main dirGeneralRepos/serverSide/objects dirGeneralRepos/interfaces \
         dirGeneralRepos/clientSide dirGeneralRepos/clientSide/entities dirGeneralRepos/commInfra
cp serverSide/main/GeneralReposMain.class dirGeneralRepos/serverSide/main
cp commInfra/*.class dirGeneralRepos/commInfra
cp serverSide/objects/GeneralRepos.class dirGeneralRepos/serverSide/objects
cp interfaces/Register.class interfaces/GeneralReposInterface.class dirGeneralRepos/interfaces
cp clientSide/entities/*.class dirGeneralRepos/clientSide/entities
echo "  Departure Airport"
rm -rf dirDepartureAirport/serverSide dirDepartureAirport/clientSide dirDepartureAirport/interfaces dirDepartureAirport/commInfra dirDepartureAirport/genclass
mkdir -p dirDepartureAirport/serverSide dirDepartureAirport/serverSide/main dirDepartureAirport/serverSide/objects dirDepartureAirport/interfaces \
         dirDepartureAirport/clientSide dirDepartureAirport/clientSide/entities dirDepartureAirport/commInfra dirDepartureAirport/genclass
cp serverSide/main/DepartureAirportMain.class dirDepartureAirport/serverSide/main
cp serverSide/objects/DepartureAirport.class dirDepartureAirport/serverSide/objects
cp interfaces/*.class dirDepartureAirport/interfaces
cp clientSide/entities/*.class dirDepartureAirport/clientSide/entities
cp commInfra/*.class dirDepartureAirport/commInfra
cp genclass/*.class dirDepartureAirport/genclass
echo "  Plane"
rm -rf dirPlane/serverSide dirPlane/clientSide dirPlane/interfaces dirPlane/commInfra dirPlane/genclass
mkdir -p dirPlane/serverSide dirPlane/serverSide/main dirPlane/serverSide/objects dirPlane/interfaces \
         dirPlane/clientSide dirPlane/clientSide/entities dirPlane/commInfra dirPlane/genclass
cp serverSide/main/PlaneMain.class dirPlane/serverSide/main
cp serverSide/objects/Plane.class dirPlane/serverSide/objects
cp interfaces/*.class dirPlane/interfaces
cp clientSide/entities/*.class dirPlane/clientSide/entities
cp commInfra/*.class dirPlane/commInfra
cp genclass/*.class dirPlane/genclass
echo "  ArrivalAirport"
rm -rf dirArrivalAirport/serverSide dirArrivalAirport/clientSide dirArrivalAirport/interfaces dirArrivalAirport/commInfra dirArrivalAirport/genclass
mkdir -p dirArrivalAirport/serverSide dirArrivalAirport/serverSide/main dirArrivalAirport/serverSide/objects dirArrivalAirport/interfaces \
         dirArrivalAirport/clientSide dirArrivalAirport/clientSide/entities dirArrivalAirport/commInfra dirArrivalAirport/genclass
cp serverSide/main/ArrivalAirportMain.class dirArrivalAirport/serverSide/main
cp serverSide/objects/ArrivalAirport.class dirArrivalAirport/serverSide/objects
cp interfaces/*.class dirArrivalAirport/interfaces
cp clientSide/entities/*.class dirArrivalAirport/clientSide/entities
cp commInfra/*.class dirArrivalAirport/commInfra
cp genclass/*.class dirArrivalAirport/genclass
echo "  Pilot"
rm -rf dirPilot/clientSide dirPilot/interfaces dirPilot/commInfra
mkdir -p dirPilot/commInfra dirPilot/clientSide dirPilot/clientSide/main dirPilot/clientSide/entities \
         dirPilot/interfaces
cp commInfra/SimulPar.class dirPilot/commInfra
cp clientSide/main/PilotMain.class dirPilot/clientSide/main
cp clientSide/entities/Pilot.class clientSide/entities/PilotStates.class dirPilot/clientSide/entities
cp interfaces/*.class dirPilot/interfaces
echo "  Hostess"
rm -rf dirHostess/clientSide dirHostess/interfaces dirHostess/commInfra
mkdir -p dirHostess/commInfra dirHostess/clientSide dirHostess/clientSide/main dirHostess/clientSide/entities \
         dirHostess/interfaces
cp commInfra/SimulPar.class dirHostess/commInfra
cp clientSide/main/HostessMain.class dirHostess/clientSide/main
cp clientSide/entities/Hostess.class clientSide/entities/HostessStates.class dirHostess/clientSide/entities
cp interfaces/*.class dirHostess/interfaces
echo "  Passengers"
rm -rf dirPassenger/clientSide dirPassenger/interfaces dirPassenger/commInfra
mkdir -p dirPassenger/commInfra dirPassenger/clientSide dirPassenger/clientSide/main dirPassenger/clientSide/entities \
         dirPassenger/interfaces
cp commInfra/SimulPar.class dirPassenger/commInfra
cp clientSide/main/PassengerMain.class dirPassenger/clientSide/main
cp clientSide/entities/Passenger.class clientSide/entities/PassengerStates.class dirPassenger/clientSide/entities
cp interfaces/*.class dirPassenger/interfaces
echo "Compressing execution environments."
echo "  RMI registry"
rm -f  dirRMIRegistry.zip
zip -rq dirRMIRegistry.zip dirRMIRegistry
echo "  Register Remote Objects"
rm -f  dirRegistry.zip
zip -rq dirRegistry.zip dirRegistry
echo "  General Repository of Information"
rm -f  dirGeneralRepos.zip
zip -rq dirGeneralRepos.zip dirGeneralRepos
echo "  Departure Airport"
rm -f  dirDepartureAirport.zip
zip -rq dirDepartureAirport.zip dirDepartureAirport
echo "  Plane"
rm -f  dirPlane.zip
zip -rq dirPlane.zip dirPlane
echo "  Arrival Airport"
rm -f  dirArrivalAirport.zip
zip -rq dirArrivalAirport.zip dirArrivalAirport
echo "  Pilot"
rm -f  dirPilot.zip
zip -rq dirPilot.zip dirPilot
echo "  Hostess"
rm -f  dirHostess.zip
zip -rq dirHostess.zip dirHostess
echo "  Passengers"
rm -f  dirPassenger.zip
zip -rq dirPassenger.zip dirPassenger
