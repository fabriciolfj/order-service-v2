package com.github.fabriciolfj.orderservice.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Mono<Order> submitOrder(final String isbn, final int quantity) {
        return Mono.just(buildRejectOrder(isbn, quantity))
                .flatMap(orderRepository::save);
    }

    public static Order buildRejectOrder(final String isbn, final int quantity) {
        return Order.build(isbn, null, null, quantity, OrderStatus.REJECTED);
    }
}
