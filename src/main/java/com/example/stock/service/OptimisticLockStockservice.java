package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OptimisticLockStockservice {
    private final StockRepository stockRepository;


    @Transactional
    public void decrease(Long id, Long quantity){
        // Stock 조회
        //재고를 감소시킨뒤
        //갱신된 값 저장
        Stock stock= stockRepository.findByIdWithOptimisticLock(id);
        stock.decrease(quantity);

        stockRepository.save(stock);
    }
}
