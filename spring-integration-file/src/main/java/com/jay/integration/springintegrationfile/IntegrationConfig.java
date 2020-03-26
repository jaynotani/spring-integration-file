package com.jay.integration.springintegrationfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.SourcePollingChannelAdapterSpec;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;

import java.io.File;

@Configuration
public class IntegrationConfig {

    @Autowired
    private Transformer transformer;

	/*
	 * @Bean public IntegrationFlow integrationFlow() { return
	 * IntegrationFlows.from(fileReader(), spec ->
	 * spec.poller(Pollers.fixedDelay(500))) .transform(transformer, "transform")
	 * .handle(fileWriter()) .get(); }
	 */

    
    @Bean
    public IntegrationFlow integrationFlow2() {
        return IntegrationFlows.from(fileReader(), new java.util.function.Consumer<SourcePollingChannelAdapterSpec>() {
			
			@Override
			public void accept(SourcePollingChannelAdapterSpec arg0) {
				arg0.poller(Pollers.fixedDelay(5000));
			}
		})
                .transform(transformer, "transform")
                .handle(fileWriter())
                .get();
    }
    
    
    @Bean
    public FileWritingMessageHandler fileWriter() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(
                new File("dest")
        );
        handler.setExpectReply(false);
        return handler;
    }

    @Bean
    public FileReadingMessageSource fileReader() {
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File("source"));
        return source;
    }

}
