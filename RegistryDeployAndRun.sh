echo "Transfering data to the registry node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws10.ua.pt 'mkdir -p test/AirLiftTP3'
sshpass -f password scp -o StrictHostKeyChecking=no dirRegistry.zip sd301@l040101-ws10.ua.pt:test/AirLiftTP3
echo "Decompressing data sent to the registry node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws10.ua.pt 'cd test/AirLiftTP3 ; unzip -uq dirRegistry.zip'
echo "Executing program at the registry node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws10.ua.pt 'cd test/AirLiftTP3/dirRegistry ; ./registry_com_d.sh sd301'
