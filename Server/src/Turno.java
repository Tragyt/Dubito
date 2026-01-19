import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Turno {
    private LinkedHashMap<GameClient, DiceCup> players;
    private int rotationIndex;
    private volatile GameClient currentTurn;
    private volatile Move currentMove;

    public Turno(LinkedHashMap<GameClient, DiceCup> players, int rotationIndex)
            throws RemoteException {
        this.players = players;
        this.rotationIndex = rotationIndex;
    }

    public int startTurn() throws RemoteException {

        flipCups();
        System.out.println("Inizia il turno");
        currentTurn = nextPlayer();
        Raise lastMove = null;
        setCurrentMove(lastMove);

        while (!(currentMove instanceof Doubt)) {
            currentTurn = nextPlayer();
            lastMove = (Raise) currentMove;
            setCurrentMove(lastMove);
        }

        ArrayList<GameClient> lst = new ArrayList<>(players.keySet());
        GameClient lastPlayer = lastMove.getPlayer();
        int dices = lastMove.getDicesNumber();
        int face = lastMove.getFace();
        int tot = 0;
        for (DiceCup cup : players.values())
            tot += cup.numberOf(face);
        if (tot < dices)
            rotationIndex = lst.indexOf(lastPlayer);
        else
            rotationIndex = lst.indexOf(currentTurn);

        GameClient looser = lst.get(rotationIndex);
        System.out.println(looser.getName() + " perde un dado");
        DiceCup looserCup = players.get(looser);
        for (GameClient player : players.keySet())
            player.endTurn(looser.getName(), looserCup.getDiceNumber() - 1, face, tot);
        return rotationIndex;
    }

    private void setCurrentMove(Raise lastMove) throws RemoteException {
        currentMove = null;
        if (lastMove == null)
            currentTurn.firstMove();
        else
            currentTurn.move(lastMove);
        while (currentMove == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        GameClient player = currentMove.getPlayer();
        if (!GameHelper.validateMove(currentMove, lastMove)) {
            player.imbroglio();
            players.get(player).disqualify();
            currentMove = lastMove;
            return;
        }

        for (GameClient p : players.keySet())
            if (p != player)
                p.opponentMove(currentMove);
        return;
    }

    private void flipCups() throws RemoteException {
        for (Map.Entry<GameClient, DiceCup> playerCup : players.entrySet()) {
            GameClient player = playerCup.getKey();
            DiceCup cup = playerCup.getValue();
            if (cup.getDiceNumber() > 0) {
                player.flipped(cup.flip());
                System.out.println(player.getName() + " ha girato il bicchiere");
            }
        }
        System.out.println("Tutti i giocatori hanno flippato il proprio bicchiere");
    }

    private GameClient nextPlayer() throws RemoteException {
        ArrayList<GameClient> lstPlayer = new ArrayList<>(players.keySet());
        System.out.print("E' il turno di ");
        GameClient player = lstPlayer.get(rotationIndex);
        while (players.get(player).getDiceNumber() == 0) {
            rotationIndex = (rotationIndex + 1) % players.size();
            player = lstPlayer.get(rotationIndex);
        }
        System.out.println(player.getName());
        rotationIndex = (rotationIndex + 1) % players.size();

        return player;
    }

    public void playerMoves(Move move) throws RemoteException {
        GameClient player = move.getPlayer();
        // System.out.println("[DEBUG]" + player.getName() + " == " +
        // currentTurn.getName());
        if (!currentTurn.equals(player)) {
            player.notYourTurn();
            return;
        }
        currentMove = move;
    }
}
