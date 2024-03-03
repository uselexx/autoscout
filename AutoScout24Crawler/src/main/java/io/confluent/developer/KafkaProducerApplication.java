package io.confluent.developer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class KafkaProducerApplication {

    private final Producer<String, String> producer;
    final String outTopic;

    public KafkaProducerApplication(final Producer<String, String> producer,
                                    final String topic) {
        this.producer = producer;
        this.outTopic = topic;
    }

    public Future<RecordMetadata> produce(final String message) {
        final String[] parts = message.split("-");
        final String key, value;
        if (parts.length > 1) {
            key = parts[0];
            value = parts[1];
        } else {
            key = null;
            value = parts[0];
        }
        final ProducerRecord<String, String> producerRecord = new ProducerRecord<>(outTopic, key, value);
        return producer.send(producerRecord);
    }

    public void shutdown() {
        producer.close();
    }

    public static Properties loadProperties(String fileName) throws IOException {
        final Properties envProps = new Properties();
        final FileInputStream input = new FileInputStream(fileName);
        envProps.load(input);
        input.close();

        return envProps;
    }

    public void printMetadata(final Collection<Future<RecordMetadata>> metadata,
                              final String fileName) {
        System.out.println("Offsets and timestamps committed in batch from " + fileName);
        metadata.forEach(m -> {
            try {
                final RecordMetadata recordMetadata = m.get();
                System.out.println("Record written to offset " + recordMetadata.offset() + " timestamp " + recordMetadata.timestamp());
            } catch (InterruptedException | ExecutionException e) {
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    public void crawlAndProduce(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements listings = doc.select("h2");

        for (Element listing : listings) {
            String title = listing.select("h2").text();
            if (title.toLowerCase().contains("audi")) {
                //String price = listing.select("p.Price_price__APlgs").text();
                String message = title;
                produce(message);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String argument = "src/main/resources/dev.properties";
        final Properties props = KafkaProducerApplication.loadProperties(argument);
        final String topic = props.getProperty("output.topic.name");
        final Producer<String, String> producer = new KafkaProducer<>(props);
        final KafkaProducerApplication producerApp = new KafkaProducerApplication(producer, topic);

        // Specify the URL to crawl. You might need to adjust this based on the listings you're interested in.
        String urlToCrawl = "https://www.autoscout24.com/lst/audi";
        producerApp.crawlAndProduce(urlToCrawl);

        producerApp.shutdown();
    }
}
