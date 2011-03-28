package util;

/**
 * A list of all possible actions by a player
 * Request history and message are currently unused
 * @author nv6
 *
 */
public enum PlayerActions
{
	GAME_OVER,
	REQUEST_PLAYER_ONE_BET_EVEN_ODD,
	REQUEST_PLAYER_ONE_BET_INTEGER_BET,
	REQUEST_PLAYER_TWO_BET_IS_EVEN,
	REQUEST_PLAYER_TWO_BET_IS_ODD,
	REQUEST_NAME,
	REQUEST_HISTORY,
	MESSAGE
}
