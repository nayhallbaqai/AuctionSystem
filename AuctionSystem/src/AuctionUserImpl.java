import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by nayhallbaqai on 30/10/2016.
 */
public class AuctionUserImpl extends UnicastRemoteObject implements AuctionUser {

    private List<Item> currentUserItems;
    private List<Item> historyUserItem;
    private String name;
    private long userID;

    public AuctionUserImpl(String name, long userID) throws RemoteException {
        this.name = name;
        this.userID = userID;
        currentUserItems = new ArrayList<Item>();
        historyUserItem = new ArrayList<Item>();
    }

    @Override
    public String getname() throws RemoteException {
        return name;
    }

    @Override
    public void setName(String name) throws RemoteException {
        this.name = name;
    }

    @Override
    public long getUserID() throws RemoteException {
        return userID;
    }

    @Override
    public void addItem(Item item) throws RemoteException {
        if(item.isInAuction()){
            currentUserItems.add(item);
        }else
            historyUserItem.add(item);
    }

    @Override
    public void removeItem(Item item) throws RemoteException {
        currentUserItems.remove(item);
    }

    @Override
    public void notificationFromServer(String message) throws RemoteException{
        System.out.println("\n----------------------------");
        System.out.println(message);
        System.out.println("------------------------------");
    }

    @Override
    public void printCurrentItems() throws RemoteException{
        Item item=null;
        for(int i=0; i<currentUserItems.size(); i++) {
            item = currentUserItems.get(i);
            System.out.println("======================");
            System.out.println("ID: " +item.getItemID());
            System.out.println("Name: " +item.getname());
            if (item.getMaxBid() == null) System.out.println("No Bids made yet.");
            else System.out.println("Highest Bid: "+item.getMaxBid().getPrice());
            System.out.println("Minimum Value: "+item.getMinValue());
            System.out.println("Owner: " +item.getOwner().getname());
            System.out.println("Alive: " +item.isInAuction());
        }
        if (item == null) System.out.println("No items to be shown");
    }

    @Override
    public void printHistoryItems() throws RemoteException {
        Item item = null;
        for(int i=0; i<historyUserItem.size(); i++) {
            item = historyUserItem.get(i);
            System.out.println("======================");
            System.out.println("ID: " +item.getItemID());
            System.out.println("Name: " +item.getname());
            System.out.println("Minimum Value: "+item.getMinValue());
            if (item.getWinner()!=null) System.out.println("sold for: "+item.getMaxBid().getPrice());
            System.out.println("Owner: " +item.getOwner().getname());
            System.out.println("Alive: " +item.isInAuction());
        }
        if (item == null) System.out.println("No items to be shown");
    }
}
