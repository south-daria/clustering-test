package com.daria.clustering.esclient.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpHost;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Optional.ofNullable;

@Getter
@Setter
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticSearchProperties {
    public static final int MAX_QUERY_SIZE = 50000;

    //application.yml data 못 읽어와서 일단 하드코딩.
    protected List<String> hosts = Arrays.asList("localhost");
    protected Integer port = 9200;
    protected String indexPostfix = "local";

    protected HttpClientProperties httpclient;

    public int getMaxConnTotal() {
        return ofNullable(httpclient).map(HttpClientProperties::getPooling).orElse(getDefaultHttpClient()).getMaxTotal();
    }

    public int getMaxConnPerRoute() {
        return ofNullable(httpclient).map(HttpClientProperties::getPooling).orElse(getDefaultHttpClient()).getDefaultMaxPerRoute();
    }

    public int getConnectionTimeout() {
        return ofNullable(httpclient).map(HttpClientProperties::getDefaultConnectionTimeout).orElse(getDefaultTimeoutHttpClient().getDefaultConnectionTimeout());
    }

    public int getReadTimeout() {
        return ofNullable(httpclient).map(HttpClientProperties::getDefaultReadTimeout).orElse(getDefaultTimeoutHttpClient().getDefaultReadTimeout());
    }

    protected HttpClientProperties getDefaultTimeoutHttpClient() {
        HttpClientProperties httpClientProperties = new HttpClientProperties();
        httpClientProperties.setDefaultConnectionTimeout(httpclient.getDefaultConnectionTimeout());
        httpClientProperties.setDefaultReadTimeout(httpclient.getDefaultReadTimeout());
        return httpclient;
    }

    protected HttpClientProperties.Pooling getDefaultHttpClient() {
        HttpClientProperties.Pooling httpClient = new HttpClientProperties.Pooling();
        httpClient.setMaxTotal(httpclient.getPooling().getMaxTotal());
        httpClient.setDefaultMaxPerRoute(httpclient.getPooling().getDefaultMaxPerRoute());
        return httpClient;
    }

    public HttpHost[] getDefaultHttpHost() {
        return ofNullable(hosts).map(host ->
                host.stream().map(h ->
                        new HttpHost(h, port)
                ).toArray(HttpHost[]::new)
        ).orElseThrow(() -> new IllegalArgumentException("ElasticSearch hostList is null!"));
    }

    public String[] getDefaultHostStringArray() {
        return ofNullable(hosts).map(hosts ->
                hosts.toArray(new String[hosts.size()])
        ).orElseThrow(() -> new IllegalArgumentException("ElasticSearch hostList is null!"));
    }
}
