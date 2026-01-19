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

    void move(Map.Entry<Integer, Integer> lastMove) throws RemoteException;

    void firstMove() throws RemoteException;

    void opponentMove(String player, Map.Entry<Integer, Integer> move) throws RemoteException;

    void winner(String player) throws RemoteException;

    void youWon() throws RemoteException;

    void youLost() throws RemoteException;

    void diceLost(int dicesLeft) throws RemoteException;

    void imbroglio() throws RemoteException;

    void endTurn(String looser, int dicesLeft, int faceCalled, int facesRealNumber) throws RemoteException;
}
