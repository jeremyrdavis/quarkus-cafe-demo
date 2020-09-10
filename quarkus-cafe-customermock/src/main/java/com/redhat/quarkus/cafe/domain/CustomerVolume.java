package com.redhat.quarkus.cafe.domain;

public enum CustomerVolume {

    DEAD(120), SLOW(60), MODERATE(45), BUSY(30), WEEDS(10);

    private int delay;

    private CustomerVolume(int delayTime) {
        this.delay = delayTime;
    }

    public int getDelay() {
        return this.delay;
    }
}
