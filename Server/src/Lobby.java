import java.rmi.RemoteException;
import java.util.ArrayList;

public class Lobby {
    private static final int MAX_PLAYERS = 4;
    private ArrayList<GameClient> players;

    public Lobby() {
        players = new ArrayList<>();
    }

    public void newPlayer(GameClient player) throws LobbyFullException{
        if(getPlayerNumber() <= MAX_PLAYERS)
            players.add(player);
        else
            throw new LobbyFullException("Lobby is full");
    }

    public int getPlayerNumber(){
        return players.size();
    }

    public boolean isFull(){
        return getPlayerNumber() == MAX_PLAYERS;
    }

    public void startGame() throws RemoteException{
        for(GameClient player: players)
            player.onGameStart();
    }
}
