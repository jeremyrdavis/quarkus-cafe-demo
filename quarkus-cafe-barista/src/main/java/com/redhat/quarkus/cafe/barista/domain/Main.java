package com.redhat.quarkus.cafe.barista.domain;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@QuarkusMain
public class Main {

    static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) {
        logger.info("starting");
        Quarkus.run(args);
    }
}
