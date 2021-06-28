echo "Transfering data to the Hostess node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws03.ua.pt 'mkdir -p test/AirLiftTP3'
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws03.ua.pt 'rm -rf test/AirLiftTP3/*'
sshpass -f password scp -o StrictHostKeyChecking=no dirHostess.zip sd301@l040101-ws03.ua.pt:test/AirLiftTP3
echo "Decompressing data sent to the Hostess node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws03.ua.pt 'cd test/AirLiftTP3 ; unzip -uq dirHostess.zip'
echo "Executing program at the Hostess node."
sshpass -f password ssh -o StrictHostKeyChecking=no sd301@l040101-ws03.ua.pt 'cd test/AirLiftTP3/dirHostess ; ./Hostess_com_d.sh'