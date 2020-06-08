import org.apache.camel.Exchange;
import org.apache.camel.model.rest.RestBindingMode;
import com.redhat.quarkus.cafe.domain.LineItem;
import com.redhat.quarkus.cafe.domain.Item;
import java.util.List;
import java.util.ArrayList;
import com.redhat.quarkus.cafe.domain.Order;
import com.redhat.quarkus.cafe.domain.CreateOrderCommand;
//import com.redhat.quarkus.cafe.domain.Beverage;

public class RestWithUndertow extends org.apache.camel.builder.RouteBuilder {
    
    private final String order = "{'beverages': [{'item': 'ESPRESSO_DOUBLE','name': 'Mickey'},{'item': 'COFFEE_BLACK','name': 'Minnie'}]}";
    @Override
    public void configure() throws Exception {
        LineItem li = new LineItem(Item.CAPPUCCINO,"Mary");
        LineItem li2 = new LineItem(Item.ESPRESSO_DOUBLE,"Mickey");
        List<LineItem> list = new ArrayList<LineItem>();
        list.add(li);
        list.add(li2);
        Order o = new Order(list);
        CreateOrderCommand coc = new CreateOrderCommand(list, null);
        //JacksonDataFormat df = new JacksonDataFormat(CreateOrderCommand.class);
        //List<Order> beverages = new ArrayList(2);
        //beverages.add(new Order(Beverage.COFFEE_WITH_ROOM, "Mickey"));
        //beverages.add(new Order(Beverage.COFFEE_BLACK, "Minnie"));
        restConfiguration()
            .component("undertow")
            .host("0.0.0.0")
            .port("8080")
            .bindingMode(RestBindingMode.auto);

        rest()
            .get("/hello")
            .to("direct:hello")
            .post("/order").type(GrubHubOrder.class).consumes("application/json")
            .produces("application/json")
            .to("direct:order");

        from("direct:hello")
            .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
            .transform().simple("Hello!");

        from("direct:order")
            .log("Body is ${body}")
            //.setBody(constant(order))
            .setBody(constant(coc))
            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
            .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
            .setHeader("Accept",constant("application/json"))
            .log("Test Create Order comamnd is " + coc.toString())
            .log("Body after transformation is ${body} with headers: ${headers}");

}

static class GrubHubOrder {
	private String orderId;
	private String orderItem;
    private String name;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(final String orderId) {
        this.orderId = orderId;
    }

    public String getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(final String orderItem) {
        this.orderItem = orderItem;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GrubHubOrder [name=" + name + ", orderId=" + orderId + ", orderItem=" + orderItem + "]";
    }
}
}
