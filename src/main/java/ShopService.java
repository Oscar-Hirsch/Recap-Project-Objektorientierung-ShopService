import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.*;

public class ShopService {
    private ProductRepo productRepo = new ProductRepo();
    private OrderRepo orderRepo = new OrderMapRepo();

    public Order addOrder(List<String> productIds) {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Product productToOrder = productRepo.getProductById(productId);
            if (productToOrder == null) {
                System.out.println("Product mit der Id: " + productId + " konnte nicht bestellt werden!");
                return null;
            }
            products.add(productToOrder);
        }

        Order newOrder = new Order(UUID.randomUUID().toString(), products, OrderStatus.PROCESSING);

        return orderRepo.addOrder(newOrder);
    }

    public List<Order> getOrdersByStatus(OrderStatus statusToFilter) {
        List<Order> orders = orderRepo.getOrders().stream()
                .filter(order -> order.orderStatus() == statusToFilter)
                .collect(Collectors.toList());
        return orders;
    }

    public void changeOrderStatus(String id, OrderStatus orderStatus) {
        Order order = orderRepo.getOrderById(id);
        try {
            orderRepo.removeOrder(id);
            orderRepo.addOrder(new Order(order.id(), order.products(), orderStatus));
        }
    }
}
