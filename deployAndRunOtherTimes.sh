xterm  -T "General Repository" -hold -e "./GeneralReposDeployAndRun.sh" &
sleep 2
xterm  -T "Departure Airport" -hold -e "./DepartureAirportDeployAndRun.sh" &
sleep 2
xterm  -T "Plane" -hold -e "./PlaneDeployAndRun.sh" &
sleep 2
xterm  -T "Arrival Airport" -hold -e "./ArrivalAirportDeployAndRun.sh" &
sleep 2
xterm  -T "Pilot" -hold -e "./PilotDeployAndRun.sh" &
xterm  -T "Hostess" -hold -e "./HostessDeployAndRun.sh" &
xterm  -T "Passenger" -hold -e "./PassengerDeployAndRun.sh" &