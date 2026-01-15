import java.rmi.RemoteException;

public class LobbyFullException extends RemoteException{
    public LobbyFullException(String message){
        super(message);
    }
}
