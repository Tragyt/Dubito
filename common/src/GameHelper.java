import java.util.Map;

public class GameHelper {
    public static boolean validateMove(Map.Entry<Integer, Integer> currentMove, Map.Entry<Integer, Integer> lastMove) {
        int last_nDices = lastMove.getKey();
        int last_face = lastMove.getValue();
        int nDices = currentMove.getKey();
        int face = currentMove.getValue();

        // mosse illegali
        if (nDices < 0 || face < 0 || face > 6 || (face == last_face && nDices == last_nDices))
            return false;

        if (nDices == 0 && face == 0) // dubito
            return true;

        if (last_face == 1) // se l'ultima mossa era paco
        {
            // rimane paco e aumenta il numero di dadi
            boolean case1 = (face == 1 && nDices > last_nDices);

            // cambia faccia e chiama un numero di dadi di almeno il doppio + 1
            boolean case2 = (face > 1 && nDices > (last_nDices * 2));
            return case1 || case2;
        }

        if (face == 1) // se la nuova mossa è paco
            // il numero di dadi dev'essere maggiore della metà
            return nDices > (last_nDices / 2);

        // almeno uno dei due valori dev'essere aumentato
        return (face >= last_face && nDices >= last_nDices);
    }
}
