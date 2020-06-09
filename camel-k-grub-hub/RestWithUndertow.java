import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.model.rest.RestBindingMode;
import com.redhat.quarkus.cafe.domain.LineItem;
import com.redhat.quarkus.cafe.domain.Item;
import java.util.List;
import java.util.ArrayList;
import com.redhat.quarkus.cafe.domain.Order;
import com.redhat.quarkus.cafe.domain.CreateOrderCommand;
import com.redhat.grubhub.cafe.domain.GrubHubOrder;
import com.redhat.grubhub.cafe.domain.GrubHubOrderItem;
//import com.redhat.quarkus.cafe.domain.Beverage;
import org.apache.camel.component.jackson.JacksonDataFormat;

public class RestWithUndertow extends org.apache.camel.builder.RouteBuilder {
    
    private final String order = "{'beverages': [{'item': 'ESPRESSO_DOUBLE','name': 'Mickey'},{'item': 'COFFEE_BLACK','name': 'Minnie'}]}";
    @Override
    public void configure() throws Exception {
        JacksonDataFormat df = new JacksonDataFormat(CreateOrderCommand.class);
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
            .log("Incoming Body is ${body}")
            .bean(this,"transformMessage")
           // .setBody(constant(coc))
            .log("Outgoing pojo Body is ${body}")
            .marshal(df)
            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
            .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
            .setHeader("Accept",constant("application/json"))
            .log("Body after transformation is ${body} with headers: ${headers}");
    }

    public void transformMessage(Exchange exchange){
        Message in = exchange.getIn();
        GrubHubOrder gho = in.getBody(GrubHubOrder.class);
        List<GrubHubOrderItem> oi = gho.getOrderItems();
        List<LineItem> list = new ArrayList<LineItem>();
        for(GrubHubOrderItem i : oi){
            LineItem li = new LineItem(Item.valueOf(i.getOrderItem()),i.getName());
            list.add(li);
        }
        //Order o = new Order(list);
        CreateOrderCommand coc = new CreateOrderCommand(list, null);
        in.setBody(coc);
    }
}
