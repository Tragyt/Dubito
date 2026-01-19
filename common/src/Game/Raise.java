public class Raise extends Move {
    private int dicesNumber;
    private int face;

    public Raise(GameClient player, int dicesNumber, int face) {
        super(player);
        this.dicesNumber = dicesNumber;
        this.face = face;
    }

    public int getDicesNumber() {
        return dicesNumber;
    }

    public int getFace() {
        return face;
    }
}
