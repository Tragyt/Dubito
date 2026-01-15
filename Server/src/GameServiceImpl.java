import java.rmi.RemoteException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GameServiceImpl implements GameService {
    private final Lobby lobby;
    private ScheduledFuture<?> countdown;
    private boolean countdownStarted;
    private static final int COUNTDOWN_SECONDS = 30;

    public GameServiceImpl() {
        lobby = new Lobby();
        countdownStarted = false;
    }

    @Override
    public void JoinGame(GameClient player) throws RemoteException {
        lobby.newPlayer(player);

        if (!countdownStarted)
            startCountdown();

        if (lobby.isFull()) {
            startGame();
            stopCountdown();
        }
    }

    private synchronized void startGame() {
        System.out.println("Game started!");
        try {
            lobby.startGame();
        } catch (RemoteException e) {
            System.out.println("Si è verificato un errore");
        }

    }

    private void startCountdown() {
        countdown = Executors.newSingleThreadScheduledExecutor().schedule(
                this::startGame, COUNTDOWN_SECONDS, TimeUnit.SECONDS);
        System.out.println("Countdown started (" + COUNTDOWN_SECONDS + ")");
        countdownStarted = true;
    }

    private void stopCountdown() {
        if (countdown != null) {
            countdown.cancel(false);
            System.out.println("Countdown stopped");
        }
        countdownStarted = false;
    }
}
