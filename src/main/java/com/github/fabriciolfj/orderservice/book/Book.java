package com.github.fabriciolfj.orderservice.book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Book(String isbn, String title, String author, Double price){ }
