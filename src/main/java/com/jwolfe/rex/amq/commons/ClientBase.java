package com.jwolfe.rex.amq.commons;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jms.Connection;
import javax.jms.JMSException;

public abstract class ClientBase {
    protected final Logger logger = LogManager.getLogger();

    protected Connection getBrokerConnection () throws JMSException {
        String amqUrl = "tcp://localhost:61616";
        var factory = new ActiveMQConnectionFactory(amqUrl);
        var connection = factory.createConnection();
        connection.start();
        logger.info("Connected to ActiveMQ at " + amqUrl);

        return connection;
    }
}
