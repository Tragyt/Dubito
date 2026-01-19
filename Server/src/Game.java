import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {
    private ArrayList<GameClient> players;
    private Map<GameClient, DiceCup> playerCups;
    private int rotationIndex;
    private int playersInGame;

    public Game(ArrayList<GameClient> players) {
        this.players = players;
        rotationIndex = 0;
        playersInGame = players.size();

        playerCups = new HashMap<>();
        for (GameClient player : players)
            playerCups.put(player, new DiceCup());
    }

    public GameClient startGame() throws RemoteException {
        while (playersInGame > 1) {

            Turno turn = new Turno(players, playerCups, rotationIndex);
            int rotationIndex = turn.startTurn();
            GameClient looser = players.get(rotationIndex);
            int dicesLeft = playerCups.get(looser).removeDice();
            looser.diceLost(dicesLeft);
            if (dicesLeft == 0) {
                looser.youLost();
                playersInGame--;
            }
        }
        return players.get(0); // return winner
    }

}
