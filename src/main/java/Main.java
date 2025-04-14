import java.util.List;
import lombok.*;

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

    }
}
