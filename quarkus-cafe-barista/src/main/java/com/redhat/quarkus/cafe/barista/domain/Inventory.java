package com.redhat.quarkus.cafe.barista.domain;

import com.redhat.quarkus.cafe.domain.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.stream.Stream;

@ApplicationScoped
public class Inventory {

    Logger logger = LoggerFactory.getLogger(Inventory.class.getName());

    Map<Item, Integer> stock;

    public Inventory() {
        super();
    }

    @PostConstruct
    private void createStock() {
        stock = new HashMap<>();
        Stream.of(Item.values()).forEach(v ->{
            stock.put(v, ThreadLocalRandom.current().nextInt(30,99));
        });
        stock.entrySet().stream().forEach(entrySet -> {
            logger.debug(entrySet.getKey() + " " + entrySet.getValue());
        });
    }

    public Map<Item, Integer> getStock() {
        return stock;
    }
}
