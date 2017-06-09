import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by nayhallbaqai on 29/10/2016.
 */
public class Server {
    public static void main(String args[]){
        AuctionSystem asObject = null;
        try {
            asObject = new AuctionSystemImpl();
            System.out.format("Created server, now advertising it\n");
            LocateRegistry.createRegistry(1099);
            Registry reg = LocateRegistry.getRegistry("localhost", 1099);
            reg.rebind("AS", asObject);
            System.out.format("Advertising completed\n");
        } catch (Exception e) {
            System.out.format("export exception\n");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
