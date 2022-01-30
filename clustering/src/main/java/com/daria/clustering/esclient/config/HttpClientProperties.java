package com.daria.clustering.esclient.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "httpclient")
public class HttpClientProperties {
    private HttpClientProperties.Pooling pooling = new HttpClientProperties.Pooling();
    private int defaultReadTimeout;
    private int defaultConnectionTimeout;

    @Getter
    @Setter
    @ToString
    public static class Pooling {
        private int maxTotal;
        private int defaultMaxPerRoute;
    }
}
