package com.daria.clustering.esclient.entity;

import com.daria.clustering.dto.ClusteringResult;
import com.daria.clustering.dto.GeoPoint;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class EsKmeans extends EsBaseTimeEntity implements Serializable {
    Integer groupId;
    Integer clusteringSize;
    List<Location> locationList;

    public static EsKmeans ofCreate(ClusteringResult clusteringResult){
        EsKmeans instance = new EsKmeans();
        instance.groupId = clusteringResult.getGroupId();
        instance.clusteringSize = clusteringResult.getClusteringSize();
        instance.locationList = clusteringResult.getClusteringLocationList().stream().map(location -> Location.of(location.getGeoPoint(), location.getLocationName())).collect(Collectors.toList());
        return instance;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "of")
    private static class Location{
        GeoPoint geoPoint;
        String locationName;
    }
}
