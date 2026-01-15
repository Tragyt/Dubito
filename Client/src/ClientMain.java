import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientMain {
    public static void main(String[] args) throws InterruptedException, RemoteException {
        Service server;
        while (true) {
            try {
                Registry registry = LocateRegistry.getRegistry("rmi-server");
                server = (Service) registry.lookup("ServerService");
                break;
            } catch (RemoteException | NotBoundException e) {
                System.out.println("Lookup fallito, riprovo in 2s...");
                Thread.sleep(2000);
            }
        }

        String response = server.sendMessage("message");
        System.out.println(response);
    }
}
