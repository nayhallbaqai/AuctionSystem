import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nayhallbaqai on 30/10/2016.
 */
public class ItemImpl extends UnicastRemoteObject implements Item {

    private long itemID;
    private String name;
    private List<Bid> bids;
    private double minValue;
    private AuctionUser owner;
    private AuctionUser winner;
    private long expiredTime;
    private boolean stillInAuction;

    public ItemImpl(String name, long itemID, AuctionUser owner, double minimumValue, long expiredTime) throws RemoteException {
        this.name = name;
        this.itemID = itemID;
        this.owner = owner;
        minValue = minimumValue;
        this.expiredTime = expiredTime;
        stillInAuction = true;
        winner = null;
        bids = new ArrayList<Bid>();
    }

    @Override
    public long getItemID() throws RemoteException {
        return itemID;
    }

    @Override
    public String getname() throws RemoteException{
        return name;
    }

    @Override
    public double getMinValue() throws RemoteException {
        return minValue;
    }

    @Override
    public void addBid(Bid bid) throws RemoteException{
        bids.add(bid);
    }

    @Override
    public List<Bid> getAllBid() throws RemoteException{
        return bids;
    }

    @Override
    public AuctionUser getOwner() throws RemoteException{
        return owner;
    }

    @Override
    public long getExpireTime() throws RemoteException {
        return expiredTime;
    }

    @Override
    public boolean isInAuction() throws RemoteException {
        return stillInAuction;
    }

    @Override
    public void setIsInAuction(boolean isInAuction) throws RemoteException {
        stillInAuction = isInAuction;
    }

    @Override
    public Bid getMaxBid() throws RemoteException {
        double maxBidPrice = 0;
        Bid maxBid = null;
        for (int i=0; i<bids.size(); i++){
            if(maxBidPrice < bids.get(i).getPrice()){
                maxBidPrice = bids.get(i).getPrice();
                maxBid = bids.get(i);
            }
        }
        return maxBid;
    }

    @Override
    public AuctionUser getWinner() throws RemoteException {
        return winner;
    }

    @Override
    public void setWinner(AuctionUser winnerUser) throws RemoteException {
        winner = winnerUser;
    }

}
