import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;

public class ClientMain {
    public static void main(String[] args) throws InterruptedException {
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

        System.out.println("Server trovato, tentativo di unirsi alla partita in corso...");
        String playername = Optional.ofNullable(System.getenv("PLAYER_NAME")).orElse("guest");
        GameClientImpl player = new GameClientImpl(playername);
        try {
            server.JoinGame(player);
            System.out.print("Ingresso riuscito, in attesa di altri giocatori...");
        } catch (LobbyFullException e) {
            System.out.println("Impossibile unirsi al server: " + e.getMessage());
            System.exit(0);
        } catch (RemoteException e) {
            System.out.println("Si è verificato un errore durante la connessione al server");
        }
    }
}
