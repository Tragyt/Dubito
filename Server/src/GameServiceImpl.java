import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GameServiceImpl extends UnicastRemoteObject implements GameService {
    private final Lobby lobby;
    private ScheduledFuture<?> countdown;
    private boolean countdownStarted;
    private static final int COUNTDOWN_SECONDS = Integer.parseInt(System.getenv("LOBBY_TIMER_SECONDS"));

    public GameServiceImpl() throws RemoteException {
        lobby = new Lobby();
        countdownStarted = false;
    }

    @Override
    public void JoinGame(GameClient player) throws RemoteException {
        if (lobby.isGameStarted()) {
            player.refused("La partita è già iniziata");
            return;
        }
        if (lobby.isFull()) {
            player.refused("La lobby è piena");
            return;
        }

        lobby.newPlayer(player);

        if (!countdownStarted && lobby.getPlayerNumber() > 1)
            startCountdown();

        if (lobby.isFull()) {
            startGame();
            stopCountdown();
        }
    }

    private void startCountdown() {
        countdown = Executors.newSingleThreadScheduledExecutor().schedule(
                this::startGame, COUNTDOWN_SECONDS, TimeUnit.SECONDS);
        System.out.println("Inizio countdown (" + COUNTDOWN_SECONDS + ")");
        countdownStarted = true;
    }

    private void stopCountdown() {
        if (countdown != null)
            countdown.cancel(false);
        countdownStarted = false;
    }

    private synchronized void startGame() {
        System.out.println("Inizia la partita!");
        new Thread(() -> {
            try {
                lobby.startGame();
            } catch (RemoteException e) {
                lobby.endGame();
                System.out.println("Si è verificato un errore");
            }
        }).start();
    }

    @Override
    public void playerMove(Move move) throws RemoteException {
        lobby.getGame().getTurn().playerMoves(move);
    }
}
