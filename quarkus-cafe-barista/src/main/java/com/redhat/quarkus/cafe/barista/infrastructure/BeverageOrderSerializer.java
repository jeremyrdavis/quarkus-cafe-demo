package com.redhat.quarkus.cafe.barista.infrastructure;

import com.redhat.quarkus.cafe.barista.domain.BeverageOrder;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.kafka.common.serialization.Serializer;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.Map;

@RegisterForReflection
public class BeverageOrderSerializer implements Serializer<BeverageOrder> {

    private Jsonb jsonb = JsonbBuilder.create();

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public byte[] serialize(String s, BeverageOrder beverageOrder) {
        byte[] retVal = null;
        try {
            retVal = jsonb.toJson(beverageOrder).toString().getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public void close() {

    }
}
