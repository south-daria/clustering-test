package com.daria.clustering.esclient.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpHost;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static java.util.Optional.ofNullable;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "data-es")
public class ElasticSearchProperties {
    public static final int MAX_QUERY_SIZE = 50000;
    protected List<String> hosts;
    protected Integer port;
    protected String indexPostfix;

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
