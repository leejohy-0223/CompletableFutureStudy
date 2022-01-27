package com.example.CompletableFutureStudy.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.CompletableFutureStudy.repository.CoffeeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    CoffeeComponent.class,
    CoffeeRepository.class
})
class CoffeeComponentTest {

    @Autowired
    private CoffeeComponent coffeeComponent;

    @Test
    public void 가격_조회_동기_블로킹_호출테스트() {
        int expectedPrice = 1100;
        int resultPrice = coffeeComponent.getPrice("latte");
        log.info("가격 전달 받음");
        coffeeComponent.getPrice("americano");

        assertEquals(expectedPrice, resultPrice);
    }

    @Test
    public void 가격_조회_비동기_블로킹_호출테스트() {
        int expectedPrice = 1100;

        CompletableFuture<Integer> future = coffeeComponent.getPriceAsync("latte");
        log.info("아직 최종 데이터를 전달받지는 않았지만, 다른 작업 수행 가능");
        Integer resultPrice = future.join(); // 이 시점에서는 블로킹된다.
        log.info("최종 가격 전달 받음");

        assertEquals(expectedPrice, resultPrice);
    }




}