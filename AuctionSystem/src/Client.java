import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 * Created by nayhallbaqai on 29/10/2016.
 */
public class Client {

    public static void main(String args[]){
        System.out.format("Client starting\n");
        AuctionSystem system = null;
        AuctionUser user= null;
        Item item;
        String task;
        Scanner standardInput = new Scanner(System.in);

        try {
            Registry reg = LocateRegistry.getRegistry("localhost",1099);
            system = (AuctionSystem) reg.lookup("AS");

            System.out.println("Enter your username: ");
            String name = standardInput.nextLine();
            user = new AuctionUserImpl(name, system.joinSystem());


        } catch (Exception e) {
            System.out.format("Error joining user to system\n");
            e.printStackTrace();
            System.exit(1);
        }
        do {
            System.out.println();
            System.out.println("Menu:");
            System.out.println("Show your auctions press            >>      1");
            System.out.println("Show your history auctions press    >>      2");
            System.out.println("Show all current auctions press     >>      3");
            System.out.println("Show recent closed auctions press   >>      4");
            System.out.println("To add auction press                >>      5");
            System.out.println("To bid on an auction press          >>      6");
            System.out.println("To ping the Auction System press    >>      7");
            System.out.println("To Quit please press                 >>     q");
            System.out.println();
            System.out.println("Enter the task: ");
            task = standardInput.nextLine();

            try{
                switch (task){
                    case "1":
                        System.out.println("Showing your current auctions\n");
                        user.printCurrentItems();
                        break;
                    case "2":
                        System.out.println("Showing your history auctions\n");
                        user.printHistoryItems();
                        break;
                    case "3":
                        System.out.println("showing all current auctions \n");
                        item=null;
                        for(int i=0; i<system.getAllItems().size();i++){
                            item = system.getAllItems().get(i);
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
                        break;
                    case "4":
                        System.out.println("Showing recent closed auctions\n");
                        item = null;
                        for(int i= 0; i<system.getHistoryItem().size(); i++){
                            item = system.getHistoryItem().get(i);
                            System.out.println("======================");
                            System.out.println("ID: " +item.getItemID());
                            System.out.println("Name: " +item.getname());
                            System.out.println("Minimum Value: "+item.getMinValue());
                            if (item.getWinner() != null) System.out.println("Sold for: "+item.getMaxBid().getPrice());
                            System.out.println("Owner: " +item.getOwner().getname());
                            System.out.println("Alive: " +item.isInAuction());
                        }
                        if (item == null) System.out.println("No items to be shown");
                        break;
                    case "5":
                        System.out.println("Adding auction");

                        System.out.println("Item name: ");
                        String name = standardInput.nextLine();

                        System.out.println("Minimum Value: ");
                        String value = standardInput.nextLine();
                        double minValue = Double.parseDouble(value);

                        System.out.println("Expired time in seconds: ");
                        String time = standardInput.nextLine();
                        long expireTime = Long.parseLong(time);

                        item = system.createNewItem(name, user, minValue, expireTime);
                        // Printing new auction
                        System.out.println("\n==============");
                        System.out.println("Item Added");
                        System.out.println("ID: " +item.getItemID());
                        System.out.println("Name: " +item.getname());
                        System.out.println("Minimum Value: "+item.getMinValue());
                        System.out.println("Owner: " +item.getOwner().getname());
                        System.out.println("Alive: " +item.isInAuction());
                        break;
                    case "6":
                        System.out.println("Biding on an auction\n");
                        System.out.println("Enter the item ID: ");
                        String itemLine = standardInput.nextLine();
                        long itemID = Long.parseLong(itemLine);

                        System.out.println("Enter the price to bid: ");
                        String priceLine = standardInput.nextLine();
                        double price = Double.parseDouble(priceLine);

                        system.addBidByItemIDandPrice(itemID, price, user);
                        break;
                    case "7":
                        System.out.println("Pinging Auction System");
                        System.out.println(system.pinging());
                        break;

                    default: throw new Exception();
                }
            }catch (Exception e) {
                if (task.length()==0 || task.charAt(0)!='q')
                    System.out.println("You must enter either '1', '2', '3', '4', '5', '6', '7' or 'q'.");
                else
                    break;
            }
            System.out.println();
        }while (true);
        standardInput.close();

        System.out.println("Exiting System");
        System.exit(1);
    }
}
