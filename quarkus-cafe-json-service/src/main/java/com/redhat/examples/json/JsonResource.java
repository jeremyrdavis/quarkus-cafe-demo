package com.redhat.examples.json;

import com.redhat.quarkus.cafe.domain.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Path("/json")
@Produces(MediaType.APPLICATION_JSON)
public class JsonResource {

    @GET
    @Path("/CreateOrderCommand")
    public OrderInCommand createOrderCommand() {
        return mockCreateOrderCommand();
    }

    private OrderInCommand mockCreateOrderCommand() {

        OrderInCommand orderInCommand = new OrderInCommand();
        orderInCommand.beverages = mockBeverages();
        orderInCommand.kitchenOrders = mockKitchenOrder();
        orderInCommand.id = UUID.randomUUID().toString();
        return orderInCommand;

    }

    @GET
    @Path("/InQueueUpdate")
    public InQueueUpdate mockInQueueUpdate() {

        return new InQueueUpdate(
                new OrderInEvent(EventType.BEVERAGE_ORDER_IN,
                        UUID.randomUUID().toString(),
                        UUID.randomUUID().toString(),
                        "Winnie",
                        Item.CAKEPOP));
    }

    @GET
    @Path("/LineItem")
    public LineItem mockLineItem() {

        return new LineItem(Item.CAKEPOP, "Winnie");
    }

    @GET
    @Path("/OrderInEvent")
    public OrderInEvent mockOrderInEvent() {
        return new OrderInEvent(EventType.BEVERAGE_ORDER_IN,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                "Winnie",
                Item.CAKEPOP);
    }

    @GET
    @Path("/OrderUpEvent")
    public OrderUpEvent mockOrderUpEvent() {
        return new OrderUpEvent(EventType.BEVERAGE_ORDER_UP,
                UUID.randomUUID().toString(),
                "Winnie",
                Item.CAKEPOP,
                UUID.randomUUID().toString(),
                "Barista Joe");
    }

    @GET
    @Path("/OrderReadyUpdate")
    public OrderReadyUpdate mockOrderReadyUpdate() {
        return new OrderReadyUpdate(new OrderUpEvent(EventType.BEVERAGE_ORDER_UP,
                UUID.randomUUID().toString(),
                "Winnie",
                Item.CAKEPOP,
                UUID.randomUUID().toString(),
                "Barista Joe"));
    }

    private List<LineItem> mockKitchenOrder() {

        return Arrays.asList(
                new LineItem((Item.CAKEPOP), "Winnie"),
                new LineItem(Item.CROISSANT, "Eeyore"));
    }

    private List<LineItem> mockBeverages() {

        return Arrays.asList(
                new LineItem(Item.COFFEE_BLACK, "Winnie"),
                new LineItem(Item.ESPRESSO, "Eeyore"));
    }

}
