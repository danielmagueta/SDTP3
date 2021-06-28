echo "Transfering data to the passenger node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws02.ua.pt 'mkdir -p test/AirLiftTP3'
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws02.ua.pt 'rm -rf test/AirLiftTP3/*'
sshpass -f password scp -o StrictHostKeyChecking=no dirPassenger.zip sd301@l040101-ws02.ua.pt:test/AirLiftTP3
echo "Decompressing data sent to the passenger node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws02.ua.pt 'cd test/AirLiftTP3 ; unzip -uq dirPassenger.zip'
echo "Executing program at the passenger node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws02.ua.pt 'cd test/AirLiftTP3/dirPassenger ; ./passenger_com_d.sh'
