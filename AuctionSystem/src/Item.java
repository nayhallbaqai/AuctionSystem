import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

/**
 * Created by nayhallbaqai on 30/10/2016.
 */
public interface Item extends Remote{

    public long getItemID() throws RemoteException;
    public String getname() throws RemoteException;
    public double getMinValue() throws RemoteException;
    public void addBid(Bid bid) throws RemoteException;
    public List<Bid> getAllBid() throws RemoteException;
    public AuctionUser getOwner() throws RemoteException;
    public long getExpireTime() throws RemoteException;
    public boolean isInAuction() throws RemoteException;
    public void setIsInAuction(boolean isInAuction) throws RemoteException;
    public Bid getMaxBid() throws RemoteException;
    public AuctionUser getWinner() throws RemoteException;
    public void setWinner(AuctionUser winnerUser) throws RemoteException;
}
