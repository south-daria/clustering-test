package com.daria.clustering.esclient.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import static java.util.Optional.ofNullable;

@Getter
@Setter
@ConfigurationProperties(prefix = "data-es")
public class DataElasticSearchProperties extends ElasticSearchProperties {

    Indecies indecies;

    public String getIndexName() {
        return ofNullable(this.indecies)
                .map(Indecies::getIndexName)
                .map(name -> StringUtils.isEmpty(indexPostfix) ? name : name + "_" + indexPostfix)
                .orElse("index");
    }

    @Getter
    @Setter
    public static class Indecies {
        String indexName;
    }
}
