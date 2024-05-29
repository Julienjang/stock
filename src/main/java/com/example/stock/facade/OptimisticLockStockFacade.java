package com.example.stock.facade;

import com.example.stock.service.OptimisticLockStockservice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OptimisticLockStockFacade {
    private final OptimisticLockStockservice optimisticLockStockservice;

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (true){
            try {
                optimisticLockStockservice.decrease(id, quantity);

                break;
            } catch (Exception e){
                Thread.sleep(50);
            }
        }
    }


}
