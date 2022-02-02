package com.daria.clustering.esclient.searcher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class AggrigationResultResponse {
    private String key;
    private long docCount;
}
