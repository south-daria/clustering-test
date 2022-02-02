package com.daria.clustering.esclient.indexer.impl;

import com.daria.clustering.esclient.config.DataElasticSearchProperties;
import com.daria.clustering.esclient.entity.EsKmeans;
import com.daria.clustering.esclient.indexer.KmeansIndexer;
import com.daria.clustering.esclient.indexer.dto.IndexingResult;
import com.daria.clustering.esclient.indexer.enumeration.KmeansUpdateType;
import com.daria.clustering.esclient.worker.ElasticSearchWorkerImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

import static org.elasticsearch.action.DocWriteRequest.OpType.CREATE;

@Slf4j
@Component
@RequiredArgsConstructor
public class KmeansIndexerImpl extends ElasticSearchWorkerImpl implements KmeansIndexer {
    
    private static final String UPSERT_FAILURE_ERROR_MESSAGE_FORMAT = "Kmeans %s upsert failure. id : %s";
    
    private final ObjectMapper objectMapper;
    private final DataElasticSearchProperties properties;

    @Override
    public IndexingResult insert(Long indexingTimestamp, EsKmeans esKmeans) throws IOException {
        Optional.ofNullable(esKmeans).orElseThrow(() -> new IllegalArgumentException("esKmeans 정보가 없습니다."));
        Optional.of(esKmeans.getGroupId()).orElseThrow(() -> new IllegalArgumentException("esKmeans ID 정보가 없습니다."));
        IndexingResult upsertResult = upsert(esKmeans, indexingTimestamp, CREATE);
        return upsertResult;
    }

    @Override
    public IndexingResult upsert(EsKmeans esKmeans, Long indexingTimestamp, DocWriteRequest.OpType opType) throws IOException {
        esKmeans.setLastIndexedTimeStamp(indexingTimestamp);
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String stamp = sdf.format(date);

        SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        sdf2.setTimeZone(TimeZone.getTimeZone("UTC"));
        String stamp2 = sdf2.format(date);

        esKmeans.setTimestamp(stamp);
        esKmeans.setTimestamp2(stamp2);
        RestHighLevelClient client = null;
        UpdateResponse updateResponse;
        try {
            client = getHighLevelClient();
            UpdateRequest updateRequest = createEsKmeansUpsertRequest(esKmeans);
            updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        } catch (ConnectException e) {
            log.warn("upsert ConnectException ", e);
            throw e;
        } catch (IOException e) {
            log.error("upsert Exception", e);
            throw e;
        } finally {
            super.closeClient(client);
        }
        return IndexingResult.getUpsertIndexingResult(updateResponse, String.valueOf(esKmeans.getGroupId()), String.format(UPSERT_FAILURE_ERROR_MESSAGE_FORMAT, opType.getLowercase(), esKmeans.getGroupId()));
    }

    private UpdateRequest createEsKmeansUpsertRequest(EsKmeans esKmeans) throws JsonProcessingException {
        UpdateRequest updateRequest = getUpdateRequestNotRefresh(esKmeans, String.valueOf(esKmeans.getGroupId()), KmeansUpdateType.UPDATE_KMEANS.getScriptName(), true);
        updateRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        return updateRequest;
    }

    private UpdateRequest getUpdateRequestNotRefresh(EsKmeans esKmeans, String id, String scriptName, boolean scriptedUpsert) throws JsonProcessingException {
        UpdateRequest updateRequest = new UpdateRequest(properties.getKmeansIndexName(), DEFAULT_TYPE, id);
        updateRequest.doc(objectMapper.writeValueAsString(esKmeans), XContentType.JSON);
        updateRequest.retryOnConflict(3);
        if (scriptedUpsert) {
            updateRequest.scriptedUpsert(scriptedUpsert);
            updateRequest.upsert(getIndexRequest(esKmeans, id));
        }
        return updateRequest;
    }

    private IndexRequest getIndexRequest(EsKmeans esKmeans, String id) throws JsonProcessingException {
        IndexRequest request = new IndexRequest(properties.getKmeansIndexName(), DEFAULT_TYPE, id);
        request.source(objectMapper.writeValueAsString(esKmeans), XContentType.JSON);
        return request;
    }

}
