echo "Transfering data to the Departure Airport Node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws07.ua.pt 'mkdir -p test/AirLiftTP3'
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws07.ua.pt 'rm -rf test/AirLiftTP3/*'
sshpass -f password scp -o StrictHostKeyChecking=no dirDepartureAirport.zip sd301@l040101-ws07.ua.pt:test/AirLiftTP3
echo "Decompressing data sent to the departure airport node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws07.ua.pt 'cd test/AirLiftTP3 ; unzip -uq dirDepartureAirport.zip'
echo "Executing program at the departure airport node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws07.ua.pt 'cd test/AirLiftTP3/dirDepartureAirport ; ./departure_com_d.sh sd301'
