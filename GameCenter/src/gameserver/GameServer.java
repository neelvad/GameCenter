package gameserver;

import java.net.ServerSocket;

import player.FirstPlayer;
import player.Player;
import player.SecondPlayer;

/**
 * Main class for game server/application 
 * @author nv6
 *
 */
public class GameServer
{
	public GameServer(String port) throws Exception
	{
		int portNumber = Integer.parseInt(port);
		
		System.out.println("Beginning game server.");
		
		HistoryService.loadHistory();
		launchGame(portNumber);
	}
	
	/**
	 * Start the game server 
	 * Allows multiple games and players to be carried on at once
	 * @param portNumber
	 * @throws Exception
	 */
	public void launchGame(int portNumber) throws Exception
	{
		ServerSocket serverSocket = new ServerSocket(portNumber);
		
		while(true)
		{
			System.out.println("Accepting new players.");
			Player playerOne = new FirstPlayer(serverSocket.accept());
			Thread playerOneInitializer = new Thread(playerOne);
			
			playerOneInitializer.start();			
			System.out.println("Player one joined. Server waiting for player two.");
			Player playerTwo = new SecondPlayer(serverSocket.accept());			
			System.out.println("Player two joined. Can begin game.");
			
			playerOneInitializer.join();
			
			(new Thread(new Game(playerOne, playerTwo))).start();
		}
	}	
	
	public static void main(String[] args) throws Exception
	{
		if(args.length != 1)
		{
			throw new IllegalArgumentException("There is not one argument.");
		}
		
		new GameServer(args[0]);
	}
}
