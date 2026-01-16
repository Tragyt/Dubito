import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Lobby {
    private static final int MAX_PLAYERS = 4;
    private ArrayList<GameClient> players;
    private boolean gameStarted;
    private Map<GameClient, DiceCup> playerCups;
    private int rotationIndex;

    public Lobby() {
        players = new ArrayList<>();
        gameStarted = false;
        playerCups = new HashMap<>();
        rotationIndex = 0;
    }

    public void newPlayer(GameClient player) throws RemoteException {
        players.add(player);
        playerCups.put(player, new DiceCup());
        System.out.println(player.getName() + " joined the lobby");
        player.lobbyJoined();
    }

    public int getPlayerNumber() {
        return players.size();
    }

    public boolean isFull() {
        return players.size() == MAX_PLAYERS;
    }

    public void startGame() throws RemoteException {
        gameStarted = true;
        Collections.shuffle(players);
        for (GameClient player : players)
            player.onGameStart(players);

        Game game = new Game(players);
        GameClient winner = game.startGame();
        winner.youWon();
        for (GameClient player : players) {
            if (player != winner)
                player.winner(winner.getName());
        }
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public ArrayList<GameClient> getPlayers() {
        return players;
    }

}
