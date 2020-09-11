package com.redhat.quarkus.cafe.domain;

import com.redhat.quarkus.cafe.infrastructure.RESTService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
public class CustomerMockerTest {

    @Inject
    CustomerMocker customerMocker;

    @InjectMock
    @RestClient
    RESTService restService;

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
