package com.test.processing;


import com.test.processing.models.MarketCap;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;

import org.springframework.cloud.stream.binder.kafka.streams.annotations.KafkaStreamsProcessor;

@Component
@EnableBinding(Processor.class)
//@EnableBinding(KafkaStreamsProcessor.class)
public class AlertGenerator {
    private final MarketCapService marketCapServiced;

    public AlertGenerator(MarketCapService marketCapService) {
        this.marketCapServiced = marketCapService;
    }

    @StreamListener("input")
    @SendTo("output")
    public Boolean process(BigDecimal input) {
        MarketCap marketCap = new MarketCap(input);
        Boolean alert = marketCapServiced.isAlert(marketCap);
        System.out.println("isAlert: " + alert);

        return alert;
    }


//    @StreamListener("input")
//    @SendTo("output")
//    public KStream<?, WordCount> process(KStream<Object, String> input) {
//        return input
//                .peek((key, value) -> System.out.println(key + " " + value))
//                .flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
//                .map((key, value) -> new KeyValue<>(value, value))
//                .groupByKey(Serialized.with(Serdes.String(), Serdes.String()))
//                .windowedBy(TimeWindows.of(3000))
//                .count(Materialized.as("WordCounts-1"))
//                .toStream()
//                .map((key, value) -> new KeyValue<>(null, new WordCount(key.key(), value, new Date(key.window().start()), new Date(key.window().end()))));
//    }

    static class WordCount {

        private String word;

        private long count;

        private Date start;

        private Date end;

        WordCount(String word, long count, Date start, Date end) {
            this.word = word;
            this.count = count;
            this.start = start;
            this.end = end;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public Date getStart() {
            return start;
        }

        public void setStart(Date start) {
            this.start = start;
        }

        public Date getEnd() {
            return end;
        }

        public void setEnd(Date end) {
            this.end = end;
        }
    }
}
