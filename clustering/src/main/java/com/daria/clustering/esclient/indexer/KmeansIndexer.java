package com.daria.clustering.esclient.indexer;

import com.daria.clustering.esclient.entity.EsKmeans;
import com.daria.clustering.esclient.indexer.dto.IndexingResult;
import org.elasticsearch.action.DocWriteRequest;

import java.io.IOException;

public interface KmeansIndexer {
    IndexingResult insert(Long indexingTimestamp, EsKmeans esKmeans) throws IOException;

    IndexingResult upsert(EsKmeans esKmeans, Long indexingTimestamp, DocWriteRequest.OpType opType) throws IOException;
}
