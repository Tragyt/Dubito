import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Lobby {
    private static final int MAX_PLAYERS = Integer.parseInt(System.getenv("MAX_PLAYER"));
    private LinkedHashMap<GameClient, DiceCup> players;
    private boolean gameStarted;
    private Game game;

    public Lobby() {
        players = new LinkedHashMap<>();
        gameStarted = false;
    }

    public void newPlayer(GameClient player) throws RemoteException {
        players.put(player, new DiceCup(player.getName()));
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
        for (GameClient player : clients) {
            try {
                player.onGameStart(clients);
            } catch (RemoteException e) {
                payerCrashed(player, players);
            }
        }

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

    public void endGame() {
        gameStarted = false;
        players = new LinkedHashMap<>();
    }

    public Game getGame() {
        return game;
    }

    public static void payerCrashed(GameClient player, LinkedHashMap<GameClient, DiceCup> players) {
        DiceCup cup = players.get(player);
        cup.crashed();

        System.out.println(cup.getPlayername() + " è crashato");
        for (Map.Entry<GameClient, DiceCup> entry : players.entrySet()) {
            if (!entry.getValue().isCrashed()) {
                try {
                    entry.getKey().playerCrashed(cup.getPlayername());
                } catch (RemoteException e) {
                    System.out.println("Maybe someone else crashed too");
                }
            }

        }
    }
}
