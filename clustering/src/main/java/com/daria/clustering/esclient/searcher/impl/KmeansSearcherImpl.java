package com.daria.clustering.esclient.searcher.impl;

import com.daria.clustering.esclient.searcher.KmeansSearcher;
import com.daria.clustering.esclient.searcher.repository.KmeansRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KmeansSearcherImpl implements KmeansSearcher {
    private final KmeansRepository kmeansRepository;
}
