package com.redhat.quarkus.cafe.domain;

import com.redhat.quarkus.cafe.infrastructure.MockMockerService;
import com.redhat.quarkus.cafe.infrastructure.MockerService;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@QuarkusTest
public class CustomerMockerTest {

    @Inject
    CustomerMocker customerMocker;

    @Inject
    MockerService mockerService;

    static MockerService mock;

    @BeforeAll
    public static void setup() {
        mock = Mockito.mock(MockerService.class);
        QuarkusMock.installMockForType(mock, MockerService.class);
    }

    @Test
    public void testDefaultSetting() {
        assertEquals(CustomerVolume.SLOW, customerMocker.getCustomerVolume());
    }

    @Test
    public void testChangeSetting() {
        assertEquals(CustomerVolume.SLOW, customerMocker.getCustomerVolume());
        customerMocker.setVolumeToBusy();
        assertEquals(CustomerVolume.BUSY, customerMocker.getCustomerVolume());
        customerMocker.setVolumeToDead();
        assertEquals(CustomerVolume.DEAD, customerMocker.getCustomerVolume());
        customerMocker.setVolumeToModerate();
        assertEquals(CustomerVolume.MODERATE, customerMocker.getCustomerVolume());
        customerMocker.setVolumeToWeeds();
        assertEquals(CustomerVolume.WEEDS, customerMocker.getCustomerVolume());
    }

    @Test
    public void testDelaySetting() {
        customerMocker.setVolumeToBusy();
        customerMocker.start();
        try {
            Thread.sleep(31000);
        } catch (InterruptedException e) {
            assertNull(e);
        }
    }
}
