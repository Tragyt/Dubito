import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerMain {

    public static void main(String[] args) throws RemoteException {
        GameService gameService = new GameServiceImpl();
        GameService stub = (GameService) UnicastRemoteObject.exportObject(gameService, 0);

        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("GameService", stub);
        System.out.println("GameServer started!");
    }
}
