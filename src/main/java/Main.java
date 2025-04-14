import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {

        // create and define product repo with products
        ProductRepo productRepo = new ProductRepo();
        productRepo.addProduct(new Product("1", "Apple"));
        productRepo.addProduct(new Product("2", "Potato"));
        productRepo.addProduct(new Product("3", "Cheesecake"));
        productRepo.addProduct(new Product("4", "Banana"));

        ShopService shopService = new ShopService(productRepo, new OrderMapRepo());
        shopService.addOrder(List.of("1", "2"));
        shopService.addOrder(List.of("2", "3"));
        shopService.addOrder(List.of("3", "4"));

        System.out.println(shopService.getOrdersByStatus(OrderStatus.PROCESSING));

        Order oldestOrder = shopService.getOldestOrderPerStatus(OrderStatus.PROCESSING);
        System.out.println(oldestOrder);

        try {
            //read file
            File file = new File("src/orders.txt");
            Scanner scanner = new Scanner(file);

            //initialize order tracker to check which orders we already did so that we can modify them later
            Map<String, String> orderTracker = new HashMap<>();

            //go through all the commands in the textfile
            while (scanner.hasNextLine()) {
                //get the lines seperately
                String line = scanner.nextLine();

                //get all the contents of the lines in a list
                List<String> commands = new ArrayList<>(List.of(line.split(" ")));


                switch (commands.get(0)) {
                    case "addOrder":
                        //get order ids
                        List<String> arguments = commands.stream().filter(Main::isNumeric).toList();
                        //perform ordering the order
                        Order order = shopService.addOrder(arguments);
                        //add order to order tracker
                        orderTracker.put(commands.get(1), order.id());
                        break;
                    case "setStatus":
                        //retrieve the orderID based on the order indicator given with the order
                        String orderID = orderTracker.get(commands.get(1));
                        //get the order status to change to
                        OrderStatus orderStatus = OrderStatus.valueOf(commands.get(2));
                        //change the order status
                        shopService.changeOrderStatus(orderID, orderStatus);
                        break;
                    case "printOrders":
                        System.out.println(shopService.getAllOrders());
                        break;
                    default:
                        System.out.println("Unknown command: " + commands.get(0));
                        break;

                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }




    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
