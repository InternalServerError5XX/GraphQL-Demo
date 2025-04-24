package ua.edu.lnu.stelmashchuk.subscriptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@EnableKafka
@Component
public class MarkerDataConsumer {
    private final TickerPublisher publisher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MarkerDataConsumer(TickerPublisher publisher) {
        this.publisher = publisher;
    }

    @KafkaListener(topics = "#{T(java.util.Arrays).asList('crpt_bnn_btc_usd', " +
            "'crpt_bnn_eth_usd')}", groupId = "1")
    public void consumeMarketData(String message) {
        var ticker = parseMessage(message);
        publisher.publish(ticker);
    }

    private Ticker parseMessage(String message) {
        try {
            var rootNode = objectMapper.readTree(message);
            var lastPrice = rootNode.path("LastPrice").asDouble();
            var exchangePairNode = rootNode.path("ExchangePair");
            var symbol = exchangePairNode.path("CurrencyPair").path("BaseAsset").asText() + "/" +
                    exchangePairNode.path("CurrencyPair").path("QuoteAsset").asText();
            var lastSize = rootNode.path("LastSize").asDouble();

            return new Ticker(symbol, lastPrice, lastSize);
        } catch (Exception e) {
            System.err.println("Error parsing message: " + e.getMessage());
            return null;
        }
    }
}
