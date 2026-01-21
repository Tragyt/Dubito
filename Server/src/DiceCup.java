import java.util.ArrayList;

public class DiceCup {
    private int diceNumber;
    private ArrayList<Integer> dices;
    private boolean crash;
    private String playername;

    public DiceCup(String playername) {
        diceNumber = 5;
        crash = false;
        this.playername = playername;
    }

    public int getDiceNumber() {
        return diceNumber;
    }

    public int removeDice() {
        diceNumber -= 1;
        return diceNumber;
    }

    public ArrayList<Integer> flip() {
        dices = new ArrayList<>();
        for (int i = 0; i < diceNumber; i++) {
            int n = (int) (Math.random() * 6) + 1;
            dices.add(n);
        }
        return dices;
    }

    public int numberOf(int n) {
        return (int) dices.stream().filter(i -> i == n || i == 1).count();
    }

    public void disqualify() {
        diceNumber = 0;
    }

    public void crashed() {
        diceNumber = 0;
        crash = true;
    }

    public boolean isCrashed() {
        return crash;
    }

    public String getPlayername() {
        return playername;
    }
}
