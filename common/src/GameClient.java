import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameClient extends Remote {
    void onGameStart() throws RemoteException;
}
