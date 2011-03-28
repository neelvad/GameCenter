package player;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.Socket;

import util.Messagable;
import util.Messenger;
import util.PlayerActions;

/**
 * Represents a player for the game center
 * @author nv6
 *
 */
public abstract class Player implements Runnable, Messagable
{
	private Socket connection;
	protected BufferedReader from;
	protected PrintStream to;
	protected String name;
	private Messenger m;
	
	/**
	 * Construction
	 * @param s non-null sockets only
	 * @param doInitialize true if should initialize IO channels now
	 * @throws Exception
	 */
	public Player(Socket s, boolean doInitialize) throws Exception
	{
		connection = s;
		
		if(doInitialize)
		{
			initializeIO();
		}
	}
	
	/**
	 * Initializes the messenger object which takes care of all messaging
	 * @throws Exception
	 */
	protected void initializeIO() throws Exception
	{
		m = new Messenger(connection);
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String toString()
	{
		return "This player's name is " + name;
	}
	
	public String requestReply(String requestMessage) throws Exception
	{
		return m.requestReply(requestMessage);
	}
	
	public String readLine() throws Exception
	{
		return m.readLine();
	}
	
	public String readBuffer() throws Exception
	{
		return m.readBuffer();
	}
	
	public void sendMessage(String message) throws Exception
	{
		m.sendMessage(message);
	}
	
	public void closeConnections()
	{
		try
		{
			m.closeConnections();
		}
		catch(Exception e)
		{
			System.out.println("Could not close connections. They could already be closed.");
		}
	}
	
	public void getAndSetNameFromPlayer() throws Exception
	{
		String name = requestReply(PlayerActions.REQUEST_NAME.toString());
		setName(name);
	}
	
	@Override
	public void run()
	{
		// DO NOTHING
	}
}
