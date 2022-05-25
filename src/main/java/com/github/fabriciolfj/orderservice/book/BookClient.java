package com.github.fabriciolfj.orderservice.book;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class BookClient {

    private static final String BOOKS_ROOT_API = "/books/";
    private final WebClient webClient;

    public BookClient(final WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Book> getBookIsbn(final String isbn) {
        return webClient
                .get()
                .uri(BOOKS_ROOT_API + isbn)
                .retrieve()
                .bodyToMono(Book.class)
                .timeout(Duration.ofSeconds(15), Mono.empty())
                .log()
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> Mono.empty())
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
                .onErrorResume(Exception.class, ex -> Mono.empty());
    }
}
