package com.daria.clustering.kmeans.service.impl;

import com.daria.clustering.dto.ClusteringRequest;
import com.daria.clustering.dto.ClusteringResult;
import com.daria.clustering.dto.GeoPoint;
import com.daria.clustering.exception.CustomIllegalArgumentException;
import com.daria.clustering.kmeans.service.KmeansClusteringService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import smile.clustering.KMeans;
import smile.clustering.PartitionClustering;

import java.util.*;

@Service
public class KmeansClusteringServiceImpl implements KmeansClusteringService {
    //테스트 위한 좌표 데이터 정의
    final static Map<GeoPoint, String> GEO_POINT_MAP = new HashMap<>();
    static {
        GEO_POINT_MAP.put(GeoPoint.of(37.503341, 127.049840),  "회사");
        GEO_POINT_MAP.put(GeoPoint.of(37.503624, 127.050337),  "마담밍");
        GEO_POINT_MAP.put(GeoPoint.of(37.503829, 127.052980),  "농민백암순대");
        GEO_POINT_MAP.put(GeoPoint.of(37.503261, 127.050715),  "연더그레이");
        GEO_POINT_MAP.put(GeoPoint.of(37.532918, 126.900196),  "집");
        GEO_POINT_MAP.put(GeoPoint.of(37.534216, 126.900980),  "다이소");
        GEO_POINT_MAP.put(GeoPoint.of(37.532254, 126.903022),  "삼성래미안");
        GEO_POINT_MAP.put(GeoPoint.of(37.532065, 126.898589) ,  "당산서중학교");
    }

    @Override
    public List<ClusteringResult> getClusteringResult(){
        List<GeoPoint> geoPointList =  new ArrayList<>(GEO_POINT_MAP.keySet());
        double[][] geoPointArray = getGeoPointArray(geoPointList);

        KMeans clusters = PartitionClustering.run(20, () -> KMeans.fit(geoPointArray, 2));
        Map<Integer, List<GeoPoint>> groupIdGeoPointMap = new HashMap<>();
        for (int i = 0, yLength = clusters.y.length; i < yLength; i++) {
            int groupId = clusters.y[i];
            GeoPoint geoPoint = geoPointList.get(i);
            if(geoPoint != null){
                groupIdGeoPointMap.computeIfAbsent(groupId, k -> new ArrayList()).add(geoPoint);
            }
        }
        List<ClusteringResult> clusteringResultList = new ArrayList<>();
        for(Map.Entry<Integer, List<GeoPoint>> entry : groupIdGeoPointMap.entrySet()){
            clusteringResultList.add(ClusteringResult.of(entry.getKey(), entry.getValue(), GEO_POINT_MAP));
        }
        return clusteringResultList;
    }

    @Override
    public List<ClusteringResult> createClustering(List<ClusteringRequest> clusteringRequestList) {
        if(clusteringRequestList.size() < 2){
            throw CustomIllegalArgumentException.message("현재 좌표는 2개 이상이어야합니다.");
        }
        List<GeoPoint> geoPointList =  new ArrayList<>();
        Map<GeoPoint, String> geoPointLocationMap = new HashMap<>();
        for(ClusteringRequest clusteringRequest : clusteringRequestList){
            clusteringRequest.validCheck(); //유효값 검사
            GeoPoint geoPoint = GeoPoint.of(clusteringRequest.getLat(), clusteringRequest.getLon());
            geoPointList.add(geoPoint);
            geoPointLocationMap.put(geoPoint, clusteringRequest.getLocationName());
        }
        double[][] geoPointArray = getGeoPointArray(geoPointList);

        int groupSize = clusteringRequestList.size()/4;
        KMeans clusters = PartitionClustering.run(20, () -> KMeans.fit(geoPointArray, groupSize < 2 ? 2 : groupSize));
        //kmeans 최소개수를 2개 이하로 할 때 에러가 나기 때문.

        Map<Integer, List<GeoPoint>> groupIdGeoPointMap = new HashMap<>();
        for (int i = 0, yLength = clusters.y.length; i < yLength; i++) {
            int groupId = clusters.y[i];
            GeoPoint geoPoint = geoPointList.get(i);
            if(geoPoint != null){
                groupIdGeoPointMap.computeIfAbsent(groupId, k -> new ArrayList()).add(geoPoint);
            }
        }

        List<ClusteringResult> clusteringResultList = new ArrayList<>();
        for(Map.Entry<Integer, List<GeoPoint>> entry : groupIdGeoPointMap.entrySet()){
            clusteringResultList.add(ClusteringResult.of(entry.getKey(), entry.getValue(), geoPointLocationMap));
        }
        return clusteringResultList;
    }


    private double[][] getGeoPointArray(List<GeoPoint> geoPointList) {
        if (CollectionUtils.isEmpty(geoPointList)) {
            return new double[0][];
        }
        double[][] geoPointArray = new double[geoPointList.size()][];
        int index = 0;
        for (GeoPoint geoPoint : geoPointList) {
            geoPointArray[index++] = new double[]{geoPoint.getLat(), geoPoint.getLon()};
        }
        return geoPointArray;
    }
}
