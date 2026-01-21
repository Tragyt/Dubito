import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

public class GameClientImpl extends UnicastRemoteObject implements GameClient {
    private final String name;
    private volatile boolean myTurn;
    private volatile boolean game;
    private volatile Raise lastMove;
    private GameService server;

    public enum AnsiColor {
        RESET("\u001B[0m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m");

        private final String code;

        AnsiColor(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return code;
        }
    }

    public GameClientImpl(String name, GameService server) throws RemoteException {
        this.name = name;
        this.server = server;
        this.game = true;
    }

    public void startInputLoop() {

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (game) {
                if (!myTurn) {
                    try {
                        Thread.sleep(100);
                        continue;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Move move = null;
                if (lastMove != null)
                    move = getMove(scanner);
                else
                    move = getFirstMove(scanner);
                try {
                    server.playerMove(move);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                myTurn = false;
            }

            scanner.close();
        }).start();
    }

    private Move getMove(Scanner scanner) {
        int last_nDices = lastMove.getDicesNumber();
        int last_face = lastMove.getFace();

        String out = AnsiColor.GREEN
                + "E' il tuo turno, digita [doubt] per dubitare o [numero di dadi] [faccia dado] per rilanciare, ";
        if (last_face == 1)
            out += "devi aumentare il numero di dadi e mantenere [1] (perudo) come faccia o inserire un valore superiore a "
                    + last_nDices * 2;
        else
            out += "devi aumentare almeno uno dei due valori oppure dimezzare per eccesso il numero di dadi e inserire [1] (perudo) come faccia di dado ";
        System.out.println(out + AnsiColor.RESET);

        Move ret;
        while (true) {
            String move = scanner.nextLine();
            if (move.trim().equals("doubt")) {
                ret = new Doubt(this);
                break;
            }
            if (move.matches("^\\d+ \\d$")) {
                String[] splitted = move.split(" ");
                ret = new Raise(this, Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]));
                if (GameHelper.validateMove(ret, lastMove))
                    break;
            }
            System.out.println(AnsiColor.RED + "Mossa illegale, riprova" + AnsiColor.RESET);
        }
        return ret;
    }

    private Raise getFirstMove(Scanner scanner) {
        System.out.println(AnsiColor.GREEN +
                "Inizi te, digita numero di dadi e faccia dadi separati da uno spazio (es. 3 6), il primo turno non puoi chiamare paco ([1] come faccia)"
                + AnsiColor.RESET);

        Raise ret;
        while (true) {
            String move = scanner.nextLine();
            if (move.matches("^\\d+ \\d$")) {
                String[] splitted = move.split(" ");
                ret = new Raise(this, Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]));
                if (GameHelper.validateMove(ret, null))
                    break;
            }
            System.out.println(AnsiColor.RED + "Mossa illegale, riprova" + AnsiColor.RESET);
        }
        return ret;
    }

    @Override
    public void onGameStart(ArrayList<GameClient> players) throws RemoteException {
        System.out.println(AnsiColor.BLUE + "La partita sta per iniziare");
        System.out.print("Giocatori: ");
        for (GameClient player : players)
            System.out.print(player.getName() + " ");
        System.out.println(AnsiColor.RESET);

        myTurn = false;
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public void refused(String message) throws RemoteException {
        System.out.println(message + ", riprova più tardi!");
    }

    @Override
    public void lobbyJoined() throws RemoteException {
        System.out.println("Ingresso riuscito, in attesa di altri giocatori...");
    }

    @Override
    public void flipped(ArrayList<Integer> dices) throws RemoteException {
        System.out.println(AnsiColor.BLUE + "Inizio round, hai " + dices.size() + " dadi!");
        System.out.println(
                "Hai girato il tuo bicchiere, questi sono i tuoi dadi (gli [1] sono i paco, da considerare come jolly):"
                        + AnsiColor.RESET);
        for (int dice : dices)
            System.out.print("[" + dice + "] ");
        System.out.println();
    }

    @Override
    public void move(Raise lastMove) throws RemoteException {
        myTurn = true;
        this.lastMove = lastMove;
    }

    @Override
    public void firstMove() throws RemoteException {
        myTurn = true;
        this.lastMove = null;
    }

    @Override
    public void opponentMove(Move move) throws RemoteException {
        if (move != null && move.getPlayer() != null) {
            if (move instanceof Doubt)
                System.out.println(AnsiColor.YELLOW + move.getPlayer().getName() + " dubita" + AnsiColor.RESET);
            else {
                Raise raise = (Raise) move;
                System.out.println(AnsiColor.YELLOW +
                        "Secondo " + move.getPlayer().getName() + " ci sono almeno " + raise.getDicesNumber()
                        + " [" + raise.getFace() + "]" + AnsiColor.RESET);
            }
        }
    }

    @Override
    public void youWon() throws RemoteException {
        System.out.println(AnsiColor.GREEN + "Congratulazioni, hai vinto" + AnsiColor.RESET);
        game = false;
    }

    @Override
    public void winner(String winner) throws RemoteException {
        System.out.println(AnsiColor.RED + "Il vincitore è " + winner + "!!" + AnsiColor.RESET);
        game = false;
    }

    @Override
    public void youLost() throws RemoteException {
        System.out.println(AnsiColor.RED + "Hai perso, sei stato eliminato" + AnsiColor.RESET);
    }

    @Override
    public void diceLost(int dicesLeft) throws RemoteException {
        System.out.println(AnsiColor.RED + "Hai perso un dado, te ne rimangono ancora " + dicesLeft + AnsiColor.RESET);
    }

    @Override
    public void imbroglio() throws RemoteException {
        System.out
                .println(AnsiColor.RED + "Hai provato ad imbrogliare, verrai eliminato dalla partita!"
                        + AnsiColor.RESET);
    }

    @Override
    public void endTurn(String looser, int dicesLeft, int faceCalled, int facesRealNumber) throws RemoteException {
        System.out.println(AnsiColor.BLUE + "In tutto ci sono " + facesRealNumber + " [" + faceCalled + "], " + looser
                + " perde un dado, gli rimangono " + dicesLeft + " dadi" + AnsiColor.RESET);
    }

    @Override
    public void notYourTurn() throws RemoteException {
        System.out.println(AnsiColor.RED + "Non è il tuo turno" + AnsiColor.RESET);
    }

    @Override
    public void playerCrashed(String playername) throws RemoteException {
        System.out.println(AnsiColor.RED + playername + " è uscito dalla partita");
    }
}
