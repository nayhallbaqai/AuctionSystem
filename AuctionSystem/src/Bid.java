import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by nayhallbaqai on 09/11/2016.
 */
public interface Bid extends Remote {

    public AuctionUser getBidder() throws RemoteException;
    public double getPrice() throws RemoteException;
    public long getItemID() throws RemoteException;
}
