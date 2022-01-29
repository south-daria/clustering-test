package com.daria.clustering.kmeans.controller;

import com.daria.clustering.kmeans.service.KmeansClusteringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/clustering/api/v1_0/kmeans")
@RequiredArgsConstructor
public class KmeansController {
    private final KmeansClusteringService kmeansClusteringService;

    @GetMapping
    }
}
