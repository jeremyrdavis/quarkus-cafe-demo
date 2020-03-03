package com.redhat.quarkus.cafe.infrastructure;

import com.redhat.quarkus.cafe.domain.*;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@ApplicationScoped
public class CafeCore {

    Logger logger = Logger.getLogger(CafeCore.class);

    @Inject
    Cafe cafe;

    @Inject @Channel("ordersout")
    Emitter<String> ordersOutEmitter;

    @Inject
    @RestClient
    DashboardService dashboardService;

    Jsonb jsonb = JsonbBuilder.create();

    //TODO Create and persist an Order
    public List<OrderEvent> orderIn(CreateOrderCommand createOrderCommand) throws ExecutionException, InterruptedException {

        List<OrderEvent> allEvents = cafe.orderIn(createOrderCommand);

        try {
            allEvents.forEach(orderEvent -> {
                ordersOutEmitter.send(jsonb.toJson(orderEvent));
            });
            dashboardService.updatedDashboard(convertJson(allEvents));
        } catch (Exception e) {
            System.out.println(e);
        }
/*
        CompletableFuture.runAsync(() -> {
            kafkaService.updateOrders(allEvents);
        }).thenRun(() -> { dashboardService.updatedDashboard(convertJson(allEvents));})
                .handle((r,e) -> {})
                .get();
*/
        return allEvents;
/*
        return kafkaService.updateOrders(allEvents)
                .thenApply(v -> {
            return allEvents;
        }).thenCompose(this::ordersIn);
*/
    }

    private List<DashboardUpdate> convertJson(List<OrderEvent> orderEvents) {
        return orderEvents.stream()
                .map(orderEvent -> {
            System.out.println("\nConverting: " + orderEvent.toString() +"\n");
            OrderStatus status;
            switch(orderEvent.eventType){
                case BEVERAGE_ORDER_IN:
                    status = OrderStatus.IN_QUEUE;
                    break;
                case BEVERAGE_ORDER_UP:
                    status = OrderStatus.READY;
                    break;
                case KITCHEN_ORDER_IN:
                    status = OrderStatus.IN_QUEUE;
                    break;
                case KITCHEN_ORDER_UP:
                    status = OrderStatus.READY;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown status" + orderEvent.eventType);
            }
            return new DashboardUpdate(
                    orderEvent.orderId,
                    orderEvent.itemId,
                    orderEvent.name,
                    orderEvent.item,
                    status);
        }).collect(Collectors.toList());
    }

    /*
        Convert the OrderEvent JSON to the JSON that the Web UI expects and call the REST endpoint
     */
    private CompletableFuture<List<OrderEvent>> ordersIn(final List<OrderEvent> orderEvents) {

        return CompletableFuture.supplyAsync(() -> {

            List<DashboardUpdate> dashboardUpdates = orderEvents.stream()
                    .map(orderEvent -> {
                        System.out.println("\nConverting: " + orderEvent.toString() +"\n");
                        OrderStatus status;
                        switch(orderEvent.eventType){
                            case BEVERAGE_ORDER_IN:
                                status = OrderStatus.IN_QUEUE;
                                break;
                            case BEVERAGE_ORDER_UP:
                                status = OrderStatus.READY;
                                break;
                            case KITCHEN_ORDER_IN:
                                status = OrderStatus.IN_QUEUE;
                                break;
                            case KITCHEN_ORDER_UP:
                                status = OrderStatus.READY;
                                break;
                            default:
                                throw new IllegalArgumentException("Unknown status" + orderEvent.eventType);
                        }
                        return new DashboardUpdate(
                                orderEvent.orderId,
                                orderEvent.itemId,
                                orderEvent.name,
                                orderEvent.item,
                                status);
                    }).collect(Collectors.toList());
            dashboardService.updatedDashboard(dashboardUpdates);
/*
            String json = jsonb.toJson(dashboardUpdates);
            System.out.println("\n"+json+"\n");
*/
            return orderEvents;
        });
    }

    private CompletableFuture<Void> updateDashboard(List<OrderEvent> orderEvents) {

        return CompletableFuture.supplyAsync(() ->{

            dashboardService.updatedDashboard(convertOrderEventsToDashboardUpdates(orderEvents));
            return null;
        });
    }

    private List<DashboardUpdate> convertOrderEventsToDashboardUpdates(List<OrderEvent> orderEvents){

        return orderEvents.stream()
                .map(orderEvent -> {
                    System.out.println("\nConverting: " + orderEvent.toString() +"\n");
                    OrderStatus status;
                    switch(orderEvent.eventType){
                        case BEVERAGE_ORDER_IN:
                            status = OrderStatus.IN_QUEUE;
                            break;
                        case BEVERAGE_ORDER_UP:
                            status = OrderStatus.READY;
                            break;
                        case KITCHEN_ORDER_IN:
                            status = OrderStatus.IN_QUEUE;
                            break;
                        case KITCHEN_ORDER_UP:
                            status = OrderStatus.READY;
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown status" + orderEvent.eventType);
                    }
                    return new DashboardUpdate(
                            orderEvent.orderId,
                            orderEvent.itemId,
                            orderEvent.name,
                            orderEvent.item,
                            status);
                }).collect(Collectors.toList());
    }


    public void orderUp(List<OrderEvent> orderEvents) {

        dashboardService.updatedDashboard(convertOrderEventsToDashboardUpdates(orderEvents));
    }
}
