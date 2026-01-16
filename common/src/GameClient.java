import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public interface GameClient extends Remote {
    String getName() throws RemoteException;

    void onGameStart(ArrayList<GameClient> players) throws RemoteException;

    void refused(String message) throws RemoteException;

    void lobbyJoined() throws RemoteException;

    void flipped(ArrayList<Integer> dices) throws RemoteException;

    Map.Entry<Integer, Integer> move(Map.Entry<Integer, Integer> lastMove) throws RemoteException;

     Map.Entry<Integer, Integer> firstMove() throws RemoteException;

    void opponentMove(Map.Entry<String, Map.Entry<Integer, Integer>> move) throws RemoteException;

}
