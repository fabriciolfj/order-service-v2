package com.github.fabriciolfj.orderservice.domain;

import com.github.fabriciolfj.orderservice.book.Book;
import com.github.fabriciolfj.orderservice.book.BookClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookClient bookClient;

    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Mono<Order> submitOrder(final String isbn, final int quantity) {
        return this.bookClient.getBookIsbn(isbn)
                .map(book -> buildAcceptedOrder(book, quantity))
                .defaultIfEmpty(buildRejectOrder(isbn, quantity))
                .flatMap(orderRepository::save);
    }

    public static Order buildAcceptedOrder(final Book book, int quantity) {
        return Order.build(book.isbn(), book.title() + " - " + book.author(),
                book.price(), quantity, OrderStatus.ACCEPTED);
    }

    public static Order buildRejectOrder(final String isbn, final int quantity) {
        return Order.build(isbn, null, null, quantity, OrderStatus.REJECTED);
    }
}
