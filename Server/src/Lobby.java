import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Lobby {
    private static final int MAX_PLAYERS = 2;
    private LinkedHashMap<GameClient, DiceCup> players;
    private boolean gameStarted;
    private Game game;

    public Lobby() {
        players = new LinkedHashMap<>();
        gameStarted = false;
    }

    public void newPlayer(GameClient player) throws RemoteException {
        players.put(player, new DiceCup());
        System.out.println(player.getName() + " entra nella lobby");
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
        ArrayList<GameClient> clients = new ArrayList<>(players.keySet());
        for (GameClient player : clients)
            player.onGameStart(clients);

        game = new Game(players);
        GameClient winner = game.startGame();
        winner.youWon();
        for (GameClient player : players.keySet()) {
            if (player != winner)
                player.winner(winner.getName());
        }
        gameStarted = false;
        players = new LinkedHashMap<>();
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public Game getGame() {
        return game;
    }
}
