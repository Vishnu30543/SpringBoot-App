package in.store.creatorstore.services;

import in.store.creatorstore.dto.OrderItemRequest;
import in.store.creatorstore.dto.OrderRequest;
import in.store.creatorstore.entities.Order;
import in.store.creatorstore.entities.OrderItem;
import in.store.creatorstore.entities.Product;
import in.store.creatorstore.repositories.OrderRepository;
import in.store.creatorstore.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(OrderRequest orderRequest) {

        BigDecimal totalPrice = BigDecimal.ZERO;

        Order order = new Order();
        order.setCustomerName(orderRequest.getCustomerName());
        order.setCustomerEmail(orderRequest.getCustomerEmail());
        order.setStatus("CONFIRMED");

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : orderRequest.getItems()) {

            Product product = productRepository.findById(
                    itemRequest.getProductId()
            ).orElseThrow(() -> new RuntimeException(
                    "Product not found with id " + itemRequest.getProductId()
            ));

            // Check stock availability
            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new RuntimeException(
                        "Not enough stock for product " + itemRequest.getProductId()
                );
            }

            // Calculate total price
            BigDecimal priceOfItem = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            totalPrice = totalPrice.add(priceOfItem);

            // Update stock
            product.setStockQuantity(
                    product.getStockQuantity() - itemRequest.getQuantity()
            );

            productRepository.save(product);

            // Create OrderItem
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .priceAtPurchase(product.getPrice())
                    .build();

            orderItems.add(orderItem);
        }

        order.setTotalPrice(totalPrice);
        order.setOrderItems(orderItems);

        return orderRepository.save(order);
    }
}
