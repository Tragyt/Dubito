import java.rmi.RemoteException;

public class GameClientImpl implements GameClient {
    private final String name;

    public GameClientImpl(String name){
        this.name = name;
    }
 
    @Override
    public void onGameStart() throws RemoteException{
        System.out.println("Game started!");
    }
}
