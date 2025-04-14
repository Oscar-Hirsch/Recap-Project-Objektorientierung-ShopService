import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    private ShopService shopService;
    private ProductRepo repo;

    @BeforeEach
    void setUp() {
        repo = new ProductRepo();
        repo.addProduct(new Product("1", "Apfel"));
        repo.addProduct(new Product("2", "Birne"));
        repo.addProduct(new Product("3", "Banane"));

        shopService = new ShopService(repo, new OrderListRepo());
    }

    @Test
    void addOrderTest() {
        //GIVEN
        List<String> productsIds = List.of("1");
        Instant orderTime = Instant.now();

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-11", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING, orderTime);
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectNull() {
        //GIVEN
        ShopService shopService = new ShopService(new ProductRepo(), new OrderListRepo());
        List<String> productsIds = List.of("1", "8");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        assertNull(actual);
    }

    @Test
    void changeOrderStatus_ShouldHaveOrderStatusProcessing_WhenOrderIsNewlyCreated() {
        //WHEN
        Order order = shopService.addOrder(List.of("1", "2"));

        //THEN
        OrderStatus expected = OrderStatus.PROCESSING;
        OrderStatus actual = order.orderStatus();

        assertEquals(expected, actual);


    }

    @Test
    void changeOrderStatus_ShouldHaveOrderStatusCompleted_WhenOrderIsCompleted() {
        //GIVEN
        Order order = shopService.addOrder(List.of("1","2"));

        //WHEN
        Order updatedOrder = shopService.changeOrderStatus(order.id(), OrderStatus.COMPLETED);

        //THEN
        OrderStatus actual = updatedOrder.orderStatus();
        OrderStatus expected = OrderStatus.COMPLETED;

        assertEquals(expected, actual);



    }

    @Test
    void getOrdersByStatus_returnsAllNewlyCreatedOrders_WhenCalledWithProcessing() {
        //GIVEN
        Order order1 = shopService.addOrder(List.of("1", "2"));
        Order order2 = shopService.addOrder(List.of("1", "2"));

        //WHEN
        List<Order> orderInProcessing = shopService.getOrdersByStatus(OrderStatus.PROCESSING);

        //THEN
        assertTrue(orderInProcessing.containsAll(List.of(order1, order2)));
    }

    @Test
    void getOrdersByStatus_returnsOnlyOneOrder_WhenCalledWithCompleted() {
        //GIVEN
        Order order1 = shopService.addOrder(List.of("1", "2"));
        Order order2 = shopService.addOrder(List.of("1", "2"));
        Order updatedOrder = shopService.changeOrderStatus(order2.id(), OrderStatus.COMPLETED);

        //WHEN
        List<Order> orderInProcessing = shopService.getOrdersByStatus(OrderStatus.COMPLETED);

        //THEN
        assertTrue(orderInProcessing.contains(updatedOrder) && !(orderInProcessing.contains(order2)));
    }

    @Test
    void getOldestOrderPerStatus() {
        //GIVEN
        Order olderOrder = shopService.addOrder(List.of("1", "2"));
        Order youngerOrder = shopService.addOrder(List.of("1", "2"));

        //WHEN
        Order actual = shopService.getOldestOrderPerStatus(OrderStatus.PROCESSING);
        Order expected = olderOrder;

        //THEN
        assertEquals(expected, actual);
    }
}
