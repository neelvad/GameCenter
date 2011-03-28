package gameserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Scanner;

import player.Player;

/**
 * Keeps track of player history
 * @author nv6
 *
 */
public class HistoryService
{
	private static Hashtable<String, Hashtable<String, Integer>> table = new Hashtable<String, Hashtable<String, Integer>>();

	private static String filepath = "history.txt";
	private final static String DELIMITER = ",";
		
	/**
	 * Don't allow instantiation
	 */
	private HistoryService()
	{
	}
	
	/**
	 * Takes the file history.txt from the same directory as the gamecenter.jar file
	 * @throws Exception
	 */
	public static void loadHistory() throws Exception
	{
		Scanner s = new Scanner(new File(filepath));
		
		while(s.hasNext())
		{
			String line = s.nextLine();
			String[] rowText = line.split(DELIMITER);
			table.put(rowText[0], new Hashtable<String, Integer>());
			Hashtable<String, Integer> row = table.get(rowText[0]);

			for(int i = 1; i < rowText.length; i++)
			{
				row.put(rowText[i], Integer.parseInt(rowText[i + 1]));
				i += 1;
			}
		}
	}
	
	/**
	 * Takes in a winner and a loser and upgrades the winner's score by one
	 * @param winner
	 * @param loser
	 */
	public static void updateHistory(Player winner, Player loser)
	{
		Hashtable<String, Integer> row = table.get(winner.getName());
		
		if(row == null)
		{
			row = new Hashtable<String, Integer>();
			table.put(winner.getName(), row);
		}
		
		Integer score = row.get(loser.getName()) == null ? 0 : row.get(loser.getName());
		
		row.put(loser.getName(), score + 1);
	}
	
	/**
	 * Save the history to history.txt
	 * @throws Exception
	 */
	public static void printHistory() throws Exception
	{
		FileOutputStream outputStream = new FileOutputStream(new File(filepath), false);
		PrintStream printer = new PrintStream(outputStream);
		
		for(String winner : table.keySet())
		{
			String line = winner + DELIMITER;
			
			for(String loser : table.get(winner).keySet())
			{
				line += loser + DELIMITER + table.get(winner).get(loser) + DELIMITER;
			}
			
			printer.println(line);
		}
	}
	
	/**
	 * Gets a single player's score history against another player
	 * @param playerOne
	 * @param playerTwo
	 * @return
	 */
	public static String getPlayerHistory(Player playerOne, Player playerTwo)
	{
		int wins = getPlayerWinScore(playerOne.getName(), playerTwo.getName());
		int losses = getPlayerWinScore(playerTwo.getName(), playerOne.getName());
		
		return "You won " + wins + " games out of " + (wins + losses) + " with " + playerTwo.getName() + ".";
	}
	
	public static int getPlayerWinScore(String winner, String loser)
	{
		Hashtable<String, Integer> row = table.get(winner);
		
		if(row == null)
			return 0;
		
		return row.get(loser) == null ? 0 : row.get(loser);
	}
}
