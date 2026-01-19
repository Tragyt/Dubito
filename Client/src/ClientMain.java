import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) throws InterruptedException, RemoteException {
        GameService server;
        while (true) {
            try {
                Registry registry = LocateRegistry.getRegistry("gameserver");
                server = (GameService) registry.lookup("GameService");
                break;
            } catch (RemoteException | NotBoundException e) {
                System.out.println("Lookup fallito, riprovo in 2s...");
                Thread.sleep(2000);
            }
        }
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);

        System.out.print(
                "Server trovato, inserisci il tuo nickname\n> ");
        String playername = Optional.of(scanner.nextLine().trim()).filter(s -> !s.isEmpty()).orElse("guest");
        GameClientImpl player = new GameClientImpl(playername, server);
        server.JoinGame(player);
        player.startInputLoop();
    }
}
