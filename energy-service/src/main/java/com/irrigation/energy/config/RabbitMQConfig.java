package com.irrigation.energy.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration RabbitMQ pour le microservice Énergie.
 * Définit l'exchange, la queue et le binding pour publier les événements de surconsommation.
 */
@Configuration
public class RabbitMQConfig {
    
    // Nom de l'exchange (Direct Exchange)
    public static final String EXCHANGE = "irrigation.exchange";
    
    // Nom de la queue pour les événements de surconsommation
    public static final String QUEUE = "overconsumption.queue";
    
    // Routing key pour router les messages
    public static final String ROUTING_KEY = "overconsumption.routing.key";
    
    /**
     * Déclaration de l'exchange Direct
     */
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }
    
    /**
     * Déclaration de la queue durable
     */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }
    
    /**
     * Binding entre l'exchange et la queue via la routing key
     */
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
    
    /**
     * Convertisseur JSON pour sérialiser/désérialiser les événements
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    /**
     * RabbitTemplate configuré avec le convertisseur JSON
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
