package com.daria.clustering.esclient.worker;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.Closeable;
import java.net.UnknownHostException;

/**
 * Created by DoYoungKim on 2020.01.21..
 */
public interface ElasticSearchWorker {
    String DEFAULT_TYPE = "_doc";

    RestHighLevelClient getHighLevelClient() throws UnknownHostException;

    void closeClient(Closeable client);
}
