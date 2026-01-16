import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

public class Turno {
    private ArrayList<GameClient> players;
    private Map<GameClient, DiceCup> playerCups;
    private int rotationIndex;

    public Turno(ArrayList<GameClient> players, Map<GameClient, DiceCup> playerCups, int rotationIndex) {
        this.players = players;
        this.playerCups = playerCups;
        this.rotationIndex = rotationIndex;
    }

    public int startTurn() throws RemoteException {

        flipCups();

        // prima chiamata
        // incremento indice
        // finchè non c'è un dubito
        // mossa giocatore di turno
        // valutazione perdente

        // Map.Entry<Integer, Integer> currentMove =
        // players.get(rotationIndex).firstMove();
        // while (currentMove == null || currentMove.getKey() > 0 ||
        // currentMove.getValue() > 0) {

        // }
        return 0; // loser index
    }

    private void flipCups() throws RemoteException {
        for (Map.Entry<GameClient, DiceCup> playerCup : playerCups.entrySet()) {
            GameClient player = playerCup.getKey();
            DiceCup cup = playerCup.getValue();
            player.flipped(cup.flip());
        }
    }

    private Map.Entry<Integer, Integer> playerMove(GameClient player, Map.Entry<Integer, Integer> lastMove)
            throws RemoteException {
        Map.Entry<Integer, Integer> move;
        if (lastMove.getKey() == 0 && lastMove.getValue() == 0)
            move = player.firstMove();
        else
            move = player.move(lastMove);
        return move;
    }
}
