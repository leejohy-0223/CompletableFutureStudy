package com.example.CompletableFutureStudy.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CompletableFuture;

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

    @Test
    public void 가격_조회_비동기_호출_콜백_반환없음_테스트() {
        int expectedPrice = 1100;

        CompletableFuture<Void> future = coffeeComponent
            .getPriceAsync("latte")
            .thenAccept(p -> { // 블로킹되지 않는다. 여기에서 특정 출력 등이 가능하다.
                log.info("콜백함수, 가격은 " + p + "원, 하지만 데이터를 반환하지 않는다.");
                assertEquals(expectedPrice, p);
            });

        log.info("아직 최종 데이터를 받지는 않았지만, 다른 작업 수행가능하다. 논블로킹!");
        assertNull(future.join());
    }

    @Test
    public void 가격_조회_비동기_호출_콜백_반환있음_테스트() {
        int expectedPrice = 1100 + 100;

        CompletableFuture<Void> future = coffeeComponent
            .getPriceAsync("latte")
            .thenApply(p -> { // 다른 콜백 함수로 전달 처리도 가능하다.
                log.info("같은 쓰레드로 동작");
                return p + 100;
            })
            .thenAccept(p -> {
                log.info("다시 콜백, 가격은 " + p + "원, 하지만 데이터를 반환하지 않는다.");
                assertEquals(expectedPrice, p);
            });
        log.info("아직 최종 데이터를 받지는 않았지만, 다른 작업 수행가능하다. 논블로킹!");

        assertNull(future.join());
    }

    @Test
    public void 임의_테스트() {
        int expectedPrice = 1200;

        CompletableFuture<Void> future = coffeeComponent
            .getPriceAsync("latte")
            .thenAccept(p -> {
                log.info("콜백, 반환된 값에 100을 더했다.");
                assertEquals(p + 100, expectedPrice);
            });

        log.info("최종데이터를 받기 전 출력될까?");

        assertNull(future.join());
    }

    @Test
    public void sync_Async_test() {
        int expectedPrice = 1200;

        CompletableFuture<Void> future = coffeeComponent
            .getPriceAsync("latte")
            .thenAccept(p -> {
                log.info("then : " + Thread.currentThread().getName());
                log.info("콜백, 반환된 값에 100을 더했다.");
                assertEquals(p + 100, expectedPrice);
            });

        log.info("최종데이터를 받기 전 출력될까?");

        assertNull(future.join());
    }
}