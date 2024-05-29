package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PessimisticLockStockserviceTest {
    @Autowired
    private PessimisticLockStockservice stockService;
    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void  before(){
        stockRepository.saveAndFlush(new Stock(1L,100L));
    }
    @AfterEach
    public void after(){
        stockRepository.deleteAll();
    }

    @Test
    public void 재고감소(){
        stockService.decrease(1L, 1L);

        Stock stock= stockRepository.findById(1L).orElseThrow();
        assertEquals(99,stock.getQuantity());
    }

    /**
     * 패시미스팅 락의 장점으론 충돌이 빈번하게 일어난다면 옵티미스팅 락보다 성능이 좋다.
     *  락을 통해 업데이트를 제어하기 때문에 데이터의 정합성이 보장됨
     *  단점은 별도의 락을 잡기 때문에 성능 감소
     **/
    @Test
    public void 동시에_100개의_요청() throws InterruptedException {
        int threadCount =100;
        ExecutorService executorService= Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch= new CountDownLatch(threadCount);

        for (int i=0; i<threadCount; i++){
            executorService.submit(()->{
                try {
                    stockService.decrease(1L,1L);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        Stock stock= stockRepository.findById(1L).orElseThrow();
        assertEquals(0,stock.getQuantity());

    }

}