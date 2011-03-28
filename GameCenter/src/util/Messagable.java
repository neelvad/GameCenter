package util;

/**
 * Specifies messaging interface.
 * Classes that want to send messages should have all of these methods
 * @author nv6
 *
 */
public interface Messagable
{
	public String requestReply(String requestMessage) throws Exception;
	
	public String readLine() throws Exception;
	
	public String readBuffer() throws Exception;
	
	public void sendMessage(String message) throws Exception;
}
