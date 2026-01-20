import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Game {
    private LinkedHashMap<GameClient, DiceCup> players;
    private int rotationIndex;
    private int playersInGame;
    private Turno turn;

    public Game(LinkedHashMap<GameClient, DiceCup> players) {
        this.players = players;
        playersInGame = players.size();
        rotationIndex = (int) (Math.random() * playersInGame);
    }

    public GameClient startGame() throws RemoteException {
        ArrayList<Map.Entry<GameClient, DiceCup>> lst = new ArrayList<>(players.entrySet());
        while (playersInGame > 1) {
            turn = new Turno(players, rotationIndex);
            rotationIndex = turn.startTurn();

            Map.Entry<GameClient, DiceCup> entry = lst.get(rotationIndex);
            int dicesLeft = entry.getValue().removeDice();
            GameClient looser = entry.getKey();
            looser.diceLost(dicesLeft);
            if (dicesLeft == 0) {
                looser.youLost();
                playersInGame--;
            }
        }
        return getWinner();
    }

    public Turno getTurn() {
        return turn;
    }

    private GameClient getWinner() {
        for (Map.Entry<GameClient, DiceCup> entry : players.entrySet()) {
            if (entry.getValue().getDiceNumber() > 0)
                return entry.getKey();
        }
        return null;
    }
}
