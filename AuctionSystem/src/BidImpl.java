import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by nayhallbaqai on 09/11/2016.
 */
public class BidImpl extends UnicastRemoteObject implements Bid {

    private AuctionUser bidder;
    private double priceBid;
    private long itemID;

    public BidImpl(long itemId, double price, AuctionUser user) throws RemoteException {
        itemID = itemId;
        priceBid = price;
        this.bidder = user;
    }

    @Override
    public AuctionUser getBidder() throws RemoteException {
        return bidder;
    }

    @Override
    public double getPrice() throws RemoteException {
        return priceBid;
    }

    @Override
    public long getItemID() throws RemoteException {
        return itemID;
    }
}
