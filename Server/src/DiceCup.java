import java.util.ArrayList;

public class DiceCup {
    private int diceNumber;
    private ArrayList<Integer> dices;

    public DiceCup() {
        diceNumber = 5;
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
}
