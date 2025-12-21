import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerMain {

    public static void main(String[] args) throws RemoteException {
        // System.setProperty("java.rmi.server.hostname", "server");

        Service service = new ServiceImpl();
        Service stub = (Service) UnicastRemoteObject.exportObject(service, 0);

        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("ServerService", stub);
        System.out.println("Server started!");
    }
}
