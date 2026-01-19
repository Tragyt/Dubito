import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Turno extends Remote {
    public void playerMove() throws RemoteException;
}
