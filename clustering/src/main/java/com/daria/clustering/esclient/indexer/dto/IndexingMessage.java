package com.daria.clustering.esclient.indexer.dto;

import com.daria.clustering.esclient.indexer.enumeration.DataChangeType;
import com.daria.clustering.esclient.indexer.enumeration.IndexingType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class IndexingMessage implements Serializable {
    private IndexingType indexingType;
    private DataChangeType dataChangeType;
    private Object payload;

    public IndexingMessage() {
    }

    public static IndexingMessage of(IndexingType indexingType, DataChangeType dataChangeType, Object payload) {
        IndexingMessage indexingMessage = new IndexingMessage();
        indexingMessage.indexingType = indexingType;
        indexingMessage.dataChangeType = dataChangeType;
        indexingMessage.payload = payload;
        return indexingMessage;
    }
}
