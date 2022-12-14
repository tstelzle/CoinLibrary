package com.coinlibrary.backend.service;

import com.coinlibrary.backend.model.Coin;
import com.coinlibrary.backend.model.Edition;
import com.coinlibrary.backend.repository.CoinRepository;
import com.coinlibrary.backend.repository.EditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CoinService {

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private EditionRepository editionRepository;

    public int updateCoin(int coinId) {
        Optional<Coin> coin = coinRepository.findById(coinId);

        if (coin.isPresent()) {
            coin.get()
                    .setAvailable(true);
            coinRepository.save(coin.get());

            return Math.toIntExact(coin.get()
                    .getId());
        }

        return -1;
    }

    public Map<String, List<Coin>> listCoins() {
        Iterable<Coin> coinIterable = coinRepository.findAll();

        return StreamSupport.stream(coinIterable.spliterator(), false)
                .collect(Collectors.groupingBy(Coin::getEditionString));
    }

    public void generateAllCoins() {
        Iterable<Edition> editionIterator = editionRepository.findAll();
        List<Integer> coinSizes = Arrays.asList(1, 2, 5, 10, 20, 50, 100, 200);

        for (Edition edition : editionIterator) {
            if (edition.getEdition() != 0) {
                for (Integer coinSize : coinSizes) {
                    Coin coin = new Coin();
                    coin.setEdition(edition);
                    coin.setSize(coinSize);
                    coin.setSpecial(false);

                    coinRepository.save(coin);
                }
            }
        }
    }
}
