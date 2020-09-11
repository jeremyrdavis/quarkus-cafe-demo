package com.redhat.quarkus.cafe.domain;

public enum CustomerVolume {

    DEAD("120s"), DEV("5s"), SLOW("60s"), MODERATE("45s"), BUSY("30s"), WEEDS("10s");

    private String delay;

    private CustomerVolume(String delayTime) {
        this.delay = delayTime;
    }

    public String getDelay() {
        return this.delay;
    }
}
