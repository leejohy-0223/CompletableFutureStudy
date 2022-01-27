package com.example.CompletableFutureStudy.model;

import java.util.concurrent.Future;

public interface CoffeeUseCase {
    int getPrice(String name); // sync
    Future<Integer> getPriceAsync(String name); // async
    Future<Integer> getDiscountPriceAsync(Integer price); // async
}
