echo "Transfering data to the general repository node."
sshpass -f password ssh sd301@l040101-ws09.ua.pt 'mkdir -p test/AirLiftTP3'
sshpass -f password ssh sd301@l040101-ws09.ua.pt 'rm -rf test/AirLiftTP3/*'
sshpass -f password scp dirGeneralRepos.zip sd301@l040101-ws09.ua.pt:test/AirLiftTP3
echo "Decompressing data sent to the general repository node."
sshpass -f password ssh sd301@l040101-ws09.ua.pt 'cd test/AirLiftTP3 ; unzip -uq dirGeneralRepos.zip'
echo "Executing program at the general repository node."
sshpass -f password ssh sd301@l040101-ws09.ua.pt 'cd test/AirLiftTP3/dirGeneralRepos ; ./repos_com_d.sh sd301'
echo "Server shutdown."
sshpass -f password ssh sd301@l040101-ws09.ua.pt 'cd test/AirLiftTP3/dirGeneralRepos ; less stat'
