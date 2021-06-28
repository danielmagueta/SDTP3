echo "Transfering data to the Arrival Airport Node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws05.ua.pt 'mkdir -p test/AirLiftTP3'
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws05.ua.pt 'rm -rf test/AirLiftTP3/*'
sshpass -f password scp -o StrictHostKeyChecking=no dirArrivalAirport.zip sd301@l040101-ws05.ua.pt:test/AirLiftTP3
echo "Decompressing data sent to the Arrival airport node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws05.ua.pt 'cd test/AirLiftTP3 ; unzip -uq dirArrivalAirport.zip'
echo "Executing program at the Arrival airport node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws05.ua.pt 'cd test/AirLiftTP3/dirArrivalAirport ; ./arrival_com_d.sh sd301'