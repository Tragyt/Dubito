import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class GameClientImpl extends UnicastRemoteObject implements GameClient {
    private final String name;

    public GameClientImpl(String name) throws RemoteException {
        this.name = name;
    }

    @Override
    public void onGameStart(ArrayList<GameClient> players) throws RemoteException {
        System.out.println("La partita sta per iniziare");
        System.out.print("Giocatori: ");
        for (GameClient player : players)
            System.out.print(player.getName() + " ");
        System.out.println();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void refused(String message) {
        System.out.println(message + ", riprova più tardi!");
    }

    @Override
    public void lobbyJoined() {
        System.out.println("Ingresso riuscito, in attesa di altri giocatori...");
    }

    @Override
    public void flipped(ArrayList<Integer> dices) {
        System.out.println("Inizio round, hai " + dices.size() + " dadi!");
        System.out.println("Hai girato il tuo bicchiere, questi sono i tuoi dadi:");
        for (int dice : dices)
            System.out.print(dice + " ");
        System.out.println();
    }
}
