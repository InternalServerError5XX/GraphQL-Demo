package ua.edu.lnu.stelmashchuk.subscriptions;

import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TickerPublisher {

    private final Map<String, Sinks.Many<Ticker>> sinkMap = new ConcurrentHashMap<>();
    private final Map<String, Ticker> tickers = new ConcurrentHashMap<>();

    public void publish(Ticker ticker) {
        tickers.put(ticker.symbol(), ticker);
        getOrCreateSink(ticker.symbol()).tryEmitNext(ticker);
    }

    public Publisher<Ticker> subscribe(String symbol) {
        return getOrCreateSink(symbol).asFlux();
    }

    public Ticker getTicker(String symbol) {
        return tickers.get(symbol);
    }

    private Sinks.Many<Ticker> getOrCreateSink(String symbol) {
        return sinkMap.computeIfAbsent(symbol, s -> Sinks.many().multicast().onBackpressureBuffer());
    }
}

