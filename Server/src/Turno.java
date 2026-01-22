import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Turno {
    private LinkedHashMap<GameClient, DiceCup> players;
    private int rotationIndex;
    private volatile GameClient currentTurn;
    private volatile Move currentMove;
    private final int CRASH_TIME = Integer.parseInt(System.getenv("CRASH_TIMER_SECONDS"));

    public Turno(LinkedHashMap<GameClient, DiceCup> players, int rotationIndex) {
        this.players = players;
        this.rotationIndex = rotationIndex;
    }

    public int startTurn() {
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

        if (lastMove == null || currentMove.getPlayer() == null) {
            return -1;
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
        DiceCup looserCup = players.get(looser);
        System.out.println(looserCup.getPlayername() + " perde un dado");

        for (Map.Entry<GameClient, DiceCup> entry : players.entrySet()) {
            GameClient player = entry.getKey();
            if (!entry.getValue().isCrashed()) {
                try {
                    player.endTurn(looserCup.getPlayername(), looserCup.getDiceNumber() - 1, face, tot);
                } catch (RemoteException e) {
                    Lobby.payerCrashed(player, players);
                }
            }
        }

        return rotationIndex;
    }

    private void setCurrentMove(Raise lastMove) {
        currentMove = null;
        try {
            if (lastMove == null)
                currentTurn.firstMove();
            else
                currentTurn.move(lastMove);
        } catch (RemoteException e) {
            Lobby.payerCrashed(currentTurn, players);
            currentMove = lastMove;
            return;
        }

        int c = 0;
        while (currentMove == null) {
            try {
                if (c < (CRASH_TIME * 10)) {
                    c++;
                } else {
                    Lobby.payerCrashed(currentTurn, players);
                    if (players.values().stream().filter(cup -> cup.getDiceNumber() > 0).count() > 1)
                        currentMove = lastMove;
                    else
                        currentMove = new Doubt(currentTurn);
                    return;
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        GameClient player = currentMove.getPlayer();
        if (!GameHelper.validateMove(currentMove, lastMove)) {
            try {
                player.imbroglio();
            } catch (RemoteException e) {
                Lobby.payerCrashed(player, players);
            }

            players.get(player).disqualify();
            currentMove = lastMove;
            return;
        }

        for (Map.Entry<GameClient, DiceCup> entry : players.entrySet()) {
            GameClient p = entry.getKey();
            if (p != player && !entry.getValue().isCrashed()) {
                try {
                    p.opponentMove(currentMove);
                } catch (RemoteException e) {
                    Lobby.payerCrashed(p, players);
                    if (players.values().stream().filter(cup -> cup.getDiceNumber() > 0).count() <= 1) {
                        currentMove = new Doubt(null);
                    }
                }
            }
        }

        return;
    }

    private void flipCups() {
        for (Map.Entry<GameClient, DiceCup> playerCup : players.entrySet()) {
            GameClient player = playerCup.getKey();
            DiceCup cup = playerCup.getValue();
            if (cup.getDiceNumber() > 0) {
                try {
                    player.flipped(cup.flip());
                    System.out.println(player.getName() + " ha girato il bicchiere");
                } catch (RemoteException e) {
                    Lobby.payerCrashed(player, players);
                }
            }
        }
        System.out.println("Tutti i giocatori hanno flippato il proprio bicchiere");
    }

    private GameClient nextPlayer() {
        ArrayList<GameClient> lstPlayer = new ArrayList<>(players.keySet());
        System.out.print("E' il turno di ");
        GameClient player = lstPlayer.get(rotationIndex);
        while (players.get(player).getDiceNumber() == 0) {
            rotationIndex = (rotationIndex + 1) % players.size();
            player = lstPlayer.get(rotationIndex);
        }
        try {
            System.out.println(player.getName());
        } catch (RemoteException e) {
            Lobby.payerCrashed(player, players);
        }
        rotationIndex = (rotationIndex + 1) % players.size();

        return player;
    }

    public void playerMoves(Move move) throws RemoteException {
        GameClient player = move.getPlayer();
        if (!currentTurn.equals(player)) {
            player.notYourTurn();
            return;
        }
        currentMove = move;
    }
}
