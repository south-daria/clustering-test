package com.daria.clustering.kmeans.service;

import com.daria.clustering.dto.ClusteringRequest;
import com.daria.clustering.dto.ClusteringResult;
import org.springframework.stereotype.Service;

import java.util.List;

public interface KmeansClusteringService {

    List<ClusteringResult> getClusteringResult();

    List<ClusteringResult> createClustering(List<ClusteringRequest> clusteringRequestList);
}
