import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface GameClient extends Remote {
    String getName() throws RemoteException;

    void onGameStart(ArrayList<GameClient> players) throws RemoteException;

    void refused(String message) throws RemoteException;

    void lobbyJoined() throws RemoteException;

    void flipped(ArrayList<Integer> dices) throws RemoteException;
}
