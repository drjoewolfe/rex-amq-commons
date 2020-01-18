package com.jwolfe.rex;

import com.jwolfe.rex.amq.commons.Consumer;
import com.jwolfe.rex.amq.commons.Producer;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.ArrayList;
import java.util.List;

public class App
{
    private static Options options;
    private static CommandLine commandLine;
    private static String queueName;

    public static void main( String[] args )
    {
        prepareCommandLineParameters();
        parseCommandLineParameters(args);

        queueName = "tsar.queue100";

        if(commandLine.hasOption("h")) {
            showHelp();
        }

        if(commandLine.hasOption("p")) {
            runProducer();
        }

        if(commandLine.hasOption("c")) {
            runConsumer();
        }
    }

    private static void runProducer() {
        Producer producer = new Producer();
        List<String> textList = new ArrayList<String>();

        for (int i = 1; i < 5001; i++) {
            textList.add("Sample message " + i);
        }

        producer.send(queueName, textList);
    }

    private static void runConsumer() {
        Consumer consumer = new Consumer();
        consumer.startListener(queueName);

        // consumer.receiveX(queueName, 5000);
    }

    private static void showHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "rex-amq-starters", options );
    }

    private static void parseCommandLineParameters(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            commandLine = parser.parse( options, args);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void prepareCommandLineParameters() {
        options = new Options();

        options.addOption("h", "help", false,"Show help");
        options.addOption("p", "producer", false,"Run as producer");
        options.addOption("c", "consumer", false,"Run as consumer");
    }
}
