package com.redhat.quarkus.cafe.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * Marker interface for events
 */
@RegisterForReflection
public interface Event {

    public EventType getEventType();
}
