package player;

import java.net.Socket;

/**
 * Takes care of second player initialization for the game center
 * @author nv6
 *
 */
public class SecondPlayer extends Player
{
	public SecondPlayer(Socket s, boolean doInitialize) throws Exception
	{
		super(s, doInitialize);
		
		sendMessage("You are player two. Player one is ready.");
		getAndSetNameFromPlayer();
	}
	
	public SecondPlayer(Socket s) throws Exception
	{
		this(s, true);
	}
}
