# clustering-test

> 개발 스택 : JAVA(SPRING BOOT), ELASTICSEARCH(DOCKER에 설치)

클러스터링 스터디 (KMEANS, XMEANS, OSRM etc...)



### KMEANS
KMEANS 클러스터링 - 출처 : https://hleecaster.com/ml-kmeans-clustering-concept/

“K“는 데이터 세트에서 찾을 것으로 예상되는 클러스터(그룹) 수를 말한다.
“Means“는 각 데이터로부터 그 데이터가 속한 클러스터의 중심까지의 평균 거리를 의미한다. (이 값을 최소화하는 게 알고리즘의 목표가 된다.)
K-Means에서는 이걸 구현하기 위해 반복적인(iterative) 접근을 취한다.

일단 K개의 임의의 중심점(centroid)을 배치하고
각 데이터들을 가장 가까운 중심점으로 할당한다. (일종의 군집을 형성한다.)
군집으로 지정된 데이터들을 기반으로 해당 군집의 중심점을 업데이트한다.
2번, 3번 단계를 그래서 수렴이 될 때까지, 즉 더이상 중심점이 업데이트 되지 않을 때까지 반복한다.
그림으로 보면 아래와 같다.

![image](https://user-images.githubusercontent.com/69445946/151665746-ce926395-fa4e-4406-a42b-faead8f82fbf.png)


여기서 일단 k 값은 2다. 그래서 (b)에서 일단 중심점 2개를 아무 데나 찍고, (c)에서는 각 데이터들을 두 개 점 중 가까운 곳으로 할당한다. (d)에서는 그렇게 군집이 지정된 상태로 중심점을 업데이트 한다. 그리고 (e)에서는 업데이트 된 중심점과 각 데이터들의 거리를 구해서 군집을 다시 할당하는 거다.

이걸 계속 반복~

아무튼 이렇게 군집화를 해놓으면 새로운 데이터가 들어와도 그게 어떤 군집에 속할지 할당해줄 수 있게 되는 셈이다.


Kmeans 클러스터링

REQUEST
```
//리스트형태로 lat, long, locationName을 넘겨준다.
[
    {"lat": 37.503624, "lon": 127.050337, "locationName": "마담밍"},
    {"lat": 37.503341, "lon": 127.049840, "locationName": "선릉로 428"}
]

```

RESPONSE
```
//클러스터링 결과를 돌려준다.
[
    {
        "groupId": 0,
        "clusteringLocationList": [
            {
                "geoPoint": {
                    "lat": 37.503341,
                    "lon": 127.04984
                },
                "locationName": "선릉로 428"
            }
        ]
    },
    {
        "groupId": 1,
        "clusteringLocationList": [
            {
                "geoPoint": {
                    "lat": 37.503624,
                    "lon": 127.050337
                },
                "locationName": "마담밍"
            }
        ]
    }
]

```

mysql 좌표 구현 예정


