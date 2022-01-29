package com.daria.clustering.kmeans.service.impl;

import com.daria.clustering.kmeans.service.KmeansClusteringService;
import org.springframework.stereotype.Service;

@Service
public class KmeansClusteringServiceImpl implements KmeansClusteringService {
    @Override
    public String test() {
        return "초기세팅 test입니다.";
    }
}
