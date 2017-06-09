import java.io.BufferedReader;
import java.io.FileReader;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nayhallbaqai on 30/10/2016.
 */
public class AuctionSystemImpl extends UnicastRemoteObject implements AuctionSystem {
    private static long userID = 0;
    private static long itemID = 0;
    private List<Item> currentItemList;
    private List<Item> historyItemList;

    public AuctionSystemImpl() throws RemoteException {
        currentItemList = new ArrayList<Item>();
        historyItemList = new ArrayList<Item>();
        loadFromFile("backup.txt");
    }

    @Override
    public synchronized long joinSystem() throws RemoteException {
        return ++userID;
    }

    private long getItemID() throws RemoteException{
        return ++itemID;
    }

    private void loadFromFile(String filename){
        AuctionUser user;
        BufferedReader br;
        try {
            user = new AuctionUserImpl("testUser", joinSystem());
            br = new BufferedReader(new FileReader(filename));
            String x;
            String[] lineArray;
            Item item;
            while ( (x = br.readLine()) != null ) {
                lineArray = x.split(",");
                double minValue = Double.parseDouble(String.valueOf(lineArray[1]));
                long expireTime = Long.parseLong(String.valueOf(lineArray[2]));
                createNewItem(lineArray[0], user, minValue, expireTime);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public synchronized Item createNewItem(String name, AuctionUser owner, double minValue, long timeToFinish) throws RemoteException {
        Item item = new ItemImpl(name, getItemID(), owner, minValue, timeToFinish);
        addItem(item);
        return item;
    }

    private synchronized void addItem(Item item) throws RemoteException {

        try{
            item.getOwner().addItem(item);
        }catch (ConnectException e){
            System.out.println("user is not connected");
        }

        if (item.isInAuction()) {
            currentItemList.add(item);
            TimeOut(item, item.getExpireTime());
        }
        else {
            historyItemList.add(item);
            TimeOut(item, 100);
        }
    }

    private void removeItem(Item item) throws RemoteException {
        currentItemList.remove(item);
        try{
            item.getOwner().removeItem(item);
        }catch (ConnectException e){
            System.out.println("user is not connected");
        }
        item.setIsInAuction(false);
        addItem(item);
    }

    private void removeFromHistory(Item item) throws RemoteException {
        historyItemList.remove(item);
    }

    @Override
    public synchronized List<Item> getAllItems() throws RemoteException {
        return currentItemList;
    }

    @Override
    public synchronized List<Item> getHistoryItem() {
        return historyItemList;
    }
    @Override
    public synchronized void addBidByItemIDandPrice(long itemID, double price, AuctionUser user) throws RemoteException {

        Item itemToBidOn = ItemInAuctionByID(itemID);
        Bid highestBid;
        String message;
        try{
            if (itemToBidOn == null) {
                message = "Item is not for Auction";
                user.notificationFromServer(message);
                return;
            }else if (itemToBidOn.getOwner().getUserID() == user.getUserID()){
                message = "You cannot bid on your own item";
                user.notificationFromServer(message);
                return;
            }

            highestBid = itemToBidOn.getMaxBid();
            if (price < itemToBidOn.getMinValue()) message = "Price is less than the minimum value of Item";
            else if (highestBid==null || highestBid.getPrice() < price){
                addBid(itemToBidOn, price, user);
                message = "You are the highest bidder with: " +price;
            }else{
                message = "You price is not greater than the highest bid: " +highestBid.getPrice();
            }
            user.notificationFromServer(message);

        }catch (ConnectException e){
            System.out.println("user is not connected, cannot add bid");
        }
    }

    public void addBid(Item item, double price, AuctionUser bidder) throws RemoteException {

        Bid bid = new BidImpl(item.getItemID(), price, bidder);
        item.addBid(bid);
    }
    @Override
    public synchronized Item ItemInAuctionByID(long ID) throws RemoteException {
        for(int i=0; i<currentItemList.size();i++){
            if (currentItemList.get(i).getItemID() == ID) return currentItemList.get(i);
        }
        return null;
    }

    private String notificationToOwner(Item item) throws RemoteException {
        String message;
        if (item.getWinner() == null) {
            message = "No one bid on the item: "+item.getname();
            return message;
        }
        Bid maxBid = item.getMaxBid();
        message = "Your item: "+item.getname()+ " is sold for:  "+maxBid.getPrice();

        return message;
    }

    private String notificationToBidder(Item item, AuctionUser bidder) throws RemoteException {
        Bid maxBid = item.getMaxBid();
        String message;
        if (item.getWinner().getUserID() == bidder.getUserID()){
            message = "You won the bid for";
            message += "\nitem: "+item.getname();
            message += "\nFor Price: "+maxBid.getPrice();
        }
        else{
            message = "You loss the auction for";
            message += "\nitem: "+item.getname();
            message += "\nThe winning price was "+maxBid.getPrice();
        }

        return message;
    }

    private synchronized void ItemClose(Item item) throws RemoteException {
        removeItem(item);
        if (item.getMaxBid() == null) item.setWinner(null);
        else item.setWinner(item.getMaxBid().getBidder());

        // notifying to the users
        try{
            item.getOwner().notificationFromServer(notificationToOwner(item));
        }catch (ConnectException e){
            System.out.println("user is not connected");
        }
            List<Bid> bids = item.getAllBid();
            List<AuctionUser> notified = new ArrayList<>();

            for(int i=0; i<bids.size(); i++) {
                try {
                    AuctionUser bidder = bids.get(i).getBidder();
                    if(notified.contains(bidder)) continue;
                    bidder.notificationFromServer((notificationToBidder(item, bidder)));
                    notified.add(bidder);
                }catch(ConnectException e){
                    System.out.println("user is not connected");
                }
            }
    }

    private void TimeOut(Item item, long finishInSec) throws RemoteException {
        try{
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (item.isInAuction()) ItemClose(item);
                        else {
                            removeFromHistory(item);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }, finishInSec*1000);
        }catch(OutOfMemoryError e){
            System.out.println("Out of Memory!");
        }
    }

    @Override
    public synchronized String pinging() throws RemoteException {
        return "Hi I am Alive!";
    }

}
