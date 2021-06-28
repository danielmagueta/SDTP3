echo "Transfering data to the Plane Node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws06.ua.pt 'mkdir -p test/AirLiftTP3'
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws06.ua.pt 'rm -rf test/AirLiftTP3/*'
sshpass -f password scp -o StrictHostKeyChecking=no dirPlane.zip sd301@l040101-ws06.ua.pt:test/AirLiftTP3
echo "Decompressing data sent to the Plane node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws06.ua.pt 'cd test/AirLiftTP3 ; unzip -uq dirPlane.zip'
echo "Executing program at the Plane node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws06.ua.pt 'cd test/AirLiftTP3/dirPlane ; ./plane_com_d.sh sd301'