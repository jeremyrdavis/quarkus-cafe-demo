import org.apache.camel.Exchange;

public class RestWithUndertow extends org.apache.camel.builder.RouteBuilder {
    @Override
    public void configure() throws Exception {
        restConfiguration()
            .component("undertow")
            .host("0.0.0.0")
            .port("8080");

        rest()
            .get("/hello")
            .to("direct:hello");

        from("direct:hello")
            .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
            .transform().simple("Hello World!");

}
}
