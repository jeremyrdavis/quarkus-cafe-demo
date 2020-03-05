package com.redhat.quarkus.cafe.web.infrastructure;

import io.quarkus.test.Mock;
import io.smallrye.reactive.messaging.annotations.Channel;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;

import javax.inject.Inject;

@Mock
public class DashboardResourceMock extends DashboardResource {

    @Inject
    @Channel("dashboard")
    PublisherBuilder<String> updates;
}
