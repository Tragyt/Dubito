import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
        System.out.println(
                "Hai girato il tuo bicchiere, questi sono i tuoi dadi (gli 1 sono i perudo, da considerare come jolly):");
        for (int dice : dices)
            System.out.print(dice + " ");
        System.out.println();
    }

    @Override
    public Map.Entry<Integer, Integer> move(Map.Entry<Integer, Integer> lastMove) {
        int last_nDices = lastMove.getKey();
        int last_face = lastMove.getKey();

        String out = "E' il tuo turno, digita [doubt] per dubitare o [numero di dadi] [faccia dado] per rilanciare, ";
        if (last_face == 1)
            out += "devi aumentare il numero di dadi e mantenere [1] (perudo) come faccia o inserire un valore superiore a "
                    + last_nDices * 2;
        else
            out += "devi aumentare almeno uno dei due valori oppure dimezzare per eccesso il numero di dadi e inserire [1] (perudo) come faccia di dado ";
        System.out.println(out);

        Map.Entry<Integer, Integer> ret;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String move = scanner.nextLine();
            if (move.trim().equals("doubt")) {
                ret = Map.entry(0, 0);
                break;
            }
            if (move.matches("^\\d \\d$")) {
                String[] splitted = move.split(" ");
                int nDices = Integer.parseInt(splitted[0]);
                int face = Integer.parseInt(splitted[1]);

                if(last_face==1 && !((face==1 && nDices > last_nDices) || (face>) ) )

                ret = Map.entry(nDices, face);
                break;
            }
        }
        scanner.close();

        return ret;
    }

    @Override
    public Map.Entry<Integer, Integer> firstMove() {
        return null;
    }

    @Override
    public void opponentMove(Map.Entry<String, Map.Entry<Integer, Integer>> move) {
        System.out.println("Secondo " + move.getKey() + " ci sono almeno (numero dadi): " + move.getValue().getKey()
                + " (faccia dado): " + move.getValue().getValue());
    }
}
