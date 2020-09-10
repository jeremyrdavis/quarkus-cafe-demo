package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.CustomerMocker;
import com.redhat.quarkus.cafe.domain.OrderInCommand;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.test.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Mock
@ApplicationScoped
public class MockMockerService extends MockerService{

    final Logger logger = LoggerFactory.getLogger(MockMockerService.class);

    int count;

    @Override
    public void placeOrders(OrderInCommand orderInCommand) {
        logger.info("order placed");
        count++;
    }

    public int getInvocations() {
        return count;
    }

    void onStop(@Observes ShutdownEvent shutdownEvent) {
        assertTrue(count <= 4);
    }

}
