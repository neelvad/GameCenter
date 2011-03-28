INFORMATION: 
The history.txt file must be in the same directory as the gamecenter.jar file.

The players and the game center can both be run on a single machine. The run commands are as follows:

java -jar gamecenter.jar 7001
java -jar player.jar localhost 7001 Neel random 3

The name should be changed when running the player. The ports on both player and gamecenter can be changed. The game should
work when distributed across a local network, but I haven't been able to test it because I only have one machine. 

The only argument for the server is the port which is open. This should be the same for all players and the game center.

The 1st argument for player.jar is the hostname and the second is the port, which should be the same as the gamecenter.jar port.
The 3rd argument for player.jar is the player's name. The fourth argument (random) can be one of human/random/constant/opposite/echo.
The fifth argument for player.jar is the ceiling on the random time between games (reconnects from the player's perspective).