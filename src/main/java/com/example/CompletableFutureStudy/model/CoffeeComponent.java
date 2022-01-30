package com.example.CompletableFutureStudy.model;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.stereotype.Component;

import com.example.CompletableFutureStudy.repository.CoffeeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoffeeComponent implements CoffeeUseCase {

    private final CoffeeRepository coffeeRepository;
    Executor executor = Executors.newFixedThreadPool(10);

    @Override
    public int getPrice(String name) {

        log.info("동기 호출 방식으로 가격 조회 시작");

        return coffeeRepository.getPriceByName(name);
    }

    @Override
    public CompletableFuture<Integer> getPriceAsync(String name) {

        log.info("비동기 호출 방식으로 가격 조회 시작");
        log.info("in getPriceAsync" + Thread.currentThread().getName());

        return CompletableFuture.supplyAsync(() -> {
            log.info("supplyAsync");
            return coffeeRepository.getPriceByName(name);
        }, executor); // 별도로 정의한 풀 사용!
    }

    @Override
    public Future<Integer> getDiscountPriceAsync(Integer price) {
        return null;
    }
}
