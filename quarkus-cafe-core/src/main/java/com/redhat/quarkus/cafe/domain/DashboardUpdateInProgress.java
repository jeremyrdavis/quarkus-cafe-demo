package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class DashboardUpdateInProgress extends DashboardUpdate{

    public DashboardUpdateInProgress() {
    }

    public DashboardUpdateInProgress(LineItemEvent lineItemEvent) {
        super(lineItemEvent);
    }
}
