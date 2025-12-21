import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientMain {
    public static void main(String[] args) throws NotBoundException, RemoteException {
        Registry registry = LocateRegistry.getRegistry("rmi-server");
        Service server = (Service) registry.lookup("ServerService");

        String response = server.sendMessage("message");
        System.out.println(response);
    }
}
