
public class GameHelper {
    public static boolean validateMove(Move currentMove, Move lastMove) {

        if (lastMove != null && lastMove instanceof Doubt) // non può esserci una mossa dopo un dubito
        {
            System.out.println("[Error]Mossa dopo dubito");
            return false;
        }

        if (currentMove instanceof Doubt && lastMove == null) // non si può dubitare come prima mossa
        {
            System.out.println("[Error]Dubito come prima mossa");
            return false;
        }
        if (currentMove instanceof Doubt) // non serve confronto con mossa precedente
            return true;

        Raise lastRaise = (Raise) lastMove;
        Raise currentRaise = (Raise) currentMove;
        int last_nDices = lastMove != null ? lastRaise.getDicesNumber() : 0;
        int last_face = lastMove != null ? lastRaise.getFace() : 0;
        int nDices = currentRaise.getDicesNumber();
        int face = currentRaise.getFace();

        // mosse illegali
        if (nDices <= 0 || face <= 0 || face > 6 || (face == last_face && nDices == last_nDices))
            return false;

        if (last_face == 1) // se l'ultima mossa era paco
        {
            // rimane paco e aumenta il numero di dadi
            boolean case1 = (face == 1 && nDices > last_nDices);

            // cambia faccia e chiama un numero di dadi di almeno il doppio + 1
            boolean case2 = (face > 1 && nDices > (last_nDices * 2));
            return case1 || case2;
        }

        if (face == 1) // se la nuova mossa è paco
        {
            if (last_nDices == 0)
                return false;
            // il numero di dadi dev'essere maggiore della metà
            return nDices > (last_nDices / 2);
        }

        // almeno uno dei due valori dev'essere aumentato
        return (face >= last_face && nDices >= last_nDices);
    }
}
