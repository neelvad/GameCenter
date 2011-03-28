package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Takes care of all messaging for a messageable object
 * @author nv6
 *
 */
public class Messenger implements Messagable
{
	private Socket connection;
	private BufferedReader from;
	private PrintStream to;
	
	/**
	 * Construction!
	 * @param s non-null sockets only
	 * @param doInitialize true if should initialize IO channels now
	 * @throws Exception
	 */
	public Messenger(Socket s) throws Exception
	{
		connection = s;
		from = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		to = new PrintStream(connection.getOutputStream());		
	}

	public synchronized String requestReply(String requestMessage) throws Exception
	{
		to.println(requestMessage);
		return readBuffer();
	}
	
	public synchronized String readLine() throws Exception
	{
		return from.readLine();
	}
	
	public synchronized String readBuffer() throws Exception
	{
		return readLine();
	}
	
	public synchronized void sendMessage(String message) throws Exception
	{
		to.println(message);
	}
	
	public synchronized void closeConnections() throws Exception
	{
		connection.close();
		from.close();
		to.close();
	}
}
