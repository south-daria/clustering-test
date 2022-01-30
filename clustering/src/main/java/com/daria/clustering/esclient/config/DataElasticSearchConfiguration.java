package com.daria.clustering.esclient.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@Slf4j
@ComponentScan
@EnableConfigurationProperties(DataElasticSearchProperties.class)
@Configuration
@RequiredArgsConstructor
public class DataElasticSearchConfiguration extends AbstractElasticsearchConfiguration {

    private final DataElasticSearchProperties dataElasticSearchProperties;

    @Override
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(dataElasticSearchProperties.getDefaultHostStringArray())
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
}
