package com.jwolfe.rex.amq.commons;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Session;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Producer extends ClientBase {
    private Connection connection;

    public void runSample() {
        String queueName = "tsar.queue100";
        send(queueName, "Sample Message");
    }

    public void send(String queueName, String text) {
        var textList = new ArrayList<String>();
        textList.add(text);
        send(queueName, textList);
    }

    public void send(String queueName, String... texts) {
        var textList = Arrays.asList(texts);
        send(queueName, textList);
    }

    public void send(String queueName, List<String> textList) {
        try {
            var connection = getBrokerConnection();
            var session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            var destination = session.createQueue(queueName);
            logger.info("Created queue - " + queueName);

            var producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            for (String text : textList) {
                var message = session.createTextMessage(text);

                producer.send(message);
                logger.info("Sent message - " + text);
            }

            session.close();
            connection.close();
            logger.info("Connection & session closed");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
