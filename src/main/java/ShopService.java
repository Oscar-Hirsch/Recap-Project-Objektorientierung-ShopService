import java.time.Instant;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.*;

@RequiredArgsConstructor

public class ShopService {
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;

    public Order addOrder(List<String> productIds) {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            try {
                Product productToOrder = productRepo.getProductById(productId).orElseThrow(NullPointerException::new);
                products.add(productToOrder);
            } catch (NullPointerException n) {
                System.out.println("Product mit der Id: " + productId + " konnte nicht bestellt werden!");
                return null;
            }

        }
        Instant orderTime = Instant.now();

        Order newOrder = new Order(UUID.randomUUID().toString(), products, OrderStatus.PROCESSING, orderTime);
        return orderRepo.addOrder(newOrder);

    }


    public List<Order> getOrdersByStatus(OrderStatus statusToFilter) {
        List<Order> orders = orderRepo.getOrders().stream()
                .filter(order -> order.orderStatus() == statusToFilter)
                .collect(Collectors.toList());
        return orders;
    }

    public Order changeOrderStatus(String id, OrderStatus orderStatus) {
        Order order = orderRepo.getOrderById(id);
        Order updatedOrder = order.withOrderStatus(orderStatus);
        try {
            orderRepo.removeOrder(id);
            orderRepo.addOrder(updatedOrder);
            return updatedOrder;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
