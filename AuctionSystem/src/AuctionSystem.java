import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by nayhallbaqai on 30/10/2016.
 */
public interface AuctionSystem extends Remote {

    public long joinSystem() throws RemoteException;
    public Item createNewItem(String name, AuctionUser owner, double minValue, long timeToFinish) throws RemoteException;
    public List<Item> getAllItems() throws RemoteException;
    public List<Item> getHistoryItem() throws RemoteException;
    public void addBidByItemIDandPrice(long itemID, double price, AuctionUser user) throws RemoteException;
    public Item ItemInAuctionByID(long ID) throws RemoteException;
    public String pinging() throws RemoteException;


}
