package com.daria.clustering.esclient.indexer.service.impl;

import com.daria.clustering.esclient.entity.EsKmeans;
import com.daria.clustering.esclient.indexer.KmeansIndexer;
import com.daria.clustering.esclient.indexer.dto.IndexingMessage;
import com.daria.clustering.esclient.indexer.dto.IndexingResult;
import com.daria.clustering.esclient.indexer.enumeration.DataChangeType;
import com.daria.clustering.esclient.indexer.enumeration.IndexingType;
import com.daria.clustering.esclient.indexer.service.KmeansIndexService;
import com.daria.clustering.exception.CustomApplicationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class KmeansIndexServiceImpl implements KmeansIndexService {
    private final KmeansIndexer kmeansIndexer;
    private final ObjectMapper objectMapper;
    
    private IndexingResult insert(EsKmeans kmeans, Long indexingTimestamp) throws IOException {
        return kmeansIndexer.insert(indexingTimestamp, kmeans);
    }

    private IndexingResult upsert(EsKmeans kmeans, Long indexingTimestamp) throws IOException {
        return kmeansIndexer.upsert(kmeans, indexingTimestamp, DocWriteRequest.OpType.CREATE);
    }

    @Async
    public Boolean asyncStoreEsKmeansIndexingData(Long sentTimestamp, EsKmeans kmeans, DataChangeType dataChangeType) {
        IndexingResult result = IndexingResult.ofFailure();
        try {
            result = syncProcessIndexingData(sentTimestamp, kmeans, dataChangeType);
        } catch (IOException e) {
            log.warn("[EsKmeans] asyncProcessIndexingData error Id : {}", Optional.ofNullable(kmeans).map(EsKmeans::getGroupId).orElse(null));
        }
        if (result.failure()) {
            log.warn(convertJson(IndexingMessage.of(IndexingType.KMEANS, dataChangeType, kmeans)));
            return result.failure();
        }
        return result.succed();
    }

    private String convertJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw CustomApplicationException.exception(e);
        }
    }

    public IndexingResult syncProcessIndexingData(Long sentTimestamp, EsKmeans kmeans, DataChangeType dataChangeType) throws IOException {
        IndexingResult result = IndexingResult.ofFailure();
        switch (dataChangeType) {
            case UPDATE:
                result = upsert(kmeans, sentTimestamp);
                break;
            case CREATE:
            case INDEX:
                result = insert(kmeans, sentTimestamp);
                break;
        }
        return result;
    }


}
