package com.daria.clustering.dto;

import com.daria.clustering.exception.CustomIllegalArgumentException;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClusteringRequest {
    double lat;
    double lon;
    @NotNull
    String locationName;

    public void validCheck(){
        if (StringUtils.isEmpty(locationName)){
            throw CustomIllegalArgumentException.message("locationName는 필수값입니다.");
        }
        if (lat < 0 || lat > 180 || lon < 0 || lon > 180){
            throw CustomIllegalArgumentException.message("유효한 좌표값을 입력해주세요.");
        }
    }
}
