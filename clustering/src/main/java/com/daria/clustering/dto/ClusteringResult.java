package com.daria.clustering.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ClusteringResult {
    Integer groupId;
    Integer clusteringSize;
    List<ClusteringLocation> clusteringLocationList = new ArrayList<>();

    public static ClusteringResult of(Integer groupId, List<GeoPoint> geoPointList, Map<GeoPoint, String> geoPointStringMap){
        ClusteringResult instance = new ClusteringResult();
        instance.groupId = groupId;
        instance.clusteringSize = geoPointList.size();
        for(GeoPoint geoPoint : geoPointList) {
            instance.clusteringLocationList.add(ClusteringLocation.of(geoPoint, geoPointStringMap.get(geoPoint)));
        }
        return instance;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "of")
    public static class ClusteringLocation {
        GeoPoint geoPoint;
        String locationName;
    }
}
