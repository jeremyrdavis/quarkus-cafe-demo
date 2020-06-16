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
import org.apache.camel.component.jackson.JacksonDataFormat;

public class RestWithUndertow extends org.apache.camel.builder.RouteBuilder {
    
    @Override
    public void configure() throws Exception {
        JacksonDataFormat df = new JacksonDataFormat(CreateOrderCommand.class);

        rest()
            .post("/order").type(GrubHubOrder.class).consumes("application/json")
            .bindingMode(RestBindingMode.json)
            .produces("application/json")
            .to("direct:order");

        from("direct:order")
            .log("Incoming Body is ${body}")
            .log("Incoming Body after unmarshal is ${body}")
            .bean(this,"transformMessage")
            .log("Outgoing pojo Body is ${body}")
            .marshal(df)
            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
            .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
            .setHeader("Accept",constant("application/json"))
            .log("Body after transformation is ${body} with headers: ${headers}")
            //need to change url after knowing what the cafe-web url will be 
            .to("http://quarkus-cafe-web-quarkus-cafe-demo.apps.cluster-rhug-384e.rhug-384e.example.opentlc.com/order?bridgeEndpoint=true&throwExceptionOnFailure=false")
            .setHeader(Exchange.HTTP_RESPONSE_CODE,constant(200));
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
        CreateOrderCommand coc = new CreateOrderCommand(list, null);
        in.setBody(coc);
    }
}
