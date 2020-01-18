package com.jwolfe.rex.amq.commons;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.List;

public class Consumer extends ClientBase implements MessageListener, ExceptionListener {
    public  List<String> runrunSample() {
        String queueName = "tsar.queue100";
        return receive(queueName);
    }

    public  List<String> receive(String queueName) {
        return receiveX(queueName, 1);
    }

    public  List<String> receiveAll(String queueName) {
        return null;
    }

    public List<String> receiveX(String queueName, int x) {
        List<String> textList = new ArrayList<>();
        try {
            var connection = getBrokerConnection();
            var session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            connection.setExceptionListener(this);

            var destination = session.createQueue(queueName);
            logger.info("Created queue - " + queueName);

            MessageConsumer consumer = session.createConsumer(destination);

            for (int i = 0; i < x; i++) {
                Message message = consumer.receive(1000);
                consumeMessage(message, textList);
            }

            consumer.close();
            session.close();
            connection.close();
            logger.info("Connection & session closed");
        } catch (JMSException e) {
            e.printStackTrace();
        }

        return textList;
    }

    public void startListener(String queueName) {
        try {
            var connection = getBrokerConnection();
            var session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            connection.setExceptionListener(this);

            var destination = session.createQueue(queueName);
            logger.info("Created queue - " + queueName);

            MessageConsumer consumer = session.createConsumer(destination);

            consumer.setMessageListener(this);
            logger.info("Listener initialized");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            consumeMessage(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onException(JMSException e) {
        System.out.println("JMS Exception occured.  Shutting down client.");
    }

    private void consumeMessage(Message message) throws JMSException {
        if(message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();

            logger.info("Received: " + text);
        }
        else {
            logger.info("Received: " + message);
        }
    }

    private void consumeMessage(Message message, List<String> textList) throws JMSException {
        if(message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            textList.add(text);

            logger.info("Received: " + text);
        }
        else {
            logger.info("Received: " + message);
        }
    }
}
