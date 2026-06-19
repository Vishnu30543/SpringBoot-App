package in.store.creatorstore.repositories;

import in.store.creatorstore.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemReposiotory extends JpaRepository<OrderItem,Long> {
}
