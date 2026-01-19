import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;

public class TurnoImpl extends UnicastRemoteObject implements Turno{
    private ArrayList<GameClient> players;
    private Map<GameClient, DiceCup> playerCups;
    private int rotationIndex;

    public Turno(ArrayList<GameClient> players, Map<GameClient, DiceCup> playerCups, int rotationIndex){
        this.players = players;
        this.playerCups = playerCups;
        this.rotationIndex = rotationIndex;
    }

    public int startTurn() throws RemoteException {

        flipCups();
        System.out.println("Inizia il turno");
        GameClient player = nextPlayer();
        Map.Entry<GameClient, Map.Entry<Integer, Integer>> currentMove = Map.entry(player,
                playerMove(player, Map.entry(0, 0)));
        rotationIndex = (rotationIndex + 1) % players.size();

        Map.Entry<GameClient, Map.Entry<Integer, Integer>> lastMove = Map.entry(null, Map.entry(0, 0));
        while (currentMove.getValue().getKey() != 0 && currentMove.getValue().getValue() != 0) {
            player = nextPlayer();
            lastMove = currentMove;
            currentMove = Map.entry(player, playerMove(player, lastMove.getValue()));
        }

        GameClient lastPlayer = lastMove.getKey();
        int dices = lastMove.getValue().getKey();
        int face = lastMove.getValue().getValue();
        int tot = 0;
        for (DiceCup cup : playerCups.values())
            tot += cup.numberOf(face);
        if (tot >= dices)
            rotationIndex = players.indexOf(player);
        else
            rotationIndex = players.indexOf(lastPlayer);

        GameClient looser = players.get(rotationIndex);
        DiceCup looserCup = playerCups.get(looser);
        for (GameClient p : players)
            p.endTurn(looser.getName(), looserCup.getDiceNumber() - 1, face, tot);
        return rotationIndex;
    }

    private void flipCups() throws RemoteException {
        for (Map.Entry<GameClient, DiceCup> playerCup : playerCups.entrySet()) {
            GameClient player = playerCup.getKey();
            DiceCup cup = playerCup.getValue();
            player.flipped(cup.flip());
            System.out.println(player.getName() + " ha girato il bicchiere");
        }
        System.out.println("Tutti i giocatori hanno flippato il proprio bicchiere");
    }

    private Map.Entry<Integer, Integer> playerMove(GameClient player, Map.Entry<Integer, Integer> lastMove)
            throws RemoteException {
        Map.Entry<Integer, Integer> move = null;

        System.out.println("Turno di " + player.getName());
        if (lastMove.getKey() == 0 && lastMove.getValue() == 0)
            move = player.firstMove();
        else
            move = player.move(lastMove);

        if (!GameHelper.validateMove(move, lastMove)) {
            player.imbroglio();
            playerCups.get(player).disqualify();
            return lastMove;
        }

        for (GameClient p : players)
            if (p != player)
                p.opponentMove(p.getName(), move);

        return move;
    }

    private GameClient nextPlayer() throws RemoteException {
        System.out.print("E' il turno di ");
        GameClient player = players.get(rotationIndex);
        while (playerCups.get(player).getDiceNumber() == 0) {
            rotationIndex = (rotationIndex + 1) % players.size();
            player = players.get(rotationIndex);
        }
        System.out.println(player.getName());
        rotationIndex = (rotationIndex + 1) % players.size();

        return player;
    }

    @Override
    public void playerMove(){
        
    }
}
