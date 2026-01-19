import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {

    public static void main(String[] args) throws RemoteException {
        GameService gameService = new GameServiceImpl();

        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("GameService", gameService);
        System.out.println("GameServer partito!");
    }
}
