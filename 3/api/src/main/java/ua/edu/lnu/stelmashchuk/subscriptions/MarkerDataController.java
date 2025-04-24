package ua.edu.lnu.stelmashchuk.subscriptions;

import org.reactivestreams.Publisher;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;

@Controller
public class MarkerDataController {

    private final TickerPublisher publisher;

    public MarkerDataController(TickerPublisher publisher) {
        this.publisher = publisher;
    }

    @QueryMapping
    public Ticker ticker(@Argument String symbol) {
        return publisher.getTicker(symbol);
    }

    @MutationMapping
    public Ticker pushTicker(@Argument String symbol, @Argument float price, @Argument int size) {
        Ticker ticker = new Ticker(symbol, price, size);
        publisher.publish(ticker);
        return ticker;
    }

    @SubscriptionMapping
    public Publisher<Ticker> tickerChanged(@Argument String symbol) {
        return publisher.subscribe(symbol);
    }
}
