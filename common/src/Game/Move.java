import java.io.Serializable;

public abstract class Move implements Serializable {
    private GameClient player;

    protected Move(GameClient player) {
        this.player = player;
    }

    public GameClient getPlayer() {
        return player;
    }
}
