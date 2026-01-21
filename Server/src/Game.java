import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Game {
    private LinkedHashMap<GameClient, DiceCup> players;
    private int rotationIndex;
    private Turno turn;

    public Game(LinkedHashMap<GameClient, DiceCup> players) {
        this.players = players;
        rotationIndex = (int) (Math.random() * players.size());
    }

    public GameClient startGame() {
        ArrayList<Map.Entry<GameClient, DiceCup>> lst = new ArrayList<>(players.entrySet());
        while (players.values().stream().filter(cup -> cup.getDiceNumber() > 0).count() > 1) {
            turn = new Turno(players, rotationIndex);
            rotationIndex = turn.startTurn();

            if (rotationIndex != -1) {
                Map.Entry<GameClient, DiceCup> entry = lst.get(rotationIndex);
                DiceCup looserCup = entry.getValue();
                GameClient looser = entry.getKey();
                int dicesLeft = looserCup.removeDice();
                if (!looserCup.isCrashed()) {
                    try {
                        looser.diceLost(dicesLeft);
                        if (dicesLeft == 0)
                            looser.youLost();
                    } catch (RemoteException e) {
                        Lobby.payerCrashed(looser, players);
                    }
                }
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
