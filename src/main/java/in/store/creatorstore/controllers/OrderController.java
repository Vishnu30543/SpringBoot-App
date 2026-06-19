package in.store.creatorstore.controllers;

import in.store.creatorstore.dto.OrderRequest;
import in.store.creatorstore.entities.Order;
import in.store.creatorstore.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public Order createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    // Get all orders
    public List<Order> getAll0rders() {
        // TODO: to be implemented
        return null;
    }

    // Get Order by ID
    public Order getOrderByID() {
        // TODO: to be implemented
        return null;
    }
}
