package com.daria.clustering.esclient.worker;

import com.daria.clustering.esclient.config.DataElasticSearchProperties;
import com.google.common.io.Closeables;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Closeable;

/**
 * Created by DoYoungKim on 2020.01.21..
 */
@Slf4j
@Component
public class ElasticSearchWorkerImpl implements ElasticSearchWorker {

    @Autowired
    DataElasticSearchProperties dataElasticSearchProperties;

    @Override
    public RestHighLevelClient getHighLevelClient() {
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(
                        dataElasticSearchProperties.getDefaultHttpHost()
                ).setHttpClientConfigCallback(httpClientBuilder -> {
                    httpClientBuilder.setMaxConnTotal(dataElasticSearchProperties.getMaxConnTotal());
                    httpClientBuilder.setMaxConnPerRoute(dataElasticSearchProperties.getMaxConnPerRoute());
                    return httpClientBuilder;
                }).setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
                        .setConnectTimeout(dataElasticSearchProperties.getConnectionTimeout())
                        .setSocketTimeout(dataElasticSearchProperties.getReadTimeout()))
        );
        return restHighLevelClient;
    }

    @Override
    public void closeClient(Closeable client) {
        try {
            Closeables.close(client, true);
        } catch (Exception e) {
            log.error("closeClientNLog", e);
        }
    }
}
