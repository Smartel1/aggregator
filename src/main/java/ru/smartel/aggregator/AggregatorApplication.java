package ru.smartel.aggregator;

import com.rometools.rome.io.SyndFeedInput;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class AggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AggregatorApplication.class, args);
	}

	@Bean
	public SyndFeedInput syndFeedInput() {
		return new SyndFeedInput();
	}
}
