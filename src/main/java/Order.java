import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.*;

@With

public record Order(String id, List<Product> products, OrderStatus orderStatus, Instant orderTime) {

}
