import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by nayhallbaqai on 30/10/2016.
 */
public interface AuctionUser extends Remote {

    public String getname() throws RemoteException;
    public void setName(String name) throws RemoteException;
    public long getUserID()throws RemoteException;
    public void addItem(Item item) throws RemoteException;
    public void removeItem(Item item) throws RemoteException;
    public void notificationFromServer(String message) throws RemoteException;
    public void printCurrentItems() throws RemoteException;
    public void printHistoryItems() throws RemoteException;
}
