echo "Transfering data to the Pilot node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws04.ua.pt 'mkdir -p test/AirLiftTP3'
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws04.ua.pt 'rm -rf test/AirLiftTP3/*'
sshpass -f password scp -o StrictHostKeyChecking=no dirPilot.zip sd301@l040101-ws04.ua.pt:test/AirLiftTP3
echo "Decompressing data sent to the Pilot node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws04.ua.pt 'cd test/AirLiftTP3 ; unzip -uq dirPilot.zip'
echo "Executing program at the Pilot node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws04.ua.pt 'cd test/AirLiftTP3/dirPilot ; ./Pilot_com_d.sh'