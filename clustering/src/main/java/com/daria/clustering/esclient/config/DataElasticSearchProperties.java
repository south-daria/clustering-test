package com.daria.clustering.esclient.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;

import static java.util.Optional.ofNullable;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "data-es")
@Primary
public class DataElasticSearchProperties extends ElasticSearchProperties {

    Indecies indecies;

    public String getKmeansIndexName() {
        return ofNullable(this.indecies)
                .map(Indecies::getKmeansIndexName)
                .orElse("index");
    }

    @Getter
    @Setter
    public static class Indecies {
        private String kmeansIndexName;
    }
}
