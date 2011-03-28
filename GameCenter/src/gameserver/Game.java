package gameserver;

import player.Player;
import util.PlayerActions;

/**
 * Controls all of the logic for running a game.
 * Runs linearly since the first and second players have already been set
 * @author nv6
 *
 */
public class Game implements Runnable
{
	private Player playerOne;
	private Player playerTwo;
	
	public Game(Player playerOne, Player playerTwo)
	{
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;
	}
	
	/**
	 * Calculates the winner of the game based on bets
	 * @param evenBetFromPlayerOne
	 * @param playerOneBet
	 * @param playerTwoBet
	 * @return
	 */
	private boolean didPlayerOneWin(boolean evenBetFromPlayerOne, int playerOneBet, int playerTwoBet)
	{
		int sum = playerOneBet + playerTwoBet;
		return (Math.abs(sum) % 2) == 0;
	}
	
	/**
	 * Contains all logic for the gameplay
	 * @throws Exception
	 */
	public void startGame() throws Exception
	{
		print(playerOne.getName() + " is now playing " + playerTwo.getName() + ".");
		// Tell player two to wait for player one
		playerTwo.sendMessage("Your opponent's name is " + playerOne.getName() + ". Waiting for player " +
			"one to make a bet.");
		
		// Acquire player one bet
		playerOne.sendMessage("Your opponent's name is " + playerTwo.getName() + ". Please make your even/odd bet.");
		String playerOneBet = playerOne.requestReply(PlayerActions.REQUEST_PLAYER_ONE_BET_EVEN_ODD.toString());
		playerOne.sendMessage("OK, you bet: " + playerOneBet + ". Now make your integer bet.");
		String playerOneIntegerBet = playerOne.requestReply(PlayerActions.REQUEST_PLAYER_ONE_BET_INTEGER_BET.toString());
		playerOneBet = playerOneBet + " " + playerOneIntegerBet;		
		playerOne.sendMessage("OK. Good. Now wait for player two to make their bet.");
		print(playerOne.getName() + " bet " + playerOneBet);
		
		boolean evenBetFromPlayerOne = playerOneBet.split(" ")[0].equals("even");
		int integerBetFromPlayerOne = Integer.parseInt(playerOneBet.split(" ")[1]);
		
		// Acquire player two bet
		playerTwo.sendMessage("Player one bet: " + playerOneBet.split(" ")[0] + ". Now make your bet.");
		PlayerActions playerTwoBetRequest = (evenBetFromPlayerOne) ? PlayerActions.REQUEST_PLAYER_TWO_BET_IS_EVEN : 
			PlayerActions.REQUEST_PLAYER_TWO_BET_IS_ODD;
		String playerTwoBet = playerTwo.requestReply(playerTwoBetRequest.toString());
		int integerBetFromPlayerTwo = Integer.parseInt(playerTwoBet);
		print(playerTwo.getName() + " bet " + playerTwoBet);
		
		// Calculate winner of game
		boolean isPlayerOneWinner = didPlayerOneWin(evenBetFromPlayerOne, integerBetFromPlayerOne, integerBetFromPlayerTwo);

		// Send answer to players (win/loss)
		playerOne.sendMessage((isPlayerOneWinner) ? "Congratulations! You WON." : "You lost.");
		playerTwo.sendMessage((! isPlayerOneWinner) ? "Congratulations! You WON." : "You lost.");
		
		updateScoreAndNotifyPlayers(isPlayerOneWinner);
		
		playerOne.sendMessage(PlayerActions.GAME_OVER.toString());
		playerTwo.sendMessage(PlayerActions.GAME_OVER.toString());
		
		HistoryService.printHistory();
		
		disposeOfConnections();
	}
	
	/**
	 * Deals with game failures by notifying each player that the game has ended
	 * @param player
	 */
	public void notifyPlayerGameEndedAbruptly(Player player)
	{
		try
		{
			player.sendMessage("The other player disconnected. This game has to end unfortunately.");
			player.sendMessage(PlayerActions.GAME_OVER.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			print("Cannot send message to player because the player has disconnected.");
		}
	}
	
	/**
	 * Deals with a finished game
	 * Updates the score and notifies players of winners
	 * @param isPlayerOneWinner
	 * @throws Exception
	 */
	public void updateScoreAndNotifyPlayers(boolean isPlayerOneWinner) throws Exception
	{
		if(isPlayerOneWinner)
		{
			HistoryService.updateHistory(playerOne, playerTwo);
			print(playerOne.getName() + " won.");
		}
		else
		{
			HistoryService.updateHistory(playerTwo, playerOne);
			print(playerTwo.getName() + " won.");
		}
		
		playerOne.sendMessage(HistoryService.getPlayerHistory(playerOne, playerTwo));
		playerTwo.sendMessage(HistoryService.getPlayerHistory(playerTwo, playerOne));		
	}
	
	public void disposeOfConnections() throws Exception
	{
		playerOne.closeConnections();
		playerTwo.closeConnections();
	}
	
	@Override
	public void run()
	{
		try
		{
			this.startGame();
		}
		catch (Exception e)
		{
			print("Game ended abruptly because of an error. Notifying remaining players of the game failure.");
			notifyPlayerGameEndedAbruptly(playerOne);
			notifyPlayerGameEndedAbruptly(playerTwo);
			print("An unexpected error occured. The game is over. Try reconnecting.");
		}
	}
	
	public synchronized void print(String message)
	{
		System.out.println(message);
	}

}
