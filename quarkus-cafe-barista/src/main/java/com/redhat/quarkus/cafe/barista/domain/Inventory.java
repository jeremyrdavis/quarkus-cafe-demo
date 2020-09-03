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

    static Map<Item, Integer> stock;

    public Inventory() {
        super();
    }

    /*
        COFFEE_BLACK and COFFEE_WITH_ROOM are simply tracked as COFFEE_BLACK
     */
    @PostConstruct
    private void createStock() {
        stock = new HashMap<>();
        Stream.of(Item.values()).forEach(v ->{
            stock.put(v, ThreadLocalRandom.current().nextInt(3,9));
        });
        stock.entrySet().stream().forEach(entrySet -> {
            logger.debug(entrySet.getKey() + " " + entrySet.getValue());
        });

        // Account for coffee
        Integer totalCoffee = stock.get(Item.COFFEE_BLACK).intValue() + stock.get(Item.COFFEE_WITH_ROOM).intValue();
        stock.remove(Item.COFFEE_BLACK);
        stock.remove(Item.COFFEE_WITH_ROOM);
        stock.put(Item.COFFEE_BLACK, totalCoffee);
    }

    public void decrementItem(Item item) throws EightySixException, EightySixCoffeeException {
        if (item.equals(Item.COFFEE_BLACK) || item.equals(Item.COFFEE_WITH_ROOM)) {
            decrementCoffee();
        }else{
            Integer currentValue = stock.get(item);
            if(currentValue <= 0) throw new EightySixException(item);
            stock.replace(item, currentValue - 1);
        }
    }

    /*
        COFFEE_BLACK and COFFEE_WITH_ROOM are simply tracked as COFFEE_BLACK
     */
    private static void decrementCoffee() throws EightySixCoffeeException {
        Integer currentValue = stock.get(Item.COFFEE_BLACK);
        if(currentValue <= 0) throw new EightySixCoffeeException();
        stock.replace(Item.COFFEE_BLACK, currentValue - 1);
    }

    public Map<Item, Integer> getStock() {
        return stock;
    }

    public Integer getTotalCoffee() {
        return stock.get(Item.COFFEE_BLACK);
    }

}
