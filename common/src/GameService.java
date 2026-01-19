import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameService extends Remote {
    void JoinGame(GameClient playername) throws RemoteException;

    void playerMove(Move move) throws RemoteException;
}
