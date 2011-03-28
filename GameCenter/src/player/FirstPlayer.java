package player;


import java.net.Socket;

public class FirstPlayer extends Player
{

	public FirstPlayer(Socket s, boolean doInitialize) throws Exception
	{
		super(s, doInitialize);
		// TODO Auto-generated constructor stub
	}
	
	public FirstPlayer(Socket s) throws Exception
	{
		super(s, false);
	}
	
	/**
	 * For the first player only. Takes care of initialization in a seperate thread in order
	 * to allow gamecenter to deal with second player connection simultaneously 
	 */
	@Override
	public void run() 
	{
		try
		{
			initializeIO();
			
			sendMessage("You are player one. Waiting for player two to join.");
			getAndSetNameFromPlayer();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
	}
}
