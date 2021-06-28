echo "Transfering data to the RMIregistry node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws10.ua.pt 'mkdir -p test/AirLiftTP3'
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws10.ua.pt 'rm -rf test/AirLiftTP3/*'
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws10.ua.pt 'mkdir -p Public/classes/interfaces'
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws10.ua.pt 'rm -rf Public/classes/interfaces/*'
sshpass -f password scp -o StrictHostKeyChecking=no dirRMIRegistry.zip sd301@l040101-ws10.ua.pt:test/AirLiftTP3
echo "Decompressing data sent to the RMIregistry node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws10.ua.pt 'cd test/AirLiftTP3 ; unzip -uq dirRMIRegistry.zip'
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws10.ua.pt 'cd test/AirLiftTP3/dirRMIRegistry ; cp interfaces/*.class /home/sd301/Public/classes/interfaces ; cp set_rmiregistry_d.sh /home/sd301'
echo "Executing program at the RMIregistry node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws10.ua.pt './set_rmiregistry_d.sh sd301 22300'
