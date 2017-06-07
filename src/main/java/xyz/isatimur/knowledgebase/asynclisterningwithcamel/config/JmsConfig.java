package xyz.isatimur.knowledgebase.asynclisterningwithcamel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

/**
 * Created by isati on 06.06.2017.
 */
@Configuration
public class JmsConfig {

    @Value("${max.concurrent.consumers}")
    private int maxConcurrentConsumers;

    @Bean
    public JmsTemplate jmsTemplate(final ConnectionFactory jmsConnectionFactory) {
        return new JmsTemplate(jmsConnectionFactory);
    }

}
