import sun.jvm.hotspot.asm.Register;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

/**
 * Created by nayhallbaqai on 20/11/2016.
 */
public class Test {
    public static void main(String args[]) {
        try {
            Registry reg = LocateRegistry.getRegistry("localhost",1099);
            AuctionSystem system = (AuctionSystem) reg.lookup("AS");
            int userNum = 100000;
            long before = System.currentTimeMillis();
            AuctionUser user = null;
            Item item =null;
            for(int i=0; i<userNum;i++){
                String name = "testUser" + i;
                user = new AuctionUserImpl(name, system.joinSystem());
            }
            long after = System.currentTimeMillis();
            System.out.println(userNum+" User joining system takes time: " +(after-before)/1000.0);

            int itemNUm = 1000;
            before = System.currentTimeMillis();
            for(int i =0; i<itemNUm;i++){
                String itemName = "item_"+i;
                item = system.createNewItem(itemName, user, 100, 10); // static values for minimum value and expired time
            }
            after = System.currentTimeMillis();
            System.out.println(itemNUm+" creation of items takes time: " +(after-before)/1000.0);

            AuctionUser user1 = new AuctionUserImpl("bidder", system.joinSystem());

            int bidNum = 100000;
            before = System.currentTimeMillis();
            for(int i =0; i<bidNum;i++) {
                Random random = new Random();
                long itemID = random.nextInt(1000+ 1);
                if (!item.isInAuction()) continue;
                system.addBidByItemIDandPrice(itemID, i, user1);
            }
            after = System.currentTimeMillis();
            System.out.println(bidNum+" Bid on item takes time: " +(after-before)/1000.0);

        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}
