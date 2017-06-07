package xyz.isatimur.knowledgebase.asynclisterningwithcamel.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by isati on 06.06.2017.
 */
@Component
public class CamelRoutes extends RouteBuilder {

    @Override
    public void configure() {
        from("timer://test?period=1000")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        String message = UUID.randomUUID().toString();
                        log.info("**********************************");
                        log.info("Send message '{}' to queue....", message);
                        exchange.getOut().setBody(message);
                    }
                })
                .to("jms:{{mq.in.queue}}");

        from("jms:{{mq.in.queue}}?asyncConsumer=true&concurrentConsumers={{max.concurrent.consumers}}")
                .log(LoggingLevel.INFO, log, "Main received Message")
                .process(p -> log.info("Main exchange:{}  " + Thread.currentThread().getName(), p))
                .to("{{outbound.endpoint}}")
                .log(LoggingLevel.INFO, log, "Main message sent.")
                .to("jms:{{mq.in.report.queue}}?as");

        from("jms:{{mq.in.report.queue}}?asyncConsumer=true&concurrentConsumers={{max.concurrent.consumers}}")
                .log(LoggingLevel.INFO, log, "Reply to Queue handling")
                .process(p -> log.info("Reply to exchange:{}  " + Thread.currentThread().getName(), p))
                .to("{{outbound.endpoint}}")
                .log(LoggingLevel.INFO, log, "Reply to Message sent.")
                .end();


    }

}