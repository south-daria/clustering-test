package com.daria.clustering.esclient.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EsBaseTimeEntity {
    protected Long lastIndexedTimeStamp;
    protected String timestamp;
    @JsonProperty("@timestamp")
    protected String timestamp2;
}
